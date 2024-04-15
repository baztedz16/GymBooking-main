package com.domzky.gymbooking.Sessions.Members.pages.Programs.tabs.WorkoutLogs;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.domzky.gymbooking.Helpers.Firebase.FirebaseHelper;
import com.domzky.gymbooking.Helpers.Things.ExerciseLogSesion;
import com.domzky.gymbooking.Helpers.Things.Schedules;
import com.domzky.gymbooking.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

public class WorkoutLogsFragment extends Fragment {

    private RecyclerView recview;
    private List<ExerciseLogSesion> list;
    private DatabaseReference db = new FirebaseHelper().getExcerciseLogs();
    private FloatingActionButton fab;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_member_programs_exercises, container, false);

        recview = view.findViewById(R.id.member_programs_exercises_recview);
        fab = view.findViewById(R.id.member_programs_exercises_add_fab);
        list = new ArrayList<>();

        //fab.setVisibility(View.GONE);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
                String dayOfWeek = dateFormat.format(calendar.getTime());

                // Show dialog with the current day
                showDialog(dayOfWeek);
            }
        });


        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                list.clear();
                SharedPreferences sharedPrefs = getContext().getSharedPreferences("member", getContext().MODE_PRIVATE);
                String userid = sharedPrefs.getString("userid", "");

                for (DataSnapshot snap : snapshot.getChildren()) {
                    Log.i("asd",snap.toString());
                    if(snap.getKey().contains(userid)){
                        list.add(new ExerciseLogSesion(
                                snap.getKey().replace(userid,"")
                        ));
                    }

                }
                recview.setAdapter(new WorkoutLogsAdapter(list));
                recview.setLayoutManager(new LinearLayoutManager(getContext()));
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {}
        });


        return view;
    }
    private void showDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(message)
                .setTitle(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}