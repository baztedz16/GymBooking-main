package com.domzky.gymbooking.Helpers.Things;

public class Membership {
    public String membership_id,name,gym_id,description;
    public int joined;
    public Double price;
    public Boolean deleted;

    public Membership(String membership_id,String name,String gym_id,Double price,String description,int joined,Boolean deleted) {
        this.membership_id = membership_id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.gym_id = gym_id;
        this.joined = joined;
        this.deleted = deleted;
    }
    public Membership(String membership_id,String name,String gym_id,Double price,String description,Boolean deleted) {
        this.membership_id = membership_id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.gym_id = gym_id;
        this.deleted = deleted;
    }
    public Membership(String membership_id,String gym_id) {
        this.membership_id = membership_id;
        this.gym_id = gym_id;
    }
    public Membership(String name,String gym_id,Double price,String description,Boolean deleted) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.gym_id = gym_id;
        this.deleted = deleted;
    }

}
