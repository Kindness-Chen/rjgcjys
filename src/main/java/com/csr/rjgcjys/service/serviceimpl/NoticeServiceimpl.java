package com.csr.rjgcjys.service.serviceimpl;

import com.csr.rjgcjys.entities.Notice;
import com.csr.rjgcjys.entities.User;
import com.csr.rjgcjys.mapper.NoticeMapper;
import com.csr.rjgcjys.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("noticeService")
public class NoticeServiceimpl implements NoticeService {
    @Autowired
    private NoticeMapper noticeMapper;
    @Override
    public int insertNotice(String title, String name, String notice, String deliverDate) {
        return noticeMapper.insertNotice(title,name,notice,deliverDate);
    }

    @Override
    public List<Notice> findNoticeAll() {
        return noticeMapper.findNoticeAll();
    }

    @Override
    public Notice findNoticeByNid(Integer nid) {
        return noticeMapper.findNoticeByNid(nid);
    }

    @Override
    public int deleteNoticeByNid(Integer nid) {
        return noticeMapper.deleteNoticeByNid(nid);
    }

    @Override
    public List<User> directorsFindTelephone() {
        return noticeMapper.directorsFindTelephone();
    }

    @Override
    public String directorsFindTelephoneByName(String name) {
        return noticeMapper.directorsFindTelephoneByName(name);
    }

    @Override
    public int directorsUpdateNotice(Notice notice) {
        return noticeMapper.directorsUpdateNotice(notice);
    }
}
