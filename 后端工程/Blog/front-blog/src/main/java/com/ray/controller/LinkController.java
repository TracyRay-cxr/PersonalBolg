package com.ray.controller;


import com.ray.domain.ResponseResult;
import com.ray.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 友链(Link)表控制层
 *
 * @author makejava
 * @since 2023-02-04 15:14:50
 */
@RestController
@RequestMapping("link")
public class LinkController {

    @Autowired
    private LinkService linkService;
    @GetMapping("getAllLink")
    public ResponseResult getAllLink(){
        return linkService.getAllLink();
    }
}

