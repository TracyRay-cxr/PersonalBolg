package com.ray.controller;

import com.ray.domain.ResponseResult;
import com.ray.domain.dto.LinkDto;
import com.ray.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/link")
public class LinkController {
    @Autowired
    private LinkService linkService;

    @GetMapping("/list")
    public ResponseResult listLink(Integer pageNum ,Integer pageSize, String  name,String status){
        return  ResponseResult.okResult(linkService.listLink(pageNum,pageSize,name,status));
    }
    @PostMapping()
    public ResponseResult addLink(@RequestBody LinkDto linkDto){
        return linkService.addLink(linkDto);
    }
    @GetMapping("/{id}")
    public ResponseResult getLinkById(@PathVariable("id")Long id){
        return ResponseResult.okResult(linkService.getLinkById(id));
    }
    @PutMapping()
    public ResponseResult updateLink(@RequestBody LinkDto linkDto){
        return linkService.updateLink(linkDto);
    }
    @DeleteMapping("/{id}")
    public ResponseResult delLink(@PathVariable("id")Long id){
        return linkService.dellink(id);
    }
}
