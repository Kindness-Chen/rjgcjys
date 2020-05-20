package com.csr.rjgcjys.mapper;

import com.csr.rjgcjys.entities.Train;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface TrainMapper {
    //添加教学任务
    @Insert("insert into t_train (username,code,sclass,name,weektime,strattime,credit,totaltime,lecturetime,experimenttime,computertime,assessment,nature,category,number,college,classname,reson) values (#{username},#{code},#{sclass},#{name},#{weektime},#{strattime},#{credit},#{totaltime},#{lecturetime},#{experimenttime},#{computertime},#{assessment},#{nature},#{category},#{number},#{college},#{classname},#{reson})")
    public int insertTrain(Train train);
    //教学查看教学任务通过username
    @Select("select * from t_train where name = #{name}")
    public List<Train>teachersFindTrainByName(@Param("name")String name);
    //查询特定的数据通过rid查询
    @Select("select * from t_train where rid = #{rid}")
    public Train findTrainByRid(@Param("rid")Integer rid);
    //教研室主任修改教学任务
    @Update("update t_train set username = #{username},code = #{code},sclass = #{sclass},name = #{name},weektime = #{weektime},strattime = #{strattime},credit = #{credit},totaltime = #{totaltime},lecturetime = #{lecturetime},experimenttime = #{experimenttime},computertime = #{computertime},assessment = #{assessment},nature = #{nature},category = #{category},number = #{number},college = #{college},classname = #{classname},reson = #{reson} where rid = #{rid}")
    public int teachersUpdateStudy(Train train);
    //删除教学任务
    @Delete("delete from t_train where rid = #{rid}")
    public int deleteTrainByRid(@Param("rid")Integer rid);
    //教研室主任查看全部教学任务
    @Select("select * from t_train")
    public List<Train>directorsFindTrainAll();
    //查询全部课程的名字
    @Select("select distinct sclass from t_train")
    public List<Train>findSclassAll();
    //查看全部班级的名字
    @Select("select distinct classname from t_train order by classname asc")
    public  List<Train>findClassName();
    //查询课程代码
    @Select("select distinct code from t_train")
    public  List<Train>findCodeAll();
    //通过课程代码查询特定数据
    @Select("select * from t_train where code = #{code}")
    public List<Train>findTrainByCode(@Param("code")String code);
}
