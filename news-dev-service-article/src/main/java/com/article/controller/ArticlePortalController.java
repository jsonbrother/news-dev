package com.article.controller;

import com.api.BaseController;
import com.api.controller.article.ArticlePortalControllerApi;
import com.article.service.ArticlePortalService;
import com.constant.RedisConstant;
import com.pojo.Article;
import com.pojo.vo.AppUserVO;
import com.pojo.vo.ArticleDetailVO;
import com.pojo.vo.IndexArticleVO;
import com.result.NewsJSONResult;
import com.result.PagedGridResult;
import com.utils.IPUtil;
import com.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.function.Function;
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

        // 2.查询所有文章列表
        PagedGridResult gridResult = articlePortalService.queryIndexArticleList(keyword, category, page, pageSize);

        // 3.重建文章封装数据
        rebuildArticleGrid(gridResult);

        return NewsJSONResult.success(gridResult);
    }

    @Override
    public NewsJSONResult hotList() {
        return NewsJSONResult.success(articlePortalService.queryHotList());
    }

    @Override
    public NewsJSONResult queryArticleListOfWriter(String writerId, Integer page, Integer pageSize) {
        logger.info("writerId={}" + writerId);

        // 1.设置分页参数
        if (page == null) {
            page = COMMON_START_PAGE;
        }

        if (pageSize == null) {
            pageSize = COMMON_PAGE_SIZE;
        }

        // 2.查询用户文章列表
        PagedGridResult gridResult = articlePortalService.queryArticleListOfWriter(writerId, page, pageSize);

        // 3.重建文章封装数据
        rebuildArticleGrid(gridResult);

        return NewsJSONResult.success(gridResult);
    }

    @Override
    public NewsJSONResult queryGoodArticleListOfWriter(String writerId) {
        PagedGridResult gridResult = articlePortalService.queryGoodArticleListOfWriter(writerId);
        return NewsJSONResult.success(gridResult);
    }

    @Override
    public NewsJSONResult detail(String articleId) {
        ArticleDetailVO detailVO = articlePortalService.queryDetail(articleId);

        Set<String> idSet = new HashSet<>();
        idSet.add(detailVO.getPublishUserId());
        Map<String, AppUserVO> publisherMap = getBasicUserMap(idSet);

        if (!publisherMap.isEmpty()) {
            detailVO.setPublishUserName(publisherMap.get(detailVO.getPublishUserId()).getNickName());
        }

        detailVO.setReadCounts(getCountsFromRedis(RedisConstant.REDIS_ARTICLE_READ_COUNTS + ":" + articleId));

        return NewsJSONResult.success(detailVO);
    }

    @Override
    public NewsJSONResult readArticle(String articleId, HttpServletRequest request) {
        String userIp = IPUtil.getRequestIp(request);
        // 设置针对当前用户ip的永久存在的key 存入到redis 表示该ip的用户已经阅读过了 无法累加阅读量
        redis.setnx(RedisConstant.REDIS_ALREADY_READ + ":" + articleId + ":" + userIp, userIp);

        redis.increment(RedisConstant.REDIS_ARTICLE_READ_COUNTS + ":" + articleId, 1);
        return NewsJSONResult.success();
    }

    /**
     * 重建文章返回数据
     * <p>如：发布者信息</>
     */
    private void rebuildArticleGrid(PagedGridResult gridResult) {

        List<Article> articleList = (List<Article>) gridResult.getRows();

        // 1. 构建发布者id列表
        Set<String> idSet = new HashSet<>();
        List<String> idList = new ArrayList<>();
        for (Article article : articleList) {
            // 1.1 构建发布者的set
            idSet.add(article.getPublishUserId());
            // 1.2 构建文章id的list
            idList.add(RedisConstant.REDIS_ARTICLE_READ_COUNTS + ":" + article.getId());
        }
        logger.info("idSet:{}", idSet.toString());

        // 2.发起远程调用（restTemplate） 请求用户服务获得用户（idSet 发布者）的列表
        Map<String, AppUserVO> publisherMap = getBasicUserMap(idSet);
        // 发起redis的mget批量查询api，获得对应的值
        List<String> readCountsRedisList = redis.mget(idList);

        // 3. 拼接两个list 重组文章列表
        List<IndexArticleVO> indexArticleList = new ArrayList<>();
        for (int i = 0; i < articleList.size(); i++) {
            Article article = articleList.get(i);
            IndexArticleVO indexArticleVO = new IndexArticleVO();
            BeanUtils.copyProperties(article, indexArticleVO);

            // 3.1 从publisherList中获得发布者的基本信息
            AppUserVO publisher = publisherMap.get(article.getPublishUserId());
            indexArticleVO.setPublisherVO(publisher);

            // 3.2 重新组装设置文章列表中的阅读量
            String redisCountsStr = readCountsRedisList.get(i);
            int readCounts = 0;
            if (StringUtils.isNotBlank(redisCountsStr)) {
                readCounts = Integer.parseInt(redisCountsStr);
            }
            indexArticleVO.setReadCounts(readCounts);

            indexArticleList.add(indexArticleVO);
        }

        gridResult.setRows(indexArticleList);
    }

}
