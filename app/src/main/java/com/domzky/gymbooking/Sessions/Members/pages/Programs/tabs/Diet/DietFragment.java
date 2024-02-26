package com.domzky.gymbooking.Sessions.Members.pages.Programs.tabs.Diet;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.domzky.gymbooking.Helpers.Firebase.FirebaseHelper;
import com.domzky.gymbooking.Helpers.Things.BMI;
import com.domzky.gymbooking.Helpers.Things.Diet;
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
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class DietFragment extends Fragment {

    private RecyclerView recview;
    private List<Diet> list;
    private DatabaseReference db = new FirebaseHelper().getDiet();
    private FloatingActionButton fab;
    private DatabaseReference dbwrite = new FirebaseHelper().getDiet();
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_member_programs_diet, container, false);

        recview = view.findViewById(R.id.member_programs_diet_recview);
        fab = view.findViewById(R.id.member_programs_diet_add_fab);
        list = new ArrayList<>();

        //fab.setVisibility(View.GONE);

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot snap: snapshot.getChildren()) {
                    list.add(new Diet(
                            snap.getKey(),
                            snap.child("meal_name").getValue(String.class),
                            snap.child("description").getValue(String.class),
                            snap.child("calories").getValue(String.class),
                            snap.child("date").getValue(String.class),
                            snap.child("time").getValue(String.class),
                            snap.child("meal_time").getValue(String.class),
                            snap.child("userid").getValue(String.class)
                    ));
//                    if (
//                            !snap.child("deleted").getValue(Boolean.class)
//                    ) {
//
//                    }
                }
                recview.setAdapter(new DietAdapter(list));
                recview.setLayoutManager(new LinearLayoutManager(getContext()));
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {}
        });
fab.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_diet_meal, null);
        builder.setView(dialogView);

        EditText editTextCalories = dialogView.findViewById(R.id.editTextCalories);
        EditText editTextmeal = dialogView.findViewById(R.id.editTextMealName);
        EditText editTextdescription = dialogView.findViewById(R.id.editTextDescription);
        Spinner spinnerMealTime = dialogView.findViewById(R.id.spinnerMealTime);

        Button buttonPickDate = dialogView.findViewById(R.id.buttonPickDate);
        Button buttonPickTime = dialogView.findViewById(R.id.buttonPickTime);

        // Setup the spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.meal_time_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMealTime.setAdapter(adapter);
        SharedPreferences sharedPrefs = getActivity().getSharedPreferences("member", getContext().MODE_PRIVATE);
        String userid = sharedPrefs.getString("userid", "");
        String userfullname = sharedPrefs.getString("fullname", "");

        buttonPickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(buttonPickDate);
            }
        });

        buttonPickTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(buttonPickTime);
            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    // Retrieve values from the dialog and use them as needed
                    String mealName = editTextmeal.getText().toString();
                    String description = editTextdescription.getText().toString();
                    int calories = Integer.parseInt(editTextCalories.getText().toString());
                    String mealTime = spinnerMealTime.getSelectedItem().toString();
                    String date = buttonPickDate.getText().toString();
                    String time = buttonPickTime.getText().toString();
                    // Do something with the values
                    String uuid = dbwrite.push().getKey();
                    dbwrite.child(uuid).setValue(new Diet(uuid,mealName,description,String.valueOf(calories),date,time,mealTime,userid
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
                    Toast.makeText(getActivity(), "Diet Added ", Toast.LENGTH_SHORT).show();
                }catch(Exception e){
                    Log.e("Error: ",e.getMessage());
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void showDatePickerDialog(Button buttonPickDate) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        buttonPickDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }
                }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    private void showTimePickerDialog(Button buttonPickTime) {
        Log.i("String","Data");
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        buttonPickTime.setText(String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute));
                    }
                }, Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), true);

        timePickerDialog.show();

    }
});
        return view;
    }
}