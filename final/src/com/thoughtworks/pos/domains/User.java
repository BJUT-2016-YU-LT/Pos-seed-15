package com.thoughtworks.pos.domains;

import com.sun.org.apache.xpath.internal.operations.Bool;

/**
 * Created by ZXR on 2016/6/29.
 */
public class User {
    private String name;
    private Boolean isVip=false;
    private int Score=0;

    public  User(String name,Boolean isVip, int Score){
        this.name=name;
        this.isVip=isVip;
        this.Score=Score;
    }
    public User(){}
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getVip() {
        return isVip;
    }

    public void setVip(Boolean vip) {

        isVip = vip;
    }

    public int getScore() {
        return Score;
    }

    public void setScore(int score) {
        Score = score;
    }
}
