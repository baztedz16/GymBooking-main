package com.domzky.gymbooking.Sessions.GymCoach.pages.MembersList;

import android.content.Context;
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
import com.domzky.gymbooking.Helpers.Users.GymMember;
import com.domzky.gymbooking.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class MembersListFragment extends Fragment {

    private RecyclerView recview;
    private DatabaseReference db = new FirebaseHelper().getUserReference("Members");
    private List<GymMember> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_coach_members_list, container, false);

        recview = view.findViewById(R.id.coach_menu_members_recview);
        list = new ArrayList<>();
        Log.d("Gym_ID",getActivity().getSharedPreferences("coach", MODE_PRIVATE)
                .getString("gymid",""));

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    if (
                           snap.child("Membership").exists() &&
                           snap.child("Membership").child("gym_id").getValue(String.class)
                                .equals(
                                    getActivity().getSharedPreferences("coach", MODE_PRIVATE)
                                        .getString("gymid","")
                                )
                    ) {
                        list.add(new GymMember(
                                snap.getKey(),
                                snap.child("fullname").getValue(String.class)
                    ));

                    }
                }
                recview.setAdapter(new MembersListAdapter(list));
                recview.setLayoutManager(new LinearLayoutManager(getContext()));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FIREBASE ERR",error.getMessage());
            }
        });

        return view;
    }
}