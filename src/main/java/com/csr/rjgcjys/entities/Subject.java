package com.csr.rjgcjys.entities;

public class Subject {
    private Integer sid;
    private String username;
    private String code;
    private String sclass;
    private String name;
    private String subjectTime;
    private String subjectPlace;
    private String nature;
    private String credit;
    private String assessment;
    private String totaltime;
    private String lecturetime;
    private String experimenttime;
    private String computertime;
    private String number;
    private String college;
    private String consist;

    public Integer getSid() {
        return sid;
    }

    public void setSid(Integer sid) {
        this.sid = sid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSclass() {
        return sclass;
    }

    public void setSclass(String sclass) {
        this.sclass = sclass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubjectTime() {
        return subjectTime;
    }

    public void setSubjectTime(String subjectTime) {
        this.subjectTime = subjectTime;
    }

    public String getSubjectPlace() {
        return subjectPlace;
    }

    public void setSubjectPlace(String subjectPlace) {
        this.subjectPlace = subjectPlace;
    }

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getAssessment() {
        return assessment;
    }

    public void setAssessment(String assessment) {
        this.assessment = assessment;
    }

    public String getTotaltime() {
        return totaltime;
    }

    public void setTotaltime(String totaltime) {
        this.totaltime = totaltime;
    }

    public String getLecturetime() {
        return lecturetime;
    }

    public void setLecturetime(String lecturetime) {
        this.lecturetime = lecturetime;
    }

    public String getExperimenttime() {
        return experimenttime;
    }

    public void setExperimenttime(String experimenttime) {
        this.experimenttime = experimenttime;
    }

    public String getComputertime() {
        return computertime;
    }

    public void setComputertime(String computertime) {
        this.computertime = computertime;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getConsist() {
        return consist;
    }

    public void setConsist(String consist) {
        this.consist = consist;
    }

    @Override
    public String toString() {
        return "Subject{" +
                "sid=" + sid +
                ", username='" + username + '\'' +
                ", code='" + code + '\'' +
                ", sclass='" + sclass + '\'' +
                ", name='" + name + '\'' +
                ", subjectTime='" + subjectTime + '\'' +
                ", subjectPlace='" + subjectPlace + '\'' +
                ", nature='" + nature + '\'' +
                ", credit=" + credit +
                ", assessment='" + assessment + '\'' +
                ", totaltime=" + totaltime +
                ", lecturetime=" + lecturetime +
                ", experimenttime=" + experimenttime +
                ", computertime=" + computertime +
                ", number=" + number +
                ", college='" + college + '\'' +
                ", consist='" + consist + '\'' +
                '}';
    }
}
