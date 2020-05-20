package com.csr.rjgcjys.entities;

public class Week {
    private Integer tid;
    private String username;
    private String name;
    private String sclass;
    private String fileName;
    private String fileExtName;
    private String fileUrl;
    private String status;
    private String reson;
    private String imgName;
    private String imgExtName;
    private String imgUrl;

    public Integer getTid() {
        return tid;
    }

    public void setTid(Integer tid) {
        this.tid = tid;
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

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public String getImgExtName() {
        return imgExtName;
    }

    public void setImgExtName(String imgExtName) {
        this.imgExtName = imgExtName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @Override
    public String toString() {
        return "Week{" +
                "tid=" + tid +
                ", username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", sclass='" + sclass + '\'' +
                ", fileName='" + fileName + '\'' +
                ", fileExtName='" + fileExtName + '\'' +
                ", fileUrl='" + fileUrl + '\'' +
                ", status='" + status + '\'' +
                ", reson='" + reson + '\'' +
                ", imgName='" + imgName + '\'' +
                ", imgExtName='" + imgExtName + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                '}';
    }
}
