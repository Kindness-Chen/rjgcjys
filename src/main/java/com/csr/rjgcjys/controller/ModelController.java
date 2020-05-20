package com.csr.rjgcjys.controller;

import com.alibaba.fastjson.JSONObject;
import com.csr.rjgcjys.entities.Model;
import com.csr.rjgcjys.entities.PPT;
import com.csr.rjgcjys.entities.Train;
import com.csr.rjgcjys.entities.User;
import com.csr.rjgcjys.entities.Week;
import com.csr.rjgcjys.service.ModelService;
import com.csr.rjgcjys.service.TrainService;
import com.csr.rjgcjys.service.WeekService;
import com.csr.rjgcjys.tools.WordUtils;
import com.zhenzi.sms.ZhenziSmsClient;
import org.apache.commons.io.IOUtils;
import org.jodconverter.DocumentConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ModelController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ModelController.class);
    //短信平台相关参数
    private String apiUrl = "https://sms_developer.zhenzikj.com";
    private String appId = "104726";
    private String appSecret = "a63a9465-9e69-425e-b23c-a672dd5f4228";
    @Autowired
    private ModelService modelService;
    @Autowired
    private TrainService trainService;
    @Autowired
    private WeekService weekService;
    // 第一步：转换器直接注入
    @Autowired
    private DocumentConverter converter;
    @Autowired
    private HttpServletResponse response;
    @Autowired
    private HttpServletRequest request;

    /**
     *
     * @return 返回老师上传学生作业样板页面
     */
    @GetMapping("/uploadWordModel")
    public String uploadWordModel(@RequestParam("name")String name, ModelMap modelMap){
        List<Train>trains = trainService.findSclassAll();
        modelMap.addAttribute("trains",trains);
        List<Week> weeks = weekService.findSclassByNameStatus(name);
        modelMap.addAttribute("weeks",weeks);
        return "teachers/teachersUploadWordModel.html";
    }
    /**
     * 上传学生作业样板
     * @param username 前端传过来的教工号
     * @param name 姓名
     * @param sclass 课程
     * @param status 状态
     * @param file1 样板1
     * @param file2 样板2
     * @param file3 样板3
     * @param file4 样板4
     * @param file5 样板5
     * @param file6 样板6
     * @return 返回json数据到前端进行判断输入是否合法
     */
    @PostMapping("/insertModel")
    @ResponseBody
    public String insertModel(@RequestParam("username")String username,@RequestParam("name")String name,@RequestParam("sclass")String sclass,@RequestParam("status")String status,@RequestParam("fileName1") MultipartFile file1,@RequestParam("fileName2") MultipartFile file2,@RequestParam("fileName3") MultipartFile file3,@RequestParam("fileName4") MultipartFile file4,@RequestParam("fileName5") MultipartFile file5,@RequestParam("fileName6") MultipartFile file6) {
        String fileUrl1 = "static/ModelWord/";//保存在数据库中的路径
        String fileUrl2 = "static/ModelWord/";//保存在数据库中的路径
        String fileUrl3 = "static/ModelWord/";//保存在数据库中的路径
        String fileUrl4 = "static/ModelWord/";//保存在数据库中的路径
        String fileUrl5 = "static/ModelWord/";//保存在数据库中的路径
        String fileUrl6 = "static/ModelWord/";//保存在数据库中的路径
        Model model = new Model();
        model.setUsername(username);
        model.setName(name);
        model.setSclass(sclass);
        model.setStatus(status);
        String originalFilename1 = file1.getOriginalFilename();
        String originalFilename2 = file2.getOriginalFilename();
        String originalFilename3 = file3.getOriginalFilename();
        String originalFilename4 = file4.getOriginalFilename();
        String originalFilename5 = file5.getOriginalFilename();
        String originalFilename6 = file6.getOriginalFilename();
        //String extName = WordUtils.getExtensionName(originalFilename);
        String fileName1 = WordUtils.getFileNameNoEx(originalFilename1);
        String fileName2 = WordUtils.getFileNameNoEx(originalFilename2);
        String fileName3 = WordUtils.getFileNameNoEx(originalFilename3);
        String fileName4 = WordUtils.getFileNameNoEx(originalFilename4);
        String fileName5 = WordUtils.getFileNameNoEx(originalFilename5);
        String fileName6 = WordUtils.getFileNameNoEx(originalFilename6);
        String fileExtName1 = WordUtils.getExtensionName(originalFilename1);
        String fileExtName2 = WordUtils.getExtensionName(originalFilename2);
        String fileExtName3 = WordUtils.getExtensionName(originalFilename3);
        String fileExtName4 = WordUtils.getExtensionName(originalFilename4);
        String fileExtName5 = WordUtils.getExtensionName(originalFilename5);
        String fileExtName6 = WordUtils.getExtensionName(originalFilename6);
        //加个时间戳，尽量避免文件名称重复
        //        String path =  "F:/Word/" + fileName;    F:\IDEA-workspace\rjgcjys\
        String path1 = System.getProperty("user.dir") + "/src/main/resources/static/ModelWord/" + fileName1;//文件保存相对路径
        String path2 = System.getProperty("user.dir") + "/src/main/resources/static/ModelWord/" + fileName2;//文件保存相对路径
        String path3 = System.getProperty("user.dir") + "/src/main/resources/static/ModelWord/" + fileName3;//文件保存相对路径
        String path4 = System.getProperty("user.dir") + "/src/main/resources/static/ModelWord/" + fileName4;//文件保存相对路径
        String path5 = System.getProperty("user.dir") + "/src/main/resources/static/ModelWord/" + fileName5;//文件保存相对路径
        String path6 = System.getProperty("user.dir") + "/src/main/resources/static/ModelWord/" + fileName6;//文件保存相对路径
        File dest1 = new File(path1);
        File dest2 = new File(path2);
        File dest3 = new File(path3);
        File dest4 = new File(path4);
        File dest5 = new File(path5);
        File dest6 = new File(path6);
        //判断文件是否已经存在
        if (dest1.exists()) {
            return "样板1文件您已提交过，请勿重复提交!!!";
        }
        if (dest2.exists()) {
            return "样板2文件您已提交过，请勿重复提交!!!";
        }
        if (dest3.exists()) {
            return"样板3文件您已提交过，请勿重复提交!!!";
        }
        if (dest4.exists()) {
            return "样板4文件您已提交过，请勿重复提交!!!";
        }
        if (dest5.exists()) {
            return "样板5文件您已提交过，请勿重复提交!!!";
        }
        if (dest6.exists()) {
            return "样板6文件您已提交过，请勿重复提交!!!";
        }
        //判断文件父目录是否存在
        if (!dest1.getParentFile().exists()) {
            dest1.getParentFile().mkdir();
        }
        try {
            file1.transferTo(dest1);
            file2.transferTo(dest2);
            file3.transferTo(dest3);
            file4.transferTo(dest4);
            file5.transferTo(dest5);
            file6.transferTo(dest6);
            LOGGER.info("上传成功");
            fileUrl1 = fileUrl1+fileName1 + fileExtName1;//本地运行项目
            fileUrl2 = fileUrl2+fileName2 + fileExtName2;
            fileUrl3 = fileUrl3+fileName3 + fileExtName3;
            fileUrl4 = fileUrl4+fileName4 + fileExtName4;
            fileUrl5 = fileUrl5+fileName5 + fileExtName5;
            fileUrl6 = fileUrl6+fileName6 + fileExtName6;
            modelService.insertModel(username, name, sclass, status, fileName1, fileName2, fileName3, fileName4, fileName5, fileName6, fileUrl1, fileUrl2, fileUrl3, fileUrl4, fileUrl5, fileUrl6);
            return "上传成功!!!";
        } catch (IOException e) {
            LOGGER.error(e.toString(), e);
        }
        return "上传失败！";
    }

    /**
     * 查看待审核的作业样板
     * @param username 前端传过来的教工号
     * @param modelMap 将数据保存在modelMap中
     * @return 返回教师查看待审核作业样板页面
     */
    @GetMapping("/findModelByUsername/{username}")
    public String findModelByUsername(@PathVariable("username")String username, ModelMap modelMap){
        List<Model>models = modelService.findModelByUsername(username);
        modelMap.addAttribute("models",models);
        return "teachers/teachersLookNoModel.html";
    }

    /**
     * 教研室主任查看全部待审核作业样板
     * @param modelMap 将查询到的数据保存在modelMap中
     * @return 返回到教研室主任查看待审核作业样板页面
     */
    @GetMapping("/findModelAll")
    public String findModelAll(ModelMap modelMap){
        List<Model>models = modelService.findModelAll();
        modelMap.addAttribute("models",models);
        return "directors/directorsCheckModel.html";
    }

    /**
     * 查询审核的特定数据并回显
     * @param mid 前端传过来的mid
     * @return 返回mid查询到的数据
     */
    @GetMapping("/findModelByMid")
    @ResponseBody
    public Model findModelByMid(Integer mid){
        return modelService.findModelByMid(mid);
    }
    /**
     * 对上传到服务器的文件进行预览
     * @param mid 前端传到后台的mid进行特定mid查询
     * @return 判断服务器是否存在预览的文件，不存在返回错误页面
     */
    @GetMapping("/previewModel/{mid}")
    public String toPdfFile(@PathVariable("mid") Integer mid) {
        Model model = modelService.findModelByMid(mid);
        String pdfPath = System.getProperty("user.dir") + "/src/main/resources/static/ModelPDF/";

        File file1 = new File(System.getProperty("user.dir") +"/src/main/resources/static/ModelWord/" + model.getFileName1());//需要转换的文件
        File file2 = new File(System.getProperty("user.dir") +"/src/main/resources/static/ModelWord/" + model.getFileName2());//需要转换的文件
        File file3 = new File(System.getProperty("user.dir") +"/src/main/resources/static/ModelWord/" + model.getFileName3());//需要转换的文件
        File file4 = new File(System.getProperty("user.dir") +"/src/main/resources/static/ModelWord/" + model.getFileName4());//需要转换的文件
        File file5 = new File(System.getProperty("user.dir") +"/src/main/resources/static/ModelWord/" + model.getFileName5());//需要转换的文件
        File file6 = new File(System.getProperty("user.dir") +"/src/main/resources/static/ModelWord/" + model.getFileName6());//需要转换的文件

        try {
            File newFile = new File(pdfPath);//转换之后文件生成的地址
            if (!newFile.exists()) {
                newFile.mkdirs();
            }
            //文件转化
            converter.convert(file1).to(new File( pdfPath+ model.getFileName1() + ".pdf") ).execute();
            converter.convert(file2).to(new File( pdfPath+ model.getFileName2() + ".pdf") ).execute();
            converter.convert(file3).to(new File( pdfPath+ model.getFileName3() + ".pdf") ).execute();
            converter.convert(file4).to(new File( pdfPath+ model.getFileName4() + ".pdf") ).execute();
            converter.convert(file5).to(new File( pdfPath+ model.getFileName5() + ".pdf") ).execute();
            converter.convert(file6).to(new File( pdfPath+ model.getFileName6() + ".pdf") ).execute();
            //使用response,将pdf文件以流的方式发送的前段
            ServletOutputStream outputStream = response.getOutputStream();
            InputStream in1 = new FileInputStream(new File(pdfPath + model.getFileName1() + ".pdf"));// 读取文件
            InputStream in2 = new FileInputStream(new File(pdfPath + model.getFileName2() + ".pdf"));// 读取文件
            InputStream in3 = new FileInputStream(new File(pdfPath + model.getFileName3() + ".pdf"));// 读取文件
            InputStream in4 = new FileInputStream(new File(pdfPath + model.getFileName4() + ".pdf"));// 读取文件
            InputStream in5 = new FileInputStream(new File(pdfPath + model.getFileName5() + ".pdf"));// 读取文件
            InputStream in6 = new FileInputStream(new File(pdfPath + model.getFileName6() + ".pdf"));// 读取文件

            // copy文件
            int i1 = IOUtils.copy(in1, outputStream);
            int i2 = IOUtils.copy(in2, outputStream);
            int i3 = IOUtils.copy(in3, outputStream);
            int i4 = IOUtils.copy(in4, outputStream);
            int i5 = IOUtils.copy(in5, outputStream);
            int i6 = IOUtils.copy(in6, outputStream);
            in1.close();
            in2.close();
            in3.close();
            in4.close();
            in5.close();
            in6.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "common/error.html";
    }

    /**
     *  教师下载作业样板1
     * @param mid 前端传过来的mid，进行特定的数据查询
     * @param response
     * @return 返回的数据判断是否下载成功
     * @throws Exception 抛异常
     */
    @GetMapping("/downloadModel1/{mid}")
    public String downloadModel1 (@PathVariable("mid") Integer mid, HttpServletResponse response) throws Exception {
        Model model = modelService.findModelByMid(mid);
        String downloadFilePath = System.getProperty("user.dir") + "/src/main/resources/static/ModelWord/" + model.getFileName1();//文件保存相对路径

        File file = new File(downloadFilePath);

        if (file.exists()) {
            String down_file_name = model.getFileName1()+".docx";
            String userAgent = request.getHeader("User-Agent");
            // 针对IE或者以IE为内核的浏览器，解决浏览器下载文件时乱码的问题
            if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
                down_file_name = URLEncoder.encode(down_file_name, "UTF-8");
            } else {
                // 非IE浏览器的处理：
                down_file_name = new String(down_file_name.getBytes("UTF-8"), "ISO-8859-1");
            }

            response.setContentType("application/force-download;charset=utf-8");// 设置强制下载不打开            
            response.addHeader("Content-Disposition", "attachment;fileName=" + down_file_name);
            response.setCharacterEncoding("utf-8");

            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                OutputStream outputStream = null;
                try {
                    outputStream = response.getOutputStream();
                } catch (Exception e) {

                }

                int i = bis.read(buffer);
                while (i != -1) {
                    outputStream.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
                if (outputStream != null) {
                    outputStream.flush();
                    outputStream.close();
                    outputStream = null;
                }

                return "下载成功";
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return "common/error.html";
    }
    /**
     *  教师下载作业样板2
     * @param mid 前端传过来的mid，进行特定的数据查询
     * @param response
     * @return 返回的数据判断是否下载成功
     * @throws Exception 抛异常
     */
    @GetMapping("/downloadModel2/{mid}")
    public String downloadModel2 (@PathVariable("mid") Integer mid, HttpServletResponse response) throws Exception {
        Model model = modelService.findModelByMid(mid);
        String downloadFilePath = System.getProperty("user.dir") + "/src/main/resources/static/ModelWord/" + model.getFileName2();//文件保存相对路径

        File file = new File(downloadFilePath);

        if (file.exists()) {
            String down_file_name = model.getFileName2()+".docx";
            String userAgent = request.getHeader("User-Agent");
            // 针对IE或者以IE为内核的浏览器，解决浏览器下载文件时乱码的问题
            if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
                down_file_name = URLEncoder.encode(down_file_name, "UTF-8");
            } else {
                // 非IE浏览器的处理：
                down_file_name = new String(down_file_name.getBytes("UTF-8"), "ISO-8859-1");
            }

            response.setContentType("application/force-download;charset=utf-8");// 设置强制下载不打开            
            response.addHeader("Content-Disposition", "attachment;fileName=" + down_file_name);
            response.setCharacterEncoding("utf-8");

            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                OutputStream outputStream = null;
                try {
                    outputStream = response.getOutputStream();
                } catch (Exception e) {

                }

                int i = bis.read(buffer);
                while (i != -1) {
                    outputStream.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
                if (outputStream != null) {
                    outputStream.flush();
                    outputStream.close();
                    outputStream = null;
                }

                return "下载成功";
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return "common/error.html";
    }
    /**
     *  教师下载作业样板3
     * @param mid 前端传过来的mid，进行特定的数据查询
     * @param response
     * @return 返回的数据判断是否下载成功
     * @throws Exception 抛异常
     */
    @GetMapping("/downloadModel3/{mid}")
    public String downloadModel3 (@PathVariable("mid") Integer mid, HttpServletResponse response) throws Exception {
        Model model = modelService.findModelByMid(mid);
        String downloadFilePath = System.getProperty("user.dir") + "/src/main/resources/static/ModelWord/" + model.getFileName3();//文件保存相对路径

        File file = new File(downloadFilePath);

        if (file.exists()) {
            String down_file_name = model.getFileName3()+".docx";
            String userAgent = request.getHeader("User-Agent");
            // 针对IE或者以IE为内核的浏览器，解决浏览器下载文件时乱码的问题
            if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
                down_file_name = URLEncoder.encode(down_file_name, "UTF-8");
            } else {
                // 非IE浏览器的处理：
                down_file_name = new String(down_file_name.getBytes("UTF-8"), "ISO-8859-1");
            }

            response.setContentType("application/force-download;charset=utf-8");// 设置强制下载不打开            
            response.addHeader("Content-Disposition", "attachment;fileName=" + down_file_name);
            response.setCharacterEncoding("utf-8");

            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                OutputStream outputStream = null;
                try {
                    outputStream = response.getOutputStream();
                } catch (Exception e) {

                }

                int i = bis.read(buffer);
                while (i != -1) {
                    outputStream.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
                if (outputStream != null) {
                    outputStream.flush();
                    outputStream.close();
                    outputStream = null;
                }

                return "下载成功";
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return "common/error.html";
    }
    /**
     *  教师下载作业样板4
     * @param mid 前端传过来的mid，进行特定的数据查询
     * @param response
     * @return 返回的数据判断是否下载成功
     * @throws Exception 抛异常
     */
    @GetMapping("/downloadModel4/{mid}")
    public String downloadModel4 (@PathVariable("mid") Integer mid, HttpServletResponse response) throws Exception {
        Model model = modelService.findModelByMid(mid);
        String downloadFilePath = System.getProperty("user.dir") + "/src/main/resources/static/ModelWord/" + model.getFileName4();//文件保存相对路径

        File file = new File(downloadFilePath);

        if (file.exists()) {
            String down_file_name = model.getFileName4()+".docx";
            String userAgent = request.getHeader("User-Agent");
            // 针对IE或者以IE为内核的浏览器，解决浏览器下载文件时乱码的问题
            if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
                down_file_name = URLEncoder.encode(down_file_name, "UTF-8");
            } else {
                // 非IE浏览器的处理：
                down_file_name = new String(down_file_name.getBytes("UTF-8"), "ISO-8859-1");
            }

            response.setContentType("application/force-download;charset=utf-8");// 设置强制下载不打开            
            response.addHeader("Content-Disposition", "attachment;fileName=" + down_file_name);
            response.setCharacterEncoding("utf-8");

            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                OutputStream outputStream = null;
                try {
                    outputStream = response.getOutputStream();
                } catch (Exception e) {

                }

                int i = bis.read(buffer);
                while (i != -1) {
                    outputStream.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
                if (outputStream != null) {
                    outputStream.flush();
                    outputStream.close();
                    outputStream = null;
                }

                return "下载成功";
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return "common/error.html";
    }
    /**
     *  教师下载作业样板5
     * @param mid 前端传过来的mid，进行特定的数据查询
     * @param response
     * @return 返回的数据判断是否下载成功
     * @throws Exception 抛异常
     */
    @GetMapping("/downloadModel5/{mid}")
    public String downloadModel5 (@PathVariable("mid") Integer mid, HttpServletResponse response) throws Exception {
        Model model = modelService.findModelByMid(mid);
        String downloadFilePath = System.getProperty("user.dir") + "/src/main/resources/static/ModelWord/" + model.getFileName5();//文件保存相对路径

        File file = new File(downloadFilePath);

        if (file.exists()) {
            String down_file_name = model.getFileName5()+".docx";
            String userAgent = request.getHeader("User-Agent");
            // 针对IE或者以IE为内核的浏览器，解决浏览器下载文件时乱码的问题
            if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
                down_file_name = URLEncoder.encode(down_file_name, "UTF-8");
            } else {
                // 非IE浏览器的处理：
                down_file_name = new String(down_file_name.getBytes("UTF-8"), "ISO-8859-1");
            }

            response.setContentType("application/force-download;charset=utf-8");// 设置强制下载不打开            
            response.addHeader("Content-Disposition", "attachment;fileName=" + down_file_name);
            response.setCharacterEncoding("utf-8");

            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                OutputStream outputStream = null;
                try {
                    outputStream = response.getOutputStream();
                } catch (Exception e) {

                }

                int i = bis.read(buffer);
                while (i != -1) {
                    outputStream.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
                if (outputStream != null) {
                    outputStream.flush();
                    outputStream.close();
                    outputStream = null;
                }

                return "下载成功";
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return "common/error.html";
    }
    /**
     *  教师下载作业样板6
     * @param mid 前端传过来的mid，进行特定的数据查询
     * @param response
     * @return 返回的数据判断是否下载成功
     * @throws Exception 抛异常
     */
    @GetMapping("/downloadModel6/{mid}")
    public String downloadModel6 (@PathVariable("mid") Integer mid, HttpServletResponse response) throws Exception {
        Model model = modelService.findModelByMid(mid);
        String downloadFilePath = System.getProperty("user.dir") + "/src/main/resources/static/ModelWord/" + model.getFileName6();//文件保存相对路径

        File file = new File(downloadFilePath);

        if (file.exists()) {
            String down_file_name = model.getFileName6()+".docx";
            String userAgent = request.getHeader("User-Agent");
            // 针对IE或者以IE为内核的浏览器，解决浏览器下载文件时乱码的问题
            if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
                down_file_name = URLEncoder.encode(down_file_name, "UTF-8");
            } else {
                // 非IE浏览器的处理：
                down_file_name = new String(down_file_name.getBytes("UTF-8"), "ISO-8859-1");
            }

            response.setContentType("application/force-download;charset=utf-8");// 设置强制下载不打开            
            response.addHeader("Content-Disposition", "attachment;fileName=" + down_file_name);
            response.setCharacterEncoding("utf-8");

            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                OutputStream outputStream = null;
                try {
                    outputStream = response.getOutputStream();
                } catch (Exception e) {

                }

                int i = bis.read(buffer);
                while (i != -1) {
                    outputStream.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
                if (outputStream != null) {
                    outputStream.flush();
                    outputStream.close();
                    outputStream = null;
                }

                return "下载成功";
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return "common/error.html";
    }

    /**
     *
     * 教研室主任审核老师上传的学会作业样板
     * @return 返回json数据到前端进行判断审核状态
     */
    @PostMapping("/directorsUpdateModelModel")
    @ResponseBody
    public String directorsUpdateModelModel(Integer mid,String username,String name,String sclass,
                                            String fileName1,String fileName2,String fileName3,
                                            String fileName4,String fileName5,String fileName6,String status,String reson){
        Model model = new Model();
        model.setMid(mid);
        model.setUsername(username);
        model.setName(name);
        model.setSclass(sclass);
        model.setFileName1(fileName1);
        model.setFileName2(fileName2);
        model.setFileName3(fileName3);
        model.setFileName4(fileName4);
        model.setFileName5(fileName5);
        model.setFileName6(fileName6);
        model.setFileUrl1(modelService.findModelByMid(mid).getFileUrl1());
        model.setFileUrl2(modelService.findModelByMid(mid).getFileUrl2());
        model.setFileUrl3(modelService.findModelByMid(mid).getFileUrl3());
        model.setFileUrl4(modelService.findModelByMid(mid).getFileUrl4());
        model.setFileUrl5(modelService.findModelByMid(mid).getFileUrl5());
        model.setFileUrl6(modelService.findModelByMid(mid).getFileUrl6());
        model.setStatus(status);
        model.setReson(reson);

        Model model1 = modelService.findModelByMid(mid);
        User user = modelService.findUserTelephone(model1.getUsername());
        String name1 = user.getName();
        String number = user.getTelephone();

        if (model.getReson().equals("") && model.getStatus().equals("审核否决")){
            return "备注不能为空";
        }else if (model.getReson().length() > 200){
            return "备注字数超过限制";
        }else if (model.getStatus().equals("审核否决")){
            modelService.updateWordModel(model);
            try {
                JSONObject json = null;
                ZhenziSmsClient client = new ZhenziSmsClient(apiUrl, appId, appSecret);
                Map<String, String> params = new HashMap<String, String>();
                params.put("message", "尊敬的:" + name1 + "老师您好，您上传的学生作业样板教研室主任已经审核完成，审核的结果为：审核否决。请您登录系统重新上传学生作业样板进行审核。");
                params.put("number", number);
                String result = client.send(params);
                json = JSONObject.parseObject(result);
                if (json.getIntValue("number") != 0) {//发送短信失败
                    return "短信发送失败";
                } //以json存放，这里使用的是阿里的fastjson
                json = new JSONObject();
                json.put("createTime", System.currentTimeMillis());
                return "审核未通过";
            }catch (Exception e) {
                e.printStackTrace();
            }
            return "审核未通过";
        }else if (model.getStatus().equals("审核通过")){
            modelService.updateWordModel(model);
            try {
                JSONObject json = null;
                ZhenziSmsClient client = new ZhenziSmsClient(apiUrl, appId, appSecret);
                Map<String, String> params = new HashMap<String, String>();
                params.put("message", "尊敬的:" + name1 + "老师您好，您上传的学生作业样板教研室主任已经审核完成，审核的结果为：审核通过。请您登录系统查看审核信息。");
                params.put("number", number);
                String result = client.send(params);
                json = JSONObject.parseObject(result);
                if (json.getIntValue("number") != 0) {//发送短信失败
                    return "短信发送失败";
                } //以json存放，这里使用的是阿里的fastjson
                json = new JSONObject();
                json.put("createTime", System.currentTimeMillis());
                return "审核通过";
            }catch (Exception e) {
                e.printStackTrace();
            }
            return "审核通过";
        }
        return "审核失败";
    }

    /**
     * 查看已经审核完成的作业样板
     * @param modelMap 将数据保存在modelMap中
     * @return 返回到教研室主任查看审核完成页面
     */
    @GetMapping("/findModelByStatus")
    public String findModelByStatus(ModelMap modelMap){
        List<Model> models = modelService.findModelByStatus();
        modelMap.addAttribute("models",models);
        return "directors/directorsLookModel.html";
    }

    /**
     * 老师查看已经审核完成的学生作业样板
     * @param username 前端传过来的username
     * @param modelMap 将查询的数据保存在modelMap中
     * @return 返回老师查看已经审核学生作业样板页面
     */
    @GetMapping("/findModelByStatusForTeacher")
    public String findModelByStatusForTeacher(@RequestParam("username")String username,ModelMap modelMap){
        List<Model>models = modelService.findModelByStatusForTeacher(username);
        modelMap.addAttribute("models",models);
        return "teachers/teachersLookModel.html";
    }
}
