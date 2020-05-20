package com.csr.rjgcjys.mapper;

import com.csr.rjgcjys.entities.Score;
import com.csr.rjgcjys.entities.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ScoreMapper {
    //查询全部学生
    @Select("select * from user where identity = '学生'")
    public List<User>findStudentsAll();
    //添加学生成绩，老师提交成绩
    @Insert("insert into t_score (username,name,sclass,usualScore,examScore,finalScore,tUsername) values (#{username},#{name},#{sclass},#{usualScore},#{examScore},#{finalScore},#{tUsername})")
    public int insertTeachersScore(@Param("username")String username,@Param("name")String name,@Param("sclass")String sclass,@Param("usualScore")Integer usualScore,@Param("examScore")Integer examScore,@Param("finalScore")Integer finalScore,@Param("tUsername")String tUsername);
    //查询各自学生的成绩，根据username查询
    @Select("select * from t_score where username = #{username}")
    public List<Score>findScoreByUsername(@Param("username")String username);
    //查询全部学生的成绩
    @Select("select * from t_score where tUsername = #{tUsername}")
    public List<Score>findStudentsScoreAll(@Param("tUsername") String tUsername);
    //查询成绩通过cid查询
    @Select("select * from t_score where cid = #{cid}")
    public Score findScoreByCid(@Param("cid")Integer cid);
    //修改学生成绩
    @Update("update t_score set username = #{username},name = #{name},sclass = #{sclass},usualScore = #{usualScore},examScore = #{examScore},finalScore = #{finalScore},tUsername = #{tUsername} where cid = #{cid}")
    public int teachersUpdateScore(Score score);
    //教师删除学生成绩
    @Delete("delete from t_score where cid = #{cid}")
    public int deleteScoreByCid(@Param("cid") Integer cid);
}
