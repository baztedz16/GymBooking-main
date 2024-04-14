package com.domzky.gymbooking.Helpers.Things;

import com.domzky.gymbooking.Helpers.Users.GymCoach;

public class Workout {
    public String uuid,workoutName,description,reps,sets,day,user;

    public Workout(String uuid, String workoutName, String description, String reps, String sets, String day, String user) {
        this.uuid = uuid;
        this.workoutName = workoutName;
        this.description = description;
        this.reps = reps;
        this.sets = sets;
        this.day = day;
        this.user = user;
    }
}
