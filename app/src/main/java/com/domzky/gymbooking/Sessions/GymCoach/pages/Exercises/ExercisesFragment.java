package com.domzky.gymbooking.Sessions.GymCoach.pages.Exercises;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.domzky.gymbooking.Helpers.Firebase.FirebaseHelper;
import com.domzky.gymbooking.Helpers.Things.Exercise;
import com.domzky.gymbooking.R;
import com.domzky.gymbooking.Sessions.GymCoach.pages.Exercises.AddExercise.AddExerciseActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class ExercisesFragment extends Fragment {

    private List<Exercise> list;

    private DatabaseReference db = new FirebaseHelper().getExerciseReference();

    private SharedPreferences preferences;
    private RecyclerView recview;
    private FloatingActionButton addBtn;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_coach_exercises, container, false);

        preferences = getActivity().getSharedPreferences("coach",MODE_PRIVATE);

        list = new ArrayList<>();

        recview = view.findViewById(R.id.coach_menu_exercise_recview);
        addBtn = view.findViewById(R.id.coach_exercise_list_add_fab);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddExerciseActivity.class));
            }
        });

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                list.clear();
                for ( DataSnapshot snap : snapshot.getChildren() ) {
                    if (
                            snap.child("coach_id").getValue(String.class).equals(preferences.getString("userid",""))
                            && !snap.child("deleted").getValue(Boolean.class)
                    ) {
                        list.add(new Exercise(
                                snap.child("exerciseid").getValue(String.class),
                                snap.child("coach_id").getValue(String.class),
                                snap.child("name").getValue(String.class),
                                snap.child("description").getValue(String.class),
                                snap.child("deleted").getValue(Boolean.class)
                        ));
                    }
                }
                recview.setAdapter(new ExercisesAdapter(list,getActivity()));
                recview.setLayoutManager(new LinearLayoutManager(getContext()));
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Log.d("FIREBASE ERR",error.getMessage());
            }
        });

//
//        recview.setAdapter(new ExercisesAdapter(list));
//        recview.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }
}