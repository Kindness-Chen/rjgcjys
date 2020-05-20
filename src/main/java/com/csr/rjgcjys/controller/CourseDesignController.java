package com.csr.rjgcjys.controller;

import com.alibaba.fastjson.JSONObject;
import com.csr.rjgcjys.entities.CourseDesign;
import com.csr.rjgcjys.entities.Train;
import com.csr.rjgcjys.entities.User;
import com.csr.rjgcjys.entities.Word;
import com.csr.rjgcjys.service.CourseDesignService;
import com.csr.rjgcjys.service.TrainService;
import com.csr.rjgcjys.service.WordService;
import com.csr.rjgcjys.tools.WordUtils;
import com.zhenzi.sms.ZhenziSmsClient;
import org.apache.commons.io.IOUtils;
import org.apache.ibatis.annotations.Param;
import org.jodconverter.DocumentConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
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
public class CourseDesignController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CourseDesignController.class);
    //短信平台相关参数
    private String apiUrl = "https://sms_developer.zhenzikj.com";
    private String appId = "104726";
    private String appSecret = "a63a9465-9e69-425e-b23c-a672dd5f4228";
    @Autowired
    private CourseDesignService courseDesignService;
    @Autowired
    private WordService wordService;
    @Autowired
    private TrainService trainService;
    @Autowired
    private DocumentConverter converter;
    @Autowired
    private HttpServletResponse response;
    @Autowired
    private HttpServletRequest request;

    /**
     *
     * @return 返回到课程设计上传页面
     */
    @GetMapping("/courseDesign")
    public String courseDesign(Model model){
        List<User> users = wordService.findUserByName();
        model.addAttribute("users",users);
        List<Train>trains = trainService.findSclassAll();
        model.addAttribute("trains",trains);
        List<Train>trains1 = trainService.findClassName();
        model.addAttribute("trains1",trains1);
        return "students/studentsUploadaCourseDesign.html";
    }

    /**
     *  学生上传课程设计给老师审核
     * @param username 学生的学号
     * @param name 学生的名字
     * @param sclass 学生的班级
     * @param subject 所属学科
     * @param teacher 授课老师
     * @param file 上传的文档
     * @return 进行判断是否上交文档
     */
    @PostMapping("/insertCourseDesign")
    @ResponseBody
    public String insertWord(String username, String name, String sclass, String subject, String teacher, @RequestParam("fileName") MultipartFile file,@RequestParam("enclosureName") MultipartFile file2) {
        String url = "static/CourseDesignWord/";
        String enclosureUrl = "static/CourseDesignEnclosure/";
        CourseDesign courseDesign = new CourseDesign();
        courseDesign.setUsername(username);
        courseDesign.setName(name);
        courseDesign.setSclass(sclass);
        courseDesign.setSubject(subject);
        courseDesign.setTeacher(teacher);
        if (file.isEmpty()) {
            return "上传失败，请选择文件!!!";
        }else if (courseDesign.getSclass().equals("")){
            return "班级不能为空";
        }else if (courseDesign.getSubject()== null){
            return "课程名不能为空";
        }else if (courseDesign.getTeacher().equals("")){
            return "请选择授课老师";
        }else{
            String originalFilename = file.getOriginalFilename();
            String extName = WordUtils.getExtensionName(originalFilename);
            String fileName = WordUtils.getFileNameNoEx(originalFilename);
            String originalFilename2 = file2.getOriginalFilename();
            String enclosureExtName = WordUtils.getExtensionName(originalFilename2);
            String enclosureName = WordUtils.getFileNameNoEx(originalFilename2);
            //加个时间戳，尽量避免文件名称重复
//        String path =  "F:/Word/" + fileName;    F:\IDEA-workspace\rjgcjys\
            courseDesign.setExtName(extName);
            courseDesign.setEnclosureExtName(enclosureExtName);
            if ((courseDesign.getExtName().equals(".doc") || courseDesign.getExtName().equals(".docx"))&&(courseDesign.getEnclosureExtName().equals(".zip")||courseDesign.getEnclosureExtName().equals(".rar"))) {
                String path = System.getProperty("user.dir") + "/src/main/resources/static/CourseDesignWord/" + fileName + extName;//文件保存相对路径
                String path2= System.getProperty("user.dir") + "/src/main/resources/static/CourseDesignEnclosure/" + enclosureName + enclosureExtName;//附件保存相对路径
                System.out.println("==========" + path);
                File dest = new File(path);//创建File类，将文件读入内存
                //判断文件是否已经存在
                if (dest.exists()) {
                    return "您已提交过，请勿重复提交!!!";
                }
                File dest2 = new File(path2);//创建File类，将文件读入内存
                if (dest2.exists()) {
                    return "您已提交过类似附件，请勿重复提交!!!";
                }
                //判断文件父目录是否存在
                if (!dest.getParentFile().exists()) {
                    dest.getParentFile().mkdir();
                }
                if (!dest2.getParentFile().exists()) {
                    dest2.getParentFile().mkdir();
                }
                try {
                    file.transferTo(dest);//把内存中的文件存进磁盘中（相当于服务器）
                    file2.transferTo(dest2);
                    LOGGER.info("上传成功");
                    url = url + fileName + extName;//本地运行项目
                    enclosureUrl = enclosureUrl + enclosureName + enclosureExtName;//附件本地运行项目
                    courseDesignService.insertCourseDesign(username, name, sclass, subject, teacher, fileName, extName, url,enclosureName,enclosureExtName,enclosureUrl);
                    return "上传成功!!!";
                } catch (IOException e) {
                    LOGGER.error(e.toString(), e);
                }
            }else {
                return "上传的格式不是Word文档或者上传的附件不是压缩格式";
            }
        }
        return "上传失败！";
    }
    /**
     * 学生查看已经上传的课程设计作业
     * @param model
     * @return 返回学生查看文课程设计页面
     */
    @GetMapping("/findCourseDesignByUsername/{username}")
    public String findCourseDesignAll(@PathVariable("username")String username, Model model) {
        List<CourseDesign> courseDesigns = courseDesignService.findCourseDesignByUsername(username);
        model.addAttribute("courseDesigns", courseDesigns);
        return "students/studentsLookCourseDesign.html";
    }

    /**
     *  学生下载课程设计
     * @param cid 前端传过来的cid，进行特定的数据查询
     * @param response
     * @return 返回的数据判断是否下载成功
     * @throws Exception 抛异常
     */
    @GetMapping("/downloadCourserDesign/{cid}")
    public String downloadFile (@PathVariable("cid") Integer cid, HttpServletResponse response) throws Exception {
        CourseDesign courseDesign = courseDesignService.findCourseDesignByCid(cid);
        String downloadFilePath = System.getProperty("user.dir") + "/src/main/resources/static/CourseDesignWord/" + courseDesign.getFileName() + courseDesign.getExtName();//文件保存相对路径

        File file = new File(downloadFilePath);//创建File类，将文件读入内存中

        if (file.exists()) {
            String down_file_name = courseDesign.getFileName() + courseDesign.getExtName();
            String userAgent = request.getHeader("User-Agent");//获得浏览器请求头中的User-Agent,获取浏览器信息
            // 针对IE或者以IE为内核的浏览器：
            if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
                down_file_name = URLEncoder.encode(down_file_name, "UTF-8");
            } else {
                // 非IE浏览器的处理：
                down_file_name = new String(down_file_name.getBytes("UTF-8"), "ISO-8859-1");
            }

            response.setContentType("application/force-download;charset=utf-8");// 设置强制下载不打开            
            response.addHeader("Content-Disposition", "attachment;fileName=" + down_file_name);
            response.setCharacterEncoding("utf-8");

            byte[] buffer = new byte[1024];//定义一个字节数组，相当于缓存
            FileInputStream fis = null;//创建一个文件输入流
            BufferedInputStream bis = null;//创建一个缓冲输入流，输入流数据分批读取，每次读取一部分到缓冲中；操作完缓冲中的这部分数据之后，再从输入流中读取下一部分的数据。
            try {
                fis = new FileInputStream(file);//文件输入流读取文件
                bis = new BufferedInputStream(fis);//缓冲输入流读取文件输入流
                OutputStream outputStream = null;//输出流
                try{
                    outputStream = response.getOutputStream();//输出流不仅能只输出字符串，还能够输出字符流数据或者二进制的字节流数据
                }catch(Exception e) {

                }

                int i = bis.read(buffer);//缓冲输入流将文件数据读取到字节数组中
                while (i != -1) {           //循环读取，得到实际读取到的字节数 读到最后返回-1
                    outputStream.write(buffer, 0, i); //把字节0到i输出
                    i = bis.read(buffer); //文件数据写完后没有数据就会返回-1，循环结束
                }
                if(outputStream != null) {
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
     *  学生下载课程设计附件
     * @param cid 前端传过来的cid，进行特定的数据查询
     * @return 返回的数据判断是否下载成功
     * @throws Exception 抛异常
     */
    @GetMapping("/downloadCourserDesignEnclosure/{cid}")
    public String downloadCourserDesignEnclosure (@PathVariable("cid") Integer cid, HttpServletResponse response) throws Exception {
        CourseDesign courseDesign = courseDesignService.findCourseDesignByCid(cid);
        String downloadFilePath = System.getProperty("user.dir") + "/src/main/resources/static/CourseDesignEnclosure/" + courseDesign.getEnclosureName() + courseDesign.getEnclosureExtName();//附件保存相对路径

        File file = new File(downloadFilePath);//创建File类，将文件读入内存中

        if (file.exists()) {
            String down_file_name = courseDesign.getEnclosureName() + courseDesign.getEnclosureExtName();
            String userAgent = request.getHeader("User-Agent");//获得浏览器请求头中的User-Agent,获取浏览器信息
            // 针对IE或者以IE为内核的浏览器：
            if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
                down_file_name = URLEncoder.encode(down_file_name, "UTF-8");
            } else {
                // 非IE浏览器的处理：
                down_file_name = new String(down_file_name.getBytes("UTF-8"), "ISO-8859-1");
            }

            response.setContentType("application/force-download;charset=utf-8");// 设置强制下载不打开            
            response.addHeader("Content-Disposition", "attachment;fileName=" + down_file_name);
            response.setCharacterEncoding("utf-8");

            byte[] buffer = new byte[1024];//定义一个字节数组，相当于缓存
            FileInputStream fis = null;//创建一个文件输入流
            BufferedInputStream bis = null;//创建一个缓冲输入流，输入流数据分批读取，每次读取一部分到缓冲中；操作完缓冲中的这部分数据之后，再从输入流中读取下一部分的数据。
            try {
                fis = new FileInputStream(file);//文件输入流读取文件
                bis = new BufferedInputStream(fis);//缓冲输入流读取文件输入流
                OutputStream outputStream = null;//输出流
                try{
                    outputStream = response.getOutputStream();//输出流不仅能只输出字符串，还能够输出字符流数据或者二进制的字节流数据
                }catch(Exception e) {

                }
                int i = bis.read(buffer);//缓冲输入流将文件数据读取到字节数组中
                while (i != -1) {            //循环读取，得到实际读取到的字节数 读到最后返回-1
                    outputStream.write(buffer, 0, i);//把字节0到i输出
                    i = bis.read(buffer);            //文件数据写完后没有数据就会返回-1，循环结束
                }
                if(outputStream != null) {
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
     * 对上传到服务器的课程设计进行预览
     * @param cid 前端传到后台的sid进行特定sid查询
     * @return 判断服务器是否存在预览的课程设计，不存在返回错误页面
     */
    @GetMapping("/previewCouserDesign/{cid}")
    public String toPdfFile(@PathVariable("cid") Integer cid) {
        CourseDesign courseDesign = courseDesignService.findCourseDesignByCid(cid);
        String pdfPath = System.getProperty("user.dir") + "/src/main/resources/static/CourseDesignPDF/";

        File file = new File(System.getProperty("user.dir") +"/src/main/resources/static/CourseDesignWord/" + courseDesign.getFileName() + courseDesign.getExtName());//需要转换的文件
        try {
            File newFile = new File(pdfPath);//转换之后文件生成的地址
            if (!newFile.exists()) {
                newFile.mkdirs();
            }
            //文件转化
            converter.convert(file).to(new File( pdfPath+ courseDesign.getFileName() + ".pdf") ).execute();
            //使用response,将pdf文件以流的方式发送到前端
            ServletOutputStream outputStream = response.getOutputStream();
            InputStream in = new FileInputStream(new File(pdfPath + courseDesign.getFileName()  + ".pdf"));// 读取文件
            // 使用工具类复制输入输出流，copy文件，文件生成在指定目录中
            int i = IOUtils.copy(in, outputStream);
            System.out.println(i);
            in.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "common/error.html";
    }

    /**
     * 教师查看学生上传的课程设计
     * @param teacher 前端传过来的teacher参数
     * @param model 将查询到的数据保存在model中
     * @return 返回到教师查看学生查看课程设计页面
     */
    @GetMapping("/findCourseDesignByTeacher")
    public String findCourseDesignByTeacher(@RequestParam("teacher")String teacher,Model model){
        List<CourseDesign>courseDesigns = courseDesignService.findCourseDesignByTeacher(teacher);
        model.addAttribute("courseDesigns",courseDesigns);
        return "teachers/teachersLookCourseDesign.html";
    }

    /**
     * 教师删除课程设计
     * @param cid 前端传过来的cid
     * @return 返回json数据进行判断是否删除成功
     */
    @DeleteMapping("/deleteCourseDesignByCid")
    @ResponseBody
    public int deleteCourseDesignByCid(@RequestParam("cid")Integer cid){
        CourseDesign courseDesign = courseDesignService.findCourseDesignByCid(cid);
        User user = courseDesignService.findUserTelephone(courseDesign.getUsername());
        String deleteFilePath = System.getProperty("user.dir") + "/src/main/resources/static/CourseDesignWord/" + courseDesign.getFileName() + courseDesign.getExtName();//文件保存相对路径
        String deleteEnclosurePath = System.getProperty("user.dir") + "/src/main/resources/static/CourseDesignEnclosure/" + courseDesign.getEnclosureName() + courseDesign.getEnclosureExtName();//附件保存相对路径
        //删除课程设计
        File file = new File(deleteFilePath);
        file.delete();
        //删除课程设计附件
        File file2 = new File(deleteEnclosurePath);
        file2.delete();
        courseDesignService.deleteCourseDesignByCid(cid);
        //删除成功发送短信
        String name = user.getName();
        String number = user.getTelephone();
        if (number!=null) {
            try {
                JSONObject json = null;
                ZhenziSmsClient client = new ZhenziSmsClient(apiUrl, appId, appSecret);
                Map<String, String> params = new HashMap<String, String>();
                params.put("message", "尊敬的:" + name + "同学你好，由于你的上传的课程设计文档不符合要求，老师已经将你上传的课程设计文档删除，请重新上传课程设计文档！");
                params.put("number", number);
                String result = client.send(params);//send方法用于单条发送短信,所有请求参数需要封装到Map中
                json = JSONObject.parseObject(result);
                if (json.getIntValue("number") != 0) {//发送短信失败
                    return 0;
                } //以json存放，这里使用的是阿里的fastjson
                json = new JSONObject();
                json.put("createTime", System.currentTimeMillis());
                return 1;
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 1;
    }

    /**
     * 教师查询特定的课程设计
     * @param cid 前端传过来的cid
     * @return 返回json数据到前端进行显示
     */
    @GetMapping("/findCourseDesignByCid")
    @ResponseBody
    public CourseDesign updateCourseDesignByCid(@RequestParam("cid")Integer cid){
        return courseDesignService.findCourseDesignByCid(cid);
    }

    /**
     *
     * 教师给学会的课程设计评成绩
     * @return 返回json数据到前端进行判断是否成功
     */
    @PostMapping("/updateCourseDesignByCid")
    @ResponseBody
    public String updateCourseDesignByCid(Integer cid,String username,String name,String sclass,String subject,
                                          String teacher,String fileName,String extName,String url,
                                          String enclosureName,String enclosureExtName,String enclosureUrl,
                                          Integer score,String reson){
        CourseDesign courseDesign = new CourseDesign();
        courseDesign.setCid(cid);
        courseDesign.setUsername(username);
        courseDesign.setName(name);
        courseDesign.setSclass(sclass);
        courseDesign.setSubject(subject);
        courseDesign.setTeacher(teacher);
        courseDesign.setFileName(fileName);
        courseDesign.setExtName(extName);
        courseDesign.setUrl(url);
        courseDesign.setEnclosureName(enclosureName);
        courseDesign.setEnclosureExtName(enclosureExtName);
        courseDesign.setEnclosureUrl(enclosureUrl);
        courseDesign.setScore(score);
        courseDesign.setReson(reson);
        courseDesignService.updateCourseDesignByCid(courseDesign);
        return "评分成功";
    }

    /**
     * 教师查看已经评成绩的学生课程设计
     * @param teacher 前端传过来的teacher
     * @param model 将查询到的数据保存在model中传到前端
     * @return 返回教师查看已经评成绩的课程设计页面
     */
    @GetMapping("/findCourseDesignByScore")
    public String findCourseDesignByScore(@Param("teacher")String teacher,Model model){
        List<CourseDesign>courseDesigns = courseDesignService.findCourseDesignByScore(teacher);
        model.addAttribute("courseDesigns",courseDesigns);
        return "teachers/teachersLookScoreCourseDesign.html";
    }
}
