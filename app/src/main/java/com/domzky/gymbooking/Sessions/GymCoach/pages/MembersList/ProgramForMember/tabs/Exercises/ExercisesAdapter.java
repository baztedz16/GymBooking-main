package com.domzky.gymbooking.Sessions.GymCoach.pages.MembersList.ProgramForMember.tabs.Exercises;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.domzky.gymbooking.Helpers.Firebase.FirebaseHelper;
import com.domzky.gymbooking.Helpers.Things.Exercise;
import com.domzky.gymbooking.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ExercisesAdapter extends RecyclerView.Adapter<ExercisesAdapter.ViewHolder>{

    public List<Exercise> list;

    private String toastMessage;

    private DatabaseReference dbread = new FirebaseHelper().getMemberExerciseReference();
    private DatabaseReference dbwrite = new FirebaseHelper().getMemberExerciseReference();

    public ExercisesAdapter(List<Exercise> list) {
        this.list = list;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name,sets,reps;
        public ImageButton deleteBtn,editBtn,infoBtn;
        public CheckBox checkDone;

        public ViewHolder(View itemView) {
            super(itemView);

//            name = itemView.findViewById(R.id.exercise_item_name);
//            sets = itemView.findViewById(R.id.exercise_item_sets);
//            reps = itemView.findViewById(R.id.exercise_item_reps);
//
//            deleteBtn = itemView.findViewById(R.id.exercise_item_btn_delete);
//            editBtn = itemView.findViewById(R.id.exercise_item_btn_edit);
//            infoBtn = itemView.findViewById(R.id.exercise_item_btn_info);
//
//            checkDone = itemView.findViewById(R.id.exercise_item_check);

            deleteBtn.setVisibility(View.GONE);
            editBtn.setVisibility(View.GONE);

            checkDone.setEnabled(false);

        }

    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.menu_exercise_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        Exercise exercise = list.get(position);

        holder.name.setText(exercise.name);
        holder.sets.setText(exercise.sets);
        holder.reps.setText(exercise.repititions);

        holder.infoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle(exercise.name);
                builder.setMessage(exercise.description);
                builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // NOTHING
                    }
                });
                builder.setCancelable(false);
                builder.create().show();
            }
        });
        dbread.child(exercise.exercise_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                ProgressDialog progress = new ProgressDialog(holder.checkDone.getContext());
                progress.setCancelable(false);

                holder.checkDone.setEnabled(true);
                holder.checkDone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        holder.checkDone.setChecked(snapshot.child("checked").getValue(Boolean.class));
                        if (!isChecked) {
                            progress.setMessage("Finishing Exercise");
                            toastMessage = "Exercise Finished.";
                        } else {
                            progress.setMessage("Unfinishing Exercise");
                            toastMessage = "Exercise not Finished.";
                        }
                        progress.show();
                        dbwrite.child(exercise.exercise_id).child("checked").setValue(isChecked)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                                        progress.dismiss();
                                        Toast.makeText(holder.checkDone.getContext(),toastMessage,Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {}
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
