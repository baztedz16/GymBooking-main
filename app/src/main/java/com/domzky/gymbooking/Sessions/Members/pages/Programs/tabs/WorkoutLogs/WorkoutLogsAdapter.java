package com.domzky.gymbooking.Sessions.Members.pages.Programs.tabs.WorkoutLogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.domzky.gymbooking.Helpers.Firebase.FirebaseHelper;
import com.domzky.gymbooking.Helpers.Things.Exercise;
import com.domzky.gymbooking.Helpers.Things.ExerciseLogSesion;
import com.domzky.gymbooking.Helpers.Things.Schedules;
import com.domzky.gymbooking.Helpers.Things.Workout;
import com.domzky.gymbooking.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class WorkoutLogsAdapter extends RecyclerView.Adapter<WorkoutLogsAdapter.ViewHolder>{
    private boolean isAlertDialogShown1 = false;
    private boolean isAlertDialogShown2 = false;
    public List<ExerciseLogSesion> list;
    List<ExerciseLogSesion> list3;
    List<ExerciseLogSesion> list4;
    private String toastMessage;

    private DatabaseReference dbread = new FirebaseHelper().getMemberExerciseReference();
    private DatabaseReference dbwrite = new FirebaseHelper().getMemberExerciseReference();
    private DatabaseReference db = new FirebaseHelper().getWorkOutLogs();
    private DatabaseReference dbExercise = new FirebaseHelper().getExcerciseLogs();
    private DatabaseReference dbwriteExcerciseLogs = new FirebaseHelper().getExcerciseLogs();
    public WorkoutLogsAdapter(List<ExerciseLogSesion> list) {
        this.list = list;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name,sets,reps;
        public ImageButton deleteBtn,editBtn,infoBtn;
        public LinearLayout dayClick;

        public ViewHolder(View itemView) {
            super(itemView);

           name = itemView.findViewById(R.id.dayString);
           dayClick = itemView.findViewById(R.id.dayClick);
            list3 = new ArrayList<>();
            list4 = new ArrayList<>();
            dayClick.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    isAlertDialogShown2 = false;
                    SharedPreferences sharedPrefs = v.getContext().getSharedPreferences("member", v.getContext().MODE_PRIVATE);
                    String userid = sharedPrefs.getString("userid", "");

                    dbExercise.child(name.getText().toString()+userid).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @com.google.firebase.database.annotations.NotNull DataSnapshot snapshot) {
                            list3.clear();
                            for ( DataSnapshot snap : snapshot.getChildren() ) {
                                Log.i("Data",snap.getKey());
//                                    if (
//                                            snap.child("coach_id").getValue(String.class).equals(userid)
//                                                    && !snap.child("deleted").getValue(Boolean.class)
//                                    ) {
//                                        Log.i("",snap.child("exerciseid").getValue(String.class));
//
//                                    }
                                list3.add(new ExerciseLogSesion(
                                        snap.getKey(),
                                        snap.child("excerciseName").getValue(String.class)
                                ));
                            }
                            if (!isAlertDialogShown2) {
                                showExerciseListDialog(list3,v.getContext(),userid,name.getText().toString()+userid);
                                isAlertDialogShown2 = true; // Set the flag to true after showing the AlertDialog
                            }
                            // Display the list in AlertDialog

                        }

                        @Override
                        public void onCancelled(@NonNull @com.google.firebase.database.annotations.NotNull DatabaseError error) {
                            Log.d("FIREBASE ERR", error.getMessage());
                        }
                    });
                }
            });

        }

    }
    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.menu_workout_listday,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        ExerciseLogSesion exerciseLogSesion = list.get(position);

        holder.name.setText(exerciseLogSesion.uuid);

    }
    private void showExerciseListDialog(List<ExerciseLogSesion> exerciseList,Context context,String userid,String day) {
        // Create an AlertDialog.Builder


        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.coach_custom_exercise_dialog, null);
        builder.setView(dialogView);

        // Get the ListView from the custom layout
        ListView listView = dialogView.findViewById(R.id.exercisesListView);

        // Create an ArrayAdapter for the ListView
        //ArrayAdapter<Exercise> adapter = new ArrayAdapter<>(context, R.layout.coach_custom_exercise_dialog, exerciseList);
        ArrayAdapter<ExerciseLogSesion> adapter = new ArrayAdapter<ExerciseLogSesion>(context, 0, exerciseList) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                // Check if the convertView is null, if so, inflate a new layout
                if (convertView == null) {
                    convertView = LayoutInflater.from(context).inflate(R.layout.coach_custom_exercise_items, parent, false);
                }

                // Get the TextViews from the custom layout
                TextView textViewExerciseId = convertView.findViewById(R.id.textViewExerciseId);
                TextView textViewExerciseDescription = convertView.findViewById(R.id.textViewExerciseDescription);

                // Set the text for each TextView based on the Exercise object
                ExerciseLogSesion exerciseLogSesion = exerciseList.get(position);
                textViewExerciseId.setText("ID: " + exerciseLogSesion.uuid.toString());
                textViewExerciseDescription.setText("Exercise: " + exerciseLogSesion.excerciseName);

                textViewExerciseDescription.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isAlertDialogShown1 = false;
                        dbExercise.child(day).child(exerciseLogSesion.uuid.toString()).child("Activity").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull @com.google.firebase.database.annotations.NotNull DataSnapshot snapshot) {
                                list4.clear();
                                for ( DataSnapshot snap : snapshot.getChildren() ) {
                                    Log.i("Data",exerciseLogSesion.uuid.toString());
//                                    if (
//                                            snap.child("coach_id").getValue(String.class).equals(userid)
//                                                    && !snap.child("deleted").getValue(Boolean.class)
//                                    ) {
//                                        Log.i("",snap.child("exerciseid").getValue(String.class));
//
//                                    }
                                    list4.add(new ExerciseLogSesion(
                                            snap.getKey(),
                                            snap.child("sets").getValue(String.class),
                                            snap.child("rept").getValue(String.class),
                                            snap.child("actualrep").getValue(String.class)

                                    ));
                                }
                                if (!isAlertDialogShown1) {
                                    showActualLogs(list4,context,exerciseLogSesion.uuid.toString(),day);
                                    isAlertDialogShown1 = true; // Set the flag to true after showing the AlertDialog
                                }
                                // Display the list in AlertDialog

                            }

                            @Override
                            public void onCancelled(@NonNull @com.google.firebase.database.annotations.NotNull DatabaseError error) {
                                Log.d("FIREBASE ERR", error.getMessage());
                            }
                        });
                    }
                });
                return convertView;
            }
        };

        // Set the adapter for the ListView
        listView.setAdapter(adapter);

        // Set up item click listener for the ListView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


            }
        });
// Add the "Add Exercise" button
        // Create and show the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void showActualLogs(List<ExerciseLogSesion> exerciseList,Context context,String userid,String day) {
        // Create an AlertDialog.Builder


        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.coach_custom_exercise_dialog, null);
        builder.setView(dialogView);

        // Get the ListView from the custom layout
        ListView listView = dialogView.findViewById(R.id.exercisesListView);

        // Create an ArrayAdapter for the ListView
        //ArrayAdapter<Exercise> adapter = new ArrayAdapter<>(context, R.layout.coach_custom_exercise_dialog, exerciseList);
        ArrayAdapter<ExerciseLogSesion> adapter = new ArrayAdapter<ExerciseLogSesion>(context, 0, exerciseList) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                // Check if the convertView is null, if so, inflate a new layout
                if (convertView == null) {
                    convertView = LayoutInflater.from(context).inflate(R.layout.coach_custom_actualexercise_items, parent, false);
                }

                TextView textViewExerciseId = convertView.findViewById(R.id.textViewExerciseId);
                TextView textViewExerciseDescription = convertView.findViewById(R.id.textViewExerciseDescription);
                TextView textViewCurrentSets = convertView.findViewById(R.id.textViewCurrentSets);
                TextView textViewSetReps = convertView.findViewById(R.id.textViewSetReps);
                EditText editTextActualReps = convertView.findViewById(R.id.editTextActualReps);
                Button minus = convertView.findViewById(R.id.minusbtn);
                Button plus = convertView.findViewById(R.id.plusbtn);

                ExerciseLogSesion exerciseLogSesion = exerciseList.get(position);
                textViewExerciseId.setText("ID: " + exerciseLogSesion.excerciseActivity);
                //textViewExerciseDescription.setText("Exercise: " + exerciseLogSesion.uuid.toString());

                // Set text for the non-editable fields
                textViewCurrentSets.setText("Current Sets: " + exerciseLogSesion.sets);
                textViewSetReps.setText("Set Reps: " + exerciseLogSesion.rept);

                // Set initial text for the editable field
                editTextActualReps.setText(String.valueOf(exerciseLogSesion.actualrep));

                int reptint = Integer.parseInt(exerciseLogSesion.rept);
                int actualreptint = Integer.parseInt(exerciseLogSesion.actualrep);
                plus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(actualreptint <= reptint){
                            dbwriteExcerciseLogs.child(day).child(userid).child("Activity").child(exerciseLogSesion.excerciseActivity).setValue(new ExerciseLogSesion(
                                exerciseLogSesion.excerciseName,
                                exerciseLogSesion.sets,
                                exerciseLogSesion.rept,
                                    String.valueOf(Integer.valueOf(editTextActualReps.getText().toString())+1)
                        ));
                            editTextActualReps.setText( String.valueOf(Integer.valueOf(editTextActualReps.getText().toString())+1));
                        }
                    }
                });
                minus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(actualreptint >=1){
                            dbwriteExcerciseLogs.child(day).child(userid).child("Activity").child(exerciseLogSesion.excerciseActivity).setValue(new ExerciseLogSesion(
                                    exerciseLogSesion.excerciseName,
                                    exerciseLogSesion.sets,
                                    exerciseLogSesion.rept,
                                    String.valueOf(Integer.valueOf(editTextActualReps.getText().toString())-1)
                            ));
                            editTextActualReps.setText( String.valueOf(Integer.valueOf(editTextActualReps.getText().toString())-1));
                        }
                    }
                });
                editTextActualReps.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
//                        dbwriteExcerciseLogs.child(day).child(userid).child("Activity").child(exerciseLogSesion.excerciseActivity).setValue(new ExerciseLogSesion(
//                                exerciseLogSesion.excerciseName,
//                                exerciseLogSesion.sets,
//                                exerciseLogSesion.rept,
//                                editTextActualReps.getText().toString()
//                        ));
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                // Disable editing for non-editable fields
                textViewCurrentSets.setEnabled(false);
                textViewSetReps.setEnabled(false);

                return convertView;
            }
        };

        // Set the adapter for the ListView
        listView.setAdapter(adapter);

        // Set up item click listener for the ListView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
// Add the "Add Exercise" button
        // Create and show the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    @Override
    public int getItemCount() {
        return list.size();
    }
}
