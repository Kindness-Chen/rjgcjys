package com.csr.rjgcjys.controller;

import com.csr.rjgcjys.entities.Achievement;
import com.csr.rjgcjys.entities.PPT;
import com.csr.rjgcjys.entities.Train;
import com.csr.rjgcjys.entities.Week;
import com.csr.rjgcjys.service.AchievementService;
import com.csr.rjgcjys.service.TrainService;
import com.csr.rjgcjys.service.WeekService;
import com.csr.rjgcjys.tools.WordUtils;
import org.apache.commons.io.IOUtils;
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
import java.util.List;

@Controller
public class AchievementController {
    private static final Logger LOGGER =  LoggerFactory.getLogger(AchievementController.class);;
    @Autowired
    private AchievementService achievementService;
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
     * 教师上传学生成绩
     * @return 返回教师上传学生页面
     */
    @GetMapping("/toTeachersUploadAchievement")
    public String toTeachersUploadAchievement(@RequestParam("name")String name, Model model){
        List<Week> weeks = weekService.findSclassByNameStatus(name);
        model.addAttribute("weeks",weeks);
        List<Train> trains = trainService.findSclassAll();
        model.addAttribute("trains",trains);
        List<Train>trains1 = trainService.findClassName();
        model.addAttribute("trains1",trains1);
        return "teachers/teachersUploadAchievement.html";
    }

    /**
     * 教师上传学生成绩文档
     * @return 返回json数据到前端进行判断输入的数据是否合法
     */
    @PostMapping("/insertAchievement")
    @ResponseBody
    public String insertAchievement(String username,String name,String sclass,String subject,@RequestParam("fileName") MultipartFile file){
        String fileUrl = "static/Achievement/";//保存在数据库中的路径
        Achievement achievement = new Achievement();
        achievement.setUsername(username);
        achievement.setName(name);
        achievement.setSclass(sclass);
        achievement.setSubject(subject);
        achievement.setStatus("待审核");

        String  originalFilename = file.getOriginalFilename();
        String fileExtName = WordUtils.getExtensionName(originalFilename);
        achievement.setFileExtName(fileExtName);
        if (achievement.getFileExtName().equals(".xls")||achievement.getFileExtName().equals(".xlsx")){
            String fileName = WordUtils.getFileNameNoEx(originalFilename);
            //加个时间戳，尽量避免文件名称重复
            String path = System.getProperty("user.dir") + "/src/main/resources/static/Achievement/" + fileName + fileExtName;//文件保存相对路径
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
                achievement.setFileName(fileName);
                achievement.setFileUrl(fileUrl);
                achievementService.insertAchievement(achievement);
                return "上传成功!!!";
            } catch (IOException e) {
                LOGGER.error(e.toString(), e);
            }
        }else {
            return "上传的格式不是Excel格式";
        }
        return "上传失败";
    }

    /**
     * 教师查看上传的成绩文档
     * @param username 前端传过来的username
     * @param model 将数据保存早model中
     * @return 返回教师查看成绩文档页面
     */
    @GetMapping("/teachersLookAchievement")
    public String teachersLookAchievement(@RequestParam("username")String username,Model model){
        List<Achievement>achievements = achievementService.teachersLookAchievement(username);
        model.addAttribute("achievements",achievements);
        return "teachers/teachersLookAchievement.html";
    }

    /**
     * 对上传到服务器的文件进行预览
     * @param zid 前端传到后台的tid进行特定zid查询
     * @return 判断服务器是否存在预览的文件，不存在返回错误页面
     */
    @GetMapping("/previewAchievement/{zid}")
    public String toPdfFile(@PathVariable("zid") Integer zid) {
        Achievement achievement = achievementService.findAchievementByZid(zid);
        String pdfPath = System.getProperty("user.dir") + "/src/main/resources/static/AchievementPDF/";

        File file = new File(System.getProperty("user.dir") +"/src/main/resources/static/Achievement/" + achievement.getFileName() + achievement.getFileExtName());//需要转换的文件
        try {
            File newFile = new File(pdfPath);//转换之后文件生成的地址
            if (!newFile.exists()) {
                newFile.mkdirs();
            }
            //文件转化
            converter.convert(file).to(new File( pdfPath+ achievement.getFileName() + ".pdf") ).execute();
            //使用response,将pdf文件以流的方式发送到前端
            ServletOutputStream outputStream = response.getOutputStream();
            InputStream in = new FileInputStream(new File(pdfPath + achievement.getFileName() + ".pdf"));// 读取文件
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
     *  教师下载成绩文档
     * @param zid 前端传过来的zid，进行特定的数据查询
     * @return 返回的数据判断是否下载成功
     * @throws Exception 抛异常
     */
    @GetMapping("/downloadAchievement/{zid}")
    public String downloadPPT (@PathVariable("zid") Integer zid, HttpServletResponse response) throws Exception {
        Achievement achievement = achievementService.findAchievementByZid(zid);
        String downloadFilePath = System.getProperty("user.dir") + "/src/main/resources/static/Achievement/" + achievement.getFileName() + achievement.getFileExtName();//文件保存相对路径

        File file = new File(downloadFilePath);//创建File类，将文件读入内存中

        if (file.exists()) {
            String down_file_name = achievement.getFileName() + achievement.getFileExtName();
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

            byte[] buffer = new byte[1024];//定义一个字节数组，相当于缓存
            FileInputStream fis = null;//创建一个文件输入流
            BufferedInputStream bis = null;//创建一个缓冲输入流，输入流数据分批读取，每次读取一部分到缓冲中；操作完缓冲中的这部分数据之后，再从输入流中读取下一部分的数据。
            try {
                fis = new FileInputStream(file);//文件输入流读取文件
                bis = new BufferedInputStream(fis);//缓冲输入流读取文件输入流
                OutputStream outputStream = null;//输出流
                try {
                    outputStream = response.getOutputStream();//输出流不仅能只输出字符串，还能够输出字符流数据或者二进制的字节流数据
                } catch (Exception e) {

                }

                int i = bis.read(buffer);//缓冲输入流将文件数据读取到字节数组中
                while (i != -1) {        //循环读取，得到实际读取到的字节数 读到最后返回-1
                    outputStream.write(buffer, 0, i); //把字节0到i输出
                    i = bis.read(buffer);   //文件数据写完后没有数据就会返回-1，循环结束
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
     * 学生查看成绩文档
     * @param model 将数据保存在model中
     * @return 返回学生查看成绩文档页面
     */
    @GetMapping("/studentsFindAchievement")
    public String studentsFindAchievement(Model model){
        List<Achievement>achievements = achievementService.findAchievementAll();
        model.addAttribute("achievements",achievements);
        return "students/studentsLookAchievement.html";
    }
    /**
     * 教研室主任查看成绩文档
     * @param model 将数据保存在model中
     * @return 返回学生查看成绩文档页面
     */
    @GetMapping("/directorsLookAchievement")
    public String directorsFindAchievement(Model model){
        List<Achievement>achievements = achievementService.directorsLookAchievementAll();
        model.addAttribute("achievements",achievements);
        return "directors/directorsLookAchievement.html";
    }

    /**
     * 教师删除上传的成绩文档
     * @param zid 前端传传过来的zid
     * @return 返回json数据到前端进行判断是否删除成功
     */
    @DeleteMapping("/deleteAchievementByZid")
    @ResponseBody
    public int deleteAchievementByZid(@RequestParam("zid")Integer zid){
        Achievement achievement = achievementService.findAchievementByZid(zid);
        String deleteFilePath = System.getProperty("user.dir") + "/src/main/resources/static/Achievement/" + achievement.getFileName() + achievement.getFileExtName();//文件保存相对路径
        File file = new File(deleteFilePath);
        file.delete();
        achievementService.deleteAchievementByZid(zid);
        return 1;
    }

    /**
     * 审核过程中根据zid查询特定的数据
     * @param zid 前端传过来的zid
     * @return 返回zid特定的json数据
     */
    @GetMapping("/findAchievementByZid")
    @ResponseBody
    public Achievement findAchievementByZid(@RequestParam("zid")Integer zid){
        return achievementService.findAchievementByZid(zid);
    }

    /**
     * 教研室主任审核成绩
     * @param achievement 前端传过来的数据
     * @return 返回json数据进行判断是否审核成功
     */
    @PostMapping("/directorsUpdateAchievement")
    @ResponseBody
    public String directorsUpdateAchievement(Achievement achievement){
        if (achievement.getStatus().equals("")){
            return "状态不能为空";
        }else if (achievement.getReson().equals("")){
            return "备注不能为空";
        }else if (achievement.getReson().length() > 200){
            return "备注超过字数限制";
        }
        achievementService.directorsUpdateAchievement(achievement);
        return "审核成功";
    }
}
