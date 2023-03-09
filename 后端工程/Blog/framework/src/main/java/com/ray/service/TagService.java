package com.ray.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ray.domain.ResponseResult;
import com.ray.domain.dto.TagListDto;
import com.ray.domain.entity.Tag;
import com.ray.domain.vo.PageVo;
import com.ray.domain.vo.TagListVo;
import com.ray.domain.vo.TagVo;


/**
 * 标签(Tag)表服务接口
 *
 * @author makejava
 * @since 2023-02-09 17:24:38
 */
public interface TagService extends IService<Tag> {

    ResponseResult<PageVo> pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto);

    ResponseResult addTag(Tag tag);

    ResponseResult delTagById(Long userId);

    ResponseResult<TagVo> getTagById(Long userId);

    ResponseResult updateTag(Tag tag);

    ResponseResult<TagListVo> listAllTag();
}

