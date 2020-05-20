package com.csr.rjgcjys.service.serviceimpl;

import com.csr.rjgcjys.entities.CourseDesign;
import com.csr.rjgcjys.entities.User;
import com.csr.rjgcjys.mapper.CourseDesignMapper;
import com.csr.rjgcjys.service.CourseDesignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("courseDesignService")
public class CourseDesignserviceimpl implements CourseDesignService {
    @Autowired
    private CourseDesignMapper courseDesignMapper;


    @Override
    public int insertCourseDesign(String username, String name, String sclass, String subject, String teacher, String fileName, String extName, String url, String enclosureName, String enclosureExtName, String enclosureUrl) {
        return courseDesignMapper.insertCourseDesign(username, name, sclass, subject, teacher, fileName, extName, url, enclosureName, enclosureExtName, enclosureUrl);
    }

    @Override
    public CourseDesign findCourseDesignByCid(Integer cid) {
        return courseDesignMapper.findCourseDesignByCid(cid);
    }

    @Override
    public List<CourseDesign> findCourseDesignAll() {
        return courseDesignMapper.findCourseDesignAll();
    }

    @Override
    public List<CourseDesign> findCourseDesignByUsername(String username) {
        return courseDesignMapper.findCourseDesignByUsername(username);
    }

    @Override
    public List<CourseDesign> findCourseDesignByTeacher(String teacher) {
        return courseDesignMapper.findCourseDesignByTeacher(teacher);
    }

    @Override
    public int deleteCourseDesignByCid(Integer cid) {
        return courseDesignMapper.deleteCourseDesignByCid(cid);
    }

    @Override
    public User findUserTelephone(String username) {
        return courseDesignMapper.findUserTelephone(username);
    }

    @Override
    public int updateCourseDesignByCid(CourseDesign courseDesign) {
        return courseDesignMapper.updateCourseDesignByCid(courseDesign);
    }

    @Override
    public List<CourseDesign> findCourseDesignByScore(String teacher) {
        return courseDesignMapper.findCourseDesignByScore(teacher);
    }

    @Override
    public Integer countCourseDesign(String username) {
        return courseDesignMapper.countCourseDesign(username);
    }
}
