package com.article.mapper;

import com.my.mapper.MyMapper;
import com.pojo.Article;
import org.springframework.stereotype.Repository;

/**
 * @author Json
 * @date 2021/2/10 20:07
 */
@Repository
public interface ArticleMapper extends MyMapper<Article> {

    /**
     * 更新定时发布为即时发布
     */
    void updateAppointToPublish();
}
