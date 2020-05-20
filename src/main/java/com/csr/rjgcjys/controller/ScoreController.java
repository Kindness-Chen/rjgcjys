package com.csr.rjgcjys.controller;

import com.csr.rjgcjys.entities.Score;
import com.csr.rjgcjys.entities.Train;
import com.csr.rjgcjys.entities.User;
import com.csr.rjgcjys.service.ScoreService;
import com.csr.rjgcjys.service.TrainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class ScoreController {
    @Autowired
    private ScoreService scoreService;
    @Autowired
    private TrainService trainService;

    /**
     * 老师端查看身份为学生的用户
     * @param model 将查询到的数据保存在model中
     * @return 返回到老师查询学生页面
     */
    @GetMapping("/findStudentsAll")
    public String findStudentsAll(Model model){
        List<User>users = scoreService.findStudentsAll();
        model.addAttribute("users",users);
        List<Train>trains = trainService.findSclassAll();
        model.addAttribute("trains",trains);
        return "teachers/teacherScore.html";
    }

    /**
     * 教师给学生评成绩
     * @param username 前端传过来的学号
     * @param name 姓名
     * @param sclass 课程名
     * @param usualScore 平时成绩
     * @param examScore 考试成绩
     * @param finalScore 总评成绩
     * @return 返回json回前台进行判断是否输入正确
     */
    @PostMapping("/insertStudentsScore")
    @ResponseBody
    public String insertStudentsScore(@RequestParam("username")String username,@RequestParam("name")String name,
                                      @RequestParam("sclass")String sclass,@RequestParam("usualScore")Integer usualScore,
                                      @RequestParam("examScore")Integer examScore,@RequestParam("finalScore")Integer finalScore,
                                      @RequestParam("tUsername")String tUsername){
        Score score = new Score();
        score.setUsername(username);
        score.setName(name);
        score.setSclass(sclass);
        score.setUsualScore(usualScore);
        score.setExamScore(examScore);
        score.setFinalScore(finalScore);
        score.settUsername(tUsername);
        if (score.getSclass().equals("")){
            return "请输入课程名";
        }else if(score.getUsualScore()==null||score.getExamScore()==null||score.getFinalScore()==null){
            return "请输入各别成绩";
        }
        scoreService.insertStudentsScore(username,name,sclass,usualScore,examScore,finalScore,tUsername);
        return "提交成功";
    }

    /**
     * 学生查询教师已经评的成绩
     * @param model 将通过username查询到的数据保存在model中
     * @return 返回学生查询页面
     */
    @GetMapping("/findScoreByUsername/{username}")
    public String findScoreByUsername(@PathVariable("username")String username, Model model){
        List<Score>scores = scoreService.findScoreByUsername(username);
        model.addAttribute("scores",scores);
        return "students/studentsLookScore.html";
    }

    /**
     * 教师查询全部已经评成绩的学生
     * @param model 将数据保存在model中
     * @return 返回教师查看学生已经评成绩页面
     */
    @GetMapping("/findStudentsScoreAll")
    public String findStudentsScoreAll(@RequestParam("tUsername")String tUsername, Model model){
        List<Score> scores = scoreService.findStudentsScoreAll(tUsername);
        model.addAttribute("scores",scores);
        return "teachers/teachersLookScore.html";
    }

    /**
     * 教师查询某个学生成绩，根据cid查询并回显数据
     * @param cid 前端传过来的cid
     * @return 返回这个cid查询到的json数据
     */
    @GetMapping("/findScoreByCid")
    @ResponseBody
    public Score findScoreByCid(@RequestParam("cid")Integer cid){
        return scoreService.findScoreByCid(cid);
    }

    /**
     * 教师编辑学生成绩
     * @param cid 前端传值的cid
     * @param username 学号
     * @param name 姓名
     * @param sclass 课程名
     * @param usualScore 平时成绩
     * @param examScore 考试成绩
     * @param finalScore 总评成绩
     * @return 返回值到前端判断数据是否合法
     */
    @GetMapping("/teachersUpdateScore")
    @ResponseBody
    public int teachersUpdateScore(Integer cid,String username,String name,String sclass,Integer usualScore,Integer examScore,Integer finalScore,String tUsername){
        Score score = new Score();
        score.setCid(cid);
        score.setUsername(username);
        score.setName(name);
        score.setSclass(sclass);
        score.setUsualScore(usualScore);
        score.setExamScore(examScore);
        score.setFinalScore(finalScore);
        score.settUsername(tUsername);
        if (score.getSclass().equals("")){
            return 1;
        }else if (score.getUsualScore()==null||score.getExamScore()==null||score.getFinalScore()==null){
            return 2;
        }
        scoreService.teachersUpdateScore(score);
        return 3;
    }

    /**
     * 删除已经评成绩的学生成绩
     * @param cid 前端传过来的cid
     * @return 返回json数据到前端判断输入是否合法
     */
    @DeleteMapping("/deleteScoreByCid")
    @ResponseBody
    public int deleteScoreByCid(@RequestParam("cid")Integer cid){
        scoreService.deleteScoreByCid(cid);
        return 1;
    }

}
