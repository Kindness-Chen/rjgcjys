package com.csr.rjgcjys.mapper;

import com.csr.rjgcjys.entities.User;
import com.csr.rjgcjys.entities.Week;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface WeekMapper {
    //添加教学周历
    @Insert("insert into t_week (username,name,sclass,fileName,fileExtName,fileUrl,status,reson,imgName,imgExtName,imgUrl) values (#{username},#{name},#{sclass},#{fileName},#{fileExtName},#{fileUrl},#{status},#{reson},#{imgName},#{imgExtName},#{imgUrl})")
    public int insertWeek(@Param("username")String username,@Param("name")String name,@Param("sclass")String sclass,@Param("fileName")String fileNanme,
                          @Param("fileExtName")String fileExtName,@Param("fileUrl")String fileUrl,@Param("status")String status,
                          @Param("reson") String reson,@Param("imgName")String imgName,@Param("imgExtName")String imgExtName,@Param("imgUrl")String imgUrl);
    //查看待审核周历
    @Select("select * from t_week where status = '待审核'")
    public List<Week>findWeekByStatus();
    //查询某个指定的教学周历
    @Select("select * from t_week where tid = #{tid}")
    public Week findWeekByTid(@Param("tid") Integer tid);
    //审核教学周历等同于更新教学周历
    @Update("update t_week set username = #{username},name = #{name},sclass = #{sclass},fileName = #{fileName},fileExtName = #{fileExtName},fileUrl = #{fileUrl},status = #{status},reson = #{reson},imgName = #{imgName},imgExtName = #{imgExtName},imgUrl = #{imgUrl} where tid = #{tid}")
    public int directorsUpdateWeek(Week week);
    //查看已经审核的教学周历
    @Select("select * from t_week where status = '审核通过' or status = '审核否决'")
    public List<Week>findHaveWeekByStatus();
    //查看已经审核的教学周历给各自的老师自己查看，只能自己看自己的教学周历
    @Select("select * from t_week where (status = '审核通过' or status = '审核否决') and username = #{username}")
    public List<Week>findHaveWeekByStatusForMyself(@Param("username")String username);
    //查看待审核审核的教学周历给各自的老师自己查看，只能自己看自己的教学周历
    @Select("select * from t_week where status = '待审核' and username = #{username}")
    public List<Week>findWeekByStatusForMySelf(@Param("username")String username);
    //通过教师的username查询教师的手机号码发短信
    @Select("select * from user where username = #{username}")
    public User findUserTelephone(@Param("username")String username);
    //学生上传作业，教师的教学周历通过审核后才能上交
    @Select("select sclass from t_week where name = #{name} and status = '审核通过'")
    public List<Week>findSclassByNameStatus(@Param("name")String name);
    //查询需要审核的教学周历多少个
    @Select("select  count(*) from t_week where status = '待审核'")
    public Integer findCountWeek();
    //查询需要审核的教学周历多少个
    @Select("select  count(*) from t_week where status = '待审核' and username = #{username}")
    public Integer findCountWeek2(@Param("username")String username);
}
