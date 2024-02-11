package com.domzky.gymbooking.Helpers.Users;

public class Gym {
    public String gym_name,gym_address,uid;
    public Boolean gym_activated,gym_status;

    public GymOwner owner;

    public Gym (String uid,String gym_name,String gym_address,Boolean gym_activated,Boolean gym_status,GymOwner owner) {
        this.uid = uid;
        this.gym_name = gym_name;
        this.gym_address = gym_address;
        this.gym_activated = gym_activated;
        this.gym_status = gym_status;
        this.owner = owner;
    }
    public Gym (String uid,String gym_name,String gym_address,Boolean gym_activated,GymOwner owner) {
        this.uid = uid;
        this.gym_name = gym_name;
        this.gym_address = gym_address;
        this.gym_activated = gym_activated;
        this.owner = owner;
    }
    public Gym (String uid,String gym_name,String gym_address,Boolean gym_activated,Boolean gym_status) {
        this.uid = uid;
        this.gym_name = gym_name;
        this.gym_address = gym_address;
        this.gym_activated = gym_activated;
        this.gym_status = gym_status;
    }
    public Gym (String gym_name,String gym_address,Boolean gym_activated,Boolean gym_status) {
        this.uid = uid;
        this.gym_name = gym_name;
        this.gym_address = gym_address;
        this.gym_activated = gym_activated;
        this.gym_status = gym_status;
    }
}
