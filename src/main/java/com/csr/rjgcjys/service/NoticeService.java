package com.csr.rjgcjys.service;

import com.csr.rjgcjys.entities.Notice;
import com.csr.rjgcjys.entities.User;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface NoticeService {
    int insertNotice(String title, String name, String notice, String deliverDate);
    List<Notice> findNoticeAll();
    Notice findNoticeByNid(Integer nid);
    int deleteNoticeByNid(Integer nid);
    List<User>directorsFindTelephone();
    String directorsFindTelephoneByName(String name);
    int directorsUpdateNotice(Notice notice);
}
