package com.domzky.gymbooking.Sessions.GymStaff.pages.TasksList;

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
import com.domzky.gymbooking.Helpers.Things.CoachBooking;
import com.domzky.gymbooking.Helpers.Users.GymCoach;
import com.domzky.gymbooking.R;
import com.domzky.gymbooking.Sessions.Members.pages.Dashboard.DashboardDesk.tabs.Booking.CoachesBookingListAdapter;
import com.domzky.gymbooking.Sessions.Members.pages.Dashboard.DashboardDesk.tabs.Session.CoachesSessionListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TasksListFragment extends Fragment {
    private RecyclerView recview;
    private List<CoachBooking> list;
    private String gym_id;
    String userid;
    private DatabaseReference db = new FirebaseHelper().getCoachBooking();
    private SharedPreferences sharedPreferences;

    private boolean isSameDay(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
                cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_staff_tasks_list, container, false);
        list = new ArrayList<>();
        recview = view.findViewById(R.id.staffTask);

        gym_id = getActivity().getSharedPreferences("member", MODE_PRIVATE).getString("gym_id","");


        gym_id = getActivity().getSharedPreferences("member", MODE_PRIVATE).getString("gym_id","");
        userid = getActivity().getSharedPreferences("member", MODE_PRIVATE).getString("userid","");
        Log.i("Data",userid+"Data");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot snap: snapshot.getChildren()) {
                    Log.i("Data",snap.child("coach").toString());
                    String dateString = snap.child("date").getValue(String.class);
                    int status = snap.child("status").getValue(Integer.class);
                    // Format for yyyy-MM-dd
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

                    try {
                        Date date = sdf.parse(dateString);
                        Date currentDate = new Date(); // Current date



                        if (date != null && date.after(currentDate)  || isSameDay(date, currentDate) && status == 0) {
                            list.add(new CoachBooking(
                                    snap.getKey(),
                                    snap.child("coach").getValue(String.class),
                                    snap.child("coachName").getValue(String.class),
                                    snap.child("gym").getValue(String.class),
                                    snap.child("time").getValue(String.class),
                                    dateString, // Use the original date string
                                    snap.child("user").getValue(String.class),
                                    snap.child("status").getValue(Integer.class),
                                    snap.child("userFullname").getValue(String.class)
                            ));
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                SharedPreferences sharedPrefs = getActivity().getSharedPreferences("member", getContext().MODE_PRIVATE);

                recview.setAdapter(new staffTaskAdapter(list,sharedPrefs));
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