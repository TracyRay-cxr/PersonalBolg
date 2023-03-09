package com.ray.job;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.ray.constants.SystemConstants;
import com.ray.domain.entity.Article;
import com.ray.service.ArticleService;
import com.ray.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class UpdateViewCountJob {
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private ArticleService articleService;
    @Scheduled(cron = "0 0/15 * * * ?")
    @Transactional
    public void updateViewCount(){
        //获取redis中的浏览量
        Map<String, Integer> viewCountMap = redisCache.getCacheMap(SystemConstants.ARTICLE_VIEWCOUNT_KEY);
        List<Article> articles = viewCountMap.entrySet()
                .stream()
                .map(new Function<Map.Entry<String, Integer>, Article>() {
                    @Override
                    public Article apply(Map.Entry<String, Integer> entry) {
                        return new Article(Long.valueOf(entry.getKey()), entry.getValue().longValue());
                    }
                })
                .collect(Collectors.toList());
        //存入数据库中,IService中实现了批量操作直接利用
//        articleService.updateBatchById(articles);
        for (Article article : articles) {
            LambdaUpdateWrapper<Article> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(Article :: getId, article.getId());
            updateWrapper.set(Article :: getViewCount, article.getViewCount());
            articleService.update(updateWrapper);
        }
    }

}
