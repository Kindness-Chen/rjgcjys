package com.csr.rjgcjys.service;

import com.csr.rjgcjys.entities.Meeting;
import com.csr.rjgcjys.entities.User;

import java.util.List;

public interface MeetingService {
    List<Meeting> findMeetingByUsername(String username);
    List<Meeting>findMeetingAll();
    int teachersInsertMeeting(Meeting meeting);
    Meeting findMeetingByJid(Integer jid);
    int teachersUpdateMeeting(Meeting meeting);
    int teachersDeleteMeeting(Integer jid);
    List<User>findUserByIdentity();
}
