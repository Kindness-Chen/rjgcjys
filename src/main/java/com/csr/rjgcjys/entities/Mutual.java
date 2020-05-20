package com.csr.rjgcjys.entities;

import java.io.Serializable;

public class Mutual {
    private Integer mid;
    private String username;
    private String name;
    private String year;
    private String name1;
    private int branch;
    private int average;
    private int number;
    private int total;

    public Integer getMid() {
        return mid;
    }

    public void setMid(Integer mid) {
        this.mid = mid;
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

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public int getBranch() {
        return branch;
    }

    public void setBranch(int branch) {
        this.branch = branch;
    }

    public int getAverage() {
        return average;
    }

    public void setAverage(int average) {
        this.average = average;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "Mutual{" +
                "mid=" + mid +
                ", username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", year='" + year + '\'' +
                ", name1='" + name1 + '\'' +
                ", branch=" + branch +
                ", average=" + average +
                ", number=" + number +
                ", total=" + total +
                '}';
    }
}
