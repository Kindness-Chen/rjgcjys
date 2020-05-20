package com.csr.rjgcjys.controller;

import com.alibaba.fastjson.JSONObject;
import com.csr.rjgcjys.entities.Subject;
import com.csr.rjgcjys.entities.User;
import com.csr.rjgcjys.entities.Week;
import com.csr.rjgcjys.service.SubjectService;
import com.csr.rjgcjys.service.WeekService;
import com.csr.rjgcjys.tools.WordUtils;
import com.zhenzi.sms.ZhenziSmsClient;
import org.apache.commons.io.IOUtils;
import org.jodconverter.DocumentConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.naming.Name;
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
public class WeekController {
    private static final Logger LOGGER = LoggerFactory.getLogger(WeekController.class);
    //短信平台相关参数
    private String apiUrl = "https://sms_developer.zhenzikj.com";
    private String appId = "104726";
    private String appSecret = "a63a9465-9e69-425e-b23c-a672dd5f4228";
    @Autowired
    private WeekService weekService;
    @Autowired
    private SubjectService subjectService;
    // 第一步：转换器直接注入
    @Autowired
    private DocumentConverter converter;
    @Autowired
    private HttpServletResponse response;
    @Autowired
    private HttpServletRequest request;

    /**
     *
     * @return 返回到教师上传教学周历页面
     */
    @GetMapping("/uploadWeek")
    public String uploadWeek(@RequestParam("name")String name, Model model){
        List<Subject> subjects = subjectService.findSclassByName(name);
        model.addAttribute("subjects",subjects);
        return "teachers/teachersUploadWeek.html";
    }

    /**
     * 上传教学周历
     * @param username 教工号
     * @param name 教师名字
     * @param file 上传的教学周历
     * @param status 周历的审核转态
     * @return 返回json数据判断是否上传成功
     */
    @PostMapping("/insertWeek")
    @ResponseBody
    public String insertWeek(String username,String name,String sclass,@RequestParam("fileName") MultipartFile file,String status){
        String fileUrl = "static/WeekWord/";//保存在数据库中的路径
        Week week = new Week();
        week.setUsername(username);
        week.setName(name);
        week.setSclass(sclass);
        week.setStatus(status);

        if (file.isEmpty()) {
            return "上传失败，请选择文件!!!";
        }else if (week.getSclass().equals("")){
            return "课程名不能为空";
        }
        String  originalFilename = file.getOriginalFilename();
        String fileExtName = WordUtils.getExtensionName(originalFilename);
        String fileName = WordUtils.getFileNameNoEx(originalFilename);
        //加个时间戳，尽量避免文件名称重复
        week.setFileExtName(fileExtName);
        if (week.getFileExtName().equals(".doc") || week.getFileExtName().equals(".docx")) {
            String path = System.getProperty("user.dir") + "/src/main/resources/static/WeekWord/" + fileName + fileExtName;//文件保存相对路径
            System.out.println("==========" + path);
            File dest = new File(path);//相对路径创建File类，将文件读入到内存
            //判断文件是否已经存在
            if (dest.exists()) {
                return "您已提交过，请勿重复提交!!!";
            }
            //判断文件父目录是否存在
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdir();
            }
            try {
                file.transferTo(dest);//把内存中的文件存进磁盘中（相当于服务器）
                LOGGER.info("上传成功");
                fileUrl = fileUrl + fileName + fileExtName;//本地运行项目
                weekService.insertWeek(username, name, sclass, fileName, fileExtName, fileUrl, status, null, null, null, null);
                return "上传成功!!!";
            } catch (IOException e) {
                LOGGER.error(e.toString(), e);
            }
        }else {
            return "上传的格式不是Word文档";
        }
        return "上传失败！";
    }

    /**
     * 教师查看待审核的教学周历
     * @param model 将查询到待审核的数据保存在model中
     * @return 返回教师待审核教学周历页面
     */
    @GetMapping("/findWeekByStatusByMyself/{username}")
    public String findWeekByStatusByMySelf(@PathVariable("username")String username, Model model){
        List<Week> weeks = weekService.findWeekByStatusByMyself(username);
        model.addAttribute("weeks",weeks);
        return "teachers/teachersLookNoWeek.html";
    }
    /**
     * 教研室主任查看待审核的教学周历
     * @param model 将查询到待审核的数据保存在model中
     * @return 返回教师待审核教学周历页面
     */
    @GetMapping("/directorsFindWeekByStatus")
    public String directorsFindWeekByStatus(Model model){
        List<Week> weeks = weekService.findWeekByStatus();
        model.addAttribute("weeks",weeks);
        return "directors/directorsCheckWeek.html";
    }

    /**
     *
     * @param tid 前端传过来的tid
     * @return 返回查到的数据
     */
    @GetMapping("/directorsFindWeekByTid")
    @ResponseBody
    public Week directorsFindWeekByTid(Integer tid){
        return weekService.findWeekByTid(tid);
    }

    /**
     *  教师下载教学周历
     * @param tid 前端传过来的tid，进行特定的数据查询
     * @param response
     * @return 返回的数据判断是否下载成功
     * @throws Exception 抛异常
     */
    @GetMapping("/downloadWeek/{tid}")
    public String downloadWeek (@PathVariable("tid") Integer tid, HttpServletResponse response) throws Exception {
        Week week = weekService.findWeekByTid(tid);
        String downloadFilePath = System.getProperty("user.dir") + "/src/main/resources/static/WeekWord/" + week.getFileName() + week.getFileExtName();//文件保存相对路径

        File file = new File(downloadFilePath);//创建File类，将文件读入内存中

        if (file.exists()) {
            String down_file_name = week.getFileName() + week.getFileExtName();
            String userAgent = request.getHeader("User-Agent");//获得浏览器请求头中的User-Agent,获取浏览器信息
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
                while (i != -1) {             //循环读取，得到实际读取到的字节数 读到最后返回-1
                    outputStream.write(buffer, 0, i);  //把字节0到i输出
                    i = bis.read(buffer);   //文件数据写完后没有数据就会返回-1，循环结束
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
     * 对上传到服务器的文件进行预览
     * @param tid 前端传到后台的tid进行特定tid查询
     * @return 判断服务器是否存在预览的文件，不存在返回错误页面
     */
    @GetMapping("/previewWeek/{tid}")
    public String toPdfFile(@PathVariable("tid") Integer tid) {
        Week week = weekService.findWeekByTid(tid);
        String pdfPath = System.getProperty("user.dir") + "/src/main/resources/static/WeeksPDF/";

        File file = new File(System.getProperty("user.dir") +"/src/main/resources/static/WeekWord/" + week.getFileName() + week.getFileExtName());//需要转换的文件
        try {
            File newFile = new File(pdfPath);//转换之后文件生成的地址
            if (!newFile.exists()) {
                newFile.mkdirs();
            }
            //文件转化
            converter.convert(file).to(new File( pdfPath+ week.getFileName() + ".pdf") ).execute();
            //使用response,将pdf文件以流的方式发送到前端
            ServletOutputStream outputStream = response.getOutputStream();
            InputStream in = new FileInputStream(new File(pdfPath + week.getFileName()  + ".pdf"));// 读取文件
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
     * 教研室主任审核老师体提交的教学周历
     * @param tid 前端传过来的tid
     * @param username 教工号
     * @param name 教师名字
     * @param status 教学周历的审核状态
     * @param reson 备注
     * @param img 图片上传
     * @return 返回各自的信息，判断是否审核成功
     */
    @PostMapping("/directorsUpdateWeek")
    @ResponseBody
    public String directorsUpdateWeek(Integer tid,String username,String name,String sclass,String status,String reson,@RequestParam(value = "imgName",required = false) MultipartFile img){
        String imgUrl = "static/WeekImg/";//保存在数据库中的路径
        Week week = new Week();
        week.setTid(tid);
        week.setUsername(username);
        week.setName(name);
        week.setSclass(sclass);
        week.setFileName(weekService.findWeekByTid(tid).getFileName());
        week.setFileExtName(weekService.findWeekByTid(tid).getFileExtName());
        week.setFileUrl(weekService.findWeekByTid(tid).getFileUrl());
        week.setReson(reson);
        week.setStatus(status);

        Week week1 =  weekService.findWeekByTid(tid);
        User user = weekService.findUserTelephone(week1.getUsername());

        String name1 = user.getName();
        String number = user.getTelephone();

        if (week.getReson().equals("")){
            return "备注不能为空!!!";
        }else if (week.getStatus().equals("审核否决")){
           week.setImgName(null);
           week.setImgExtName(null);
           week.setImgUrl(null);
           weekService.directorsUpdateWeek(week);
            try {
                JSONObject json = null;
                ZhenziSmsClient client = new ZhenziSmsClient(apiUrl, appId, appSecret);
                Map<String, String> params = new HashMap<String, String>();
                params.put("message", "尊敬的:" + name1 + "老师您好，您上传的教学周历教研室主任已经审核完成，审核的结果为：审核否决。请您登录系统重新上传教学周历进行审核。");
                params.put("number", number);
                String result = client.send(params);
                json = JSONObject.parseObject(result);
                if (json.getIntValue("number") != 0) {//发送短信失败
                    return "短信发送失败";
                } //以json存放，这里使用的是阿里的fastjson
                json = new JSONObject();
                json.put("createTime", System.currentTimeMillis());
                return "审核未通过!!!";
            }catch (Exception e) {
                e.printStackTrace();
            }
           return "审核未通过!!!";
        }else {
            String originalFilename = img.getOriginalFilename();
            String imgExtName = WordUtils.getExtensionName(originalFilename);
            String imgName = WordUtils.getFileNameNoEx(originalFilename);

            //加个时间戳，尽量避免文件名称重复
            week.setImgExtName(imgExtName);
            if (week.getImgExtName().equals(".jpg")||week.getImgExtName().equals(".png")||week.getImgExtName().equals(".bmp")) {
                String path = System.getProperty("user.dir") + "/src/main/resources/static/WeekImg/" + imgName + imgExtName;//图片保存相对路径
                System.out.println("==========" + path);
                File dest = new File(path);
                //判断文件是否已经存在
                if (dest.exists()) {
                    return "您已提交过，请勿重复提交!!!";
                }
                //判断文件父目录是否存在
                if (!dest.getParentFile().exists()) {
                    dest.getParentFile().mkdir();
                }
                try {
                    img.transferTo(dest);
                    LOGGER.info("审核成功");
                    imgUrl = imgUrl + imgName + imgExtName;//本地运行项目
                    week.setImgName(imgName);
                    week.setImgExtName(imgExtName);
                    week.setImgUrl(imgUrl);
                    weekService.directorsUpdateWeek(week);
                    try {
                        JSONObject json = null;
                        ZhenziSmsClient client = new ZhenziSmsClient(apiUrl, appId, appSecret);
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("message", "尊敬的:" + name1 + "老师您好，您上传的教学周历教研室主任已经审核完成，审核的结果为：审核通过。您可以登录系统下载教研室主任上传的审核通过的签名。");
                        params.put("number", number);
                        String result = client.send(params);//send方法用于单条发送短信,所有请求参数需要封装到Map中
                        json = JSONObject.parseObject(result);
                        if (json.getIntValue("number") != 0) {//发送短信失败
                            return "短信发送失败";
                        } //以json存放，这里使用的是阿里的fastjson
                        json = new JSONObject();
                        json.put("createTime", System.currentTimeMillis());
                        return "审核成功!!!";
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return "审核成功!!!";
                } catch (IOException e) {
                    LOGGER.error(e.toString(), e);
                }
            }else {
                return "上传的格式不是图片";
            }
        }
    return "审核失败！";
    }

    /**
     * 教研室主任查看已经审核完的教学周历
     * @param model 将查询到的数据保存在model中
     * @return 返回查看审核完成的页面
     */
    @GetMapping("/findHaveWeekByStatus")
    public String findHaveWeekByStatus(Model model){
        List<Week> weeks = weekService.findHaveWeekByStatus();
        model.addAttribute("weeks",weeks);
        return "directors/directorsLookWeek.html";
    }
    /**
     * 教师查看已经审核完的教学周历
     * @param model 将查询到的数据保存在model中
     * @return 返回查看审核完成的页面
     */
    @GetMapping("/findHaveWeekByStatusForMyself")
    public String findHaveWeekByStatusForMyself(@RequestParam(name="username") String username, Model model){
        List<Week> weeks = weekService.findHaveWeekByStatusForMyself(username);
        model.addAttribute("weeks",weeks);
        return "teachers/teachersHaveWeek.html";
    }

    /**
     *  教师下载审核通过图片
     * @param tid 前端传过来的tid，进行特定的数据查询
     * @param response
     * @return 返回的数据判断是否下载成功
     * @throws Exception 抛异常
     */
    @GetMapping("/downloadImg/{tid}")
    public String downloadImg (@PathVariable("tid") Integer tid, HttpServletResponse response) throws Exception {
        Week week = weekService.findWeekByTid(tid);
        String downloadFilePath = System.getProperty("user.dir") + "/src/main/resources/static/WeekImg/" + week.getImgName() + week.getImgExtName();//图片保存相对路径

        File file = new File(downloadFilePath);//创建File类，将相对路径的文件读入内存

        if (file.exists()) {
            String down_file_name = week.getImgName() + week.getImgExtName();
            String userAgent = request.getHeader("User-Agent");//获得浏览器请求头中的User-Agent,从而获取浏览器信息
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

            byte[] buffer = new byte[1024];//定义一个字节数组，相当于缓存
            FileInputStream fis = null;//创建文件输入流fis
            BufferedInputStream bis = null;//创建缓冲输入流bis
            try {
                fis = new FileInputStream(file);//fis读取文件数据
                bis = new BufferedInputStream(fis);//bis读取fis数据
                OutputStream outputStream = null;//创建一个输出流
                try{
                    outputStream = response.getOutputStream(); //用于输出字符流数据或者二进制的字节流数据都可以输出。这样就可以写图片、文件
                }catch(Exception e) {

                }

                int i = bis.read(buffer);//把bis缓冲区里的东西读到bytes数组里去
                while (i != -1) { //循环读取，得到实际读取到的字节数 读到最后返回-1
                    outputStream.write(buffer, 0, i);//把字节0到i输出
                    i = bis.read(buffer);//文件数据写完后没有数据就会返回-1，循环结束
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

}
