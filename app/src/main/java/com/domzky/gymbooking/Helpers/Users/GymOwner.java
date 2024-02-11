package com.domzky.gymbooking.Helpers.Users;

public class GymOwner {

    public String gym_name,gym_address;
    
    public String uid,fullname,email,phone,username,password;

    public GymOwner(String uid,String fullname,String gym_name,String gym_address) {
        this.uid = uid;
        this.gym_name = gym_name;
        this.gym_address = gym_address;
        this.fullname = fullname;
    }

    public GymOwner(String fullname,String email,String phone,String username,String password) {
        this.email = email;
        this.phone = phone;
        this.fullname = fullname;
        this.username = username;
        this.password = password;
    }

    public GymOwner(String uid,String fullname,String email,String phone,String username,String password) {
        this.uid = uid;
        this.email = email;
        this.phone = phone;
        this.fullname = fullname;
        this.username = username;
        this.password = password;
    }

    
    

}
