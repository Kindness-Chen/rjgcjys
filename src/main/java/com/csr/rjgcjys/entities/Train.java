package com.csr.rjgcjys.entities;

public class Train {
    private Integer rid;
    private String username;
    private String code;
    private String sclass;
    private String name;
    private String weektime;
    private String strattime;
    private String credit;
    private String totaltime;
    private String lecturetime;
    private String experimenttime;
    private String computertime;
    private String assessment;
    private String nature;
    private String category;
    private String number;
    private String college;
    private String classname;
    private String reson;

    public Integer getRid() {
        return rid;
    }

    public void setRid(Integer rid) {
        this.rid = rid;
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

    public String getWeektime() {
        return weektime;
    }

    public void setWeektime(String weektime) {
        this.weektime = weektime;
    }

    public String getStrattime() {
        return strattime;
    }

    public void setStrattime(String strattime) {
        this.strattime = strattime;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
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

    public String getAssessment() {
        return assessment;
    }

    public void setAssessment(String assessment) {
        this.assessment = assessment;
    }

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getReson() {
        return reson;
    }

    public void setReson(String reson) {
        this.reson = reson;
    }

    @Override
    public String toString() {
        return "Train{" +
                "rid=" + rid +
                ", username='" + username + '\'' +
                ", code='" + code + '\'' +
                ", sclass='" + sclass + '\'' +
                ", name='" + name + '\'' +
                ", weektime='" + weektime + '\'' +
                ", strattime='" + strattime + '\'' +
                ", credit='" + credit + '\'' +
                ", totaltime='" + totaltime + '\'' +
                ", lecturetime='" + lecturetime + '\'' +
                ", experimenttime='" + experimenttime + '\'' +
                ", computertime='" + computertime + '\'' +
                ", assessment='" + assessment + '\'' +
                ", nature='" + nature + '\'' +
                ", category='" + category + '\'' +
                ", number='" + number + '\'' +
                ", college='" + college + '\'' +
                ", classname='" + classname + '\'' +
                ", reson='" + reson + '\'' +
                '}';
    }
}