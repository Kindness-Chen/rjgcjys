package com.csr.rjgcjys.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.csr.rjgcjys.entities.Mutual;
import com.csr.rjgcjys.entities.User;
import com.csr.rjgcjys.service.MutualService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.expression.Lists;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.List;

@Controller
public class MutualController {
    @Autowired
    private MutualService mutualService;

    /**
     * 教师查看可以互评的老师页面
     * @param model 将获取的数据保存在model中
     * @return 返回教师查看互评页面
     */
    @GetMapping("/teachersMutual")
    public String teachersMutual(@RequestParam("username")String username, Model model){
        List<User>users = mutualService.findUserByIdentity(username);
        Calendar date = Calendar.getInstance();
        String year = String.valueOf(date.get(Calendar.YEAR));
        model.addAttribute("year",year);
        model.addAttribute("users",users);
        return "teachers/teachersLookMutual.html";
    }

    /**
     * 老师给其他老师评分
     * @param mutual 前端传过来的mutual数据
     * @return 返回json数据到前端进行判断输入是否合理
     */
    @PostMapping("/insertMutual")
    @ResponseBody
    public String insertMutual(Mutual mutual){
        int number = mutualService.findCountUsername(mutual.getUsername())+1;//评分人数
        List<Mutual> haveMutual = mutualService.findCommonMutual(mutual.getUsername(), mutual.getName1(),mutual.getYear());//这个老师是否评过分
        int zong = mutualService.findSumBranch(mutual.getUsername())+mutual.getBranch();//总分
        int average = zong/number;

        List<Mutual> haveTotal = mutualService.findMutualByTotal(mutual.getUsername());

        if (!haveMutual.isEmpty()){
            return "您这个年度已经给这位老师评过分，不能再评分";
        }
        if (!haveTotal.isEmpty()){
            return "教研室主任已经对这个老师进行年度总评，不能再评分";
        }
        mutual.setNumber(number);
        mutual.setAverage(average);
        mutualService.insertMutual(mutual);
        return "互评成功";
    }

    /**
     * 教师查看其他老师对其的评分
     * @param username 前端传过来的username
     * @param model 将查询到的数据保存在model中
     * @return 返回教师查看评分页面
     */
    @GetMapping("/findMutualByUsername")
    public String findMutualByUsername(@RequestParam("username")String username,Model model){
        List<Mutual>mutuals = mutualService.findMutualByUsername(username);
        model.addAttribute("mutuals",mutuals);
        return "teachers/teachersLookHaveMutual.html";
    }

    /**
     * 教研室主任查看老师互评平均分并评总分
     * @param model 将查询到的数据保存在model中
     * @return 返回到教研室主任查看老师互评页面
     */
    @GetMapping("/directorsLookMutual")
    public String directorsLookMutual(Model model){
//        List<Mutual>mutuals = mutualService.directorsLookMutual();
//        model.addAttribute("mutuals",mutuals);
        List<Mutual>mutuals = mutualService.findMutualByAll();
        model.addAttribute("mutuals",mutuals);
        return "directors/directorsLookMutual.html";
    }

    /**
     * 根据mid查询特定的数据
     * @param mid 前端传过来的mid
     * @return 返回这个mid的json数据
     */
    @GetMapping("/findMutualByMid")
    @ResponseBody
    public Mutual findMutualByMid(@RequestParam("mid")Integer mid){
        return mutualService.findMutualByMid(mid);
    }

    /**
     * 教研室主任添加年度总评
     * @param mutual 前端传过来的mutual
     * @return 返回json数据进行判断是否修改成功
     */
    @PostMapping("/directorsUpdateMutual")
    @ResponseBody
    public int directorsUpdateMutual(Mutual mutual){
        mutualService.directorsUpdateMutual(mutual);
        return 1;
    }

    /**
     * 老师查看教研室主任总评
     * @param model 将查询到的数据保存在model中
     * @return 返回到老师查看教研室总评页面
     */
    @GetMapping("/teacherLookFinalMutual")
    public String teacherLookFinalMutual(Model model){
        List<Mutual>mutuals = mutualService.findMutualByAll();
        model.addAttribute("mutuals",mutuals);
        return "teachers/teachersLookFinalMutual.html";
    }

    /**
     * 教师一键互评
     * @param jsonData 前端使用ajax穿过来的json数据
     * @return 返回json数据到前端进行判断是否一键互评成功
     */
    @PostMapping("/addMutual")
    @ResponseBody
    public int oneMutual(@RequestParam("jsonData") String jsonData,@RequestParam("name1")String name1){
        List<Mutual>findTeacherHaveMutual = mutualService.findTeacherHaveMutual(name1);
        if (!findTeacherHaveMutual.isEmpty()){
            return 3;
        }
        List<Mutual> findDirectorHaveMutual = mutualService.findDirectorHaveMutual();
        if(!findDirectorHaveMutual.isEmpty()){
            return 2;
        }else {
            List<Mutual> mutuals = JSON.parseArray(jsonData, Mutual.class);
            mutualService.oneMutual(mutuals);
            return 1;
        }
    }

    /**
     * 教研室主任一键互评
     * @param jsonData 前端使用ajax穿过来的json数据
     * @return 返回json数据到前端进行判断是否一键总评成功
     */
    @PostMapping("/updateMutual")
    @ResponseBody
    public int updateMutual(@RequestParam("jsonData") String jsonData){

        List<Mutual> findDirectorHaveMutual = mutualService.findDirectorHaveMutual();
        if(!findDirectorHaveMutual.isEmpty()){
            return 2;
        }else {
            List<Mutual> mutuals = JSON.parseArray(jsonData, Mutual.class);
            for (Mutual mutual : mutuals) {
                mutualService.directorsUpdateMutual(mutual);
            }
            return 1;
        }
    }
}
