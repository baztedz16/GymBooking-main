package com.domzky.gymbooking.Helpers.Things;

public class Diet {

    public String meal_name,date,time,meal_time,userid,calories;
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

//    public Diet (String diet_id,String coach_id,String member_id,String foodname,String description,String portions,String intakeTime,Boolean deleted) {
//        this.diet_id = diet_id;
//        this.coach_id = coach_id;
//        this.member_id = member_id;
//        this.description = description;
//        this.foodname = foodname;
//        this.portions = portions;
//        this.intakeTime = intakeTime;
//        this.deleted = deleted;
//    }
    public Diet (String diet_id,String meal_name,String description,String calories,String date,String time,String meal_time,String userid) {
        this.diet_id = diet_id;
        this.meal_name = meal_name;
        this.description = description;
        this.calories = calories;
        this.date = date;
        this.time = time;
        this.meal_time = meal_time;
        this.userid = userid;
    }

}
