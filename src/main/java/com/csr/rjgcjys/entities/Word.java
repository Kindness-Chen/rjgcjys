package com.csr.rjgcjys.entities;

import javax.persistence.Table;

@Table(name = "s_word")
public class Word {
    private Integer sid;
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
    private int score;
    private String reson;

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
        return this.extName;
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

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
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
        return "Word{" +
                "sid=" + sid +
                ", username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", sclass='" + sclass + '\'' +
                ", subject='" + subject + '\'' +
                ", teacher='" + teacher + '\'' +
                ", fileName='" + fileName + '\'' +
                ", extName='" + extName + '\'' +
                ", url='" + url + '\'' +
                ", score=" + score +
                ", reson='" + reson + '\'' +
                '}';
    }
}
