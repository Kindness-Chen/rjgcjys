package com.csr.rjgcjys.controller;

import com.csr.rjgcjys.entities.PPT;
import com.csr.rjgcjys.entities.Week;
import com.csr.rjgcjys.service.PPTService;
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
public class PPTController {
    private static final Logger LOGGER = LoggerFactory.getLogger(PPTController.class);
    @Autowired
    private PPTService pptService;
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
     * 进入到老师添加ppt页面
     * @return 返回老师添加ppt页面
     */
    @GetMapping("/uploadPPT")
    public String uploadPPT(@RequestParam("name")String name,Model model){
        List<Week> weeks = weekService.findSclassByNameStatus(name);
        model.addAttribute("weeks",weeks);
        return "teachers/teachersUploadPPT.html";
    }

    /**
     * 教师上传ppt
     * @param username 前端传过开的username
     * @param name 前端传过来的name
     * @param ppt 需要上传的ppt
     * @return 返回json数据回前端进行判断输入的数据是否合法
     */
    @PostMapping("/insertPPT")
    @ResponseBody
    public String insertPPT(@RequestParam("username")String username,@RequestParam("name")String name,@RequestParam("sclass")String sclass, @RequestParam("pptName")MultipartFile ppt){
        String pptUrl = "static/PPT/";//保存在数据库中的路径
        PPT ppt1 = new PPT();
        ppt1.setUsername(username);
        ppt1.setName(name);
        ppt1.setSclass(sclass);

        if (ppt.isEmpty()) {
            return "上传失败，请选择文件!!!";
        }else if (ppt1.getSclass().equals("")){
            return "课程名不能为空";
        }
        String  originalFilename = ppt.getOriginalFilename();
        String pptExtName = WordUtils.getExtensionName(originalFilename);
        ppt1.setPptExtName(pptExtName);
        System.out.println(ppt1.getPptExtName());
        if (ppt1.getPptExtName().equals(".ppt")||ppt1.getPptExtName().equals(".pptx")||ppt1.getPptExtName().equals(".zip")||ppt1.getPptExtName().equals(".rar")){
            String pptName = WordUtils.getFileNameNoEx(originalFilename);
            //加个时间戳，尽量避免文件名称重复
            String path = System.getProperty("user.dir") + "/src/main/resources/static/PPT/" + pptName + pptExtName;//文件保存相对路径
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
                ppt.transferTo(dest);//把内存中的文件存进磁盘中（相当于服务器）
                LOGGER.info("上传成功");
                pptUrl = pptUrl + pptName + pptExtName;//本地运行项目
                pptService.insertPPT(username, name, sclass, pptName, pptExtName, pptUrl);
                return "上传成功!!!";
            } catch (IOException e) {
                LOGGER.error(e.toString(), e);
            }
        }else {
            return "上传的格式不正确";
        }
        return "上传失败";
    }

    /**
     * 教师通过自己的username查询自己已经上传的ppt
     * @param model 将数据保存在model中
     * @param username 前端传过来的username
     * @return 返回到教师查看ppt页面
     */
    @GetMapping("/findPPTByUsername")
    public String findPPTByUsername(Model model,@RequestParam("username")String username){
        List<PPT>ppts = pptService.findPPTByUsername(username);
        model.addAttribute("ppts",ppts);
        return "teachers/teachersLookPPT.html";
    }

    /**
     *  教师下载PPT
     * @param pid 前端传过来的pid，进行特定的数据查询
     * @param response
     * @return 返回的数据判断是否下载成功
     * @throws Exception 抛异常
     */
    @GetMapping("/downloadPPT/{pid}")
    public String downloadPPT (@PathVariable("pid") Integer pid, HttpServletResponse response) throws Exception {
        PPT ppt = pptService.findPPTByPid(pid);
        String downloadFilePath = System.getProperty("user.dir") + "/src/main/resources/static/PPT/" + ppt.getPptName() + ppt.getPptExtName();//文件保存相对路径

        File file = new File(downloadFilePath);//创建File类，将文件读入内存中

        if (file.exists()) {
            String down_file_name = ppt.getPptName() + ppt.getPptExtName();
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
                while (i != -1) {          //循环读取，得到实际读取到的字节数 读到最后返回-1
                    outputStream.write(buffer, 0, i);//把字节0到i输出
                    i = bis.read(buffer);           //文件数据写完后没有数据就会返回-1，循环结束
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
     * @param pid 前端传到后台的tid进行特定tid查询
     * @return 判断服务器是否存在预览的文件，不存在返回错误页面
     */
    @GetMapping("/previewPPT/{pid}")
    public String toPdfFile(@PathVariable("pid") Integer pid) {
        PPT ppt = pptService.findPPTByPid(pid);
        String pdfPath = System.getProperty("user.dir") + "/src/main/resources/static/PPTPDF/";

        File file = new File(System.getProperty("user.dir") +"/src/main/resources/static/PPT/" + ppt.getPptName() + ppt.getPptExtName());//需要转换的文件
        try {
            File newFile = new File(pdfPath);//转换之后文件生成的地址
            if (!newFile.exists()) {
                newFile.mkdirs();
            }
            //文件转化
            converter.convert(file).to(new File( pdfPath+ ppt.getPptName() + ".pdf") ).execute();
            //使用response,将pdf文件以流的方式发送到前端
            ServletOutputStream outputStream = response.getOutputStream();
            InputStream in = new FileInputStream(new File(pdfPath + ppt.getPptName() + ".pdf"));// 读取文件
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
     * 教师删除上传的PPT
     * @param pid 前端传过来的pid
     * @return 返回json数据进行判断是否删除成功
     */
    @DeleteMapping("/deletePPTByPid")
    @ResponseBody
    public int deletePPTByPid(@RequestParam("pid")Integer pid){
        PPT ppt = pptService.findPPTByPid(pid);
        String deleteFilePath = System.getProperty("user.dir") + "/src/main/resources/static/PPT/" + ppt.getPptName() + ppt.getPptExtName();//文件保存相对路径
        //删除PPT
        File file = new File(deleteFilePath);
        file.delete();
        pptService.deletePPTByPid(pid);
        return 1;
    }

    /**
     * 教研室主任查看教学资料
     * @param model 将查询到的数据保存到model中
     * @return 返回到教研室主任查看教学资料页面
     */
    @GetMapping("/findPPTAll")
    public String findPPTAll(Model model){
        List<PPT> ppts = pptService.findPPTAll();
        model.addAttribute("ppts",ppts);
        return "directors/directorsLookPPT.html";
    }
}
