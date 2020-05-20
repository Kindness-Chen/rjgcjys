package com.csr.rjgcjys.controller;

import com.csr.rjgcjys.entities.Subject;
import com.csr.rjgcjys.entities.Train;
import com.csr.rjgcjys.service.SubjectService;
import com.csr.rjgcjys.service.TrainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
public class SubjectController {
    @Autowired
    private TrainService trainService;
    @Autowired
    private SubjectService subjectService;

    /**
     * 教研室主任添加课程页面
     * @param model 将查询到的课程代码保存在model中
     * @return 返回到教研室主任添加课程页面
     */
    @GetMapping("/toDirectorsAddSubject")
    public String toDirectorsAddSubject(Model model){
        List<Train>trains = trainService.findCodeAll();
        model.addAttribute("trains",trains);
        return "directors/directorsAddSubject.html";
    }

    /**
     * 将查询到的课程代码相关信息
     * @param code 前端传过来的code
     * @return 返回根据code查询到的相关信息
     */
    @GetMapping("/findTrainByCode")
    @ResponseBody
    public List<Train> findTrainByCode(@RequestParam("code")String code){
        return  trainService.findTrainByCode(code);
    }

    /**
     * 教师添加课程
     * @param subject 前端传过来的添加对象
     * @return 放回json数据进行判断是否成功
     */
    @PostMapping("/insertSubject")
    @ResponseBody
    public String insertSubject(Subject subject){
        if (subject.getCode().equals("")||subject.getSclass().equals("")||subject.getName().equals("")||subject.getSubjectTime().equals("")
        ||subject.getSubjectPlace().equals("")||subject.getNature().equals("")||subject.getCredit().equals("")||subject.getAssessment().equals("")
        ||subject.getTotaltime().equals("")||subject.getLecturetime().equals("")||subject.getExperimenttime().equals("")||subject.getComputertime().equals("")
        ||subject.getNumber().equals("")||subject.getCollege().equals("")||subject.getConsist().equals("")){
            return "红星标号数据不能为空";
        }
        subjectService.insertSubject(subject);
        return "添加成功";
    }

    /**
     * 教研室主任查看课程
     * @param model 将查询到的数据保存在model中
     * @return 返回教研室主任查看页面
     */
    @GetMapping("/findSubjectAll")
    public String findSubjectAll(Model model){
        List<Train>trains = trainService.findCodeAll();
        model.addAttribute("trains",trains);
        List<Subject>subjects = subjectService.findSubjectAll();
        model.addAttribute("subjects",subjects);
        return "directors/directorsLookSubject.html";
    }

    /**
     * 根据sid查询特定的数据
     * @param sid 前端传过来的sid
     * @return 返回json数据到前端
     */
    @GetMapping("/findSubjectBySid")
    @ResponseBody
    public Subject findSubjectBySid(@RequestParam("sid")Integer sid){
        return subjectService.findSubjectBySid(sid);
    }

    @GetMapping("/directorsUpdateSubject")
    @ResponseBody
    public String directorsUpdateSubject(Subject subject){
        if (subject.getCode().equals("")||subject.getSclass().equals("")||subject.getName().equals("")||subject.getSubjectTime().equals("")
                ||subject.getSubjectPlace().equals("")||subject.getNature().equals("")||subject.getCredit().equals("")||subject.getAssessment().equals("")
                ||subject.getTotaltime().equals("")||subject.getLecturetime().equals("")||subject.getExperimenttime().equals("")||subject.getComputertime().equals("")
                ||subject.getNumber().equals("")||subject.getCollege().equals("")||subject.getConsist().equals("")){
            return "红星标号数据不能为空";
        }
        subjectService.directorsUpdateSubject(subject);
        return "修改成功";
    }

    /**
     * 教研室主任删除课程
     * @param sid 前端传过来的sid
     * @return 返回json数据判断是否删除成功
     */
    @DeleteMapping("/deleteSubjectBySid")
    @ResponseBody
    public int deleteSubjectBySid(@RequestParam("sid")Integer sid){
        subjectService.deleteSubjectBySid(sid);
        return 1;
    }

    /**
     * 教研室主任导入课程
     * @param file 前端传过来的file
     * @return 返回json数据进行判断是否成功
     */
    @PostMapping("/importSubject")
    @ResponseBody
    public int importSubject(@RequestParam("file") MultipartFile file) {
        boolean a = false;
        String fileName = file.getOriginalFilename();
        try {
            a = subjectService.batchImport(fileName, file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1;
    }

    /**
     * 教师查看自己的课程
     * @param name 前端传过来的name
     * @param model 将查询到的数据保存早model中
     * @return 放回教师查看课程页面
     */
    @GetMapping("/teachersFindSubjectByName")
    public String teacherFindSubjectByName(@RequestParam("name")String name,Model model){
        List<Subject>subjects = subjectService.teacherFindSubjectByName(name);
        model.addAttribute("subjects",subjects);
        return "teachers/teachersLookSubject.html";
    }


}
