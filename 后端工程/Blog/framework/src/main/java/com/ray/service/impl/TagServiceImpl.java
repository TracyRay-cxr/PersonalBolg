package com.ray.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.injector.methods.Update;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ray.domain.ResponseResult;
import com.ray.domain.dto.TagListDto;
import com.ray.domain.entity.Tag;
import com.ray.domain.vo.PageVo;
import com.ray.domain.vo.TagListVo;
import com.ray.domain.vo.TagVo;
import com.ray.enums.AppHttpCodeEnum;
import com.ray.exception.SystemException;
import com.ray.mapper.TagMapper;
import com.ray.service.TagService;
import com.ray.utils.BeanCopyUtils;
import com.ray.utils.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 标签(RayTag)表服务实现类
 *
 * @author makejava
 * @since 2023-02-09 17:24:38
 */
@Service("tagService")
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Override
    public ResponseResult<PageVo> pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto) {
        //分页查询
        LambdaQueryWrapper<Tag> queryWrapper =
                new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.hasText(tagListDto.getName()),Tag::getName,tagListDto.getName());
        queryWrapper.eq(StringUtils.hasText(tagListDto.getRemark()),Tag::getRemark,tagListDto.getRemark());
        Page<Tag> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        page(page,queryWrapper);
        //封装数据返回
        PageVo pageVo = new PageVo(page.getRecords(),page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult addTag(Tag tag) {
        //对参数判空
        if (!(StringUtils.hasText(tag.getName())&&StringUtils.hasText(tag.getRemark()))){
            throw new SystemException(AppHttpCodeEnum.NAMEANDREMARK_NOT_NULL);
        }
        save(tag);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult delTagById(Long userId) {
        if (Objects.isNull(userId)){
            throw new SystemException(AppHttpCodeEnum.COMMENT_NOT_NULL);
        }
        //逻辑删除
//        Tag tag = new Tag();
//        tag.setId(userId);
//        tag.setDelFlag(1);
//        tag.setUpdateTime(new Date());
//        tag.setUpdateBy(SecurityUtils.getUserId());
//        updateById(tag);
        //因为ymal全局配置了逻辑删除,所以mp自动把delete换成了update
        LambdaQueryWrapper<Tag> queryWrapper =
                new LambdaQueryWrapper<>();
        queryWrapper.eq(Tag::getId,userId);
        remove(queryWrapper);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult<TagVo> getTagById(Long userId) {
        if (Objects.isNull(userId)){
            throw new SystemException(AppHttpCodeEnum.COMMENT_NOT_NULL);
        }
        //查询对应的实体
        LambdaQueryWrapper<Tag> queryWrapper =
                new LambdaQueryWrapper<>();
        queryWrapper.eq(Tag::getId,userId);
        Tag tag = getOne(queryWrapper);
        //封装成Vo返回
        TagVo tagVo = BeanCopyUtils.copyBean(tag, TagVo.class);
        return ResponseResult.okResult(tagVo);
    }

    @Override
    public ResponseResult updateTag(Tag tag) {
        //判空
        if (Objects.isNull(tag)){
            throw new SystemException(AppHttpCodeEnum.COMMENT_NOT_NULL);
        }
        updateById(tag);
        return null;
    }

    @Override
    public ResponseResult<TagListVo> listAllTag() {
        //查询标签
        LambdaQueryWrapper<Tag> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(Tag::getId,Tag::getName);
        List<Tag> list = list(wrapper);
        //封装成Vo返回
        List<TagListVo> tagListVos = BeanCopyUtils.copyBeanList(list, TagListVo.class);
        return ResponseResult.okResult(tagListVos);
    }

}

