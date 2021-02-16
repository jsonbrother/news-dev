package com.article.task;

import com.article.service.ArticleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;

/**
 * @author Json
 * @date 2021/2/10 23:52
 */
//@Configuration
//@EnableScheduling
public class TaskPublishArticles {

    private final Logger logger = LoggerFactory.getLogger(TaskPublishArticles.class);

    private final ArticleService articleService;

    @Autowired
    public TaskPublishArticles(ArticleService articleService) {
        this.articleService = articleService;
    }

    @Scheduled(cron = "0/3 * * * * ?")
    private void publishArticles() {
        logger.info("执行定时任务:{}", LocalDateTime.now());

        articleService.updateAppointToPublish();
    }
}
