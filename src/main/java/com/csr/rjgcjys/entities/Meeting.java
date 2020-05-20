package com.csr.rjgcjys.entities;

public class Meeting {
    private Integer jid;
    private String username;
    private String meetingPlace;
    private String meetingTime;
    private String meetingPeople;
    private String meetingHold;
    private String meetingReport;
    private String meetingContent;

    public Integer getJid() {
        return jid;
    }

    public void setJid(Integer jid) {
        this.jid = jid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMeetingPlace() {
        return meetingPlace;
    }

    public void setMeetingPlace(String meetingPlace) {
        this.meetingPlace = meetingPlace;
    }

    public String getMeetingTime() {
        return meetingTime;
    }

    public void setMeetingTime(String meetingTime) {
        this.meetingTime = meetingTime;
    }

    public String getMeetingPeople() {
        return meetingPeople;
    }

    public void setMeetingPeople(String meetingPeople) {
        this.meetingPeople = meetingPeople;
    }

    public String getMeetingHold() {
        return meetingHold;
    }

    public void setMeetingHold(String meetingHold) {
        this.meetingHold = meetingHold;
    }

    public String getMeetingReport() {
        return meetingReport;
    }

    public void setMeetingReport(String meetingReport) {
        this.meetingReport = meetingReport;
    }

    public String getMeetingContent() {
        return meetingContent;
    }

    public void setMeetingContent(String meetingContent) {
        this.meetingContent = meetingContent;
    }

    @Override
    public String toString() {
        return "Meeting{" +
                "jid=" + jid +
                ", username='" + username + '\'' +
                ", meetingPlace='" + meetingPlace + '\'' +
                ", meetingTime='" + meetingTime + '\'' +
                ", meetingPeople='" + meetingPeople + '\'' +
                ", meetingHold='" + meetingHold + '\'' +
                ", meetingReport='" + meetingReport + '\'' +
                ", meetingContent='" + meetingContent + '\'' +
                '}';
    }
}
