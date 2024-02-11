package com.domzky.gymbooking.Helpers.Users;

public class GymCoach {
    public String uid,gym_id,fullname,email,phone,username,password;
    public Boolean activated;
    public Gym gym;

    public GymCoach(String uid,String fullname) {
        this.uid = uid;
        this.fullname = fullname;
    }

    public GymCoach(String uid,String fullname,String email,String phone,String username,String password,Boolean activated,String gym_id) {
        this.uid = uid;
        this.fullname = fullname;
        this.email = email;
        this.phone = phone;
        this.username = username;
        this.password = password;
        this.activated = activated;
        this.gym_id = gym_id;
    }
    public GymCoach(String fullname,String email,String phone,String username,String password,Boolean activated,String gym_id) {
        this.fullname = fullname;
        this.email = email;
        this.phone = phone;
        this.username = username;
        this.password = password;
        this.activated = activated;
        this.gym_id = gym_id;
    }
}
