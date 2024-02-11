package com.domzky.gymbooking.Sessions.GymCoach.pages.MembersList.ProgramForMember.tabs.Diet;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.domzky.gymbooking.Helpers.Firebase.FirebaseHelper;
import com.domzky.gymbooking.Helpers.Things.Diet;
import com.domzky.gymbooking.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DietAdapter extends RecyclerView.Adapter<DietAdapter.ViewHolder> {

    public List<Diet> list;
    private ProgressDialog progress;
    private DatabaseReference db = new FirebaseHelper().getDietReference();

    public DietAdapter(List<Diet> list) {
        this.list = list;
    }
    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.program_diet_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        Diet diet = list.get(position);

        holder.item_name.setText(diet.foodname);
        holder.item_portions.setText(diet.portions);
        holder.item_intake.setText(diet.intakeTime);
        holder.item_description.setText(diet.description);
        
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress = new ProgressDialog(v.getContext());
                progress.setCancelable(false);
                progress.setMessage("Deleting Diet Item");

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage("Are You Sure you want to delete " + diet.foodname + " in the diet list?");
                builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        progress.show();
                        db.child(diet.diet_id).removeValue()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                                        progress.dismiss();
                                        Toast.makeText(v.getContext(), "Diet Item Deleted.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });
                builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // NOTHING
                    }
                });
                builder.setCancelable(false);
                builder.create().show();
            }
        });
        holder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView item_name,item_portions,item_intake,item_description;
        public ImageButton deleteBtn,editBtn;

        public ViewHolder(View itemView) {
            super(itemView);
            item_name = itemView.findViewById(R.id.program_diet_item_name);
            item_portions = itemView.findViewById(R.id.program_diet_item_portion);
            item_intake = itemView.findViewById(R.id.program_diet_item_intake);

            deleteBtn = itemView.findViewById(R.id.program_diet_item_button_delete);
            editBtn = itemView.findViewById(R.id.program_diet_item_button_edit);

            deleteBtn.setVisibility(View.GONE);
            editBtn.setVisibility(View.GONE);
        }
    }

}
