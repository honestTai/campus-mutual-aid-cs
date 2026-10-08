package com.confession.back.api.controller;

import com.confession.back.api.bean.general.result.Result;
import com.confession.back.util.SystemLog;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/upload")
public class UpLoadController {

    private String filePath = "G:/system/confess/";

    private String fileUrl = "http://127.0.0.1:8776/api/confess/";

    @PostMapping("/upload")
    @ResponseBody
    @SystemLog(info = "上传图片文件")
    public Result uploadImgAddUser(@RequestParam("image") MultipartFile uploadFile) throws Exception {
        String fileName = uploadFile.getOriginalFilename();
        fileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "_" + fileName;
        //加个时间戳，尽量避免文件名称重复
        String path = filePath + fileName;
        //创建文件路径
        java.io.File dest = new java.io.File(path);
        try {
            //保存文件
            uploadFile.transferTo(dest);
            //构造Url
            return Result.success(fileUrl + fileName);
        } catch (IOException e) {
            throw new Exception(e.getMessage());
        }
    }
}
