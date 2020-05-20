package com.csr.rjgcjys.controller;

import com.alibaba.fastjson.JSONObject;
import com.csr.rjgcjys.entities.Notice;
import com.csr.rjgcjys.entities.User;
import com.csr.rjgcjys.service.NoticeService;
import com.zhenzi.sms.ZhenziSmsClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Controller
public class NoticeController {
    @Autowired
    private NoticeService noticeService;
    //短信平台相关参数
    private String apiUrl = "https://sms_developer.zhenzikj.com";
    private String appId = "104726";
    private String appSecret = "a63a9465-9e69-425e-b23c-a672dd5f4228";

    /**
     *
     * @return 跳转到新增发布通知页面
     */
    @GetMapping("/deliverNotice")
    public String deliverNotice(){
        return "directors/directorsDeliverNotice.html";
    }

    /**
     *
     * @return 跳转到教研室主任主页
     */
    @GetMapping("/directorsMain")
    public String directorsMain(){
        return "directors/directorsMain.html";
    }

    /**
     *
     * @return 跳转到老师主页
     */
    @GetMapping("/teachersMain")
    public String teachersMain(){
        return "teachers/teachersMain.html";
    }

    /**
     *
     * @param binder ajax前端传过来的Date是String格式，该功能在Controller中把String转换为Date格式
     */
    @InitBinder
    public void initBinder(WebDataBinder binder, WebRequest request) {
        //转换日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //CustomDateEditor为自定义日期编辑器
        binder.registerCustomEditor(Date.class, new CustomDateEditor(sdf, true));
    }

    /**
     *  添加发布通知
     * @param title 标题
     * @param name 发布人
     * @param notice 发布内容
     * @param deliverDate 发布日期
     * @return 前端判断
     */
    @PostMapping("/insertNotice")
    @ResponseBody
    public int insertNotice(@RequestParam("title") String title, @RequestParam("name") String name, @RequestParam("notice")String notice, @RequestParam("deliverDate") String deliverDate){
       if (title.equals("")){
           return 1;
       }else if (notice.equals("<p><br></p>")){
            return 2;
        }else if (deliverDate.equals("")){
           return 3;
       }
        noticeService.insertNotice(title,name,notice,deliverDate);
        return 4;
    }

    /**
     * 教研室主任查询全部已经发布的通知
     * @return 回显到查询通知的页面
     */
    @GetMapping("/findNoticeAll")
    public String findNoticeAll(Model model){
        List<User>users = noticeService.directorsFindTelephone();
        model.addAttribute("users",users);
        List<Notice> notices = noticeService.findNoticeAll();
        model.addAttribute("notices",notices);
        return "directors/directorsLookNotice.html";
    }

    /**
     * 老师主任查询全部已经发布的通知
     * @return 回显到查询通知的页面
     */
    @GetMapping("/teacherFindNoticeAll")
    public String teacherFindNoticeAll(Model model){
        List<Notice> notices = noticeService.findNoticeAll();
        model.addAttribute("notices",notices);
        return "teachers/teacherLookNotice.html";
    }

    /**
     * 查询已经发布的一个同并回显
     * @param nid 需要查询的nid，前台传值过来
     * @return 返回根据nid查询到的数据
     */
    @GetMapping("/findNoticeByNid")
    @ResponseBody
    public Notice findNoticeByNid(@RequestParam("nid") Integer nid){
        return noticeService.findNoticeByNid(nid);
    }

    /**
     * 删除已经发布的通知
     * @param nid 前端传过来需要删除的nid
     * @return 返回1到前端进行判断是否删除成功
     */
    @DeleteMapping("/deleteNoticeByNid")
    @ResponseBody
    public int deleteNoticeByNid(@RequestParam("nid")Integer nid){
         noticeService.deleteNoticeByNid(nid);
         return 1;
    }

    /**
     * 教研室主任修改通知
     * @param nid 前端传过来的nid
     * @param title 前端传过来的title
     * @param name 前端传过来的name
     * @param notice 前端传过来的notice
     * @param deliverDate 前端传过来的deliverDate
     * @return 返回json数据到前端进行判断输入的数据是否合法
     */
    @PostMapping("/directorsUpdateNotice")
    @ResponseBody
    public int directorsUpdateNotice(@RequestParam("nid")Integer nid,@RequestParam("title") String title, @RequestParam("name") String name, @RequestParam("notice")String notice, @RequestParam("deliverDate") String deliverDate){
        Notice notice1 = new Notice();
        notice1.setNid(nid);
        notice1.setTitle(title);
        notice1.setName(name);
        notice1.setNotice(notice);
        notice1.setDeliverDate(deliverDate);
        if (notice1.getTitle().equals("")){
            return 1;
        }else if (notice1.getNotice().equals("<p><br></p>")){
            return 2;
        }else if (notice1.getDeliverDate().equals("")){
            return 3;
        }
        noticeService.directorsUpdateNotice(notice1);
        return 4;
    }
    /**
     * 教研室主任单发短信给老师
     * @param notice1 前端传过来的通知内容
     * @param name 前端传过来的教师名字
     * @return 返回json数据到前端进行判断是否发送成功
     */
    @PostMapping("/directorsSendNotice")
    @ResponseBody
    public String directorsSendNotice(String notice1,String name){
        String number = noticeService.directorsFindTelephoneByName(name);
        if (notice1!=null){
            try {
                JSONObject json = null;
                ZhenziSmsClient client = new ZhenziSmsClient(apiUrl, appId, appSecret);
                Map<String, String> params = new HashMap<String, String>();
                params.put("message", "尊敬的:"+name+"老师您好，教研室主任已经发布新通知，通知内容为：" + notice1 + "您也可以登录系统查看更多的通知！");
                params.put("number", number);
                String result = client.send(params);//send方法用于单条发送短信,所有请求参数需要封装到Map中
                json = JSONObject.parseObject(result);
                if (json.getIntValue("notice1")!=0){//发送短信失败
                    return  "失败";
                }
                //以json存放，这里使用的是阿里的fastjson
                json = new JSONObject();
                json.put("createTime",System.currentTimeMillis());
                return "成功";
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "失败";
    }
}