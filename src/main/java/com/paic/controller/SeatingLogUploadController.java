package com.paic.controller;

import com.paic.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@RestController
@Slf4j
public class SeatingLogUploadController {

    @Value("${upload.file.to.server.save.directory}")
    public String uploadFileDirectory;

    /**
     * 打成jar包运行的时候，return 里面页面的路径不能以 / 开头
     *
     * @return
     */
    @RequestMapping(value = "/index")
    public ModelAndView index() {
        return new ModelAndView("upload/upload.html");
    }

    @RequestMapping("/upload")
    public String uploadByUMUserName(@RequestParam("files") MultipartFile[] multipartFiles) {
        if (multipartFiles.length == 0) {
            return "请选择上传的文件!";
        }
        boolean uploadFlag = true;
        for (MultipartFile multipartFile : multipartFiles) {
            boolean flag = FileUtil.uploadFile(multipartFile, uploadFileDirectory);
            if (uploadFlag && !flag) {
                uploadFlag = flag;
            }
        }
        String returnMsg = "上传成功.";
        if (!uploadFlag) {
            returnMsg = "部分文件上传成功!";
        }
        return returnMsg;
    }

}
