package com.domzky.gymbooking.Helpers.Things;

import com.domzky.gymbooking.Helpers.Users.GymCoach;

public class CoachBooking {
    public String coach,coachName,gym,time,date,user,uid,userFullname;
    public int status;
    public int sets;
    public GymCoach CoachBooking;

    public CoachBooking(String coach,String coachName, String gym, String time, String date, String user,int Status,String userFullname) {
        this.coach = coach;
        this.coachName = coachName;
        this.gym = gym;
        this.time = time;
        this.date = date;
        this.user = user;
        this.status = Status;
        this.userFullname = userFullname;
    }
    public CoachBooking(String uid,String coach,String coachName, String gym, String time, String date, String user,int Status,String userFullname) {
        this.uid = uid;
        this.coach = coach;
        this.coachName = coachName;
        this.gym = gym;
        this.time = time;
        this.date = date;
        this.user = user;
        this.status = Status;
        this.userFullname = userFullname;
    }
}
