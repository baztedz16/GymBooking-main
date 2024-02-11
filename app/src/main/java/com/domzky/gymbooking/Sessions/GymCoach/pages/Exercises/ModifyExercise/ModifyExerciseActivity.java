package com.domzky.gymbooking.Sessions.GymCoach.pages.Exercises.ModifyExercise;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import com.domzky.gymbooking.Helpers.FieldSyntaxes.FieldValidations;
import com.domzky.gymbooking.Helpers.Firebase.FirebaseHelper;
import com.domzky.gymbooking.Helpers.Things.Exercise;
import com.domzky.gymbooking.Helpers.Things.ExerciseWorkout;
import com.domzky.gymbooking.R;
import com.domzky.gymbooking.Sessions.GymCoach.pages.Exercises.AddExercise.AddExerciseActivity;
import com.domzky.gymbooking.Sessions.GymCoach.pages.Exercises.ExercisesAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ModifyExerciseActivity extends AppCompatActivity {


    private DatabaseReference dbread = new FirebaseHelper().getExerciseReference();
    private DatabaseReference dbwrite = new FirebaseHelper().getExerciseReference();
    private DatabaseReference dbread2 = new FirebaseHelper().getWorkoutReference();
    private DatabaseReference dbwrite2 = new FirebaseHelper().getWorkoutReference();
    private List<ExerciseWorkout> list;
    private TextView banner;
    private EditText nameField,descriptionField;
    private Button addBtn,resetBtn,addWorkout;

    private String name,description,reset_name,reset_description,reset_uid;

    private SharedPreferences preferences;
    private FieldValidations fieldVal = new FieldValidations();
    private ProgressDialog progress;
    private RecyclerView recview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_exercise_form);
        list = new ArrayList<>();
        recview = findViewById(R.id.recyclerView);
        preferences = this.getSharedPreferences("coach",MODE_PRIVATE);
        progress = new ProgressDialog(ModifyExerciseActivity.this);
        progress.setCancelable(false);
        progress.setMessage("Changing Exercise");
        addWorkout = findViewById(R.id.addWorkOut);

        reset_uid = getIntent().getStringExtra("exercise_id");
        reset_name = getIntent().getStringExtra("exercise_name");
        reset_description = getIntent().getStringExtra("exercise_description");

        banner = findViewById(R.id.exercise_form_banner);
        banner.setText("Modify Exercise Form".toUpperCase(Locale.ROOT)+getIntent().getStringExtra("exercise_id"));

        nameField = findViewById(R.id.exercise_form_field_name);
        descriptionField = findViewById(R.id.exercise_form_field_description);

        addBtn = findViewById(R.id.exercise_form_button_submit);
        resetBtn = findViewById(R.id.exercise_form_button_reset);

        resetFieldsByDefault();

        addBtn = findViewById(R.id.exercise_form_button_submit);
        resetBtn = findViewById(R.id.exercise_form_button_reset);

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetFieldsByDefault();
            }
        });
        dbread2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                list.clear();
                for ( DataSnapshot snap : snapshot.getChildren() ) {
                    if (
                            snap.child("exerciseid").getValue(String.class).equals(reset_uid)
                                    && !snap.child("deleted").getValue(Boolean.class)
                    ) {
                        list.add(new ExerciseWorkout(
                                snap.child("exerciseid").getValue(String.class),
                                snap.child("coach_id").getValue(String.class),
                                snap.child("name").getValue(String.class),
                                snap.child("description").getValue(String.class),
                                snap.child("deleted").getValue(Boolean.class)
                        ));
                    }
                }
                recview.setAdapter(new ExercisesWorkoutAdapter(list,ModifyExerciseActivity.this));
                recview.setLayoutManager(new LinearLayoutManager(getBaseContext()));
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Log.d("FIREBASE ERR",error.getMessage());
            }
        });
        addBtn.setText("update Exercise");

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearAllFieldValidations();
                progress.show();
                name = nameField.getText().toString().trim();
                description = descriptionField.getText().toString().trim();


                if (checkIfFieldsAreNotDefaultState(name,description)) {
                    Toast.makeText(ModifyExerciseActivity.this,"No Changes been Made.",Toast.LENGTH_SHORT).show();
                    progress.dismiss();
                    return;
                }

                if (name.isEmpty()) {
                    nameField.setError("This Field is Required.");
                    progress.dismiss();
                    return;
                }


                dbread.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (
                                fieldVal.isExerciseNameExists(snapshot,name,preferences.getString("userid",""))
                                && !name.equals(reset_name)
                        ) {
                            nameField.setError("This Exercise Exists.");
                            progress.dismiss();
                            return;
                        }
                        dbwrite.child(reset_uid).setValue(new Exercise(
                                name,
                                preferences.getString("userid",""),
                                description,
                                false,
                                dbwrite.getKey()

                        )).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                progress.dismiss();
                                Toast.makeText(ModifyExerciseActivity.this,"Exercise Added Successfully.",Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("FIREBASE ERROR",""+ e.getMessage());
                                Toast.makeText(getBaseContext(),"Exercise Adding Failed. Please Try Again",Toast.LENGTH_SHORT).show();
                            }
                        });


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("FIREBASE ERROR",""+ error.getMessage());
                        Toast.makeText(getBaseContext(),"Exercise Adding Failed. Please Try Again",Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        addWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenAddWorkout();
            }
        });

    }


    private void resetFieldsByDefault() {
        nameField.setText(reset_name);
        descriptionField.setText(reset_description);
    }
    private void OpenAddWorkout(){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.coach_menu_addworkout);

        final EditText editText1 = dialog.findViewById(R.id.workout);
        final EditText editText2 = dialog.findViewById(R.id.repetition);
        Button btnAdd = dialog.findViewById(R.id.btnAdd);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Add your logic for handling the "Add" button click here
                String workout = editText1.getText().toString();
                String repetition = editText2.getText().toString();
                dbread2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String uuid = dbwrite2.push().getKey();

                        dbwrite2.child(uuid).setValue(new ExerciseWorkout(
                                workout,
                                preferences.getString("userid",""),
                                description,
                                false,
                                reset_uid,
                                Integer.parseInt(repetition)
                        )).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                // Close the dialog
                                //dialog.dismiss();
                                Toast.makeText(ModifyExerciseActivity.this,"Exercise Added Successfully.",Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("FIREBASE ERROR",""+ e.getMessage());
                                Toast.makeText(getBaseContext(),"Exercise Adding Failed. Please Try Again",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                // Perform any desired action with the entered values
                // For example, you can display a Toast with the values
                // Toast.makeText(MainActivity.this, "Field 1: " + value1 + "\nField 2: " + value2, Toast.LENGTH_SHORT).show();


            }
        });

        dialog.show();
    }

    private void clearAllFieldValidations() {
        nameField.setError(null);
    }

    private boolean checkIfFieldsAreNotDefaultState(String nameGet,String descriptionGet) {
        return reset_name.equals(nameGet)
                && reset_description.equals(descriptionGet);
    }



}