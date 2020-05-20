package com.csr.rjgcjys.service;

import com.csr.rjgcjys.entities.CourseDesign;
import com.csr.rjgcjys.entities.User;

import java.util.List;

public interface CourseDesignService {
    int insertCourseDesign(String username,String name,String sclass,String subject,String teacher,String fileName,String extName,String url,String enclosureName,String enclosureExtName,String enclosureUrl);
    CourseDesign findCourseDesignByCid(Integer cid);
    List<CourseDesign> findCourseDesignAll();
    List<CourseDesign> findCourseDesignByUsername(String username);
    List<CourseDesign>findCourseDesignByTeacher(String teacher);
    int deleteCourseDesignByCid(Integer cid);
    User findUserTelephone(String username);
    int updateCourseDesignByCid(CourseDesign courseDesign);
    List<CourseDesign>findCourseDesignByScore(String teacher);
    Integer countCourseDesign(String username);
}
