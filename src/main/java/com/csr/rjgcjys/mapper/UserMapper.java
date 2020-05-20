package com.csr.rjgcjys.mapper;

import com.csr.rjgcjys.entities.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import javax.validation.constraints.Max;
import java.util.List;

@Mapper
public interface UserMapper {

    //添加用户
    @Insert("insert into user (username,password,name,telephone,identity) values (#{username},#{password},#{name},#{telephone},#{identity})")
    public int insertUser(User user);
    //查询全部用户
    @Select("select * from user")
    public List<User> findUserAll();
    //查询特定的用户，进行修改
    @Select("select * from user where uid = #{uid}")
    public List<User> findUserById(Integer uid);
    //修改用户
    @Update("update user set username = #{username},password = #{password},name = #{name},telephone = #{telephone},identity = #{identity} where uid = #{uid}")
    public int updateUser(User user);
    //删除用户
    @Delete("delete from user where uid = #{uid}")
    public int deleteUser(Integer uid);
    //根据用户角色进行查询，进行角色验证登录
    @Select("select * from user where username = #{username} and password = #{password} and identity = #{identity}")
    public List<User> findUserByIdentity(@Param("username") String username, @Param("password") String password,@Param("identity") String identity);
    //查询各类用户
    @Select("select count(*) from user where identity = '学生'")
    public int countStudents();
    @Select("select count(*) from user where identity = '老师'")
    public int countTeachers();
    @Select("select count(*) from user where identity = '教研室主任'")
    public int countDirectors();
    @Select("select count(*) from user where identity = '管理员'")
    public int countAdmins();

    //修改密码
    @Select("select * from user where uid = #{uid}")
    public User findUserByUid(Integer uid);

}
