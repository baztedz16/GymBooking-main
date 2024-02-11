package com.domzky.gymbooking.Sessions.GymOwner.pages.Membership;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.domzky.gymbooking.Helpers.Firebase.FirebaseHelper;
import com.domzky.gymbooking.Helpers.Things.Membership;
import com.domzky.gymbooking.R;
import com.domzky.gymbooking.Sessions.GymOwner.pages.Membership.AddMembership.AddMembershipActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MembershipListFragment extends Fragment {

    private DatabaseReference dbread = new FirebaseHelper().getRootReference();
    private DatabaseReference dbwrite = new FirebaseHelper().getRootReference();

    private RecyclerView recyclerView;
    private FloatingActionButton fab;

    private List<Membership> list;

    private SharedPreferences preferences;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_owner_membership, container, false);

        preferences = getActivity().getSharedPreferences("owner", Context.MODE_PRIVATE);
        list = new ArrayList<>();

        recyclerView = view.findViewById(R.id.membership_rec_view);
        fab = view.findViewById(R.id.membership_list_add_fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AddMembershipActivity.class));
            }
        });



        dbread.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                DataSnapshot snapMembership = snapshot.child("Memberships");
                DataSnapshot snapJoined = snapshot.child("JoinedMemberships");

                for(DataSnapshot snap: snapMembership.getChildren()) {
                    if (
                            !snap.child("deleted").getValue(Boolean.class)
                            && snap.child("gym_id").getValue(String.class).equals(preferences.getString("userid",""))
                    ) {
                        list.add(new Membership(
                                snap.getKey(),
                                snap.child("name").getValue(String.class),
                                snap.child("gym_id").getValue(String.class),
                                snap.child("price").getValue(Double.class),
                                snap.child("description").getValue(String.class),
                                getMembershipCount(snapJoined,snap.child("name").getValue(String.class),snap.getKey()),
                                snap.child("deleted").getValue(Boolean.class)
                        ));
                    }
                }
                Log.d("Membership count",String.valueOf(list.size()));
                recyclerView.setAdapter(new MembershipListAdapter(list,getContext()));
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }

    public int getMembershipCount(DataSnapshot snapshot,String name,String gym_id) {
        int count = 0;
        for (DataSnapshot snap : snapshot.child("JoinedMemberships").getChildren()) {
            if (
                    snap.child("gym_id").getValue(String.class).equals(gym_id)
                    && snap.child("name").getValue(String.class).equals(name)
            ) {
                count++;
            }
        }
        return count;
    }

}