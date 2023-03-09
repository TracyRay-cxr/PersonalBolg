package com.ray.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ray.domain.ResponseResult;
import com.ray.domain.entity.Comment;


/**
 * 评论表(Comment)表服务接口
 *
 * @author makejava
 * @since 2023-02-06 11:58:26
 */

public interface CommentService extends IService<Comment> {

    ResponseResult commentList(String type,Long articleId, Integer pageNum, Integer pageSize);

    ResponseResult addComment(Comment comment);
}

