package com.csr.rjgcjys.entities;

public class Score {
    private Integer cid;
    private String username;
    private String name;
    private String sclass;
    private Integer usualScore;
    private Integer examScore;
    private Integer finalScore;
    private String tUsername;

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

    public Integer getUsualScore() {
        return usualScore;
    }

    public void setUsualScore(Integer usualScore) {
        this.usualScore = usualScore;
    }

    public Integer getExamScore() {
        return examScore;
    }

    public void setExamScore(Integer examScore) {
        this.examScore = examScore;
    }

    public Integer getFinalScore() {
        return finalScore;
    }

    public void setFinalScore(Integer finalScore) {
        this.finalScore = finalScore;
    }

    public String gettUsername() {
        return tUsername;
    }

    public void settUsername(String tUsername) {
        this.tUsername = tUsername;
    }

    @Override
    public String toString() {
        return "Score{" +
                "cid=" + cid +
                ", username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", sclass='" + sclass + '\'' +
                ", usualScore=" + usualScore +
                ", examScore=" + examScore +
                ", finalScore=" + finalScore +
                ", tUsername='" + tUsername + '\'' +
                '}';
    }
}
