package com.csr.rjgcjys.service.serviceimpl;

import com.csr.rjgcjys.entities.PPT;
import com.csr.rjgcjys.mapper.PPTMapper;
import com.csr.rjgcjys.service.PPTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("pptService")
public class PPTServiceimpl implements PPTService {
    @Autowired
    private PPTMapper pptMapper;
    @Override
    public int insertPPT(String username, String name, String sclass, String pptName, String pptExtName, String pptUrl) {
        return pptMapper.insertPPT(username, name, sclass, pptName, pptExtName, pptUrl);
    }

    @Override
    public List<PPT> findPPTByUsername(String username) {
        return pptMapper.findPPTByUsername(username);
    }

    @Override
    public PPT findPPTByPid(Integer pid) {
        return pptMapper.findPPTByPid(pid);
    }

    @Override
    public int deletePPTByPid(Integer pid) {
        return pptMapper.deletePPTByPid(pid);
    }

    @Override
    public List<PPT> findPPTAll() {
        return pptMapper.findPPTAll();
    }
}
