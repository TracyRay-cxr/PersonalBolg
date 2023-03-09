package com.ray.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ray.constants.SystemConstants;
import com.ray.domain.ResponseResult;
import com.ray.domain.entity.Comment;
import com.ray.domain.entity.User;
import com.ray.domain.vo.CommentVo;
import com.ray.domain.vo.PageVo;
import com.ray.enums.AppHttpCodeEnum;
import com.ray.exception.SystemException;
import com.ray.mapper.CommentMapper;
import com.ray.service.CommentService;
import com.ray.service.UserService;
import com.ray.utils.BeanCopyUtils;
import com.ray.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 评论表(Comment)表服务实现类
 *
 * @author makejava
 * @since 2023-02-06 11:58:26
 */
@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    private UserService userService;

    @Override
    public ResponseResult commentList(String type,Long articleId, Integer pageNum, Integer pageSize) {
        //查询对应文章的根评论

        LambdaQueryWrapper<Comment> lambdaQueryWrapper =
                new LambdaQueryWrapper<>();
        //判断评论类型
        lambdaQueryWrapper.eq(Comment::getType,type);
        //articleId进行判断
        lambdaQueryWrapper.eq(SystemConstants.COMMENT_ARTICLE_TYPE.equals(type),Comment::getArticleId, articleId);
        //根评论的rootId为-1
        lambdaQueryWrapper.eq(Comment::getRootId, SystemConstants.ROOT_COMMENT_ID);
        lambdaQueryWrapper.orderByDesc(Comment::getCreateTime);
        //分页查询
        Page<Comment> page=new Page<>(pageNum,pageSize);
        page(page,lambdaQueryWrapper);
        //封装返回
        List<CommentVo> commentVoList = toCommentVoList(page.getRecords());
        //查询所有根评论对应的子评论集合，并且赋值给对应的属性
        for (CommentVo commentVo : commentVoList) {
            //查询对应的子评论
            List<CommentVo> children = getChildren(commentVo.getId());
            //赋值
            commentVo.setChildren(children);
        }
        return ResponseResult.okResult(new PageVo(commentVoList,page.getTotal()));
    }

    @Override
    public ResponseResult addComment(Comment comment) {
        //评论内容不能为空
        if (!StringUtils.hasText(comment.getContent())){
            throw new SystemException(AppHttpCodeEnum.COMMENT_NOT_NULL);
        }
        save(comment);
        return ResponseResult.okResult();
    }

    /**
     * 根据根评论id查询对应子评论集合
     * @param id 根评论id
     * @return
     */
    private List<CommentVo> getChildren(Long id) {
        LambdaQueryWrapper<Comment> lambdaQueryWrapper =
                new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Comment::getRootId,id);
        lambdaQueryWrapper.orderByDesc(Comment::getCreateTime);
        List<Comment> comments = list(lambdaQueryWrapper);
        List<CommentVo> commentVos = toCommentVoList(comments);
        return commentVos;
    }

    private List<CommentVo> toCommentVoList(List<Comment> list){
    List<CommentVo> commentVos =BeanCopyUtils.copyBeanList(list,CommentVo.class);
        //遍历Vos
      for (CommentVo commentVo : commentVos) {
          //通过creatyBy查询用户昵称并赋值
          String nickName = userService.getById(commentVo.getCreateBy()).getNickName();
          commentVo.setUsername(nickName);
          //通过toCommentUserId查询用户昵称并赋值
          //判断是否为根评论，根评论没有toCommentUserName的值会空指针
          if (commentVo.getToCommentId() != SystemConstants.ROOT_COMMENT_ID){

              String toCommentUserName = userService.getById(commentVo.getToCommentUserId()).getNickName();
              commentVo.setToCommentUserName(toCommentUserName);
          }

      }
      return commentVos;
}
}


