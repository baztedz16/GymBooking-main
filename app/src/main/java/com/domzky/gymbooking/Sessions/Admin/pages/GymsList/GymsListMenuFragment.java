package com.domzky.gymbooking.Sessions.Admin.pages.GymsList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.domzky.gymbooking.Helpers.Firebase.FirebaseHelper;
import com.domzky.gymbooking.Helpers.Users.Gym;
import com.domzky.gymbooking.Helpers.Users.GymOwner;
import com.domzky.gymbooking.R;
import com.domzky.gymbooking.Sessions.Admin.pages.GymsList.AddGym.AddGymActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GymsListMenuFragment extends Fragment {

    private RecyclerView recyclerView;
    private FloatingActionButton add_fab;

    private DatabaseReference ownersRead = new FirebaseHelper().getRootReference();

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_owners_list, container, false);

        recyclerView = view.findViewById(R.id.gym_list_menu_recview);
        add_fab = view.findViewById(R.id.gym_list_add_fab);

        List<Gym> gyms = new ArrayList<>();

        ownersRead.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                gyms.clear();
                for (DataSnapshot snap: snapshot.child("Users").child("Owners").getChildren()) {
                    gyms.add(new Gym(
                            snap.getKey(),
                            snapshot.child("Gyms").child(snap.getKey()).child("gym_name").getValue(String.class),
                            snapshot.child("Gyms").child(snap.getKey()).child("gym_address").getValue(String.class),
                            snapshot.child("Gyms").child(snap.getKey()).child("gym_activated").getValue(Boolean.class),
                            snapshot.child("Gyms").child(snap.getKey()).child("gym_status").getValue(Boolean.class),
                            new GymOwner(
                                    snap.child("fullname").getValue(String.class),
                                    snap.child("email").getValue(String.class),
                                    snap.child("phone").getValue(String.class),
                                    snap.child("username").getValue(String.class),
                                    snap.child("password").getValue(String.class)
                            )
                    ));
                }
                recyclerView.setAdapter(new GymsListAdapter(gyms,getActivity()));
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FIREBASE ERR",""+error.getMessage());
            }
        });



        add_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AddGymActivity.class));
            }
        });

        return view;
    }
}