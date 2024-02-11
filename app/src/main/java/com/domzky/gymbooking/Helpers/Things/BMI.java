package com.domzky.gymbooking.Helpers.Things;

import com.domzky.gymbooking.Helpers.Users.GymCoach;

public class BMI {
    public String datetime,member_id,coach_id,description,bmi_id,bmi;
    public Boolean deleted,heightEnglish,weightEnglish;
    public Double height,weight,totalBMI;
    public GymCoach coach;

    public BMI(){}

    public BMI(String member_id,String coach_id,String datetime,String description,Double height,Double weight,Boolean deleted,String bmi) {
        this.member_id = member_id;
        this.coach_id = coach_id;
        this.datetime = datetime;
        this.description = description;
        this.height = height;
        this.weight = weight;
        this.deleted = deleted;
        this.bmi =bmi;
    }

    public BMI(String bmi_id,String member_id,GymCoach coach,String datetime,String description,Double height,Double weight,Boolean heightEnglish,Boolean weightEnglish,Boolean deleted) {
        this.member_id = member_id;
        this.bmi_id = bmi_id;
        this.coach = coach;
        this.datetime = datetime;
        this.description = description;
        this.height = height;
        this.weight = weight;
        this.deleted = deleted;
        this.heightEnglish = heightEnglish;
        this.weightEnglish = weightEnglish;
        this.totalBMI = this.convertToBMI(height,weight,heightEnglish,weightEnglish);
    }

    public Double getTotalBMI() {
        return totalBMI;
    }

    public Double convertToBMI(Double height, Double weight, Boolean heightEnglish, Boolean weightEnglish) {
        Double meters,kilos;
//        if (heightEnglish) {
//            meters = height * 0.3048;
//        } else {
//            meters = height;
//        }
//        if (weightEnglish) {
//            kilos = weight * 0.45359237;
//        } else {
//            kilos = weight;
//        }
        meters = heightEnglish? height * 0.3048 : height;
        kilos = weightEnglish? weight * 0.45359237: weight;
        return kilos/Math.pow(meters,2);
    }
}
