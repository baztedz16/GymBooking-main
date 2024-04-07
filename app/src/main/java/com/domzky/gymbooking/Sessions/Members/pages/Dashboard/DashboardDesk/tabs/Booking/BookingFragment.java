package com.domzky.gymbooking.Sessions.Members.pages.Dashboard.DashboardDesk.tabs.Booking;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.domzky.gymbooking.Helpers.Firebase.FirebaseHelper;
import com.domzky.gymbooking.Helpers.Users.GymCoach;
import com.domzky.gymbooking.R;
import com.domzky.gymbooking.Sessions.Members.pages.CoachesList.CoachesListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BookingFragment extends Fragment {

    private LayoutInflater layoutInflater;
    private ViewGroup viewGroup;
    private RecyclerView recview;
    private List<GymCoach> list;
    private String gym_id,member;
    private DatabaseReference db = new FirebaseHelper().getUserReference("Coaches");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_member_dashboard_booking, container, false);

        list = new ArrayList<>();
        recview = view.findViewById(R.id.CoachListingRec);

        gym_id = getActivity().getSharedPreferences("member", MODE_PRIVATE).getString("gym_id","");

        member = getActivity().getSharedPreferences("member", MODE_PRIVATE).getString("member_type","");

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot snap: snapshot.getChildren()) {
                    Log.i("Member Type",member);
                    if (
                            snap.child("activated").getValue(Boolean.class) &&
                                    snap.child("gym_id").getValue(String.class).equals(gym_id)
                    ) {
                        if(member.equals("Member")){
                            if(!snap.child("fullname").getValue(String.class).equals("gym schedule")){
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

                        }else{
                            if(snap.child("fullname").getValue(String.class).equals("gym schedule")){
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
                    }
                }
                SharedPreferences sharedPrefs = getActivity().getSharedPreferences("member", getContext().MODE_PRIVATE);

                recview.setAdapter(new CoachesBookingListAdapter(list,sharedPrefs));
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