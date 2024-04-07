package com.domzky.gymbooking.Helpers.Users;

import androidx.annotation.Keep;

@Keep
public class GymMember {

    public String fullname,email,phone,username,password,uid,gymid;
    public Boolean activated;

    public GymMember(String uid,String fullname) {
        this.uid = uid;
        this.fullname = fullname;
    }

    public GymMember(String fullname,String email,String phone,String username,String password,Boolean activated,String gymid) {
        this.fullname = fullname;
        this.email = email;
        this.phone = phone;
        this.username = username;
        this.password = password;
        this.activated = activated;
        this.gymid = gymid;
    }
    public GymMember(String fullname,String email,String phone,String username,String password,Boolean activated) {
        this.fullname = fullname;
        this.email = email;
        this.phone = phone;
        this.username = username;
        this.password = password;
        this.activated = activated;
    }

}
