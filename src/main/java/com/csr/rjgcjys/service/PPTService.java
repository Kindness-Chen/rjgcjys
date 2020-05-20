package com.csr.rjgcjys.service;

import com.csr.rjgcjys.entities.PPT;

import java.util.List;

public interface PPTService {
    int insertPPT(String username,String name,String sclass,String pptName,String pptExtName,String pptUrl);
    List<PPT>findPPTByUsername(String username);
    PPT findPPTByPid(Integer pid);
    int deletePPTByPid(Integer pid);
    List<PPT>findPPTAll();
}
