package com.domzky.gymbooking.Sessions.Members.pages.Programs.tabs.BMI;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.domzky.gymbooking.Helpers.Firebase.FirebaseHelper;
import com.domzky.gymbooking.Helpers.Things.BMI;
import com.domzky.gymbooking.Helpers.Things.CoachBooking;
import com.domzky.gymbooking.Helpers.Users.GymCoach;
import com.domzky.gymbooking.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BMIFragment extends Fragment {
    private SharedPreferences sharedPreferences;
    private RecyclerView recview;
    private List<BMI> list;
    private DatabaseReference db = new FirebaseHelper().getRootReference();
    private FloatingActionButton fab;
    private DatabaseReference dbwrite = new FirebaseHelper().getBMIRecords();
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_member_programs_bmi, container, false);
        //this.sharedPreferences = sharedPreferences;
        SharedPreferences sharedPrefs = getActivity().getSharedPreferences("member", getContext().MODE_PRIVATE);
        recview = view.findViewById(R.id.member_programs_bmi_recview);
        fab = view.findViewById(R.id.member_programs_bmi_add_fab);
        list = new ArrayList<>();
        String userid = sharedPrefs.getString("userid", "");
        String userfullname = sharedPrefs.getString("fullname", "");
        //fab.setVisibility(View.GONE);

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot snap: snapshot.child("BMIRecords").getChildren()) {
                    if (
                            !snap.child("deleted").getValue(Boolean.class)
                            && snap.child("member_id").getValue(String.class)
                                    .equals(getActivity().getSharedPreferences("member", Context.MODE_PRIVATE).getString("userid",""))
                    ) {
                        DataSnapshot snapCoach = snapshot.child("Users").child("Coaches").child(snap.child("coach_id").getValue(String.class));
                        list.add(new BMI(
                                snap.child("member_id").getValue(String.class),
                                snap.child("coach_id").getValue(String.class),
                                snap.child("datetime").getValue(String.class),
                                snap.child("description").getValue(String.class),
                                snap.child("height").getValue(Double.class),
                                snap.child("weight").getValue(Double.class),
                                snap.child("deleted").getValue(Boolean.class),
                                snap.child("bmi").getValue(String.class)
                        ));
                    }
                }
                recview.setAdapter(new BMIAdapter(list));
                recview.setLayoutManager(new LinearLayoutManager(getContext()));
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {}
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create and show dialog with custom layout
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()); // Use getActivity() for Fragment or use this for Activity

                // Inflate the custom layout for the dialog
                LayoutInflater inflater = requireActivity().getLayoutInflater(); // Use getActivity() for Fragment or use this for Activity
                View dialogView = inflater.inflate(R.layout.dialog_add_bmi, null);

                // Find views in the custom layout
                EditText heightEditText = dialogView.findViewById(R.id.heightEditText);
                EditText weightEditText = dialogView.findViewById(R.id.weightEditText);
                DatePicker datePicker = dialogView.findViewById(R.id.datePicker);

                builder.setView(dialogView)
                        .setTitle("Enter BMI Details")
                        .setPositiveButton("Compute", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    // Retrieve input from the fields
                                    double height = Integer.parseInt(heightEditText.getText().toString());
                                    double weight = Integer.parseInt(weightEditText.getText().toString());

                                    // Retrieve date from DatePicker
                                    int day = datePicker.getDayOfMonth();
                                    int month = datePicker.getMonth() + 1; // Months start from 0
                                    int year = datePicker.getYear();

                                    // Calculate BMI
                                    double heightInMeters = height / 100.0; // Convert height to meters
                                    double bmi = weight / (heightInMeters * heightInMeters);


                                    String uuid = dbwrite.push().getKey();
                                    dbwrite.child(uuid).setValue(new BMI(userid,"",String.valueOf(month)+'/'+String.valueOf(day)+'/'+String.valueOf(+year),
                                            "My BMI",height,weight,false,String.valueOf(bmi)
                                    )).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e("FIREBASE ERROR",""+ e.getMessage());

                                        }
                                    });
                                    Toast.makeText(getActivity(), "BMI: " + bmi, Toast.LENGTH_SHORT).show();
                                }catch (Exception e){
                                    Log.i("Error",e.getMessage());
                                }
                                // Show BMI or perform further actions

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                builder.show();
            }
        });
        return view;
    }
}