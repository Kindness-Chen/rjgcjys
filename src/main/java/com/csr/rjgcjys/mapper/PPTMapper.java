package com.csr.rjgcjys.mapper;

import com.csr.rjgcjys.entities.PPT;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PPTMapper {
    //教师添加ppt
    @Insert("insert into t_ppt (username,name,sclass,pptName,pptExtName,pptUrl) values (#{username},#{name},#{sclass},#{pptName},#{pptExtName},#{pptUrl})")
    public int insertPPT(@Param("username")String username,@Param("name")String name,@Param("sclass")String sclass,@Param("pptName")String pptName,@Param("pptExtName")String pptExtName,@Param("pptUrl")String pptUrl);
    //查看ppt,每个老师自己看自己的
    @Select("select * from t_ppt where username = #{username}")
    public List<PPT>findPPTByUsername(@Param("username")String username);
    //查看特定的ppt
    @Select("select * from t_ppt where pid = #{pid}")
    public PPT findPPTByPid(@Param("pid")Integer pid);
    //删除ppt
    @Delete("delete from t_ppt where pid = #{pid}")
    public int deletePPTByPid(@Param("pid")Integer pid);
    //教研室主任查看教学资料
    @Select("select * from t_ppt")
    public List<PPT>findPPTAll();
}
