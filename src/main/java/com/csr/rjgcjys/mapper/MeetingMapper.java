package com.csr.rjgcjys.mapper;

import com.csr.rjgcjys.entities.Meeting;
import com.csr.rjgcjys.entities.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface MeetingMapper {
    //查询全部记录内容，根据username查询
    @Select("select * from t_meeting where username = #{username}")
    public List<Meeting>findMeetingByUsername(@Param("username")String username);
    //查询全部记录内容给教研室在主任查看
    @Select("select * from t_meeting ")
    public List<Meeting>findMeetingAll();
    //教师添加会议记录
    @Insert("insert into t_meeting (username,meetingPlace,meetingTime,meetingPeople,meetingHold,meetingReport,meetingContent) value (#{username},#{meetingPlace},#{meetingTime},#{meetingPeople},#{meetingHold},#{meetingReport},#{meetingContent})")
    public int teachersInsertMeeting(Meeting meeting);
    //教师查看会议记录
    @Select("select * from t_meeting where jid = #{jid}")
    public Meeting findMeetingByJid(@Param("jid")Integer jid);
    //教师修改会议记录
    @Update("update t_meeting set username = #{username},meetingPlace = #{meetingPlace},meetingTime = #{meetingTime},meetingPeople= #{meetingPeople},meetingHold = #{meetingHold},meetingReport = #{meetingReport},meetingContent = #{meetingContent} where jid = #{jid}")
    public int teachersUpdateMeeting(Meeting meeting);
    //教师删除会议记录
    @Delete("delete from t_meeting where jid = #{jid}")
    public int teachersDeleteMeeting(@Param("jid")Integer jid);
    //查询老师列表
    @Select("select * from user where identity = '老师'")
    public List<User>findUserByIdentity();
}
