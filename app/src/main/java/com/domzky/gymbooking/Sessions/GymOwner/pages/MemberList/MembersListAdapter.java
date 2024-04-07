package com.domzky.gymbooking.Sessions.GymOwner.pages.MemberList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.domzky.gymbooking.Helpers.Providers.SIM.SIMHelper;
import com.domzky.gymbooking.Helpers.Users.GymMember;
import com.domzky.gymbooking.Helpers.Users.GymStaff;
import com.domzky.gymbooking.R;
import com.domzky.gymbooking.Sessions.GymOwner.pages.StaffsList.ModifyStaff.ModifyStaffActivity;

import java.util.List;

public class MembersListAdapter extends RecyclerView.Adapter<MembersListAdapter.ViewHolder> {

    public List<GymMember> list;
    public Context wholeContext;

    public MembersListAdapter(List<GymMember> list) {
        this.list = list;
    }

    public MembersListAdapter(List<GymMember> list, Context wholeContext) {
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
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_of_profiles_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GymMember staff = list.get(position);

        holder.fullname.setText(staff.fullname);

        if (staff.activated) {
            holder.profilePic.setColorFilter(0xFF004953); // MIDNIGHT GREEN
        } else {
            holder.profilePic.setColorFilter(0xFF432938); // MIDNIGHT RED
        }

        holder.callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SIMHelper(v.getContext()).callNumber(staff.phone);
            }
        });
        holder.textBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SIMHelper(v.getContext()).textNumber(staff.phone);
            }
        });

        holder.profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wholeContext.startActivity(new Intent(wholeContext, ModifyStaffActivity.class)
                        .putExtra("staffuid",staff.uid)
                        .putExtra("gymuid",staff.gymid)
                        .putExtra("stafffullname",staff.fullname)
                        .putExtra("staffemail",staff.email)
                        .putExtra("staffphone",staff.phone)
                        .putExtra("staffusername",staff.username)
                        .putExtra("staffpassword",staff.password)
                        .putExtra("staffactivated",staff.activated)
                );
            }
        });

    }


    @Override
    public int getItemCount() {
        return list.size();
    }
}
