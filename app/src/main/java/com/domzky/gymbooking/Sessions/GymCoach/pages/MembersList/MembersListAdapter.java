package com.domzky.gymbooking.Sessions.GymCoach.pages.MembersList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.domzky.gymbooking.Helpers.Users.GymMember;
import com.domzky.gymbooking.R;

import java.util.List;

public class MembersListAdapter extends RecyclerView.Adapter<MembersListAdapter.ViewHolder> {
    public List<GymMember> list;

    public MembersListAdapter (List<GymMember> list) {
        this.list = list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView membername,membertype;
        public ViewHolder(View itemView) {
            super(itemView);
            membername = itemView.findViewById(R.id.profile_item_member_name);
            membertype = itemView.findViewById(R.id.profile_item_member_type);
        }
    }


    @NonNull
    @Override
    public MembersListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.members_list_item,parent,false);
        MembersListAdapter.ViewHolder viewHolder = new MembersListAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MembersListAdapter.ViewHolder holder, int position) {
        GymMember member = list.get(position);
        holder.membername.setText(member.fullname);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
