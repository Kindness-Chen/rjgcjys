package com.csr.rjgcjys.entities;

public class CourseDesign {
    private Integer cid;
    private String username;
    private String name;
    private String sclass;
    private String subject;
    private String teacher;
    private String fileName;
    /**
     * 文件扩展名(含.)
     */
    private String extName;
    private String url;
    private String enclosureName;
    private String enclosureExtName;
    private String enclosureUrl;
    private Integer score;
    private String reson;

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getExtName() {
        return extName;
    }

    public void setExtName(String extName) {
        this.extName = extName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEnclosureName() {
        return enclosureName;
    }

    public void setEnclosureName(String enclosureName) {
        this.enclosureName = enclosureName;
    }

    public String getEnclosureExtName() {
        return enclosureExtName;
    }

    public void setEnclosureExtName(String enclosureExtName) {
        this.enclosureExtName = enclosureExtName;
    }

    public String getEnclosureUrl() {
        return enclosureUrl;
    }

    public void setEnclosureUrl(String enclosureUrl) {
        this.enclosureUrl = enclosureUrl;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getReson() {
        return reson;
    }

    public void setReson(String reson) {
        this.reson = reson;
    }

    @Override
    public String toString() {
        return "CourseDesign{" +
                "cid=" + cid +
                ", username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", sclass='" + sclass + '\'' +
                ", subject='" + subject + '\'' +
                ", teacher='" + teacher + '\'' +
                ", fileName='" + fileName + '\'' +
                ", extName='" + extName + '\'' +
                ", url='" + url + '\'' +
                ", enclosureName='" + enclosureName + '\'' +
                ", enclosureExtName='" + enclosureExtName + '\'' +
                ", enclosureUrl='" + enclosureUrl + '\'' +
                ", score=" + score +
                ", reson='" + reson + '\'' +
                '}';
    }
}