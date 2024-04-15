package com.domzky.gymbooking.Helpers.Things;

import com.domzky.gymbooking.Helpers.Users.GymCoach;

public class ExerciseLogSesion {
    public String user,excerciseID,excerciseName,day,date;

    public String excerciseActivity,sets,rept,actualrep;
    public String uuid;
    public ExerciseLogSesion(String user, String excerciseID, String excerciseName,String day,String date) {
        this.user = user;
        this.excerciseID = excerciseID;
        this.excerciseName = excerciseName;
        this.day = day;
        this.date = date;
    }
    public ExerciseLogSesion(String excerciseActivity, String sets, String rept,String actualrep) {
        this.excerciseActivity = excerciseActivity;
        this.sets = sets;
        this.rept = rept;
        this.actualrep = actualrep;
    }
    public ExerciseLogSesion(String uuid) {
        this.uuid = uuid;
    }
    public ExerciseLogSesion(String uuid,String excerciseName) {
        this.uuid = uuid;
        this.excerciseName = excerciseName;
    }
}
