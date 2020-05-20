package com.csr.rjgcjys.mapper;

import com.csr.rjgcjys.entities.Leave;
import com.csr.rjgcjys.entities.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface LeaveMapper {
    //教师申请调课
    @Insert("insert into t_leave (username,sclass,subject,name,name1,classTime,afterTime,classPlace,afterPlace,classReson,resonTime,status,reson,imgName,imgExt,imgUrl) values (#{username},#{sclass},#{subject},#{name},#{name1},#{classTime},#{afterTime},#{classPlace},#{afterPlace},#{classReson},#{resonTime},#{status},#{reson},#{imgName},#{imgExt},#{imgUrl})")
    public int insertLeave(Leave leave);
    //教师查看待审核的调课申请
    @Select("select * from t_leave where status = '待审核' and username = #{username}")
    public List<Leave>findLeaveByStatus(@Param("username")String usernmae);
    //查看指定的调课申请
    @Select("select * from t_leave where lid = #{lid}")
    public Leave findLeaveByLid(@Param("lid")Integer lid);
    //教师修改调课申请
    @Update("update t_leave set username = #{username},sclass = #{sclass},subject = #{subject},name = #{name},name1 = #{name1},classTime = #{classTime},afterTime = #{afterTime},classPlace = #{classPlace},afterPlace = #{afterPlace},classReson = #{classReson},resonTime = #{resonTime},status = #{status},reson = #{reson},imgName = #{imgName},imgExt = #{imgExt},imgUrl = #{imgUrl} where lid = #{lid}")
    public int updateLeave(Leave leave);
    //教师删除调课申请
    @Delete("delete from t_leave where lid = #{lid}")
    public int deleteLeaveByLid(@Param("lid")Integer lid);
    //教研室主任查看待审核调课申请
    @Select("select * from t_leave where status = '待审核'")
    public List<Leave>directorsFindLeave();
    //教研室主任审核调课申请
    @Update("update t_leave set username = #{username},sclass = #{sclass},subject = #{subject},name = #{name},name1 = #{name1},classTime = #{classTime},afterTime = #{afterTime},classPlace = #{classPlace},afterPlace = #{afterPlace},classReson = #{classReson},resonTime = #{resonTime},status = #{status},reson = #{reson},imgName = #{imgName},imgExt = #{imgExt},imgUrl = #{imgUrl} where lid = #{lid}")
    public int directorsUpdateLeave(Leave leave);
    //教研室主任查看已经审核的调课申请
    @Select("select * from t_leave where status = '审核通过' or status = '审核否决'")
    public List<Leave>directorsFindHaveLeave();
    //教师查看已经审核调课申请
    @Select("select * from t_leave where (status = '审核通过' or status = '审核否决') and username = #{username}")
    public List<Leave>teachersFindHaveLeave(@Param("username")String username);
    //通过username查询教师的手机号码
    @Select("select * from user where username = #{username}")
    public User findUserTelephone(@Param("username")String username);
    //查询需要审核的调课申请有多少个
    @Select("select  count(*) from t_leave where status = '待审核'")
    public Integer findCountLeave();
    //老师查询需要审核的调课申请有多少个
    @Select("select  count(*) from t_leave where status = '待审核' and username = #{username}")
    public Integer findCountLeave2(@Param("username")String username);
}
