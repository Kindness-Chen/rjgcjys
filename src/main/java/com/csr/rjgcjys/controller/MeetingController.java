package com.csr.rjgcjys.controller;

import com.csr.rjgcjys.entities.Meeting;
import com.csr.rjgcjys.entities.User;
import com.csr.rjgcjys.service.MeetingService;
import com.csr.rjgcjys.service.WordService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class MeetingController {
    @Autowired
    private MeetingService meetingService;

    /**
     * 教师查看记录会议
     * @param username 前端传过来的username
     * @param model 将查询到的数据保存在model中
     * @return 返回教师查看会议记录页面
     */
    @GetMapping("/findMeetingByUsername")
    public String findMeetingByUsername(@RequestParam("username")String username, Model model){
        List<User> users = meetingService.findUserByIdentity();
        model.addAttribute("users",users);
        List<Meeting>meetings = meetingService.findMeetingByUsername(username);
        model.addAttribute("meetings",meetings);
        return "teachers/teachersLookMeeting.html";
    }

    /**
     * 教师添加会议记录
     * @param meeting 前端传过来的添加记录
     * @return 返回json数据到前端进行判断添加是否成功
     */
    @PostMapping("/teachersInsertMeeting")
    @ResponseBody
    public String teachersInsertMeeting(Meeting meeting){
        if (meeting.getMeetingTime().equals("")||meeting.getMeetingPlace().equals("")||meeting.getMeetingHold()==null||
        meeting.getMeetingPeople()==null||meeting.getMeetingContent().equals("")){
            return "红星标号框的值不能为空";
        }
        meetingService.teachersInsertMeeting(meeting);
        return "添加成功";
    }

    /**
     * 教师根据jid查询特定的会议记录
     * @param jid 前端传过来的jid
     * @return 返回json到前端
     */
    @GetMapping("/findMeetingByJid")
    @ResponseBody
    public Meeting findMeetingByJid(@Param("jid")Integer jid){
        return meetingService.findMeetingByJid(jid);
    }

    /**
     * 教师删除会议记录
     * @param jid 前端传过来的记得
     * @return 返回json数据到前端进行判断是否删除成功
     */
    @DeleteMapping("/teachersDeleteMeeting")
    @ResponseBody
    public int teachersDeleteMeeting(@Param("jid")Integer jid){
        meetingService.teachersDeleteMeeting(jid);
        return 1;
    }

    /**
     * 教研室主任查看会议记录
     * @param model 将获取的数据保存在model中
     * @return 返回教研室查看会议记录页面
     */
    @GetMapping("/findMeetingAll")
    public String findMeetingAll(Model model){
        List<Meeting>meetings = meetingService.findMeetingAll();
        model.addAttribute("meetings",meetings);
        return "directors/directorsLookMeeting.html";
    }
}
