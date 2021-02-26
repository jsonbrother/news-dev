package com.article.controller;

import com.api.BaseController;
import com.api.controller.article.CommentControllerApi;
import com.api.controller.user.UserControllerApi;
import com.article.service.CommentPortalService;
import com.constant.RedisConstant;
import com.pojo.bo.CommentReplyBO;
import com.pojo.vo.AppUserVO;
import com.result.NewsJSONResult;
import com.result.PagedGridResult;
import com.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Json
 * @date 2021/2/15 23:32
 */
@RestController
public class CommentController extends BaseController implements CommentControllerApi {

    private final Logger logger = LoggerFactory.getLogger(CommentController.class);

    private final CommentPortalService commentPortalService;
    private final UserControllerApi userControllerApi;

    @Autowired
    public CommentController(CommentPortalService commentPortalService, UserControllerApi userControllerApi) {
        this.commentPortalService = commentPortalService;
        this.userControllerApi = userControllerApi;
    }

    @Override
    public NewsJSONResult createArticle(@Valid CommentReplyBO commentReplyBO, BindingResult result) {

        // 1.判断BindingResult中是否保存了错误的验证信息
        if (result.hasErrors()) {
            Map<String, String> errorMap = getErrors(result);
            return NewsJSONResult.errorMap(errorMap);
        }

        // 2.根据留言用户的id查询他的昵称 用于存入到数据表进行字段的冗余处理 从而避免多表关联查询的性能影响
        String commentUserId = commentReplyBO.getCommentUserId();

        // 2. 发起restTemplate调用用户服务，获得用户侧昵称
        Set<String> idSet = new HashSet<>();
        idSet.add(commentUserId);
        String nickName = getBasicUserMap(idSet).get(commentUserId).getNickName();
        String face = getBasicUserMap(idSet).get(commentUserId).getFace();

        // 3. 保存用户评论的信息到数据库
        commentPortalService.createComment(commentReplyBO.getArticleId(), commentReplyBO.getFatherId(), commentReplyBO.getContent(),
                commentUserId, nickName, face);

        return NewsJSONResult.success();
    }

    @Override
    public NewsJSONResult commentCounts(String articleId) {

        // 1.获得文章评论数量
        Integer counts = getCountsFromRedis(RedisConstant.REDIS_ARTICLE_COMMENT_COUNTS + ":" + articleId);
        return NewsJSONResult.success(counts);
    }

    @Override
    public NewsJSONResult list(String articleId, Integer page, Integer pageSize) {
        if (page == null) {
            page = COMMON_START_PAGE;
        }
        if (pageSize == null) {
            pageSize = COMMON_PAGE_SIZE;
        }

        PagedGridResult gridResult = commentPortalService.queryArticleComments(articleId, page, pageSize);
        return NewsJSONResult.success(gridResult);
    }

    @Override
    public NewsJSONResult mng(String writerId, Integer page, Integer pageSize) {
        if (page == null) {
            page = COMMON_START_PAGE;
        }
        if (pageSize == null) {
            pageSize = COMMON_PAGE_SIZE;
        }

        PagedGridResult gridResult = commentPortalService.queryWriterCommentsMng(writerId, page, pageSize);
        return NewsJSONResult.success(gridResult);
    }

    @Override
    public NewsJSONResult delete(String writerId, String commentId) {
        commentPortalService.deleteComment(writerId, commentId);
        return NewsJSONResult.success();
    }

    /**
     * 发起远程调用 获得用户的基本信息
     *
     * @param idSet 用户id集合
     * @return 用户信息Map集合
     */
    private Map<String, AppUserVO> getBasicUserMap(Set idSet) {
        NewsJSONResult bodyResult = userControllerApi.queryByIds(JsonUtils.objectToJson(idSet));
        List<AppUserVO> userVOList = null;
        if (bodyResult != null && bodyResult.getStatus() == HttpStatus.OK.value()) {
            String userJson = JsonUtils.objectToJson(bodyResult.getData());
            userVOList = JsonUtils.jsonToList(userJson, AppUserVO.class);
        }

        assert userVOList != null;
        return userVOList.stream().collect(Collectors.toMap(AppUserVO::getId, Function.identity(), (k1, k2) -> k2));
    }
}
