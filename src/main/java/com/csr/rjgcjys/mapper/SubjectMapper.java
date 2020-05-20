package com.csr.rjgcjys.mapper;

import com.csr.rjgcjys.entities.Subject;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface SubjectMapper {
    //教研室主任添加课程
    @Insert("insert into d_subject(username,code,sclass,name,subjectTime,subjectPlace,nature,credit,assessment,totaltime,lecturetime,experimenttime,computertime,number,college,consist) values (#{username},#{code},#{sclass},#{name},#{subjectTime},#{subjectPlace},#{nature},#{credit},#{assessment},#{totaltime},#{lecturetime},#{experimenttime},#{computertime},#{number},#{college},#{consist})")
    public int insertSubject(Subject subject);
    //教研室主任查看课程
    @Select("select * from d_subject")
    public List<Subject>findSubjectAll();
    //查询特定的数据通过sid
    @Select("select * from d_subject where sid = #{sid}")
    public Subject findSubjectBySid(@Param("sid")Integer sid);
    //更新课程
    @Update("update d_subject set username = #{username},code = #{code},sclass = #{sclass},name = #{name},subjectTime = #{subjectTime},subjectPlace = #{subjectPlace},nature = #{nature},credit = #{credit},assessment = #{assessment},totaltime = #{totaltime},lecturetime = #{lecturetime},experimenttime = #{experimenttime},computertime = #{computertime},number = #{number},college = #{college},consist = #{consist} where sid = #{sid}")
    public int directorsUpdateSubject(Subject subject);
    //删除课程
    @Delete("delete from d_subject where sid = #{sid}")
    public int deleteSubjectBySid(@Param("sid")Integer sid);
    //查询课程
    @Select("select distinct sclass from d_subject where name = #{name}")
    public List<Subject>findSclassByName(@Param("name")String name);
    //老师通过name查询课程
    @Select("select * from d_subject where name = #{name}")
    public List<Subject>teacherFindSubjectByName(@Param("name")String name);
}
