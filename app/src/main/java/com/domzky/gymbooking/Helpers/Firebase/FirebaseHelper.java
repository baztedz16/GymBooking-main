package com.domzky.gymbooking.Helpers.Firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class FirebaseHelper {

    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

    public DatabaseReference getDietReference() {
        return ref.child("Diets");
    }
    public DatabaseReference getRootReference() {
        return ref;
    }
    public DatabaseReference getGymReference() {
        return ref.child("Gyms");
    }

    public DatabaseReference getWholeUserReference() {
        return ref.child("Users");
    }

    public DatabaseReference getGetGymReference() {
        return ref.child("Gyms");
    }

    public DatabaseReference getUserReference(String usertype) {
        return ref.child("Users").child(usertype);
    }

    public DatabaseReference getMembershipReference () {
        return ref.child("Memberships");
    }

    public DatabaseReference getExerciseReference () {
        return ref.child("Exercises");
    }
    public DatabaseReference getWorkoutReference () {
        return ref.child("Workout");
    }
    public DatabaseReference getCoachExerciseReference () {
        return ref.child("Exercises").child("GymCoaches");
    }
    public DatabaseReference getMemberExerciseReference () {
        return ref.child("Exercises").child("Members");
    }
    public DatabaseReference getCoachBooking () {
        return ref.child("CoachBooking");
    }
    public DatabaseReference getMemberSessionExcercise () {
        return ref.child("MemberSessionExercise");
    }

    public DatabaseReference getBmiReference() {
        return ref.child("BMI");
    }


}
