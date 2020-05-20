package com.csr.rjgcjys.controller;

import com.alibaba.fastjson.JSONObject;
import com.csr.rjgcjys.entities.Leave;
import com.csr.rjgcjys.entities.Train;
import com.csr.rjgcjys.entities.User;
import com.csr.rjgcjys.entities.Week;
import com.csr.rjgcjys.service.LeaveService;
import com.csr.rjgcjys.service.TrainService;
import com.csr.rjgcjys.service.WeekService;
import com.csr.rjgcjys.service.WordService;
import com.csr.rjgcjys.tools.WordUtils;
import com.zhenzi.sms.ZhenziSmsClient;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class LeaveController {
    private static final Logger LOGGER = LoggerFactory.getLogger(LeaveController.class);
    //短信平台相关参数
    private String apiUrl = "https://sms_developer.zhenzikj.com";
    private String appId = "104726";
    private String appSecret = "a63a9465-9e69-425e-b23c-a672dd5f4228";
    @Autowired
    private LeaveService leaveService;
    @Autowired
    private WordService wordService;
    @Autowired
    private TrainService trainService;
    @Autowired
    private WeekService weekService;
    @Autowired
    private HttpServletResponse response;
    @Autowired
    private HttpServletRequest request;
    /**
     * 教师进入调课申请
     * @return 返回教师调课申请页面
     */
    @GetMapping("/teachersAddLeave")
    public String teachersAddLeave(@RequestParam("name")String name, Model model){
        List<User> users = wordService.findUserByName();
        model.addAttribute("users",users);
        List<Week> weeks = weekService.findSclassByNameStatus(name);
        model.addAttribute("weeks",weeks);
        List<Train>trains1 = trainService.findClassName();
        model.addAttribute("trains1",trains1);
        return "teachers/teachersAddLeave.html";
    }

    /**
     * 教师申请调课申请
     * @param leave 前端传过来的数据，fro表单传过来
     * @return 返回json数据到前端进行判断输入是否合法
     */
    @PostMapping("/insertLeave")
    @ResponseBody
    public String insertLeave(Leave leave){
        leave.setReson(null);
        leave.setImgName(null);
        leave.setImgExt(null);
        leave.setImgUrl(null);

        if (leave.getSclass().equals("")||leave.getSubject().equals("")||leave.getName().equals("")||leave.getName1().equals("")||
        leave.getClassTime().equals("")||leave.getAfterTime().equals("")||leave.getClassPlace().equals("")||leave.getAfterPlace().equals("")||
        leave.getClassReson().equals("")||leave.getResonTime().equals("")){
            return "红星标号为必填项不能为空";
        }else {
            leaveService.insertLeave(leave);
            return "申请成功";
        }
    }

    /**
     * 教师查看调课申请
     * @param username 前端传过来的username
     * @param model 将查询到的数据保存在model中
     * @return 返回教师查看调课申请页面
     */
    @GetMapping("/findLeaveByStatus")
    public String findLeaveByStatus(@RequestParam("username")String username, Model model){
        List<User> users = wordService.findUserByName();
        model.addAttribute("users",users);
        List<Train>trains = trainService.findSclassAll();
        model.addAttribute("trains",trains);
        List<Train>trains1 = trainService.findClassName();
        model.addAttribute("trains1",trains1);
        List<Leave>leaves = leaveService.findLeaveByStatus(username);
        model.addAttribute("leaves",leaves);
        return "teachers/teachersLookNoLeave.html";
    }

    /**
     * 教师查看调课申请
     * @param lid 前端传过来额lid
     * @return 返回查到的数据返回前端
     */
    @GetMapping("/findLeaveByLid")
    @ResponseBody
    public Leave findLeaveByLid(@RequestParam("lid")Integer lid){
        return leaveService.findLeaveByLid(lid);
    }

    /**
     * 教师修改调课申请
     * @param leave 前端传过来的数据
     * @return 返回json数据到前端进行判断修改是否合法
     */
    @GetMapping("/updateLeave")
    @ResponseBody
    public String updateLeave(Leave leave){
        leave.setReson(null);
        leave.setImgName(null);
        leave.setImgExt(null);
        leave.setImgUrl(null);

        if (leave.getSclass().equals("")||leave.getSubject().equals("")||leave.getName().equals("")||leave.getName1().equals("")||
                leave.getClassTime().equals("")||leave.getAfterTime().equals("")||leave.getClassPlace().equals("")||leave.getAfterPlace().equals("")||
                leave.getClassReson().equals("")||leave.getResonTime().equals("")){
            return "红星标号为必填项不能为空";
        }else {
            leaveService.updateLeave(leave);
            return "修改成功";
        }
    }

    /**
     * 教师删除调课申请
     * @param lid 前端传过来的lid
     * @return 返回json数据进行判断是否删除成功
     */
    @DeleteMapping("/deleteLeaveByLid")
    @ResponseBody
    public int deleteLeave(@RequestParam("lid")Integer lid){
        leaveService.deleteLeaveByLid(lid);
        return 1;
    }

    /**
     * 教研室主任查看待审核调课申请
     * @param model 将数据保存在model中
     * @return 返回教研室主任查看待审核调课申请页面
     */
    @GetMapping("/directorsFindLeave")
    public String directorsFindLeave(Model model){
        List<User> users = wordService.findUserByName();
        model.addAttribute("users",users);
        List<Train>trains = trainService.findSclassAll();
        model.addAttribute("trains",trains);
        List<Leave>leaves = leaveService.directorsFindLeave();
        model.addAttribute("leaves",leaves);
        return "directors/directorsCheckLeave.html";
    }

    /**
     *教研室主审核调课申请
     * @return 返回json数据到前端进行判断
     */
    @PostMapping("/directorsUpdateLeave")
    @ResponseBody
    public String directorsUpdateLeave(@RequestParam(value = "imgName",required = false) MultipartFile img,Integer lid,String username,String sclass,String subject,String name,String name1,String classTime,String afterTime,String classPlace,String afterPlace,String classReson,String resonTime,String status,String reson){
        String imgUrl = "static/LeaveImg/";//保存在数据库中的路径
        Leave leave = new Leave();
        leave.setLid(lid);
        leave.setUsername(username);
        leave.setSclass(sclass);
        leave.setSubject(subject);
        leave.setName(name);
        leave.setName1(name1);
        leave.setClassTime(classTime);
        leave.setAfterTime(afterTime);
        leave.setClassPlace(classPlace);
        leave.setAfterPlace(afterPlace);
        leave.setClassReson(classReson);
        leave.setResonTime(resonTime);
        leave.setStatus(status);
        leave.setReson(reson);

        Leave leave1 = leaveService.findLeaveByLid(lid);
        User user = leaveService.findUserTelephone(leave1.getUsername());
        String name2 = user.getName();
        String number = user.getTelephone();

       if(leave.getStatus().equals("审核否决")){
            leave.setImgName(null);
            leave.setImgExt(null);
            leave.setImgUrl(null);
            leaveService.directorsUpdateLeave(leave);
            try {
                JSONObject json = null;
                ZhenziSmsClient client = new ZhenziSmsClient(apiUrl, appId, appSecret);
                Map<String, String> params = new HashMap<String, String>();
                params.put("message", "尊敬的:" + name2 + "老师您好，您上传的调课申请教研室主任已经审核完成，审核的结果为：审核否决。请您登录系统重新上传调课申请进行审核。");
                params.put("number", number);
                String result = client.send(params);//send方法用于单条发送短信,所有请求参数需要封装到Map中
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
        }else{
            String originalFilename = img.getOriginalFilename();
            String imgExt = WordUtils.getExtensionName(originalFilename);
            String imgName = WordUtils.getFileNameNoEx(originalFilename);
            //加个时间戳，尽量避免文件名称重复
            leave.setImgExt(imgExt);
            if (leave.getImgExt().equals(".jpg")||leave.getImgExt().equals(".png")||leave.getImgExt().equals(".bmp")){
            String path = System.getProperty("user.dir") + "/src/main/resources/static/LeaveImg/" + imgName + imgExt;//图片保存相对路径
            System.out.println("==========" + path);
            File dest = new File(path);
            //判断文件是否已经存在
            if (dest.exists()) {
                return "您已提交过，请勿重复提交";
            }else if (img.getSize()>10*1024*1024) {
                System.out.println(img.getSize());
                return "上传的图片超过限制的10M";
            }
            //判断文件父目录是否存在
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdir();
            }
            try {
                img.transferTo(dest);
                LOGGER.info("审核成功");
                imgUrl = imgUrl + imgName + imgExt;//本地运行项目
                leave.setImgName(imgName);
                leave.setImgExt(imgExt);
                leave.setImgUrl(imgUrl);
                leaveService.directorsUpdateLeave(leave);
                try {
                    JSONObject json = null;
                    ZhenziSmsClient client = new ZhenziSmsClient(apiUrl, appId, appSecret);
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("message", "尊敬的:" + name2 + "老师您好，您上传的调课申请教研室主任已经审核完成，审核的结果为：审核通过。请您登录系统下载教研室主任审核通过的签名图片。");
                    params.put("number", number);
                    String result = client.send(params);
                    json = JSONObject.parseObject(result);
                    if (json.getIntValue("number") != 0) {//发送短信失败
                        return "短信发送失败";
                    } //以json存放，这里使用的是阿里的fastjson
                    json = new JSONObject();
                    json.put("createTime", System.currentTimeMillis());
                    return "审核成功";
                }catch (Exception e) {
                    e.printStackTrace();
                }
                return "审核成功";
            } catch (IOException e) {
                LOGGER.error(e.toString(), e);
            }
        }else {
                return "上传的格式不是图片";
            }
        }
        return "审核失败";
    }

    /**
     * 教研室主任查看已经审核的调课申请
     * @param model 将查询到的数据保存载model中
     * @return 返回教研室主任查看调课申请页面
     */
    @GetMapping("/directorsFindHaveLeave")
    public String directorsFindHaveLeave(Model model){
        List<User> users = wordService.findUserByName();
        model.addAttribute("users",users);
        List<Train>trains = trainService.findSclassAll();
        model.addAttribute("trains",trains);
        List<Leave>leaves = leaveService.directorsFindHaveLeave();
        model.addAttribute("leaves",leaves);
        return "directors/directorsLookLeave.html";
    }

    /**
     * 教师查看已经审核调课申请
     * @param username 前端传过来的username
     * @param model 将查询到的数据保存在model中
     * @return 返回教师查看已经审核调课申请页面
     */
    @GetMapping("/teachersFindHaveLeave")
    public String teachersFindHaveLeave(@RequestParam("username")String username, Model model){
        List<User> users = wordService.findUserByName();
        model.addAttribute("users",users);
        List<Train>trains = trainService.findSclassAll();
        model.addAttribute("trains",trains);
        List<Leave>leaves = leaveService.teachersFindHaveLeave(username);
        model.addAttribute("leaves",leaves);
        return "teachers/teachersLookHaveLeave.html";
    }

    /**
     *  教师下载审核通过图片
     * @param lid 前端传过来的lid，进行特定的数据查询
     * @return 返回的数据判断是否下载成功
     * @throws Exception 抛异常
     */
    @GetMapping("/downloadLeaveImg/{lid}")
    public String downloadImg (@PathVariable("lid") Integer lid, HttpServletResponse response) throws Exception {
        Leave leave = leaveService.findLeaveByLid(lid);
        String downloadFilePath = System.getProperty("user.dir") + "/src/main/resources/static/LeaveImg/" + leave.getImgName() + leave.getImgExt();//图片保存相对路径

        File file = new File(downloadFilePath);

        if (file.exists()) {
            String down_file_name = leave.getImgName() + leave.getImgExt();
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
                try{
                    outputStream = response.getOutputStream();
                }catch(Exception e) {

                }

                int i = bis.read(buffer);
                while (i != -1) {
                    outputStream.write(buffer, 0, i);
                    i = bis.read(buffer);
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

