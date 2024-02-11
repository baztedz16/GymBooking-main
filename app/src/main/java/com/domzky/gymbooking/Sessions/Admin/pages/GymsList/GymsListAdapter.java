package com.domzky.gymbooking.Sessions.Admin.pages.GymsList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.domzky.gymbooking.Helpers.Providers.SIM.SIMHelper;
import com.domzky.gymbooking.Helpers.Users.Gym;
import com.domzky.gymbooking.Helpers.Users.GymOwner;
import com.domzky.gymbooking.R;
import com.domzky.gymbooking.Sessions.Admin.pages.GymsList.ModifyGym.ModifyGymActivity;

import java.util.List;
import java.util.Locale;

public class GymsListAdapter extends RecyclerView.Adapter<GymsListAdapter.ViewHolder> {

    List<Gym> list;
    Context wholeContext;

    public GymsListAdapter(List<Gym> list,Context context) {
        this.list = list;
        this.wholeContext = context;
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
    public GymsListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_of_profiles_item,parent,false);
        GymsListAdapter.ViewHolder viewHolder = new GymsListAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull GymsListAdapter.ViewHolder holder, int position) {
        Gym gym = list.get(position);

        holder.fullname.setText(gym.gym_name);
        holder.profilePic.setImageResource(R.drawable.ic_baseline_other_houses_24);

        if (gym.gym_activated) {
            holder.profilePic.setColorFilter(0xFF004953); // MIDNIGHT GREEN
        } else {
            holder.profilePic.setColorFilter(0xFF432938); // MIDNIGHT RED
        }

        holder.callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SIMHelper(v.getContext()).callNumber(gym.owner.phone);
            }
        });
        holder.textBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SIMHelper(v.getContext()).textNumber(gym.owner.phone);
            }
        });

        holder.profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wholeContext.startActivity(new Intent(wholeContext, ModifyGymActivity.class)
                        .putExtra("gymuid",gym.uid)
                        .putExtra("gymname",gym.gym_name)
                        .putExtra("gymaddress",gym.gym_address)
                        .putExtra("gymactivated",gym.gym_activated)
                        .putExtra("gymstatus",gym.gym_status)
                        .putExtra("ownername",gym.owner.fullname)
                        .putExtra("owneremail",gym.owner.email)
                        .putExtra("ownerphone",gym.owner.phone)
                        .putExtra("ownerusername",gym.owner.username)
                        .putExtra("ownerpassword",gym.owner.password)
                );
            }
        });

//        holder.gymname.setText(gym.gym_name.toUpperCase(Locale.ROOT));
//
//        if (gym.gym_activated) {
//            holder.gymaddress.setText("Gym Active".toUpperCase(Locale.ROOT));
//            holder.gymaddress.setTextColor(Color.GREEN);
//        } else {
//            holder.gymaddress.setText("Gym Inactive".toUpperCase(Locale.ROOT));
//            holder.gymaddress.setTextColor(Color.RED);
//        }
//
//        holder.fullname.setText(gym.gym_address);
//
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                wholeContext.startActivity(new Intent(wholeContext, ModifyGymActivity.class)
//                        .putExtra("gymuid",gym.uid)
//                        .putExtra("gymname",gym.gym_name)
//                        .putExtra("gymaddress",gym.gym_address)
//                        .putExtra("gymactivated",gym.gym_activated)
//                        .putExtra("gymstatus",gym.gym_status)
//                        .putExtra("ownername",gym.owner.fullname)
//                        .putExtra("owneremail",gym.owner.email)
//                        .putExtra("ownerphone",gym.owner.phone)
//                        .putExtra("ownerusername",gym.owner.username)
//                        .putExtra("ownerpassword",gym.owner.password)
//                );
//            }
//        });
        
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
