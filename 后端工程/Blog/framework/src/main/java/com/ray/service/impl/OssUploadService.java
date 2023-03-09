package com.ray.service.impl;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.ray.domain.ResponseResult;
import com.ray.enums.AppHttpCodeEnum;
import com.ray.exception.SystemException;
import com.ray.service.UploadService;
import com.ray.utils.PathUtils;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * 用于oss服务逻辑层
 */
@Data
@Service
@ConfigurationProperties(prefix = "oss")
public class OssUploadService implements UploadService {

    private String accessKey;
    private String secretKey;
    private String bucket;
    private String domainName;

    @Override
    public ResponseResult uploadImg(MultipartFile file) {

        //TODO 判断文件类型或大小
        //获取原始文件名
        String originalFilename = file.getOriginalFilename();
        //对原始文件名进行判断
        if (!(originalFilename.endsWith(".png")||originalFilename.endsWith(".jpg"))){
            throw new SystemException(AppHttpCodeEnum.FILE_TYPE_ERROR);
        }
        //上传文件到oss
        String filePath = PathUtils.generateFilePath(originalFilename);
        String url =uploadOss(file,filePath);
        return ResponseResult.okResult(url);
    }

    private String uploadOss(MultipartFile file,String filePath){
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.autoRegion());
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;// 指定分片上传版本
//...其他参数参考类注释

        UploadManager uploadManager = new UploadManager(cfg);
//...生成上传凭证，然后准备上传
//        String accessKey = "accessKey";
//        String secretKey = "secretKey";
//        String bucket = "bucket";

//默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = filePath;

        try {
            InputStream inputStream = file.getInputStream();
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);

            try {
                Response response = uploadManager.put(inputStream,key,upToken,null, null);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                System.out.println(putRet.key);
                System.out.println(putRet.hash);
                //返回外链
                return "http://"+domainName+"/"+key;
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    //ignore
                }
            }
        } catch (Exception ex) {
            //ignore
        }
        return "外链地址";
    }
}
