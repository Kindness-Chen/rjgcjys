package com.csr.rjgcjys.controller;

import com.csr.rjgcjys.entities.Train;
import com.csr.rjgcjys.entities.User;
import com.csr.rjgcjys.service.TrainService;
import com.csr.rjgcjys.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
public class TrainController {
    @Autowired
    private TrainService trainService;
    @Autowired
    private WordService wordService;
    /**
     * 转到教研室主任上传教学任务界面
     * @return 返回老师上传教学任务页面
     */
    @GetMapping("/toTeachersAddStudy")
    public String toTeachersAddStudy(Model model){
        List<User> users = wordService.findUserByName();
        model.addAttribute("users",users);
        return "directors/directorsAddStudy.html";
    }

    /**
     * 教研室主任上传教学任务
     * @param train 前端传过来的数据
     * @return 返回json数据进行判断是否上传成功
     */
    @PostMapping("/insertTrain")
    @ResponseBody
    public String insertTrain(Train train ){
        if (train.getCode().equals("")||train.getSclass().equals("")||train.getName().equals("")||
        train.getWeektime().equals("")||train.getStrattime().equals("")||train.getCredit().equals("")||train.getTotaltime().equals("")||
        train.getLecturetime().equals("")||train.getComputertime().equals("")||
        train.getCategory().equals("")||train.getNumber().equals("")||train.getCollege().equals("")||train.getClassname().equals("")){
            return "红星标号为必填项不能为空";
        }
        trainService.insertTrain(train);
        return "上传成功";
    }

    /**
     * 教学查看教学任务
     * @param username 前端传过来的username
     * @param model 将查询到的数据保存在model中
     * @return 返回教师查看教学任务页面
     */
    @GetMapping("/teachersFindTrainByName")
    public String teachersFindTrainByName(@RequestParam("name")String name, Model model){
        List<Train>trains = trainService.teachersFindTrainByName(name);
        model.addAttribute("trains",trains);
        return "teachers/teachersLookStudy.html";
    }

    /**
     * 查询特定的数据
     * @param rid 前端传过来额rid
     * @return 返回查询到的数据转成json数据
     */
    @GetMapping("/findTrainByRid")
    @ResponseBody
    public Train findTrainByRid(@RequestParam("rid")Integer rid){
        return trainService.findTrainByRid(rid);
    }

    /**
     * 教师修改教学任务
     * @param train 前端传过来的train
     * @return 返回json数据到前端进行判断是否修改成功
     */
    @GetMapping("/teachersUpdateStudy")
    @ResponseBody
    public String teachersUpdateStudy(Train train){
        if (train.getCode().equals("")||train.getSclass().equals("")||train.getName().equals("")||
                train.getWeektime().equals("")||train.getStrattime().equals("")||train.getCredit().equals("")||train.getTotaltime().equals("")||
                train.getLecturetime().equals("")||train.getComputertime().equals("")||
                train.getCategory().equals("")||train.getNumber().equals("")||train.getCollege().equals("")||train.getClassname().equals("")){
            return "红星标号为必填项不能为空";
        }
        trainService.teachersUpdateStudy(train);
        return "修改成功";
    }

    /**
     * 老师删除教学任务
     * @param rid 前端传过来的rid
     * @return 放回json数据到后台进行判断
     */
    @DeleteMapping("/deleteTrainByRid")
    @ResponseBody
    public int deleteTrainByRid(@RequestParam("rid")Integer rid){
        trainService.deleteTrainByRid(rid);
        return 1;
    }

    /**
     * 检验室主任查看查看教学任务
     * @param model 将查询到的全部教学任务保存在model
     * @return 返回教研室查询页面
     */
    @GetMapping("/directorsFindTrainAll")
    public String directorsFindTrainAll(Model model){
        List<Train>trains = trainService.directorsFindTrainAll();
        model.addAttribute("trains",trains);
        List<User> users = wordService.findUserByName();
        model.addAttribute("users",users);
        return "directors/directorsLookStudy.html";
    }
    /**
     * 教研室主任导入课程
     * @param file 前端传过来的file
     * @return 返回json数据进行判断是否成功
     */
    @PostMapping("/importStudy")
    @ResponseBody
    public int importStudy(@RequestParam("file") MultipartFile file) {
        boolean a = false;
        String fileName = file.getOriginalFilename();
        try {
            a = trainService.batchImport(fileName, file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1;
    }

}
