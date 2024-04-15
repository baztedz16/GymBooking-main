package com.domzky.gymbooking.Sessions.Members.pages.Programs.tabs.Exercises;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import com.domzky.gymbooking.Helpers.FieldSyntaxes.MoneyTextWatcher;
import com.domzky.gymbooking.Helpers.Firebase.FirebaseHelper;
import com.domzky.gymbooking.Helpers.Things.Exercise;
import com.domzky.gymbooking.Helpers.Things.ExerciseLogSesion;
import com.domzky.gymbooking.Helpers.Things.ExerciseSesion;
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
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ExercisesAdapter extends RecyclerView.Adapter<ExercisesAdapter.ViewHolder>{
    private boolean isAlertDialogShown = false; // Flag to track if the AlertDialog has been shown
    private boolean isAlertDialogShown2 = false; // Flag to track if the AlertDialog has been shown
    public List<Schedules> list;
    List<Workout> list2;
    List<Exercise> list3;
    private String toastMessage;

    private DatabaseReference dbread = new FirebaseHelper().getMemberExerciseReference();
    private DatabaseReference dbwrite = new FirebaseHelper().getMemberExerciseReference();
    private DatabaseReference dbwriteExcerciseLogs = new FirebaseHelper().getExcerciseLogs();
    private DatabaseReference db = new FirebaseHelper().getWorkOutLogs();
    private DatabaseReference dbExercise = new FirebaseHelper().getExerciseReference();
    public ExercisesAdapter(List<Schedules> list) {
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

           list2 = new ArrayList<>();
           list3 = new ArrayList<>();
           dayClick.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   isAlertDialogShown2 = false;
                   db.addValueEventListener(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull @com.google.firebase.database.annotations.NotNull DataSnapshot snapshot) {
                           list2.clear();
                           for ( DataSnapshot snap : snapshot.getChildren() ) {
//                                    if (
//                                            snap.child("coach_id").getValue(String.class).equals(userid)
//                                                    && !snap.child("deleted").getValue(Boolean.class)
//                                    ) {
//                                        Log.i("",snap.child("exerciseid").getValue(String.class));
//
//                                    }
                               if(name.getText().toString().equals(snap.child("day").getValue(String.class))){
                                   list2.add(new Workout(
                                           snap.child("uuid").getValue(String.class),
                                           snap.child("workoutName").getValue(String.class),
                                           snap.child("description").getValue(String.class),
                                           snap.child("reps").getValue(String.class),
                                           snap.child("sets").getValue(String.class),
                                           snap.child("day").getValue(String.class),
                                           snap.child("user").getValue(String.class)
                                   ));
                               }
                           }

                           SharedPreferences sharedPrefs = v.getContext().getSharedPreferences("member", v.getContext().MODE_PRIVATE);
                           String userid = sharedPrefs.getString("userid", "");
                           // Display the list in AlertDialog
                           if (!isAlertDialogShown2) {
                               showWorkOutListDialog(list2, v.getContext(),userid,name.getText().toString());
                               isAlertDialogShown2 = true; // Set the flag to true after showing the AlertDialog
                           }

                       }

                       @Override
                       public void onCancelled(@NonNull @com.google.firebase.database.annotations.NotNull DatabaseError error) {
                           Log.d("FIREBASE ERR", error.getMessage());
                       }
                   });
               }
           });
//            sets = itemView.findViewById(R.id.exercise_item_sets);
//            reps = itemView.findViewById(R.id.exercise_item_reps);
//
//            deleteBtn = itemView.findViewById(R.id.exercise_item_btn_delete);
//            editBtn = itemView.findViewById(R.id.exercise_item_btn_edit);
//            infoBtn = itemView.findViewById(R.id.exercise_item_btn_info);
//
//            checkDone = itemView.findViewById(R.id.exercise_item_check);
//
//            deleteBtn.setVisibility(View.GONE);
//            editBtn.setVisibility(View.GONE);
//
//            checkDone.setEnabled(false);

        }

    }
    private void showWorkOutListDialog(List<Workout> workoutList, Context context, String userid, String day) {
        // Create an AlertDialog.Builder


        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.coach_custom_exercise_dialog, null);
        builder.setView(dialogView);

        // Get the ListView from the custom layout
        ListView listView = dialogView.findViewById(R.id.exercisesListView);

        // Create an ArrayAdapter for the ListView
        //ArrayAdapter<Exercise> adapter = new ArrayAdapter<>(context, R.layout.coach_custom_exercise_dialog, exerciseList);
        ArrayAdapter<Workout> adapter = new ArrayAdapter<Workout>(context, 0, workoutList) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                // Check if the convertView is null, if so, inflate a new layout
                if (convertView == null) {
                    convertView = LayoutInflater.from(context).inflate(R.layout.coach_custom_workout_items, parent, false);
                }

                // Get the TextViews from the custom layout
                TextView textViewExerciseId = convertView.findViewById(R.id.textViewExerciseId);
                TextView textViewExerciseDescription = convertView.findViewById(R.id.textViewExerciseDescription);
                TextView reps = convertView.findViewById(R.id.reps);
                TextView sets = convertView.findViewById(R.id.sets);

                // Set the text for each TextView based on the Exercise object
                Workout workout = workoutList.get(position);
                textViewExerciseDescription.setText("Name: " + workout.workoutName);
                textViewExerciseId.setText("Target: " + workout.description.toString());
                reps.setText("Repetition: " + workout.reps);
                sets.setText("Sets: " + workout.sets);


                return convertView;
            }
        };

        // Set the adapter for the ListView
        listView.setAdapter(adapter);

        // Set up item click listener for the ListView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Workout workout = workoutList.get(position);
                String uuid = dbwrite.push().getKey();

                // Create dialog for inputting reps and sets
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                LayoutInflater inflater = LayoutInflater.from(context);
                View dialogView = inflater.inflate(R.layout.dialog_input_reps_sets, null);
                dialogBuilder.setView(dialogView);

                EditText repsEditText = dialogView.findViewById(R.id.repsEditText);
                EditText setsEditText = dialogView.findViewById(R.id.setsEditText);

                // Add submit button
                dialogBuilder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String reps = repsEditText.getText().toString();
                        String sets = setsEditText.getText().toString();
                        db.child(workout.uuid).setValue(new Workout(
                                workout.uuid,
                                workout.workoutName,
                                workout.description,
                                repsEditText.getText().toString(),
                                setsEditText.getText().toString(),
                                workout.day,
                                userid

                        )).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
//                        progress.dismiss();
//                        Toast.makeText(AddExerciseActivity.this,"Exercise Added Successfully.",Toast.LENGTH_SHORT).show();
//                        finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("FIREBASE ERROR",""+ e.getMessage());
                                //Toast.makeText(getBaseContext(),"Exercise Adding Failed. Please Try Again",Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });

                // Create and show the AlertDialog
                AlertDialog dialog = dialogBuilder.create();
                dialog.show();
            }


        });

// Add the "Add Exercise" button
        builder.setPositiveButton("Add Exercise", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dbExercise.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @com.google.firebase.database.annotations.NotNull DataSnapshot snapshot) {
                        list3.clear();
                        for ( DataSnapshot snap : snapshot.getChildren() ) {
                            Log.i("",snap.child("exerciseid").getValue(String.class));
//                                    if (
//                                            snap.child("coach_id").getValue(String.class).equals(userid)
//                                                    && !snap.child("deleted").getValue(Boolean.class)
//                                    ) {
//                                        Log.i("",snap.child("exerciseid").getValue(String.class));
//
//                                    }
                            list3.add(new Exercise(
                                    snap.child("name").getValue(String.class),
                                    "",
                                    snap.child("description").getValue(String.class),
                                    snap.child("deleted").getValue(Boolean.class),
                                    snap.child("exerciseid").getValue(String.class)
                            ));
                        }

                        // Display the list in AlertDialog
                        if (!isAlertDialogShown) {
                            showExerciseListDialog(list3, context, userid, day);
                            isAlertDialogShown = true; // Set the flag to true after showing the AlertDialog
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @com.google.firebase.database.annotations.NotNull DatabaseError error) {
                        Log.d("FIREBASE ERR", error.getMessage());
                    }
                });
                // Add code here to handle adding an exercise
            }
        });

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
        SimpleDateFormat dateFormatsa =  new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String dayOfWeek = dateFormat.format(calendar.getTime());

        if(dayOfWeek.equals(day)){
            builder.setNeutralButton("Start Now", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {


                    //ExerciseLogSesion exerciseLogSesion = exerciseList.get(position);
                    SharedPreferences sharedPrefs = context.getSharedPreferences("member", context.MODE_PRIVATE);
                    String userid = sharedPrefs.getString("userid", "");


                    String SchedID = dateFormatsa.format(new Date()) +""+ userid;
                    for (int i = 0; i < list2.size(); i++){
                        String uuid = dbwriteExcerciseLogs.push().getKey();
                        Log.i("data: ",String.valueOf(i));
                        dbwriteExcerciseLogs.child(SchedID).child(uuid).setValue(new ExerciseLogSesion(
                                userid,
                                list2.get(i).uuid,
                                list2.get(i).workoutName,
                                day,
                                dateFormatsa.format(new Date())
                        ));

                        for (int ii = 0; ii < Integer.parseInt(list2.get(i).sets); ii++){
                           try{
                               String uuid2 = dbwriteExcerciseLogs.push().getKey();
                               Log.i("data: ",String.valueOf(ii));
                               dbwriteExcerciseLogs.child(SchedID).child(uuid).child("Activity").child(uuid2).setValue(new ExerciseLogSesion(
                                       list2.get(i).workoutName,
                                       String.valueOf(ii),
                                       list2.get(i).reps,
                                       "0"
                               ));
                           }catch (Exception e){

                           }
                        }
                    }

                }
            });

        }
        // Create and show the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showExerciseListDialog(List<Exercise> exerciseList,Context context,String userid,String day) {
        // Create an AlertDialog.Builder


        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.coach_custom_exercise_dialog, null);
        builder.setView(dialogView);

        // Get the ListView from the custom layout
        ListView listView = dialogView.findViewById(R.id.exercisesListView);

        // Create an ArrayAdapter for the ListView
        //ArrayAdapter<Exercise> adapter = new ArrayAdapter<>(context, R.layout.coach_custom_exercise_dialog, exerciseList);
        ArrayAdapter<Exercise> adapter = new ArrayAdapter<Exercise>(context, 0, exerciseList) {
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
                Exercise exercise = exerciseList.get(position);
                textViewExerciseId.setText("ID: " + exercise.exerciseid.toString());
                textViewExerciseDescription.setText("Exercise: " + exercise.name);

                return convertView;
            }
        };

        // Set the adapter for the ListView
        listView.setAdapter(adapter);

        // Set up item click listener for the ListView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Exercise selectedExercise = exerciseList.get(position);
                String uuid = dbwrite.push().getKey();
                db.child(uuid).setValue(new Workout(
                        uuid,
                    selectedExercise.name,
                    selectedExercise.description,
                    "0",
                    "0",
                        day,
                        userid

            )).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
//                        progress.dismiss();
//                        Toast.makeText(AddExerciseActivity.this,"Exercise Added Successfully.",Toast.LENGTH_SHORT).show();
//                        finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("FIREBASE ERROR",""+ e.getMessage());
                    //Toast.makeText(getBaseContext(),"Exercise Adding Failed. Please Try Again",Toast.LENGTH_SHORT).show();
                }
            });
            }
        });
// Add the "Add Exercise" button
        // Create and show the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.menu_workout_listday,parent,false);
        ExercisesAdapter.ViewHolder viewHolder = new ExercisesAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        Schedules schedule = list.get(position);

        holder.name.setText(schedule.name);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
