package com.csr.rjgcjys.service.serviceimpl;

import com.csr.rjgcjys.entities.Model;
import com.csr.rjgcjys.entities.User;
import com.csr.rjgcjys.mapper.ModelMapper;
import com.csr.rjgcjys.service.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("modelService")
public class ModelServiceimpl implements ModelService {
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public int insertModel(String username, String name, String sclass, String status, String fileName1, String fileName2, String fileName3, String fileName4, String fileName5, String fileName6, String fileUrl1, String fileUrl2, String fileUrl3, String fileUrl4, String fileUrl5, String fileUrl6) {
        return modelMapper.insertModel(username, name,sclass,status, fileName1, fileName2, fileName3, fileName4, fileName5, fileName6, fileUrl1, fileUrl2, fileUrl3, fileUrl4, fileUrl5, fileUrl6);
    }

    @Override
    public List<Model> findModelByUsername(String username) {
        return modelMapper.findModelByUsername(username);
    }

    @Override
    public Model findModelByMid(Integer mid) {
        return modelMapper.findModelByMid(mid);
    }

    @Override
    public List<Model> findModelAll() {
        return modelMapper.findModelAll();
    }

    @Override
    public int updateWordModel(Model model) {
        return modelMapper.updateWordModel(model);
    }

    @Override
    public List<Model> findModelByStatus() {
        return modelMapper.findModelByStatus();
    }

    @Override
    public List<Model> findModelByStatusForTeacher(String username) {
        return modelMapper.findModelByStatusForTeacher(username);
    }

    @Override
    public User findUserTelephone(String username) {
        return modelMapper.findUserTelephone(username);
    }
}
