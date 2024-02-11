package com.domzky.gymbooking.Sessions.GymCoach.pages.Exercises.ModifyExercise;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.domzky.gymbooking.Helpers.Firebase.FirebaseHelper;
import com.domzky.gymbooking.Helpers.Things.Exercise;
import com.domzky.gymbooking.Helpers.Things.ExerciseWorkout;
import com.domzky.gymbooking.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class ExercisesWorkoutAdapter extends RecyclerView.Adapter<ExercisesWorkoutAdapter.ViewHolder> {

    public List<ExerciseWorkout> list;
    public Context wholeContext;

    private DatabaseReference db = new FirebaseHelper().getWorkoutReference();

    public ExercisesWorkoutAdapter(List<ExerciseWorkout> list, Context wholeContext) {
        this.list = list;
        this.wholeContext = wholeContext;
    }
    
    @NonNull
    @Override
    public ExercisesWorkoutAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.coach_menu_workout_item,parent,false);
        ExercisesWorkoutAdapter.ViewHolder holder = new ExercisesWorkoutAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ExercisesWorkoutAdapter.ViewHolder holder, int position) {
        ExerciseWorkout exercise = list.get(position);

        holder.exercisename.setText(exercise.name);
//        holder.exercisedesc.setText(exercise.description);
        
        holder.delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog progress = new ProgressDialog(wholeContext);
                progress.setCancelable(false);
                progress.setMessage("Removing Exercise");
                AlertDialog.Builder builder = new AlertDialog.Builder(wholeContext);
                builder.setTitle("Delete " + exercise.name);
                builder.setMessage("Are you sure you want to remove this exercise?");
                builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // DO NOTHING
                    }
                });
                builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        progress.show();
                        db.child(exercise.exercise_id).child("deleted").setValue(true)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        progress.dismiss();
                                        Toast.makeText(wholeContext,"Exercise Removed.",Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.e("FIREBASE ERROR",""+ e.getMessage());
                                        Toast.makeText(wholeContext,"Exercise Removing Failed. Please Try Again",Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });
                builder.create().show();
            }
        });
        
        holder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wholeContext.startActivity(new Intent(wholeContext, ModifyExerciseActivity.class)
                        .putExtra("exercise_id", exercise.exerciseid)
                        .putExtra("exercise_name", exercise.name)
                        .putExtra("exercise_description", exercise.description)
                );
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView exercisename,exercisedesc;
        ImageButton delBtn,editBtn;
        public ViewHolder(View itemView) {
            super(itemView);
            exercisename = itemView.findViewById(R.id.coach_exercise_item_name);
//            exercisedesc = itemView.findViewById(R.id.coach_exercise_item_description);
            delBtn = itemView.findViewById(R.id.coach_exercise_item_btn_delete);
            editBtn = itemView.findViewById(R.id.coach_exercise_item_btn_edit);
        }
        
    }
}
