package com.domzky.gymbooking.Sessions.Members.pages.Programs.tabs.BMI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.domzky.gymbooking.Helpers.Things.BMI;
import com.domzky.gymbooking.R;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BMIAdapter extends RecyclerView.Adapter<BMIAdapter.ViewHolder> {
    List<BMI> list;

    public BMIAdapter(List<BMI> list) {
        this.list = list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView bmi,date,coach,height,weight;
        public ImageButton deleteBtn,editBtn;

        public ViewHolder(View itemView) {
            super(itemView);

            bmi = itemView.findViewById(R.id.bmi_item_bmi);
            date = itemView.findViewById(R.id.bmi_item_date);
            coach = itemView.findViewById(R.id.bmi_item_coach);
            height = itemView.findViewById(R.id.bmi_item_height);
            weight = itemView.findViewById(R.id.bmi_item_weight);

            deleteBtn = itemView.findViewById(R.id.bmi_item_btn_delete);
            editBtn = itemView.findViewById(R.id.bmi_item_btn_edit);

            deleteBtn.setVisibility(View.GONE);
            editBtn.setVisibility(View.GONE);
        }

    }

    @NonNull
    @NotNull
    @Override
    public BMIAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.menu_exercise_item,parent,false);
        BMIAdapter.ViewHolder viewHolder = new BMIAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        BMI bmi = list.get(position);

        holder.bmi.setText(bmi.getTotalBMI().toString() + " BMI");
        holder.date.setText("Checked at "+bmi.datetime);
        holder.coach.setText("By "+bmi.coach.fullname);
        if (bmi.heightEnglish) {
            holder.height.setText(bmi.weight + "ft");
        } else {
            holder.height.setText(bmi.weight + "m");
        }
        if (bmi.weightEnglish) {
            holder.height.setText(bmi.weight + "lbs");
        } else {
            holder.height.setText(bmi.weight + "kgs");
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
