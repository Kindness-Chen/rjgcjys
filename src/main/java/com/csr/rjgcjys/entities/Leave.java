package com.csr.rjgcjys.entities;

public class Leave {
    private Integer lid;
    private String username;
    private String sclass;
    private String subject;
    private String name;
    private String name1;
    private String classTime;
    private String afterTime;
    private String classPlace;
    private String afterPlace;
    private String classReson;
    private String resonTime;
    private String status;
    private String reson;
    private String imgName;
    private String imgExt;
    private String imgUrl;

    public Integer getLid() {
        return lid;
    }

    public void setLid(Integer lid) {
        this.lid = lid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSclass() {
        return sclass;
    }

    public void setSclass(String sclass) {
        this.sclass = sclass;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public String getClassTime() {
        return classTime;
    }

    public void setClassTime(String classTime) {
        this.classTime = classTime;
    }

    public String getAfterTime() {
        return afterTime;
    }

    public void setAfterTime(String afterTime) {
        this.afterTime = afterTime;
    }

    public String getClassPlace() {
        return classPlace;
    }

    public void setClassPlace(String classPlace) {
        this.classPlace = classPlace;
    }

    public String getAfterPlace() {
        return afterPlace;
    }

    public void setAfterPlace(String afterPlace) {
        this.afterPlace = afterPlace;
    }

    public String getClassReson() {
        return classReson;
    }

    public void setClassReson(String classReson) {
        this.classReson = classReson;
    }

    public String getResonTime() {
        return resonTime;
    }

    public void setResonTime(String resonTime) {
        this.resonTime = resonTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReson() {
        return reson;
    }

    public void setReson(String reson) {
        this.reson = reson;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public String getImgExt() {
        return imgExt;
    }

    public void setImgExt(String imgExt) {
        this.imgExt = imgExt;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @Override
    public String toString() {
        return "Leave{" +
                "lid=" + lid +
                ", username='" + username + '\'' +
                ", sclass='" + sclass + '\'' +
                ", subject='" + subject + '\'' +
                ", name='" + name + '\'' +
                ", name1='" + name1 + '\'' +
                ", classTime='" + classTime + '\'' +
                ", afterTime='" + afterTime + '\'' +
                ", classPlace='" + classPlace + '\'' +
                ", afterPlace='" + afterPlace + '\'' +
                ", classReson='" + classReson + '\'' +
                ", resonTime='" + resonTime + '\'' +
                ", status='" + status + '\'' +
                ", reson='" + reson + '\'' +
                ", imgName='" + imgName + '\'' +
                ", imgExt='" + imgExt + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                '}';
    }
}
