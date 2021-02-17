package com.article.controller;

import com.api.BaseController;
import com.api.controller.article.ArticleControllerApi;
import com.article.service.ArticleService;
import com.constant.RedisConstant;
import com.enums.ArticleCoverType;
import com.enums.ArticleReviewStatus;
import com.enums.ResponseStatusEnum;
import com.enums.YesOrNo;
import com.mongodb.client.gridfs.GridFSBucket;
import com.pojo.Category;
import com.pojo.bo.NewArticleBO;
import com.pojo.vo.ArticleDetailVO;
import com.result.NewsJSONResult;
import com.result.PagedGridResult;
import com.utils.JsonUtils;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.File;
import java.io.InputStream;
import java.util.*;

/**
 * @author Json
 * @date 2021/2/10 20:01
 */
@RestController
public class ArticleController extends BaseController implements ArticleControllerApi {

    private static final Logger logger = LoggerFactory.getLogger(ArticleController.class);

    private final ArticleService articleService;
    private final GridFSBucket gridFSBucket;

    @Autowired
    public ArticleController(ArticleService articleService, GridFSBucket gridFSBucket) {
        this.articleService = articleService;
        this.gridFSBucket = gridFSBucket;
    }

    @Override
    public NewsJSONResult createArticle(@Valid NewArticleBO newArticleBO, BindingResult result) {

        // 1.判断BindingResult中是否保存了错误的验证信息
        if (result.hasErrors()) {
            Map<String, String> map = getErrors(result);
            return NewsJSONResult.errorMap(map);
        }

        // 2.判断文章封面类型 单图必填 纯文字则设置为空
        if (newArticleBO.getArticleType().equals(ArticleCoverType.ONE_IMAGE.type)) {
            if (StringUtils.isBlank(newArticleBO.getArticleCover())) {
                return NewsJSONResult.errorCustom(ResponseStatusEnum.ARTICLE_COVER_NOT_EXIST_ERROR);
            }
        } else if (newArticleBO.getArticleType().equals(ArticleCoverType.WORDS.type)) {
            newArticleBO.setArticleCover("");
        }

        // 3.判断分类id是否存在
        String allCatJson = redis.get(RedisConstant.REDIS_ALL_CATEGORY);
        if (StringUtils.isBlank(allCatJson)) {
            return NewsJSONResult.errorCustom(ResponseStatusEnum.SYSTEM_OPERATION_ERROR);
        }
        Category temp = getCategory(allCatJson, newArticleBO.getCategoryId());
        if (temp == null) {
            return NewsJSONResult.errorCustom(ResponseStatusEnum.ARTICLE_CATEGORY_NOT_EXIST_ERROR);
        }

        // 4.发布文章
        articleService.createArticle(newArticleBO, temp);

        return NewsJSONResult.success();
    }

    @Override
    public NewsJSONResult queryMyList(String userId, String keyword, Integer status, Date startDate, Date endDate, Integer page, Integer pageSize) {

        // 1.判断请求参数
        if (StringUtils.isBlank(userId)) {
            return NewsJSONResult.errorCustom(ResponseStatusEnum.ARTICLE_QUERY_PARAMS_ERROR);
        }

        // 2.设置分页默认值
        if (page == null) {
            page = COMMON_START_PAGE;
        }
        if (pageSize == null) {
            pageSize = COMMON_PAGE_SIZE;
        }

        // 3.查询我的列表 调用service
        PagedGridResult gridResult = articleService.queryMyArticleList(userId, keyword, status, startDate, endDate, page, pageSize);

        return NewsJSONResult.success(gridResult);
    }

    @Override
    public NewsJSONResult queryAllList(Integer status, Integer page, Integer pageSize) {

        // 1.设置分页默认值
        if (page == null) {
            page = COMMON_START_PAGE;
        }

        if (pageSize == null) {
            pageSize = COMMON_PAGE_SIZE;
        }

        // 2.查询我的列表 调用service
        PagedGridResult gridResult = articleService.queryAllArticleListAdmin(status, page, pageSize);

        return NewsJSONResult.success(gridResult);
    }

    @Override
    public NewsJSONResult doReview(String articleId, Integer passOrNot) {

        // 1.判断请求参数
        Integer pendingStatus;
        if (passOrNot.equals(YesOrNo.YES.type)) {
            // 审核成功
            pendingStatus = ArticleReviewStatus.SUCCESS.type;
        } else if (passOrNot.equals(YesOrNo.NO.type)) {
            // 审核失败
            pendingStatus = ArticleReviewStatus.FAILED.type;
        } else {
            return NewsJSONResult.errorCustom(ResponseStatusEnum.ARTICLE_REVIEW_ERROR);
        }

        // 2.保存到数据库 更改文章的状态为审核成功或者失败
        articleService.updateArticleStatus(articleId, pendingStatus);

        // 3.审核成功 生成文章详情页静态html
        if (pendingStatus.equals(ArticleReviewStatus.SUCCESS.type)) {
            try {
                // 3.1获得静态化html在gridFs中的主键
                String articleMongoId = createArticleHTMLToGridFS(articleId);

                // 3.2存储到对应的文章 进行关联保存
                articleService.updateArticleToGridFS(articleId, articleMongoId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return NewsJSONResult.success();
    }

    @Override
    public NewsJSONResult delete(String userId, String articleId) {
        articleService.deleteArticle(userId, articleId);
        return NewsJSONResult.success();
    }

    @Override
    public NewsJSONResult withdraw(String userId, String articleId) {
        articleService.withdrawArticle(userId, articleId);
        return NewsJSONResult.success();
    }

    private Category getCategory(String allCatJson, Integer categoryId) {
        List<Category> catList = JsonUtils.jsonToList(allCatJson, Category.class);
        Category temp = null;
        for (Category c : Objects.requireNonNull(catList)) {
            if (c.getId().equals(categoryId)) {
                temp = c;
                break;
            }
        }
        return temp;
    }

    // 文章生成HTML并存储到gridFs中
    private String createArticleHTMLToGridFS(String articleId) throws Exception {
        Configuration cfg = new Configuration(Configuration.getVersion());
        String classpath = this.getClass().getResource("/").getPath();
        cfg.setDirectoryForTemplateLoading(new File(classpath + "templates"));

        Template template = cfg.getTemplate("detail.ftl", "utf-8");

        // 获得文章的详情数据
        ArticleDetailVO detailVO = getArticleDetail(articleId);
        Map<String, Object> map = new HashMap<>();
        map.put("articleDetail", detailVO);

        String htmlContent = FreeMarkerTemplateUtils.processTemplateIntoString(template, map);

        InputStream inputStream = IOUtils.toInputStream(htmlContent);
        ObjectId fileId = gridFSBucket.uploadFromStream(detailVO.getId() + ".html", inputStream);
        return fileId.toString();
    }

    // 发起远程调用rest 获得文章详情数据
    private ArticleDetailVO getArticleDetail(String articleId) {
        String url = "http://www.news.com:8001/portal/article/detail?articleId=" + articleId;
        ResponseEntity<NewsJSONResult> responseEntity = restTemplate.getForEntity(url, NewsJSONResult.class);
        NewsJSONResult bodyResult = responseEntity.getBody();
        ArticleDetailVO detailVO = null;
        if (bodyResult != null && bodyResult.getStatus() == 200) {
            String detailJson = JsonUtils.objectToJson(bodyResult.getData());
            detailVO = JsonUtils.jsonToPojo(detailJson, ArticleDetailVO.class);
        }
        return detailVO;
    }
}
