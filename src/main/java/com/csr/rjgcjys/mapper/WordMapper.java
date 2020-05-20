package com.csr.rjgcjys.mapper;

import com.csr.rjgcjys.entities.User;
import com.csr.rjgcjys.entities.Word;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface WordMapper {
    //添加作业
    @Insert("insert into s_word (username,name,sclass,subject,teacher,fileName,extName,url) values (#{username},#{name},#{sclass},#{subject},#{teacher},#{fileName},#{extName},#{url})")
    public int insertWord(@Param("username") String username,@Param("name") String name,@Param("sclass") String sclass,@Param("subject") String subject,@Param("teacher") String teacher,@Param("fileName") String fileName,@Param("extName") String extName,@Param("url") String url);
    //查看全部作业
    @Select("select * from s_word")
    public List<Word>findWordAll();
    //查看单个作业
    @Select("select * from s_word where sid=#{sid}")
    public Word findWordBySid(@Param("sid") Integer sid );
    //查询user表中的全部老师的名字
    @Select("select * from user where identity = '老师'")
    public List<User>findUserByName();
    //查看作业只能自己看自己的作业
    @Select("select * from s_word where username = #{username}")
    public List<Word>findWordByUserName(@Param("username")String username);
    //老师查看作业通过授课老师名字查看，审核自己学生的作业
    @Select("select * from s_word where teacher = #{teacher} and score is null")
    public List<Word>findWordByName(@Param("teacher")String teacher);
    //删除学生上传的作业
    @Delete("delete from s_word where sid = #{sid}")
    public int deleteWordBySid(@Param("sid")Integer sid);
    //删除学生上传的作业并发短信提示
    @Select("select * from user where username = #{username}")
    public User findUserTelephone(@Param("username")String username);
    //教师给学会作业评分
    @Update("update s_word set username = #{username},name = #{name},sclass = #{sclass},subject = #{subject},teacher = #{teacher},fileName = #{fileName},extName = #{extName},url = #{url},score = #{score},reson = #{reson} where sid =#{sid}")
    public int updateWordBySid(Word word);
    //老师查看作业通过授课老师名字查看，查看已经评分自己学生的作业
    @Select("select * from s_word where teacher = #{teacher} and score is not null")
    public List<Word>findWordByScore(@Param("teacher")String teacher);
    //学生查看已经批改的作业数量
    @Select("select  count(*) from s_word where score is not null and username = #{username}")
    public Integer countWord(@Param("username")String username);
}
