package com.csr.rjgcjys.entities;

public class PPT {
    private Integer pid;
    private String username;
    private String name;
    private String sclass;
    private String pptName;
    private String pptExtName;
    private String pptUrl;

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
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

    public String getPptName() {
        return pptName;
    }

    public void setPptName(String pptName) {
        this.pptName = pptName;
    }

    public String getPptExtName() {
        return pptExtName;
    }

    public void setPptExtName(String pptExtName) {
        this.pptExtName = pptExtName;
    }

    public String getPptUrl() {
        return pptUrl;
    }

    public void setPptUrl(String pptUrl) {
        this.pptUrl = pptUrl;
    }

    @Override
    public String toString() {
        return "PPT{" +
                "pid=" + pid +
                ", username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", sclass='" + sclass + '\'' +
                ", pptName='" + pptName + '\'' +
                ", pptExtName='" + pptExtName + '\'' +
                ", pptUrl='" + pptUrl + '\'' +
                '}';
    }
}
