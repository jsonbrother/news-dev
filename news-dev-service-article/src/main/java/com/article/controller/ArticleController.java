package com.article.controller;

import com.api.BaseController;
import com.api.config.RabbitMQConfig;
import com.api.controller.article.ArticleControllerApi;
import com.article.service.ArticleService;
import com.constant.RedisConstant;
import com.constant.RoutingKeyConstant;
import com.enums.ArticleCoverType;
import com.enums.ArticleReviewStatus;
import com.enums.ResponseStatusEnum;
import com.enums.YesOrNo;
import com.pojo.Category;
import com.pojo.bo.NewArticleBO;
import com.result.NewsJSONResult;
import com.result.PagedGridResult;
import com.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author Json
 * @date 2021/2/10 20:01
 */
@RestController
public class ArticleController extends BaseController implements ArticleControllerApi {

    private static final Logger logger = LoggerFactory.getLogger(ArticleController.class);

    private final ArticleService articleService;
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public ArticleController(ArticleService articleService, RabbitTemplate rabbitTemplate) {
        this.articleService = articleService;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public NewsJSONResult createArticle(@Valid NewArticleBO newArticleBO) {

        // 1.判断文章封面类型 单图必填 纯文字则设置为空
        if (newArticleBO.getArticleType().equals(ArticleCoverType.ONE_IMAGE.type)) {
            if (StringUtils.isBlank(newArticleBO.getArticleCover())) {
                return NewsJSONResult.errorCustom(ResponseStatusEnum.ARTICLE_COVER_NOT_EXIST_ERROR);
            }
        } else if (newArticleBO.getArticleType().equals(ArticleCoverType.WORDS.type)) {
            newArticleBO.setArticleCover("");
        }

        // 2.判断分类id是否存在
        String allCatJson = redis.get(RedisConstant.REDIS_ALL_CATEGORY);
        if (StringUtils.isBlank(allCatJson)) {
            return NewsJSONResult.errorCustom(ResponseStatusEnum.SYSTEM_OPERATION_ERROR);
        }
        Category temp = getCategory(allCatJson, newArticleBO.getCategoryId());
        if (temp == null) {
            return NewsJSONResult.errorCustom(ResponseStatusEnum.ARTICLE_CATEGORY_NOT_EXIST_ERROR);
        }

        // 3.发布文章
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

                // 3.1.发送消息到MQ队列 让消费者监听并且执行生成html
                doGenerateArticleHTMLByMQ(articleId);
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

    @Override
    public NewsJSONResult saveArticleMongoFileId(String articleId, String mongoFileId) {
        articleService.updateArticleToGridFS(articleId, mongoFileId);
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

    /**
     * 发送下载文章HTML的消息到MQ
     */
    private void doGenerateArticleHTMLByMQ(String articleId) {

        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_ARTICLE,
                RoutingKeyConstant.ARTICLE_GENERATE_DO, articleId);
    }

}
