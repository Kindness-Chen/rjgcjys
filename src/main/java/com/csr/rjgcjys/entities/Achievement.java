package com.csr.rjgcjys.entities;

public class Achievement {
    private Integer zid;
    private String username;
    private String name;
    private String sclass;
    private String subject;
    private String fileName;
    private String fileExtName;
    private String fileUrl;
    private String status;
    private String reson;

    public Integer getZid() {
        return zid;
    }

    public void setZid(Integer zid) {
        this.zid = zid;
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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileExtName() {
        return fileExtName;
    }

    public void setFileExtName(String fileExtName) {
        this.fileExtName = fileExtName;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
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

    @Override
    public String toString() {
        return "Achievement{" +
                "zid=" + zid +
                ", username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", sclass='" + sclass + '\'' +
                ", subject='" + subject + '\'' +
                ", fileName='" + fileName + '\'' +
                ", fileExtName='" + fileExtName + '\'' +
                ", fileUrl='" + fileUrl + '\'' +
                ", status='" + status + '\'' +
                ", reson='" + reson + '\'' +
                '}';
    }
}
