package com.ray.controller;

import com.ray.domain.ResponseResult;
import com.ray.domain.dto.TagListDto;
import com.ray.domain.entity.Tag;
import com.ray.domain.vo.PageVo;
import com.ray.domain.vo.TagListVo;
import com.ray.domain.vo.TagVo;
import com.ray.enums.AppHttpCodeEnum;
import com.ray.exception.SystemException;
import com.ray.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/tag")
public class TagController {
    @Autowired
    private TagService tagService;

    @GetMapping("/list")
    public ResponseResult<PageVo> list(Integer pageNum, Integer pageSize,TagListDto tagListDto){
        return tagService.pageTagList(pageNum,pageSize,tagListDto);
    }

    @PostMapping
    public ResponseResult addTag(@RequestBody Tag tag){
        return  tagService.addTag(tag);
    }
    @DeleteMapping ("/{id}")
    private ResponseResult delTagById(@PathVariable("id") Long userId){
        return tagService.delTagById(userId);
    }
    @GetMapping("/{id}")
    public ResponseResult<TagVo> getTagById(@PathVariable("id") Long userId){
        return tagService.getTagById(userId);
    }
    @PutMapping
    public ResponseResult updateTag(@RequestBody Tag tag){
        return tagService.updateTag(tag);
    }
    @GetMapping("/listAllTag")
    public ResponseResult<TagListVo> listAllTag(){
      return tagService.listAllTag();
    }

}
