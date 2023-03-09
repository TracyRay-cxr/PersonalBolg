package com.ray.controller;

import com.ray.domain.ResponseResult;
import com.ray.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UploadController {
    @Autowired
    private UploadService uploadService;

    @PostMapping("upload")
    public ResponseResult uploadImg(@RequestParam(name = "img") MultipartFile file){
        return uploadService.uploadImg(file);
    }
}
