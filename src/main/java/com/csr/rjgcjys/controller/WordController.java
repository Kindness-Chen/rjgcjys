package com.csr.rjgcjys.controller;

import com.alibaba.fastjson.JSONObject;
import com.csr.rjgcjys.entities.Leave;
import com.csr.rjgcjys.entities.Subject;
import com.csr.rjgcjys.entities.Train;
import com.csr.rjgcjys.entities.User;
import com.csr.rjgcjys.entities.Week;
import com.csr.rjgcjys.entities.Word;
import com.csr.rjgcjys.service.SubjectService;
import com.csr.rjgcjys.service.TrainService;
import com.csr.rjgcjys.service.WeekService;
import com.csr.rjgcjys.service.WordService;
import com.csr.rjgcjys.tools.WordUtils;
import com.zhenzi.sms.ZhenziSmsClient;
import org.apache.commons.io.IOUtils;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Update;
import org.jodconverter.DocumentConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
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
import java.util.HashMap;
import java.util.List;
import java.net.URLEncoder;
import java.util.Map;

@Controller
public class WordController {
    private static final Logger LOGGER = LoggerFactory.getLogger(WordController.class);//创建日志实例
    //短信平台相关参数
    private String apiUrl = "https://sms_developer.zhenzikj.com";
    private String appId = "104726";
    private String appSecret = "a63a9465-9e69-425e-b23c-a672dd5f4228";
    @Autowired
    private WordService wordService;
    @Autowired
    private TrainService trainService;
    @Autowired
    private SubjectService subjectService;
    @Autowired
    private WeekService weekService;
    // 第一步：转换器直接注入
    @Autowired
    private DocumentConverter converter;//转换
    @Autowired
    private HttpServletResponse response;//响应
    @Autowired
    private HttpServletRequest request;//请求

    /**
     *查询全部老师的名字
     * @return 返回到学生上传文档页面
     */
    @GetMapping("/word")
    public String word(Model model){
        List<User> users = wordService.findUserByName();
        model.addAttribute("users",users);
        List<Train>trains1 = trainService.findClassName();
        model.addAttribute("trains1",trains1);
        return "students/studentsUploadWord.html";
    }

    /**
     * 学生上传作业要根据这个老师的教学周历是否审核通过才能提交这门课程
     * @param name 前端传过来的name
     * @param model 将查询到的数据保存在model中
     * @return 返回查询的数据以json数据的格式返回前端
     */
    @GetMapping("/findSclassByNameStatus")
    @ResponseBody
    public List<Week> findSclassByNameStatus(@RequestParam("name")String name, Model model){
        List<Week> weeks = weekService.findSclassByNameStatus(name);
        model.addAttribute("weeks",weeks);
        return weeks;
    }

    /**
     *
     * @return 返回学生主页面
     */
    @GetMapping("/studentsMain")
    public String studentsMain(){
        return "students/studentsMain.html";
    }

    /**
     *  学生上传文档给老师审核
     * @param username 学生的学号
     * @param name 学生的名字
     * @param sclass 学生的班级
     * @param subject 所属学科
     * @param teacher 授课老师
     * @param file 上传的文档
     * @param model 将添加的内容保存在model中
     * @return 进行判断是否上交文档
     */
    @PostMapping("/insertWord")
    @ResponseBody
    public String insertWord(String username, String name, String sclass, String subject, String teacher, @RequestParam("fileName") MultipartFile file,Model model) {
        String url = "static/Word/";//保存在数据库中的路径
        Word word = new Word();
        word.setUsername(username);
        word.setName(name);
        word.setSclass(sclass);
        word.setSubject(subject);
        word.setTeacher(teacher);
        if (file.isEmpty()) {
            return "上传失败，请选择文件!!!";
        }else if (word.getSclass().equals("")){
            return "班级不能为空";
        }else if (word.getSubject()==null){
            return "课程名不能为空";
        }else if (word.getTeacher().equals("")){
            return "请选择授课老师";
        }else {
            String originalFilename = file.getOriginalFilename();
            String extName = WordUtils.getExtensionName(originalFilename);
            String fileName = WordUtils.getFileNameNoEx(originalFilename);
            //加个时间戳，尽量避免文件名称重复
            //        String path =  "F:/Word/" + fileName;    F:\IDEA-workspace\rjgcjys\
            word.setExtName(extName);
            if (word.getExtName().equals(".doc") || word.getExtName().equals(".docx")) {
                String path = System.getProperty("user.dir") + "/src/main/resources/static/Word/" + fileName + extName;//文件保存相对路径
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
                    url = url + fileName + extName;//本地运行项目
                    wordService.insertWord(username, name, sclass, subject, teacher, fileName, extName, url);
                    return "上传成功!!!";
                } catch (IOException e) {
                    LOGGER.error(e.toString(), e);
                }
            }else {
                return "上传的格式不是Word文档";
            }
        }
        return "上传失败！";
    }

    /**
     * 学生查看已经上传的文档作业
     * @param model 将数据保存在model中
     * @return 返回学生查看文档页面
     */
    @GetMapping("/findWordByUserName")
    public String findWordAll(@RequestParam(name="username")String username, Model model) {
        List<Word> words = wordService.findWordByUserName(username);
        model.addAttribute("words", words);
        return "students/studentsLookWord.html";
    }
    /**
     * 老师查看学生已经上传的文档作业
     * @param model 将数据保存在model中
     * @return 返回老师查看文档页面
     */
    @GetMapping("/findWordByName")
    public String findWordByName(@RequestParam(name="teacher")String teacher, Model model) {
        List<Word> words = wordService.findWordByName(teacher);
        model.addAttribute("words", words);
        return "teachers/teachersLookWord.html";
    }

    /**
     *  学生下载文档
     * @param sid 前端传过来的sid，进行特定的数据查询
     * @param response
     * @return 返回的数据判断是否下载成功
     * @throws Exception 抛异常
     */
    @GetMapping("/downloadword/{sid}")
    public String downloadFile (@PathVariable("sid") Integer sid,HttpServletResponse response) throws Exception {
        Word word = wordService.findWordBySid(sid);
        String downloadFilePath = System.getProperty("user.dir") + "/src/main/resources/static/Word/" + word.getFileName() + word.getExtName();//文件保存相对路径

        File file = new File(downloadFilePath);//相对路劲创建一个File类，将文件读入内存

        if (file.exists()) {
            String down_file_name = word.getFileName() + word.getExtName();
            String userAgent = request.getHeader("User-Agent");//获得浏览器请求头中的User-Agent,从而获取浏览器信息
            // 针对IE或者以IE为内核的浏览器，解决浏览器下载文件时乱码的问题
            if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
                down_file_name = URLEncoder.encode(down_file_name, "UTF-8");
            } else {
                // 非IE浏览器的处理：
                down_file_name = new String(down_file_name.getBytes("UTF-8"), "ISO-8859-1");
            }

            response.setContentType("application/force-download;charset=utf-8");// 设置强制下载不打开            
            response.addHeader("Content-Disposition", "attachment;fileName=" + down_file_name);//将文件名转译成utf-8
            response.setCharacterEncoding("utf-8");

            byte[] buffer = new byte[1024]; //定义一个字节数组,相当于缓存
            FileInputStream fis = null; //因为File没有读写的能力，所以需要使用InputStream；创建字节输入流；
            BufferedInputStream bis = null; //创建缓冲的输入流,输入流数据分批读取，每次读取一部分到缓冲中；操作完缓冲中的这部分数据之后，再从输入流中读取下一部分的数据。
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                OutputStream outputStream = null;
                try{
                    outputStream = response.getOutputStream();  //用于输出字符流数据或者二进制的字节流数据都可以输出。这样就可以写图片、文件
                }catch(Exception e) {

                }

                int i = bis.read(buffer);//把bis缓冲区里的东西读到bytes数组里去
                while (i != -1) {            //循环读取，得到实际读取到的字节数 读到最后返回-1
                    outputStream.write(buffer, 0, i);  //把字节0到i输出
                    i = bis.read(buffer);
                }
                if(outputStream != null) {
                    outputStream.flush();//刷新此输出流并强制写出所有缓冲的输出字节,实现下载
                    outputStream.close();
                    outputStream = null;
                }

                return "下载成功";
            } catch (Exception e) {
                e.printStackTrace();
            } finally {                         //一定要关闭文件流。并且关闭文件流必须放在finally里面
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
     * @param sid 前端传到后台的sid进行特定sid查询
     * @return 判断服务器是否存在预览的文件，不存在返回错误页面
     */
    @GetMapping("/preview/{sid}")
    public String toPdfFile(@PathVariable("sid") Integer sid) {
        Word word = wordService.findWordBySid(sid);
        String pdfPath = System.getProperty("user.dir") + "/src/main/resources/static/PDF/";

        File file = new File(System.getProperty("user.dir") +"/src/main/resources/static/Word/" + word.getFileName() + word.getExtName());//需要转换的文件
        try {
            File newFile = new File(pdfPath);//转换之后文件生成的地址
            if (!newFile.exists()) {
                newFile.mkdirs();
            }
            //文件转化
            converter.convert(file).to(new File( pdfPath+ word.getFileName() + ".pdf") ).execute();
            //使用response,将pdf文件以流的方式发送到前端
            ServletOutputStream outputStream = response.getOutputStream();
            InputStream in = new FileInputStream(new File(pdfPath + word.getFileName()  + ".pdf"));// 读取文件
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
     *  老师删除上传的作业
     * @param sid 前端传过来的sid
     * @return 返回json数据进行判断是否删除成功
     */
    @DeleteMapping("/deleteWordBySid")
    @ResponseBody
    public int deleteWordBySid(@RequestParam("sid")Integer sid){
        Word word = wordService.findWordBySid(sid);
        User user = wordService.findUserTelephone(word.getUsername());
        String deleteFilePath = System.getProperty("user.dir") + "/src/main/resources/static/Word/" + word.getFileName() + word.getExtName();//文件保存相对路径
        File file = new File(deleteFilePath);
        file.delete();
        wordService.deleteWordBySid(sid);
        //删除成功发送短信
        String name = user.getName();
        String number = user.getTelephone();
        if (number!=null) {
            try {
                JSONObject json = null;
                ZhenziSmsClient client = new ZhenziSmsClient(apiUrl, appId, appSecret);
                Map<String, String> params = new HashMap<String, String>();//集合
                params.put("message", "尊敬的:" + name + "同学你好，由于你的上传的作业文档不符合要求，老师已经将你上传的作业文档删除，请重新上传作业文档！");
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
     * 教师查看单个作业
     * @param sid 前端传过来额sid
     * @return 返回查到的数据返回前端
     */
    @GetMapping("/findWordBySid")
    @ResponseBody
    public Word findWordBySid(@RequestParam("sid")Integer sid){
        return wordService.findWordBySid(sid);
    }

    /**
     * 教师给学生作业评分
     * @return 返回json数据到前端进行判断是否成功
     */
    @PostMapping("/updateWordBySid")
    @ResponseBody
    public String updateWordBySid(Integer sid,String username,String name,String sclass,
                                  String subject,String teacher,String fileName,String extName,
                                  String url,Integer score,String reson){
        Word word = new Word();
        word.setSid(sid);
        word.setUsername(username);
        word.setName(name);
        word.setSclass(sclass);
        word.setSubject(subject);
        word.setTeacher(teacher);
        word.setFileName(fileName);
        word.setExtName(extName);
        word.setUrl(url);
        word.setScore(score);
        word.setReson(reson);
        wordService.updateWordBySid(word);
        return "评分成功";
    }

    /**
     * 教师查看已经评成绩的作业
     * @param teacher 前端传过来的teacher参数
     * @param model 将查询到的数据保存在model中
     * @return 返回教师查看已经评分的作业页面
     */
    @GetMapping("/findWordByScore")
    public String findWordByScore(@RequestParam("teacher")String teacher,Model model){
        List<Word>words = wordService.findWordByScore(teacher);
        model.addAttribute("words",words);
        return "teachers/teachersLookScoreWord.html";
    }
}
