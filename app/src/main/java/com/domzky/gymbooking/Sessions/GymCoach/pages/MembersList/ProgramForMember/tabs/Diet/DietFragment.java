package com.domzky.gymbooking.Sessions.GymCoach.pages.MembersList.ProgramForMember.tabs.Diet;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.domzky.gymbooking.Helpers.Firebase.FirebaseHelper;
import com.domzky.gymbooking.Helpers.Things.Diet;
import com.domzky.gymbooking.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DietFragment extends Fragment {

    private RecyclerView recview;
    private List<Diet> list;
    private DatabaseReference db = new FirebaseHelper().getDietReference();
    private FloatingActionButton fab;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_member_programs_diet, container, false);

        recview = view.findViewById(R.id.member_programs_diet_recview);
        fab = view.findViewById(R.id.member_programs_diet_add_fab);
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
                for (DataSnapshot snap: snapshot.getChildren()) {
                    if (
                            !snap.child("deleted").getValue(Boolean.class)
                    ) {
                        list.add(new Diet(
                                snap.getKey(),
                                snap.child("coach_id").getValue(String.class),
                                snap.child("member_id").getValue(String.class),
                                snap.child("foodname").getValue(String.class),
                                snap.child("description").getValue(String.class),
                                snap.child("portions").getValue(String.class),
                                snap.child("intakeTime").getValue(String.class),
                                snap.child("member_id").getValue(Boolean.class)
                        ));
                    }
                }
                recview.setAdapter(new DietAdapter(list));
                recview.setLayoutManager(new LinearLayoutManager(getContext()));
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {}
        });

        return view;
    }
}