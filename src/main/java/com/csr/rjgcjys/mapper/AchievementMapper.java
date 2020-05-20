package com.csr.rjgcjys.mapper;

import com.csr.rjgcjys.entities.Achievement;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface AchievementMapper {
    //老师上传学生成绩
    @Insert("insert into t_achievement (username,name,sclass,subject,fileName,fileExtName,fileUrl,status) values (#{username},#{name},#{sclass},#{subject},#{fileName},#{fileExtName},#{fileUrl},#{status})")
    public int insertAchievement(Achievement achievement);
    //老师查看上传的成绩文档
    @Select("select * from t_achievement where username = #{username}")
    public List<Achievement>teachersLookAchievement(@Param("username")String username);
    //查看特定的成绩文档
    @Select("select * from t_achievement where zid = #{zid}")
    public Achievement findAchievementByZid(@Param("zid")Integer zid);
    //学生查看成绩文档
    @Select("select * from t_achievement where status = '审核通过'")
    public List<Achievement>findAchievementAll();
    //教师删除成绩文档
    @Delete("delete from t_achievement where zid = #{zid}")
    public int deleteAchievementByZid(@Param("zid") Integer zid);
    //教研室主任查看全部成绩
    @Select("select * from t_achievement")
    public List<Achievement>directorsLookAchievementAll();
    //教研室主任审核成绩
    @Update("update t_achievement set username = #{username},name = #{name},sclass = #{sclass},subject = #{subject},fileName = #{fileName},fileExtName = #{fileExtName},fileUrl = #{fileUrl},status = #{status},reson = #{reson} where zid = #{zid}")
    public int directorsUpdateAchievement(Achievement achievement);
}
