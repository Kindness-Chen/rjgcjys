package com.csr.rjgcjys.service;

import com.csr.rjgcjys.entities.User;
import com.csr.rjgcjys.entities.Week;

import java.util.List;

public interface WeekService {
    int insertWeek(String username,String name,String sclass,String fileName,String fileExtName,String fileUrl,String status,
                   String reson,String imgName,String imgExtName,String imgUrl);
    List<Week>findWeekByStatus();
    Week findWeekByTid(Integer tid);
    int directorsUpdateWeek(Week week);
    List<Week>findHaveWeekByStatus();
    List<Week>findHaveWeekByStatusForMyself(String Username);
    List<Week>findWeekByStatusByMyself(String username);
    User findUserTelephone(String username);
    List<Week>findSclassByNameStatus(String name);
    Integer findCountWeek();
    Integer findCountWeek2(String username);
}
