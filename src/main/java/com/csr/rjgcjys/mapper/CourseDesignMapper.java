package com.csr.rjgcjys.mapper;

import com.csr.rjgcjys.entities.CourseDesign;
import com.csr.rjgcjys.entities.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface CourseDesignMapper {
    //添加课程设计
    @Insert("insert into s_coursedesign (username,name,sclass,subject,teacher,fileName,extName,url,enclosureName,enclosureExtName,enclosureUrl) values (#{username},#{name},#{sclass},#{subject},#{teacher},#{fileName},#{extName},#{url},#{enclosureName},#{enclosureExtName},#{enclosureUrl})")
    public int insertCourseDesign(@Param("username") String username, @Param("name") String name, @Param("sclass") String sclass, @Param("subject") String subject, @Param("teacher") String teacher, @Param("fileName") String fileName, @Param("extName") String extName, @Param("url") String url,@Param("enclosureName")String enclosureName,@Param("enclosureExtName")String enclosureExtName,@Param("enclosureUrl")String enclosureUrl);
    //查看全部课程设计
    @Select("select * from s_coursedesign")
    public List<CourseDesign> findCourseDesignAll();
    //查看单个课程设计
    @Select("select * from s_coursedesign where cid=#{cid}")
    public CourseDesign findCourseDesignByCid(@Param("cid") Integer  cid);
    //查看课程设计只能自己看自己的
    @Select("select * from s_coursedesign where username = #{username}")
    public List<CourseDesign>findCourseDesignByUsername(@Param("username")String username);
    //教师查看课程设计通过teacher
    @Select("select * from s_coursedesign where teacher = #{teacher} and score is null")
    public List<CourseDesign>findCourseDesignByTeacher(@Param("teacher")String teacher);
    //教师删除课程设计
    @Delete("delete from s_coursedesign where cid = #{cid}")
    public int deleteCourseDesignByCid(@Param("cid")Integer cid);
    //通过username查找telephone
    @Select("select * from user where username = #{username}")
    public User findUserTelephone(@Param("username") String username);
    //教师给学生评课程设计成绩
    @Update("update s_coursedesign set username = #{username},name = #{name},sclass = #{sclass},subject = #{subject},teacher = #{teacher},fileName = #{fileName},extName = #{extName},url = #{url},enclosureName = #{enclosureName},enclosureExtName = #{enclosureExtName},enclosureUrl = #{enclosureUrl},score = #{score},reson = #{reson} where cid = #{cid}")
    public int updateCourseDesignByCid(CourseDesign courseDesign);
    //教师查看已经评成绩的课程设计
    @Select("select * from s_coursedesign where teacher = #{teacher} and score is not null")
    public List<CourseDesign>findCourseDesignByScore(@Param("teacher")String teacher);
    //学生查看已经批改的课程设计数量
    @Select("select  count(*) from s_coursedesign where score is not null and username = #{username}")
    public Integer countCourseDesign(@Param("username")String username);
}
