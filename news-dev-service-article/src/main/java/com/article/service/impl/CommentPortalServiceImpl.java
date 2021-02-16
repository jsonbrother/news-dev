package com.article.service.impl;

import com.api.service.BaseService;
import com.article.mapper.CommentsMapper;
import com.article.service.ArticlePortalService;
import com.article.service.CommentPortalService;
import com.constant.RedisConstant;
import com.github.pagehelper.PageHelper;
import com.pojo.Comments;
import com.pojo.vo.ArticleDetailVO;
import com.pojo.vo.CommentsVO;
import com.result.PagedGridResult;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Json
 * @date 2021/2/15 23:34
 */
@Service
public class CommentPortalServiceImpl extends BaseService implements CommentPortalService {

    private final Sid sid;
    private final ArticlePortalService articlePortalService;
    private final CommentsMapper commentsMapper;

    @Autowired
    public CommentPortalServiceImpl(Sid sid, ArticlePortalService articlePortalService, CommentsMapper commentsMapper) {
        this.sid = sid;
        this.articlePortalService = articlePortalService;
        this.commentsMapper = commentsMapper;
    }

    @Transactional
    @Override
    public void createComment(String articleId, String fatherCommentId, String content, String userId, String nickname, String face) {
        String commentId = sid.nextShort();

        ArticleDetailVO article = articlePortalService.queryDetail(articleId);

        Comments comments = new Comments();
        comments.setId(commentId);

        comments.setWriterId(article.getPublishUserId());
        comments.setArticleTitle(article.getTitle());
        comments.setArticleCover(article.getCover());
        comments.setArticleId(articleId);

        comments.setFatherId(fatherCommentId);
        comments.setCommentUserId(userId);
        comments.setCommentUserNickname(nickname);
        comments.setCommentUserFace(face);

        comments.setContent(content);
        comments.setCreateTime(new Date());

        commentsMapper.insert(comments);

        // 评论数累加
        redis.increment(RedisConstant.REDIS_ARTICLE_COMMENT_COUNTS + ":" + articleId, 1);
    }

    @Override
    public PagedGridResult queryArticleComments(String articleId, Integer page, Integer pageSize) {
        Map<String, Object> map = new HashMap<>();
        map.put("articleId", articleId);

        PageHelper.startPage(page, pageSize);
        List<CommentsVO> list = commentsMapper.queryArticleCommentList(map);
        return setterPagedGrid(list, page);
    }

    @Override
    public PagedGridResult queryWriterCommentsMng(String writerId, Integer page, Integer pageSize) {
        Comments comment = new Comments();
        comment.setWriterId(writerId);

        PageHelper.startPage(page, pageSize);
        List<Comments> list = commentsMapper.select(comment);
        return setterPagedGrid(list, page);
    }

    @Override
    public void deleteComment(String writerId, String commentId) {
        Comments comment = new Comments();
        comment.setId(commentId);
        comment.setWriterId(writerId);

        commentsMapper.delete(comment);
    }
}
