package com.domzky.gymbooking.Sessions.Members.pages.Programs.tabs.Exercises;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.domzky.gymbooking.Helpers.Firebase.FirebaseHelper;
import com.domzky.gymbooking.Helpers.Things.Exercise;
import com.domzky.gymbooking.Helpers.Things.Schedules;
import com.domzky.gymbooking.Helpers.Users.GymCoach;
import com.domzky.gymbooking.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ExercisesFragment extends Fragment {

    private RecyclerView recview;
    private List<Schedules> list;
    private DatabaseReference db = new FirebaseHelper().getRootReference();
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


        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                list.clear();

                for (DataSnapshot snap : snapshot.child("Schedules").getChildren()) {
                    list.add(new Schedules(
                            snap.child("id").getValue(Integer.class),
                            snap.child("name").getValue(String.class)
                    ));
                }
                recview.setAdapter(new ExercisesAdapter(list));
                recview.setLayoutManager(new LinearLayoutManager(getContext()));
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {}
        });


        return view;
    }
}