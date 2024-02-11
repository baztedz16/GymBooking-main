package com.domzky.gymbooking.Sessions.GymOwner.pages.CoachesList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.domzky.gymbooking.Helpers.Providers.SIM.SIMHelper;
import com.domzky.gymbooking.Helpers.Users.GymCoach;
import com.domzky.gymbooking.Helpers.Users.GymStaff;
import com.domzky.gymbooking.R;
import com.domzky.gymbooking.Sessions.Admin.pages.GymsList.ModifyGym.ModifyGymActivity;
import com.domzky.gymbooking.Sessions.GymOwner.pages.CoachesList.ModifyCoach.ModifyCoachActivity;
import com.domzky.gymbooking.Sessions.GymOwner.pages.StaffsList.ModifyStaff.ModifyStaffActivity;

import java.util.List;

public class CoachListAdapter extends RecyclerView.Adapter<CoachListAdapter.ViewHolder> {

    public List<GymCoach> list;
    public Context wholeContext;

    public CoachListAdapter(List<GymCoach> list) {
        this.list = list;
    }
    public CoachListAdapter(List<GymCoach> list, Context wholeContext) {
        this.list = list;
        this.wholeContext = wholeContext;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView fullname;
        public ImageButton callBtn,textBtn;
        public ImageView profilePic;

        public ViewHolder(View itemView) {
            super(itemView);
            fullname = itemView.findViewById(R.id.list_of_profiles_button_fullname);
            callBtn = itemView.findViewById(R.id.list_of_profiles_button_call);
            textBtn = itemView.findViewById(R.id.list_of_profiles_button_text);
            profilePic = itemView.findViewById(R.id.list_of_profiles_profile_image);
        }

    }

    @NonNull
    @Override
    public CoachListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_of_profiles_item,parent,false);
        CoachListAdapter.ViewHolder holder = new CoachListAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CoachListAdapter.ViewHolder holder, int position) {
        GymCoach coach = list.get(position);

        holder.fullname.setText(coach.fullname);

        if (coach.activated) {
            holder.profilePic.setColorFilter(0xFF004953); // MIDNIGHT GREEN
        } else {
            holder.profilePic.setColorFilter(0xFF432938); // MIDNIGHT RED
        }

        holder.callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SIMHelper(v.getContext()).callNumber(coach.phone);
            }
        });
        holder.textBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SIMHelper(v.getContext()).textNumber(coach.phone);
            }
        });

        holder.profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wholeContext.startActivity(new Intent(wholeContext, ModifyCoachActivity.class)
                        .putExtra("coachuid",coach.uid)
                        .putExtra("gymuid",coach.gym_id)
                        .putExtra("coachfullname",coach.fullname)
                        .putExtra("coachemail",coach.email)
                        .putExtra("coachphone",coach.phone)
                        .putExtra("coachusername",coach.username)
                        .putExtra("coachpassword",coach.password)
                        .putExtra("coachactivated",coach.activated)
                );
            }
        });

    }


    @Override
    public int getItemCount() {
        return list.size();
    }
}

