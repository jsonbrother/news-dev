package com.article.mapper;

import com.my.mapper.MyMapper;
import com.pojo.Comments;
import com.pojo.vo.CommentsVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author Json
 * @date 2021/2/15 23:34
 */
@Repository
public interface CommentsMapper extends MyMapper<Comments> {

    /**
     * 查询文章评论
     */
    List<CommentsVO> queryArticleCommentList(@Param("paramMap") Map<String, Object> map);
}
