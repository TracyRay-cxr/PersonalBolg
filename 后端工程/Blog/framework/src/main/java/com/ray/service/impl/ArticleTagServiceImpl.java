package com.ray.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ray.domain.entity.ArticleTag;
import com.ray.mapper.ArticleTagMapper;
import com.ray.service.ArticleTagService;
import org.springframework.stereotype.Service;

/**
 * 文章标签关联表(ArticleTag)表服务实现类
 * 多表联查没用到mp
 * @author makejava
 * @since 2023-02-15 10:32:37
 */
@Service("articleTagService")
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag> implements ArticleTagService {

}

