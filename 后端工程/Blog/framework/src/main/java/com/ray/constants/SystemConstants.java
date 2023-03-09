package com.ray.constants;

import io.swagger.models.auth.In;

public class SystemConstants
{
    /**
     * redis中浏览量的key
     */
    public static final String ARTICLE_VIEWCOUNT_KEY="article:viewCount";
    /**
     *  文章是草稿
     */
    public static final int ARTICLE_STATUS_DRAFT = 1;
    /**
     *  文章是正常分布状态
     */
    public static final int ARTICLE_STATUS_NORMAL = 0;
    /**
     *  分类是正常分布状态
     */
    public static final String STATUS_NORMAL="0";
    /**
     * 友链审核通过状态
     */
    public static final String LINK_STATUS_NORMAL="0";

    /**
     * 根评论的id
     */
    public static final Long ROOT_COMMENT_ID=-1L;
    /**
     * 文章评论类型
     */
    public static final String COMMENT_ARTICLE_TYPE = "0";
    /**
     * 友链评论类型
     */
    public static final String COMMENT_LINK_TYPE = "1";

    /**
     * 菜单类型
     */
    public static final String MENU = "C";

    /**
     * 按钮类型
     */
    public static final String BUTTON = "F";

    public static final String NORMAL = "0";
    public static final String ADMAIN = "1";
    public static final Integer DEL_FLAG =1 ;
}