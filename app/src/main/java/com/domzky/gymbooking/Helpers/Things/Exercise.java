package com.domzky.gymbooking.Helpers.Things;

import com.domzky.gymbooking.Helpers.Users.GymCoach;

public class Exercise {
    public String name,description,exercise_id,coach_id,member_id,gym_id,repititions,exerciseid;
    public boolean deleted,checked;
    public int sets;
    public GymCoach coach;

    public Exercise(String name, String coach_id, String description, Boolean deleted,String exerciseid) {
        this.name = name;
        this.coach_id = coach_id;
        this.description = description;
        this.deleted = deleted;
        this.exerciseid = exerciseid;
    }
    public Exercise(String exercise_id, String name, String description, String member_id, GymCoach coach, String gym_id, String repititions, int sets, Boolean checked, Boolean deleted) {
        this.exercise_id = exercise_id;
        this.name = name;
        this.description = description;
        this.coach = coach;
        this.member_id = member_id;
        this.gym_id = gym_id;
        this.deleted = deleted;
        this.checked = checked;
        this.repititions = repititions;
        this.sets = sets;
    }

    public Exercise(String name,String description,String member_id,String coach_id,String gym_id,String repititions,int sets,Boolean checked,Boolean deleted) {
        this.name = name;
        this.description = description;
        this.coach_id = coach_id;
        this.member_id = member_id;
        this.gym_id = gym_id;
        this.deleted = deleted;
        this.checked = checked;
        this.repititions = repititions;
        this.sets = sets;
    }

    public Exercise(String exerciseid, String coach_id, String name, String description, Boolean deleted) {
        this.exerciseid = exerciseid;
        this.coach_id = coach_id;
        this.name = name;
        this.description = description;
        this.deleted = deleted;
    }
}
