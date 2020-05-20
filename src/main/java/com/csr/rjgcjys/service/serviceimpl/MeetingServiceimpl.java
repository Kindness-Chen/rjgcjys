package com.csr.rjgcjys.service.serviceimpl;

import com.csr.rjgcjys.entities.Meeting;
import com.csr.rjgcjys.entities.User;
import com.csr.rjgcjys.mapper.MeetingMapper;
import com.csr.rjgcjys.service.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("meetingService")
public class MeetingServiceimpl implements MeetingService {
    @Autowired
    private MeetingMapper meetingMapper;
    @Override
    public List<Meeting> findMeetingByUsername(String username) {
        return meetingMapper.findMeetingByUsername(username);
    }

    @Override
    public List<Meeting> findMeetingAll() {
        return meetingMapper.findMeetingAll();
    }

    @Override
    public int teachersInsertMeeting(Meeting meeting) {
        return meetingMapper.teachersInsertMeeting(meeting);
    }

    @Override
    public Meeting findMeetingByJid(Integer jid) {
        return meetingMapper.findMeetingByJid(jid);
    }

    @Override
    public int teachersUpdateMeeting(Meeting meeting) {
        return meetingMapper.teachersUpdateMeeting(meeting);
    }

    @Override
    public int teachersDeleteMeeting(Integer jid) {
        return meetingMapper.teachersDeleteMeeting(jid);
    }

    @Override
    public List<User> findUserByIdentity() {
        return meetingMapper.findUserByIdentity();
    }
}
