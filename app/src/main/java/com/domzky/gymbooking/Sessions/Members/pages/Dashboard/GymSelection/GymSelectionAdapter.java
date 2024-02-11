package com.domzky.gymbooking.Sessions.Members.pages.Dashboard.GymSelection;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.domzky.gymbooking.Helpers.Users.Gym;
import com.domzky.gymbooking.R;
import com.domzky.gymbooking.Sessions.Members.pages.Dashboard.GymSelection.GymItem.GymItemActivity;
import com.domzky.gymbooking.Sessions.Members.pages.Dashboard.GymSelection.GymItem.GymItemMembershipAdapter;

import java.util.List;

public class GymSelectionAdapter extends RecyclerView.Adapter<GymSelectionAdapter.ViewHolder> {

    List<Gym> list;
    Context wholeContext;

    public GymSelectionAdapter(List<Gym> list, Context wholeContext){
        this.list = list;
        this.wholeContext = wholeContext;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView gymname,gymaddress;
        LinearLayout itemClick;

        public ViewHolder(View itemView) {
            super(itemView);
            gymname = itemView.findViewById(R.id.member_gym_item_name);
            gymaddress = itemView.findViewById(R.id.member_gym_item_address);
            itemClick = itemView.findViewById(R.id.member_gym_item_clicker);
        }

    }

    @NonNull
    @Override
    public GymSelectionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.member_gym_item, parent, false);
        GymSelectionAdapter.ViewHolder viewHolder = new GymSelectionAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull GymSelectionAdapter.ViewHolder holder, int position) {
        Gym gym = list.get(position);

        holder.gymname.setText(gym.gym_name);
        holder.gymaddress.setText(gym.gym_address);

        holder.itemClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wholeContext.startActivity(new Intent(wholeContext,GymItemActivity.class)
                        .putExtra("gym_id",gym.uid));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
