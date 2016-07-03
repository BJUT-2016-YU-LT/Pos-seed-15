package com.thoughtworks.pos.domains;

import java.util.List;

/**
 * Created by Administrator on 2014/12/31.
 */
public class Report{
    private List<ItemGroup> itemGroupies;
    private User user;
    public Report(List<ItemGroup> itemGroupies){
        this.itemGroupies = itemGroupies;
    }
    public Report(List<ItemGroup> itemGroupies,User user)
    {
        this.itemGroupies = itemGroupies;
        this.user=user;
    }

    public List<ItemGroup> getItemGroupies() {
        return itemGroupies;
    }

    public double getTotal(){
        double result = 0.00;
        for (ItemGroup itemGroup : itemGroupies)
            result += itemGroup.subTotal();
        return result;
    }

    public double getSaving(){
        double result = 0.00;
        for (ItemGroup itemGroup : itemGroupies)
            result += itemGroup.saving();
        return result;
    }


    public int getScore() {
        int x=0;
        if(0<=user.getScore()&&user.getScore()<=200)
            x=1;
        else if(200<user.getScore()&&user.getScore()<=500)
            x=3;
        else if(user.getScore()>500)
            x=5;
        int score=user.getScore();
        score= score+(int)Math.floor(getTotal()/5.0)*x;
        user.setScore(score);
        return score;
    }

    public User getUser() {
        return user;
    }

    public String getUserName()
    {
        return user.getName();
    }
    public void setUser(User user) {
        this.user = user;
    }
}
