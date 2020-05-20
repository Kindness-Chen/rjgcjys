package com.csr.rjgcjys.mapper;

import com.csr.rjgcjys.entities.Notice;
import com.csr.rjgcjys.entities.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.Date;
import java.util.List;


@Mapper
public interface NoticeMapper {
    //添加通知发布
    @Insert("insert into d_notice (title,name,notice,deliverDate) values ( #{title},#{name},#{notice},#{deliverDate})")
    public int insertNotice(@Param("title") String title, @Param("name") String name, @Param("notice") String notice, @Param("deliverDate") String deliverDate);
    //查询全部通知
    @Select("select * from d_notice")
    public List<Notice> findNoticeAll();
    //查询一个通知
    @Select("select * from d_notice where nid = #{nid}")
    public Notice findNoticeByNid(@Param("nid") Integer nid);
    //删除通知
    @Delete("delete from d_notice where nid = #{nid}")
    public int deleteNoticeByNid(@Param("nid") Integer nid);
    //给老师发送短信
    @Select("select name from user where identity = '老师' ")
    public List<User>directorsFindTelephone();
    //查询老师的电话号码
    @Select("select telephone from user where identity = '老师' and name = #{name}")
    public String directorsFindTelephoneByName(@Param("name")String name);
    //修改通知
    @Update("update d_notice set title = #{title},notice = #{notice},deliverDate = #{deliverDate} where nid = #{nid}")
    public int directorsUpdateNotice(Notice notice);
}
