package com.article.controller;

import com.api.BaseController;
import com.api.controller.article.ArticlePortalControllerApi;
import com.article.service.ArticlePortalService;
import com.pojo.Article;
import com.pojo.vo.AppUserVO;
import com.pojo.vo.IndexArticleVO;
import com.result.NewsJSONResult;
import com.result.PagedGridResult;
import com.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author Json
 * @date 2021/2/12 17:11
 */
@RestController
public class ArticlePortalController extends BaseController implements ArticlePortalControllerApi {

    private static final Logger logger = LoggerFactory.getLogger(ArticlePortalController.class);

    private final ArticlePortalService articlePortalService;
    private final RestTemplate restTemplate;

    @Autowired
    public ArticlePortalController(ArticlePortalService articlePortalService, RestTemplate restTemplate) {
        this.articlePortalService = articlePortalService;
        this.restTemplate = restTemplate;
    }

    @Override
    public NewsJSONResult list(String keyword, Integer category, Integer page, Integer pageSize) {

        // 1.设置分页参数
        if (page == null) {
            page = COMMON_START_PAGE;
        }
        if (pageSize == null) {
            pageSize = COMMON_PAGE_SIZE;
        }

        // 2.查询文章列表
        PagedGridResult gridResult = articlePortalService.queryIndexArticleList(keyword, category, page, pageSize);

        // 2.构建发布者id列表
        List<Article> articleList = (List<Article>) gridResult.getRows();
        Set<String> idSet = articleList.stream().map(Article::getPublishUserId).collect(Collectors.toSet());

        // 3.发起远程调用（restTemplate），请求用户服务获得用户（idSet 发布者）的列表
        Map<String, AppUserVO> publisherMap = getPublisherList(idSet);

        // 4. 拼接两个list，重组文章列表
        List<IndexArticleVO> indexArticleList = new ArrayList<>();
        for (Article article : articleList) {
            IndexArticleVO indexArticleVO = new IndexArticleVO();
            BeanUtils.copyProperties(article, indexArticleVO);

            // 4.1 从publisherList中获得发布者的基本信息
            AppUserVO publisher = publisherMap.get(article.getPublishUserId());
            indexArticleVO.setPublisherVO(publisher);
            indexArticleList.add(indexArticleVO);
        }
        gridResult.setRows(indexArticleList);

        return NewsJSONResult.success(gridResult);
    }

    @Override
    public NewsJSONResult hotList() {
        return NewsJSONResult.success(articlePortalService.queryHotList());
    }

    @Override
    public NewsJSONResult queryArticleListOfWriter(String writerId, Integer page, Integer pageSize) {
        return NewsJSONResult.success();
    }

    @Override
    public NewsJSONResult queryGoodArticleListOfWriter(String writerId) {
        return NewsJSONResult.success();
    }

    @Override
    public NewsJSONResult detail(String articleId) {
        return NewsJSONResult.success();
    }

    @Override
    public NewsJSONResult readArticle(String articleId, HttpServletRequest request) {
        return NewsJSONResult.success();
    }

    // 发起远程调用 获得用户的基本信息
    private Map<String, AppUserVO> getPublisherList(Set<String> idSet) {
        String userServerUrlExecute = "http://user.news.com:8003/user/queryByIds?userIds=" + JsonUtils.objectToJson(idSet);
        ResponseEntity<NewsJSONResult> responseEntity = restTemplate.getForEntity(userServerUrlExecute, NewsJSONResult.class);
        NewsJSONResult bodyResult = responseEntity.getBody();
        List<AppUserVO> publisherList = null;
        if (bodyResult != null && bodyResult.getStatus() == 200) {
            String userJson = JsonUtils.objectToJson(bodyResult.getData());
            publisherList = JsonUtils.jsonToList(userJson, AppUserVO.class);
        }

        assert publisherList != null;
        return publisherList.stream().collect(Collectors.toMap(AppUserVO::getId, Function.identity(), (k1, k2) -> k2));
    }

}
