package com.csr.rjgcjys.service;

import com.csr.rjgcjys.entities.Leave;
import com.csr.rjgcjys.entities.User;

import java.util.List;

public interface LeaveService {
    int insertLeave(Leave leave);
    List<Leave> findLeaveByStatus(String username);
    Leave findLeaveByLid(Integer lid);
    int updateLeave(Leave leave);
    int deleteLeaveByLid(Integer lid);
    List<Leave>directorsFindLeave();
    int directorsUpdateLeave(Leave leave);
    List<Leave>directorsFindHaveLeave();
    List<Leave>teachersFindHaveLeave(String username);
    User findUserTelephone(String username);
    Integer findCountLeave();
    Integer findCountLeave2(String username);
}
