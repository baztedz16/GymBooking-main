package com.domzky.gymbooking.Sessions.Members.pages.CoachesList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.domzky.gymbooking.Helpers.Providers.SIM.SIMHelper;
import com.domzky.gymbooking.Helpers.Users.GymCoach;
import com.domzky.gymbooking.R;

import java.util.List;

public class CoachesListAdapter extends RecyclerView.Adapter<CoachesListAdapter.ViewHolder> {

    List<GymCoach> list;

    public CoachesListAdapter(List<GymCoach> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public CoachesListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_of_profiles_item,parent,false);
        CoachesListAdapter.ViewHolder viewHolder = new CoachesListAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GymCoach coach = list.get(position);

        holder.fullname.setText(coach.fullname);

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
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView fullname;
        public ImageButton callBtn,textBtn;
        public ViewHolder(View itemView) {
            super(itemView);
            fullname = itemView.findViewById(R.id.list_of_profiles_button_fullname);
            callBtn = itemView.findViewById(R.id.list_of_profiles_button_call);
            textBtn = itemView.findViewById(R.id.list_of_profiles_button_text);
        }

    }
}
