package com.csr.rjgcjys.mapper;

import com.csr.rjgcjys.entities.Model;
import com.csr.rjgcjys.entities.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ModelMapper {
    //添加学生作业样板
    @Insert("insert into t_model (username,name,sclass,status,fileName1,fileName2,fileName3,fileName4,fileName5,fileName6,fileUrl1,fileUrl2,fileUrl3,fileUrl4,fileUrl5,fileUrl6) values (#{username},#{name},#{sclass},#{status},#{fileName1},#{fileName2},#{fileName3},#{fileName4},#{fileName5},#{fileName6},#{fileUrl1},#{fileUrl2},#{fileUrl3},#{fileUrl4},#{fileUrl5},#{fileUrl6})")
    public int insertModel(@Param("username")String username,@Param("name")String name,@Param("sclass")String sclass,@Param("status")String status,@Param("fileName1")String fileName1,@Param("fileName2")String fileName2,@Param("fileName3")String fileName3,@Param("fileName4")String fileName4,@Param("fileName5")String fileName5,@Param("fileName6")String fileName6,@Param("fileUrl1")String fileUrl1,@Param("fileUrl2")String fileUrl2,@Param("fileUrl3")String fileUrl3,@Param("fileUrl4")String fileUrl4,@Param("fileUrl5")String fileUrl5,@Param("fileUrl6")String fileUrl6);
    //查看待审核的作业样板
    @Select("select * from t_model where username = #{username} and status = '待审核'")
    public List<Model>findModelByUsername(@Param("username") String username);
    //查看特定的作业样板
    @Select("select * from t_model where mid = #{mid}")
    public Model findModelByMid(@Param("mid")Integer mid);
    //教研室主任查看所有需要审核的学生作业样板
    @Select("select * from t_model where status = '待审核'")
    public List<Model>findModelAll();
    //审核学生作业样板等同于更新
    @Update("update t_model set username = #{username},name = #{name},sclass = #{sclass},status = #{status},fileName1 = #{fileName1},fileName2 = #{fileName2},fileName3 = #{fileName3},fileName4 = #{fileName4},fileName5 = #{fileName5},fileName6 = #{fileName6},fileUrl1 = #{fileUrl1},fileUrl2 = #{fileUrl2},fileUrl3 = #{fileUrl3},fileUrl4 = #{fileUrl4},fileUrl5 = #{fileUrl5},fileUrl6 = #{fileUrl6},reson = #{reson} where mid = #{mid}")
    public int updateWordModel(Model model);
    //教研室主任查看已经审核的学生作业样板
    @Select("select * from t_model where status = '审核通过' or status = '审核否决'")
    public List<Model>findModelByStatus();
    //老师查看待审核的作业样板
    @Select("select * from t_model where username = #{username} and (status = '审核通过' or status = '审核否决')")
    public List<Model>findModelByStatusForTeacher(@Param("username") String username);
    //通过username查询教师的手机号码
    @Select("select * from user where username = #{username}")
    public User findUserTelephone(@Param("username")String username);
 }
