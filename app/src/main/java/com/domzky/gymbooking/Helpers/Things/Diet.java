package com.domzky.gymbooking.Helpers.Things;

public class Diet {

    public String foodname,portions,intakeTime,coach_id,member_id,diet_id,description;
    public Boolean deleted;

    public Diet (String coach_id,String member_id,String foodname,String description,String portions,String intakeTime,Boolean deleted) {
        this.coach_id = coach_id;
        this.member_id = member_id;
        this.description = description;
        this.foodname = foodname;
        this.portions = portions;
        this.intakeTime = intakeTime;
        this.deleted = deleted;
    }

    public Diet (String diet_id,String coach_id,String member_id,String foodname,String description,String portions,String intakeTime,Boolean deleted) {
        this.diet_id = diet_id;
        this.coach_id = coach_id;
        this.member_id = member_id;
        this.description = description;
        this.foodname = foodname;
        this.portions = portions;
        this.intakeTime = intakeTime;
        this.deleted = deleted;
    }

}
