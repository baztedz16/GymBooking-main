package com.domzky.gymbooking.Helpers.Things;

import com.domzky.gymbooking.Helpers.Users.GymCoach;

public class ExerciseSesion {
    public String user,description,exercise_id,coach_id,member_id,gym_id,repititions,exerciseid,userFullname,excerciseName;
    public boolean deleted,checked;
    public int sets;
    public GymCoach coach;

    public ExerciseSesion(String user, String coach_id, String description, Boolean deleted, String exerciseid,String userFullname,String excerciseName) {
        this.user = user;
        this.coach_id = coach_id;
        this.description = description;
        this.deleted = deleted;
        this.exerciseid = exerciseid;
        this.userFullname = userFullname;
        this.excerciseName = excerciseName;
    }

}
