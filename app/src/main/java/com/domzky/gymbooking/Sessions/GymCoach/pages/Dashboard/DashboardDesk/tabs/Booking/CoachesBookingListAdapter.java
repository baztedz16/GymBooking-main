package com.domzky.gymbooking.Sessions.GymCoach.pages.Dashboard.DashboardDesk.tabs.Booking;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.domzky.gymbooking.Helpers.Firebase.FirebaseHelper;
import com.domzky.gymbooking.Helpers.Things.CoachBooking;
import com.domzky.gymbooking.Helpers.Users.GymCoach;
import com.domzky.gymbooking.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CoachesBookingListAdapter extends RecyclerView.Adapter<CoachesBookingListAdapter.ViewHolder> {

    List<GymCoach> list;
    private SharedPreferences preferences;
    private DatabaseReference dbwrite = new FirebaseHelper().getCoachBooking();
    private SharedPreferences sharedPreferences;
    public CoachesBookingListAdapter(List<GymCoach> list,SharedPreferences sharedPreferences) {
        this.list = list;
        this.sharedPreferences = sharedPreferences;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_of_profiles_sessionitem,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String userid = sharedPreferences.getString("userid", "");
        String userfullname = sharedPreferences.getString("fullname", "");

        GymCoach coach = list.get(position);

        holder.fullname.setText(coach.fullname);

//        holder.callBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new SIMHelper(v.getContext()).callNumber(coach.phone);
//            }
//        });
        holder.callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the current date
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                // Create a DatePickerDialog to pick a date
                DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                        // Use the selected date
                        String selectedDate = selectedYear + "-" + String.format(Locale.getDefault(), "%02d", (selectedMonth + 1)) + "-" + String.format(Locale.getDefault(), "%02d", selectedDay);

// Get the current time
                        Calendar currentTime = Calendar.getInstance();
                        int hour = currentTime.get(Calendar.HOUR_OF_DAY);
                        int minute = currentTime.get(Calendar.MINUTE);

// Create a TimePickerDialog to pick a time
                        TimePickerDialog timePickerDialog = new TimePickerDialog(v.getContext(), new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
                                // Use the selected time
                                String formattedHour = String.format(Locale.getDefault(), "%02d", selectedHour);
                                String formattedMinute = String.format(Locale.getDefault(), "%02d", selectedMinute);

                                // Combine date and time
                                String selectedDateTime = selectedDate + " " + formattedHour + ":" + formattedMinute;

                                // Show the selected date and time in a dialog or perform any other action
                                showDialogWithDateTime(userid,coach.uid,coach.fullname,coach.gym_id,selectedDate, formattedHour + ":" + formattedMinute, v.getContext(),userfullname);
                            }
                        }, hour, minute, false);
                        timePickerDialog.show(); // false for 12-hour format, true for 24-hour format
                        timePickerDialog.show();
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });



    }
    // Method to show the selected date and time in a dialog
    private void showDialogWithDateTime(String userid,String coach,String coachName,String gym,String date, String time, Context context,String userfullname) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Selected Date and Time: " + date + " " + time)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Do something when the user clicks Yes, e.g., book the schedule
                        bookSchedule(userid,coach,coachName,gym,date, time, context,userfullname);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Do something when the user clicks No, e.g., cancel the booking process
                        dialog.dismiss(); // Close the dialog
                    }
                });
        builder.create().show();
    }
    private void bookSchedule(String userid,String coach,String coachName,String gym,String date, String time, Context context,String userfullname) {
        // Implement the booking logic here
        // For example, you can send a network request to book the selected schedule
        // After booking, you can show a success message or perform any other action

        // For now, let's show a simple success message
        AlertDialog.Builder successBuilder = new AlertDialog.Builder(context);
        successBuilder.setMessage("Schedule booked successfully! "+coach+ " @ the Gym: "+gym)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Do something when the user clicks OK
                        String uuid = dbwrite.push().getKey();
                        dbwrite.child(uuid).setValue(new CoachBooking(
                                coach,
                                coachName,
                                gym,
                                time,
                                date,
                                userid,
                                0,
                                userfullname
                        )).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("FIREBASE ERROR",""+ e.getMessage());

                            }
                        });
                    }
                });
        successBuilder.create().show();
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView fullname,dateTime;
        public ImageButton callBtn,textBtn;
        public ViewHolder(View itemView) {
            super(itemView);
            fullname = itemView.findViewById(R.id.list_of_profiles_button_fullname);

            dateTime = itemView.findViewById(R.id.dateTime);
            callBtn = itemView.findViewById(R.id.list_of_profiles_button_call);
            textBtn = itemView.findViewById(R.id.list_of_profiles_button_text);
        }

    }
}
