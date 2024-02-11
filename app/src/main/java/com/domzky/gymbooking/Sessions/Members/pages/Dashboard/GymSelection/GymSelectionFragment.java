package com.domzky.gymbooking.Sessions.Members.pages.Dashboard.GymSelection;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.domzky.gymbooking.Helpers.Firebase.FirebaseHelper;
import com.domzky.gymbooking.Helpers.Users.Gym;
import com.domzky.gymbooking.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GymSelectionFragment extends Fragment {

    private RecyclerView recview;

    private DatabaseReference db = new FirebaseHelper().getGymReference();

    private List<Gym> list;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_member_gyms_list, container, false);

        recview = view.findViewById(R.id.member_gymslist_recview);
        list = new ArrayList<>();

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    if (snap.child("gym_activated").getValue(Boolean.class)) {
                        list.add(new Gym(
                                snap.getKey(),
                                snap.child("gym_name").getValue(String.class),
                                snap.child("gym_address").getValue(String.class),
                                snap.child("gym_activated").getValue(Boolean.class),
                                snap.child("gym_status").getValue(Boolean.class)
                                ));
                    }
                }

                recview.setAdapter(new GymSelectionAdapter(list,getActivity()));
                recview.setLayoutManager(new LinearLayoutManager(getContext()));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("FIREBASE ERR",""+ error.getMessage());
            }
        });

        return view;
    }
}