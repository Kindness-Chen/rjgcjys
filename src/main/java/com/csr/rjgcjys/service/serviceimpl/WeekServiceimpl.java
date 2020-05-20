package com.csr.rjgcjys.service.serviceimpl;

import com.csr.rjgcjys.entities.User;
import com.csr.rjgcjys.entities.Week;
import com.csr.rjgcjys.mapper.WeekMapper;
import com.csr.rjgcjys.service.WeekService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("weekService")
public class WeekServiceimpl implements WeekService {
    @Autowired
    private WeekMapper weekMapper;
    @Override
    public int insertWeek(String username, String name,String sclass, String fileName, String fileExtName, String fileUrl, String status, String reson, String imgName, String imgExtName, String imgUrl) {
        return weekMapper.insertWeek(username, name, sclass, fileName, fileExtName, fileUrl, status, reson, imgName, imgExtName, imgUrl);
    }

    @Override
    public List<Week> findWeekByStatus() {
        return weekMapper.findWeekByStatus();
    }

    @Override
    public Week findWeekByTid(Integer tid) {
        return weekMapper.findWeekByTid(tid);
    }

    @Override
    public int directorsUpdateWeek(Week week) {
        return weekMapper.directorsUpdateWeek(week);
    }

    @Override
    public List<Week> findHaveWeekByStatus() {
        return weekMapper.findHaveWeekByStatus();
    }

    @Override
    public List<Week> findHaveWeekByStatusForMyself(String username) {
        return weekMapper.findHaveWeekByStatusForMyself(username);
    }

    @Override
    public List<Week> findWeekByStatusByMyself(String username) {
        return weekMapper.findWeekByStatusForMySelf(username);
    }

    @Override
    public User findUserTelephone(String username) {
        return weekMapper.findUserTelephone(username);
    }

    @Override
    public List<Week> findSclassByNameStatus(String name) {
        return weekMapper.findSclassByNameStatus(name);
    }

    @Override
    public Integer findCountWeek() {
        return weekMapper.findCountWeek();
    }

    @Override
    public Integer findCountWeek2(String username) {
        return weekMapper.findCountWeek2(username);
    }
}
