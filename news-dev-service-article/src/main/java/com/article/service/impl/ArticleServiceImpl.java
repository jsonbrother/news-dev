package com.article.service.impl;

import com.api.service.BaseService;
import com.article.mapper.ArticleMapper;
import com.article.service.ArticleService;
import com.constant.RoutingConstant;
import com.enums.*;
import com.exception.NewsException;
import com.github.pagehelper.PageHelper;
import com.mongodb.client.gridfs.GridFSBucket;
import com.pojo.Article;
import com.pojo.Category;
import com.pojo.bo.NewArticleBO;
import com.result.NewsJSONResult;
import com.result.PagedGridResult;
import com.utils.extend.AliTextReviewUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

/**
 * @author Json
 * @date 2021/2/10 20:02
 */
@Service
public class ArticleServiceImpl extends BaseService implements ArticleService {

    private final ArticleMapper articleMapper;
    private final Sid sid;
    private final AliTextReviewUtils aliTextReviewUtils;
    private final GridFSBucket gridFSBucket;
    public final RestTemplate restTemplate;

    @Autowired
    public ArticleServiceImpl(ArticleMapper articleMapper, Sid sid, AliTextReviewUtils aliTextReviewUtils,
                              GridFSBucket gridFSBucket, RestTemplate restTemplate) {
        this.articleMapper = articleMapper;
        this.sid = sid;
        this.aliTextReviewUtils = aliTextReviewUtils;
        this.gridFSBucket = gridFSBucket;
        this.restTemplate = restTemplate;
    }

    @Transactional
    @Override
    public void createArticle(NewArticleBO newArticleBO, Category category) {
        String articleId = sid.nextShort();

        Article article = new Article();
        BeanUtils.copyProperties(newArticleBO, article);

        article.setId(articleId);
        article.setCategoryId(category.getId());
        article.setArticleStatus(ArticleReviewStatus.REVIEWING.type);
        article.setCommentCounts(0);
        article.setReadCounts(0);

        article.setIsDelete(YesOrNo.NO.type);
        article.setCreateTime(new Date());
        article.setUpdateTime(new Date());

        if (article.getIsAppoint().equals(ArticleAppointType.TIMING.type)) {
            article.setPublishTime(newArticleBO.getPublishTime());
        } else if (article.getIsAppoint().equals(ArticleAppointType.IMMEDIATELY.type)) {
            article.setPublishTime(new Date());
        }

        int res = articleMapper.insert(article);
        if (res != 1) {
            NewsException.display(ResponseStatusEnum.ARTICLE_CREATE_ERROR);
        }

        // 通过阿里智能AI实现对文章文本的自动检测（自动审核）
        // String reviewTextResult = aliTextReviewUtils.reviewTextContent(newArticleBO.getContent());
        String reviewTextResult = ArticleReviewLevel.REVIEW.type;
        if (reviewTextResult.equalsIgnoreCase(ArticleReviewLevel.PASS.type)) {
            // 修改当前的文章，状态标记为审核通过
            this.updateArticleStatus(articleId, ArticleReviewStatus.SUCCESS.type);
            // TODO 文章审核通过需要生成静态化页面
        } else if (reviewTextResult.equalsIgnoreCase(ArticleReviewLevel.REVIEW.type)) {
            // 修改当前的文章，状态标记为需要人工审核
            this.updateArticleStatus(articleId, ArticleReviewStatus.WAITING_MANUAL.type);
        } else if (reviewTextResult.equalsIgnoreCase(ArticleReviewLevel.BLOCK.type)) {
            // 修改当前的文章，状态标记为审核未通过
            this.updateArticleStatus(articleId, ArticleReviewStatus.FAILED.type);
        }
    }

    @Transactional
    @Override
    public void updateAppointToPublish() {
        articleMapper.updateAppointToPublish();
    }

    @Override
    public PagedGridResult queryMyArticleList(String userId, String keyword, Integer status, Date startDate, Date endDate, Integer page, Integer pageSize) {
        Example example = new Example(Article.class);
        example.orderBy("createTime").desc();
        Example.Criteria criteria = example.createCriteria();

        criteria.andEqualTo("publishUserId", userId);

        if (StringUtils.isNotBlank(keyword)) {
            criteria.andLike("title", "%" + keyword + "%");
        }

        if (ArticleReviewStatus.isArticleStatusValid(status)) {
            criteria.andEqualTo("articleStatus", status);
        }

        if (status != null && status == 12) {
            criteria.andEqualTo("articleStatus", ArticleReviewStatus.REVIEWING.type)
                    .orEqualTo("articleStatus", ArticleReviewStatus.WAITING_MANUAL.type);
        }

        criteria.andEqualTo("isDelete", YesOrNo.NO.type);

        if (startDate != null) {
            criteria.andGreaterThanOrEqualTo("publishTime", startDate);
        }
        if (endDate != null) {
            criteria.andLessThanOrEqualTo("publishTime", endDate);
        }

        PageHelper.startPage(page, pageSize);
        List<Article> list = articleMapper.selectByExample(example);
        return setterPagedGrid(list, page);
    }

    @Transactional
    @Override
    public void updateArticleStatus(String articleId, Integer pendingStatus) {
        Example example = new Example(Article.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id", articleId);

        Article pendingArticle = new Article();
        pendingArticle.setArticleStatus(pendingStatus);

        int res = articleMapper.updateByExampleSelective(pendingArticle, example);
        if (res != 1) {
            NewsException.display(ResponseStatusEnum.ARTICLE_REVIEW_ERROR);
        }
    }

    @Transactional
    @Override
    public void updateArticleToGridFS(String articleId, String articleMongoId) {
        Article pendingArticle = new Article();
        pendingArticle.setId(articleId);
        pendingArticle.setMongoFileId(articleMongoId);
        articleMapper.updateByPrimaryKeySelective(pendingArticle);
    }

    @Override
    public PagedGridResult queryAllArticleListAdmin(Integer status, Integer page, Integer pageSize) {
        Example articleExample = new Example(Article.class);
        articleExample.orderBy("createTime").desc();

        Example.Criteria criteria = articleExample.createCriteria();
        if (ArticleReviewStatus.isArticleStatusValid(status)) {
            criteria.andEqualTo("articleStatus", status);
        }

        // 审核中是机审和人审核的两个状态 所以需要单独判断
        if (status != null && status == 12) {
            criteria.andEqualTo("articleStatus", ArticleReviewStatus.REVIEWING.type)
                    .orEqualTo("articleStatus", ArticleReviewStatus.WAITING_MANUAL.type);
        }

        //isDelete 必须是0
        criteria.andEqualTo("isDelete", YesOrNo.NO.type);

        PageHelper.startPage(page, pageSize);
        List<Article> list = articleMapper.selectByExample(articleExample);
        return setterPagedGrid(list, page);
    }

    @Transactional
    @Override
    public void deleteArticle(String userId, String articleId) {
        Example articleExample = makeExampleCriteria(userId, articleId);

        Article pending = new Article();
        pending.setIsDelete(YesOrNo.YES.type);

        int result = articleMapper.updateByExampleSelective(pending, articleExample);
        if (result != 1) {
            NewsException.display(ResponseStatusEnum.ARTICLE_DELETE_ERROR);
        }

        deleteHTML(articleId);
    }

    @Transactional
    @Override
    public void withdrawArticle(String userId, String articleId) {
        Example articleExample = makeExampleCriteria(userId, articleId);

        Article pending = new Article();
        pending.setArticleStatus(ArticleReviewStatus.WITHDRAW.type);

        int result = articleMapper.updateByExampleSelective(pending, articleExample);
        if (result != 1) {
            NewsException.display(ResponseStatusEnum.ARTICLE_WITHDRAW_ERROR);
        }

        // 删除消费端文章HTML
        doDeleteArticleHTML(articleId);
    }

    /**
     * 文章删除后删除gridFs与消费端的HTML
     */
    private void deleteHTML(String articleId) {
        // 1. 查询文章的mongoFileId
        Article pending = articleMapper.selectByPrimaryKey(articleId);
        String articleMongoId = pending.getMongoFileId();

        // 2. 删除gridFS上的文件
        gridFSBucket.delete(new ObjectId(articleMongoId));

        // 3. 删除消费端的HTML文件
        doDeleteArticleHTML(articleId);
    }

    /**
     * 删除消费端文章HTML
     */
    private void doDeleteArticleHTML(String articleId) {
        String url = RoutingConstant.ARTICLE_MAKER_DELETE + "?articleId=" + articleId;
        ResponseEntity<NewsJSONResult> responseEntity = restTemplate.getForEntity(url, NewsJSONResult.class);
        NewsJSONResult bodyResult = responseEntity.getBody();
        if (bodyResult != null && bodyResult.getStatus() != HttpStatus.OK.value()) {
            NewsException.display(ResponseStatusEnum.SYSTEM_OPERATION_ERROR);
        }
    }


    private Example makeExampleCriteria(String userId, String articleId) {
        Example articleExample = new Example(Article.class);
        Example.Criteria criteria = articleExample.createCriteria();
        criteria.andEqualTo("publishUserId", userId);
        criteria.andEqualTo("id", articleId);
        return articleExample;
    }

}
