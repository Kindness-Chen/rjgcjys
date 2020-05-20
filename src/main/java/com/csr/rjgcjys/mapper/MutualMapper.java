package com.csr.rjgcjys.mapper;

import com.csr.rjgcjys.entities.Mutual;
import com.csr.rjgcjys.entities.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface MutualMapper {
    //查询全部角色为教师的用户
    @Select("select * from user where identity = '老师' and username != #{username}")
    public List<User>findUserByIdentity(@Param("username")String username);
    //根据username查询某个用户互评的总分
    @Select("select  IFNULL(sum(branch),0) from t_mutual where username = #{username} and year = Year(CurDate())")
    public Integer findSumBranch(@Param("username") String username);
    //查询评分的人数
    @Select("select  count(*) from t_mutual where username = #{username} and year = Year(CurDate()) ")
    public Integer findCountUsername(@Param("username") String username);
    //查询这个老师这个年度是否有给这个老师评分
    @Select("select * from t_mutual where username = #{username} and name1 = #{name1} and year = Year(CurDate()) ")
    public List<Mutual> findCommonMutual(@Param("username") String username,@Param("name1") String name1,@Param("year") String year);
    //老师给其他老师评分
    @Insert("insert into t_mutual (username,name,year,name1,branch,average,number) values (#{username},#{name},#{year},#{name1},#{branch},#{average},#{number})")
    public int insertMutual(Mutual mutual);
    //老师查看其他老师给自己的评分
    @Select("select * from t_mutual where username = #{username}")
    public List<Mutual>findMutualByUsername(@Param("username")String username);
    //教研室主任查看教师互评并评分
    @Select("select * from t_mutual as a where number = (select max(b.number) from t_mutual as b where a.username = b.username and a.year = b.year )")
    public List<Mutual>directorsLookMutual();
    //根据id查询特定的数据
    @Select("select * from t_mutual where mid = #{mid}")
    public Mutual findMutualByMid(@Param("mid")Integer mid);
    //教研室主任给年度总评
    @Update("update t_mutual set username = #{username},name = #{name},year = #{year},name1 = #{name1},branch = #{branch},average = #{average},number = #{number},total= #{total} where mid = #{mid}")
    public int directorsUpdateMutual(Mutual mutual);
    //如果教研室主任已经年段总评，老师不能进行评分
    @Select("select * from t_mutual where username = #{username} and total is not null and year = Year(CurDate())")
    public List<Mutual>findMutualByTotal(@Param("username")String username);
    //批量添加互评
    @Insert({"<script> insert into t_mutual(username,name,year,name1,branch,average,number) values  ",
            "  <foreach collection='mutuals' item='item' separator=',' > " ,
            "  (#{item.username},#{item.name},#{item.year},#{item.name1},#{item.branch},#{item.average},#{item.number})" ,
            "  </foreach> </script>"})
    int oneMutual(@Param("mutuals") List<Mutual> mutuals);
    //教研室主任查看平均成绩
    @Select("select mid,username,name,year,name1, branch,count(username) as number ,ROUND(sum(branch)/count(username),0) as average ,total from t_mutual where year = Year(CurDate()) GROUP BY username")
    public List<Mutual> findMutualByAll();
    //查看教研室主任是否在这个年度进行了总评
    @Select("select * from t_mutual where total is not null and year = Year(CurDate())")
    public List<Mutual>findDirectorHaveMutual();
    //查询这个年度老师是否给其他老师进行评分
    @Select("select * from t_mutual where name1 = #{name1} and year = Year(CurDate()) ")
    public List<Mutual>findTeacherHaveMutual(@Param("name1")String name1);
}
