package com.csr.rjgcjys.service;

import com.csr.rjgcjys.entities.Model;
import com.csr.rjgcjys.entities.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ModelService {
    int insertModel(String username,String name,String sclass,String status,String fileName1,String fileName2,String fileName3,String fileName4,String fileName5,String fileName6,String fileUrl1,String fileUrl2,String fileUrl3,String fileUrl4,String fileUrl5,String fileUrl6);
    List<Model>findModelByUsername(String username);
    Model findModelByMid(Integer mid);
    List<Model>findModelAll();
    int updateWordModel(Model model);
    List<Model>findModelByStatus();
    List<Model>findModelByStatusForTeacher(String username);
    User findUserTelephone(String username);
}
