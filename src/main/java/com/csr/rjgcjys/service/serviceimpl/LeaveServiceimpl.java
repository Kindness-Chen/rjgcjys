package com.csr.rjgcjys.service.serviceimpl;

import com.csr.rjgcjys.entities.Leave;
import com.csr.rjgcjys.entities.User;
import com.csr.rjgcjys.mapper.LeaveMapper;
import com.csr.rjgcjys.service.LeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("leaveService")
public class LeaveServiceimpl implements LeaveService {
    @Autowired
    private LeaveMapper leaveMapper;
    @Override
    public int insertLeave(Leave leave) {
        return leaveMapper.insertLeave(leave);
    }

    @Override
    public List<Leave> findLeaveByStatus(String username) {
        return leaveMapper.findLeaveByStatus(username);
    }

    @Override
    public Leave findLeaveByLid(Integer lid) {
        return leaveMapper.findLeaveByLid(lid);
    }

    @Override
    public int updateLeave(Leave leave) {
        return leaveMapper.updateLeave(leave);
    }

    @Override
    public int deleteLeaveByLid(Integer lid) {
        return leaveMapper.deleteLeaveByLid(lid);
    }

    @Override
    public List<Leave> directorsFindLeave() {
        return leaveMapper.directorsFindLeave();
    }

    @Override
    public int directorsUpdateLeave(Leave leave) {
        return leaveMapper.directorsUpdateLeave(leave);
    }

    @Override
    public List<Leave> directorsFindHaveLeave() {
        return leaveMapper.directorsFindHaveLeave();
    }

    @Override
    public List<Leave> teachersFindHaveLeave(String username) {
        return leaveMapper.teachersFindHaveLeave(username);
    }

    @Override
    public User findUserTelephone(String username) {
        return leaveMapper.findUserTelephone(username);
    }

    @Override
    public Integer findCountLeave() {
        return leaveMapper.findCountLeave();
    }

    @Override
    public Integer findCountLeave2(String username) {
        return leaveMapper.findCountLeave2(username);
    }
}
