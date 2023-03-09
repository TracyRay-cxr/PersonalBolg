package com.ray.domain.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentVo {
    private Long id;

    //文章id
    private Long articleId;
    //根评论id
    private Long rootId;
    //评论内容
    private String content;
    //所回复的目标评论人的userid
    private Long toCommentUserId;
    // 目标评论人的name
    private String toCommentUserName;
    //回复目标评论id
    private Long toCommentId;
    //评论人id
    private Long createBy;

    private Date createTime;
    //评论人的name
    private String username;
    //子评论
    private List<CommentVo> children;
}
