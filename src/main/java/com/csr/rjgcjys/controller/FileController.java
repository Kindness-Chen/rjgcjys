//package com.csr.rjgcjys.controller;
//
//import com.csr.rjgcjys.service.FileService;
//import com.csr.rjgcjys.tools.WordUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.File;
//import java.io.IOException;
//
//@Controller
//public class FileController {
//    private static final Logger LOGGER = LoggerFactory.getLogger(FileController.class);
//    @Autowired
//    private FileService fileService;
//
//    @PostMapping("/insertModel")
//    @ResponseBody
//    public String insertModel( @RequestParam("fileName") MultipartFile [] files) {
//        String url = "static/ModelWord/";//保存在数据库中的路径
//        StringBuilder sb = new StringBuilder();
//        for(int i=0;i<files.length;i++) {
//            MultipartFile file = files[i];
//            String originalFilename = file.getOriginalFilename();
//            String extName = WordUtils.getExtensionName(originalFilename);
//            String fileName = WordUtils.getFileNameNoEx(originalFilename);
//            //加个时间戳，尽量避免文件名称重复
//            //        String path =  "F:/Word/" + fileName;    F:\IDEA-workspace\rjgcjys\
//            String path = System.getProperty("user.dir") + "/src/main/resources/static/ModelWord/" + fileName + extName;//文件保存相对路径
//            System.out.println("==========" + path);
//            File dest = new File(path);
//            //判断文件是否已经存在
//            if (dest.exists()) {
//                sb.append(originalFilename+"[已存在！]");
//            }
//            //判断文件父目录是否存在
//            if (!dest.getParentFile().exists()) {
//                dest.getParentFile().mkdir();
//            }
//            try {
//                file.transferTo(dest);
//                LOGGER.info("上传成功");
//                url = url + fileName + extName;//本地运行项目
//                fileService.insertFile(fileName, extName, url,null);
//                sb.append(originalFilename+"【上传成功!!!】");
//            } catch (IOException e) {
//                LOGGER.error(e.toString(), e);
//            }
//
//        }
////        if(sb.length() > 0) {
////            sb.append("文件已提交过，请勿重复提交!!!");
////        }
//
//      return sb.toString();
//}
//
//}
