package com.ray.domain.entity;


import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * 文章标签关联表(ArticleTag)表实体类
 *
 * @author makejava
 * @since 2023-02-15 10:32:37
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("ray_article_tag")
public class ArticleTag implements Serializable {
    private static final long serialVersionUID = 625337492348897098L;
    //文章id
    @TableId
    private Long articleId;
    //标签id
    private Long tagId;


    public ArticleTag(Long id, Integer tagId) {
        this.articleId=id;
        this.tagId=tagId.longValue();
    }
}

