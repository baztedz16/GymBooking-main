package com.domzky.gymbooking.Sessions.Members.pages.CoachesList;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Context;
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

import com.domzky.gymbooking.Helpers.Firebase.FirebaseHelper;
import com.domzky.gymbooking.Helpers.Users.GymCoach;
import com.domzky.gymbooking.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CoachesListFragment extends Fragment {

    private RecyclerView recview;
    private List<GymCoach> list;

    private String gym_id;
    private DatabaseReference db = new FirebaseHelper().getUserReference("Coaches");

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_member_coaches_list, container, false);

        list = new ArrayList<>();
        recview = view.findViewById(R.id.member_coaches_list_recview);

        gym_id = getActivity().getSharedPreferences("member", MODE_PRIVATE).getString("gym_id","");

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot snap: snapshot.getChildren()) {
                    if (
                            snap.child("activated").getValue(Boolean.class) &&
                            snap.child("gym_id").getValue(String.class).equals(gym_id)
                    ) {
                        list.add(new GymCoach(
                                snap.getKey(),
                                snap.child("fullname").getValue(String.class),
                                snap.child("email").getValue(String.class),
                                snap.child("phone").getValue(String.class),
                                snap.child("username").getValue(String.class),
                                snap.child("password").getValue(String.class),
                                snap.child("activated").getValue(Boolean.class),
                                snap.child("gym_id").getValue(String.class)
                        ));
                    }
                }
                recview.setAdapter(new CoachesListAdapter(list));
                recview.setLayoutManager(new GridLayoutManager(getContext(),2));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("FIREBASE ERR", error.getMessage());
            }
        });
        return view;
    }
}