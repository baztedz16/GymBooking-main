package com.domzky.gymbooking.Sessions.GymCoach.pages.Dashboard.DashboardDesk.tabs.History;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.domzky.gymbooking.Helpers.Firebase.FirebaseHelper;
import com.domzky.gymbooking.Helpers.Things.CoachBooking;
import com.domzky.gymbooking.R;
import com.domzky.gymbooking.Sessions.Members.pages.Dashboard.DashboardDesk.tabs.Session.CoachesSessionListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HistoryFragment extends Fragment {

    private String gym_id;
    String userid;
    private DatabaseReference db = new FirebaseHelper().getCoachBooking();

    private RecyclerView recview;
    private List<CoachBooking> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_member_dashboard_history, container, false);

        list = new ArrayList<>();
        recview = view.findViewById(R.id.historylist);

        gym_id = getActivity().getSharedPreferences("member", MODE_PRIVATE).getString("gym_id","");
        userid = getActivity().getSharedPreferences("member", MODE_PRIVATE).getString("userid","");

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot snap: snapshot.getChildren()) {
                    Log.i("Data",snap.child("coach").toString());
                    String dateString = snap.child("date").getValue(String.class);

                    // Format for yyyy-MM-dd
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

                    try {
                        Date date = sdf.parse(dateString);
                        Date currentDate = new Date(); // Current date



                        if (date != null && date.before(currentDate)) {
                            list.add(new CoachBooking(
                                    snap.getKey(),
                                    snap.child("coach").getValue(String.class),
                                    snap.child("coachName").getValue(String.class),
                                    snap.child("gym").getValue(String.class),
                                    snap.child("time").getValue(String.class),
                                    dateString, // Use the original date string
                                    snap.child("user").getValue(String.class),
                                    snap.child("status").getValue(Integer.class),
                                    getActivity().getSharedPreferences("member", MODE_PRIVATE).getString("fullname", "")
                            ));
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                SharedPreferences sharedPrefs = getActivity().getSharedPreferences("member", getContext().MODE_PRIVATE);

                recview.setAdapter(new CoachesSessionListAdapter(list,sharedPrefs));
                recview.setLayoutManager(new GridLayoutManager(getContext(),1));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("FIREBASE ERR", error.getMessage());
            }
        });


        return view;
    }
}