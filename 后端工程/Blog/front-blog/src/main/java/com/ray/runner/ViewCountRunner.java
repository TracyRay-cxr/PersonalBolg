package com.ray.runner;

import com.ray.constants.SystemConstants;
import com.ray.domain.entity.Article;
import com.ray.mapper.ArticleMapper;
import com.ray.utils.RedisCache;
import io.lettuce.core.RedisChannelHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class ViewCountRunner implements CommandLineRunner {
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private RedisCache redisCache;

    @Override
    public void run(String... args) throws Exception {
        //查询博客信息 id viewCount
        List<Article> articles = articleMapper.selectList(null);
        Map<String, Integer> viewCountMap = articles.stream()
                .collect(Collectors.toMap(article1 -> article1.getId().toString(), article -> article.getViewCount().intValue()));

        //存储到redistribution中
        redisCache.setCacheMap(SystemConstants.ARTICLE_VIEWCOUNT_KEY,viewCountMap);

    }
}
