package com.domzky.gymbooking.Sessions.GymCoach.pages.MembersList.ProgramForMember.tabs.BMI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.domzky.gymbooking.Helpers.Firebase.FirebaseHelper;
import com.domzky.gymbooking.Helpers.Things.BMI;
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

public class BMIFragment extends Fragment {

    private RecyclerView recview;
    private List<BMI> list;
    private DatabaseReference db = new FirebaseHelper().getRootReference();
    private FloatingActionButton fab;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_member_programs_bmi, container, false);

        recview = view.findViewById(R.id.member_programs_bmi_recview);
        fab = view.findViewById(R.id.member_programs_bmi_add_fab);
        list = new ArrayList<>();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot snap: snapshot.child("BMI").getChildren()) {
                    if (
                            !snap.child("deleted").getValue(Boolean.class)
                            && snap.child("member_id").getValue(String.class)
                                    .equals(getActivity().getSharedPreferences("member", Context.MODE_PRIVATE).getString("userid",""))
                    ) {
                        DataSnapshot snapCoach = snapshot.child("Users").child("Coaches").child(snap.child("coach_id").getValue(String.class));
                        list.add(new BMI(
                                snap.getKey(),
                                snap.child("member_id").getValue(String.class),
                                new GymCoach(
//                                        snap.child("coach_id").getValue(String.class),
                                        snapCoach.getKey(),
                                        snapCoach.child("fullname").getValue(String.class)
                                ),
                                snap.child("datetime").getValue(String.class),
                                snap.child("description").getValue(String.class),
                                snap.child("height").getValue(Double.class),
                                snap.child("weight").getValue(Double.class),
                                snap.child("heightEnglish").getValue(Boolean.class),
                                snap.child("weightEnglish").getValue(Boolean.class),
                                snap.child("deleted").getValue(Boolean.class)
                        ));
                    }
                }
                recview.setAdapter(new BMIAdapter(list));
                recview.setLayoutManager(new LinearLayoutManager(getContext()));
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {}
        });

        return view;
    }
}