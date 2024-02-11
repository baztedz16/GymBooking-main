package com.domzky.gymbooking.Helpers.Things;

public class JoinedMembership {
    public String joined_id,membership_id,gym_id,member_id;

    public JoinedMembership(String joined_id,String membership_id,String gym_id,String member_id) {
        this.joined_id = joined_id;
        this.membership_id = membership_id;
        this.gym_id = gym_id;
        this.member_id = member_id;
    }

    public JoinedMembership(String membership_id,String gym_id,String member_id) {
        this.membership_id = membership_id;
        this.gym_id = gym_id;
        this.member_id = member_id;
    }

}
