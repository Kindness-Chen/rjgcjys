package com.csr.rjgcjys.controller;

import com.alibaba.fastjson.JSON;
import com.csr.rjgcjys.entities.User;
import com.csr.rjgcjys.service.CourseDesignService;
import com.csr.rjgcjys.service.LeaveService;
import com.csr.rjgcjys.service.UserService;
import com.csr.rjgcjys.service.WeekService;
import com.csr.rjgcjys.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

@Controller
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private WeekService weekService;
    @Autowired
    private LeaveService leaveService;
    @Autowired
    private WordService wordService;
    @Autowired
    private CourseDesignService courseDesignService;
    /**
     * 管理员添加用户
     * @param map
     * @param username 学生的学号或者老师的教工号或者教研室主任的教工号
     * @param password 密码
     * @param name      学生、老师、教研室主任的名字
     * @param telephone 学生、老师、教研室主任的手机号码
     * @param identity   学生、老师、教研室主任、管理员的身份区分
     * @return 返回的数字用于判断添加的字段是否为空，输入的内容是否合法
     */
    @PostMapping("/insertUser")
    @ResponseBody
    public int insertUser(ModelMap map, String username, String password, String name, String telephone, String identity) {
        int msg ;
        User user = new User();
        List<User>users = userService.findUserAll();
       user.setUsername(username);
       user.setPassword(password);
       user.setName(name);
       user.setTelephone(telephone);
       user.setIdentity(identity);
        for(User findCommonUsername:users){
            if (user.getUsername().equals(findCommonUsername.getUsername())){
                return 5;
            }
        }
       if(user.getUsername().equals("")){
           msg = 0;
           return msg;
       }else if (user.getUsername().length() !=12){
           msg = 6;
           return msg;
       }else if (user.getPassword().length() >15){
           msg = 7;
           return msg;
       }else if (user.getPassword().equals("")){
           msg = 1;
           return msg;
       }else if (user.getName().equals("")){
           msg = 2;
           return msg;
       }else if (user.getName().length() > 10){
           msg = 8;
           return msg;
       }else if (user.getTelephone().length() != 11){
           msg = 3;
           return msg;
       } else{
           userService.insertUser(user);
           msg = 4;
           return msg;
       }
    }

    /**
     * 查询全部用户
     * @param model 将查询出来的用户保存在model中
     * @return 查询成功后返回管理员管理用户页面
     */
    @GetMapping("/findUserAll")
    public String findUserAll(Model model){
        List<User> users = userService.findUserAll();
        model.addAttribute("users",users);
        return "admin/admin.html";
    }

    /**
     * 通过uid查询特定的用户
     * @param uid 前台传过来的uid，进行特定的用户查询
     * @param model 将查询到的用户保存在model中
     * @return 返回查询到的用户数据
     */
    @GetMapping("/findUserById")
    @ResponseBody
    public List<User> findUserById(Integer uid, Model model){
        List<User> user = userService.findUserById(uid);
        model.addAttribute("user",user);
        return user;
    }

    /**
     * 管理员修改用户的信息
     * @param user 用户的数据
     * @param model
     * @return 返回的数字回到前台进行判断，修改内容是否合法正确
     */
    @PutMapping("/updateUser")
    @ResponseBody
    public int updateUser(User user,Model model){
        int msg;
        if(user.getUsername().equals("")||user.getUsername().equals(null)){
            msg = 0;
            return msg;
        }else if (user.getPassword().equals("")||user.getPassword().equals(null)){
            msg = 1;
            return msg;
        }else if (user.getName().equals("")||user.getName().equals(null)){
            msg = 2;
            return msg;
        }else if (user.getName().length() > 10){
            msg = 6;
            return msg;
        }else if (user.getTelephone().length() != 11){
            msg = 3;
            return msg;
        }else if (user.getPassword().length() >15){
            msg = 5;
            return msg;
        } else{
            userService.updateUser(user);
            msg = 4;
            return msg;
        }
    }

    /**
     * 管理员删除用户
     * @param uid 根据前台查询到的uid进行删除
     * @return 返回json数据进行判断是否删除成功
     */
    @DeleteMapping("/deleteUser")
    @ResponseBody
    public String deleteUser(Integer uid){
        Integer inte = userService.deleteUser(uid);
        return JSON.toJSONString(inte);
    }

    /**
     * 返回到登录页面
     * @return
     */
    @GetMapping("/login")
    public String login(){
        return "login";
    }

    /**
     * 各个角色登录的验证
     * @param username 学生、老师、教研室主任、管理员的账号
     * @param password 密码
     * @param identity 角色，按钮进行选择
     * @param map 保存需要返回前端显示的提示信息
     * @param session 将查询到的用户保存在session中，进行前端显示
     * @return 根据角色不同进行登录，返回到不同的页面
     */
    @PostMapping("/login/findUserByIdentity")
    public String findUserByIdentity(@RequestParam("username") String username,
                                         @RequestParam("password") String password,
                                         @RequestParam("identity") String identity,
                                         Map<String,Object> map, HttpSession session){
        List<User> findUserByIdentity = userService.findUserByIdentity(username,password,identity);
        int countWeek = weekService.findCountWeek();
        int countLeave = leaveService.findCountLeave();
        int countWord = wordService.countWord(username);
        int countWeek2 = weekService.findCountWeek2(username);
        int countLeave2 = leaveService.findCountLeave2(username);
        int countCourseDesign = courseDesignService.countCourseDesign(username);
        for (User u:findUserByIdentity){
            if (u.getUsername().equals(username) && u.getPassword().equals(password) && u.getIdentity().equals("管理员")){
                session.setAttribute("user1",findUserByIdentity);
                return "redirect:/main";
            }else if (u.getUsername().equals(username) && u.getPassword().equals(password) && u.getIdentity().equals("学生")){
                session.setAttribute("user1",findUserByIdentity);
                session.setAttribute("countWord",countWord);
                session.setAttribute("countCourseDesign",countCourseDesign);
                return "redirect:/studentsMain";
            }else if (u.getUsername().equals(username) && u.getPassword().equals(password) && u.getIdentity().equals("老师")){
                session.setAttribute("user1",findUserByIdentity);
                session.setAttribute("countWeek2",countWeek2);
                session.setAttribute("countLeave2",countLeave2);
                return "redirect:/teachersMain";
            }else if (u.getUsername().equals(username) && u.getPassword().equals(password) && u.getIdentity().equals("教研室主任")){
                session.setAttribute("user1",findUserByIdentity);
                session.setAttribute("countWeek",countWeek);
                session.setAttribute("countLeave",countLeave);
                return "redirect:/directorsMain";
            }
        }
        map.put("msg","用户名或密码错误");
        return "common/login.html";
    }

    /**
     *
     * @return 返回到管理员主页面
     */
    @GetMapping("/main")
    public String toMain(){
        return "admin/main.html";
    }

    /**
     * 退出登录
     * @param request 将保存在session中的数据全部清除
     * @return 返回到登录页面
     */
    @GetMapping("/tologin")
    public String tologout(HttpServletRequest request){
        Enumeration em = request.getSession().getAttributeNames();
        while(em.hasMoreElements()){
            request.getSession().removeAttribute(em.nextElement().toString());
        }
        return "common/login.html";
    }

    /**
     *
     * @return 查询各个角色的用户数
     */
    @GetMapping("/countStudents")
    @ResponseBody
    public int countUser(){
        return userService.countStudents();
    }
    @GetMapping("/countTeachers")
    @ResponseBody
    public int countTeachers(){
        return userService.countTeachers();
    }
    @GetMapping("/countDirectors")
    @ResponseBody
    public int countDirectors(){
        return userService.countDirectors();
    }
    @GetMapping("/countAdmins")
    @ResponseBody
    public int countAdmins(){
        return userService.countAdmins();
    }

    /**
     * 学生、老师、教研室主任进行密码修改
     * @param uid 用户的uid，进行判断是那一条数据
     * @param password0 旧密码
     * @param password1 新密码判断
     * @param password 新密码判断存入数据中
     * @return 修改成功后返回到用户的主页面
     */
    @GetMapping("/updatePassword/{uid}")
    @ResponseBody
    public String updatePassword(@PathVariable("uid") Integer uid,
                                 @RequestParam("password0") String password0,
                                 @RequestParam("password1") String password1,
                                 @RequestParam("password") String password){
       User user = userService.findUserByUid(uid);
       if (password0.equals("")||password0 == null){
           return "旧密码不能为空!!!";
       }else if (!user.getPassword().equals(password0)){
           return "旧密码错误，请重新输入!!!";
       }else if (password1.equals("")|| password.equals("")){
           return "新密码不能为空!!!";
       } else if(!password1.equals(password) ){
           return "输入两次的新密码不一致，请重新输入!!!";
       }
       user.setPassword(password);
       userService.updateUser(user);
       return "修改密码成功!!!";
    }
}
