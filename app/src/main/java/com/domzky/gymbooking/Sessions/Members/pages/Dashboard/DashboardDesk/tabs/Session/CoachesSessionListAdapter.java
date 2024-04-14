package com.domzky.gymbooking.Sessions.Members.pages.Dashboard.DashboardDesk.tabs.Session;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.domzky.gymbooking.Helpers.Firebase.FirebaseHelper;
import com.domzky.gymbooking.Helpers.Things.CoachBooking;
import com.domzky.gymbooking.Helpers.Things.Exercise;
import com.domzky.gymbooking.Helpers.Things.ExerciseSesion;
import com.domzky.gymbooking.Helpers.Users.GymCoach;
import com.domzky.gymbooking.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CoachesSessionListAdapter extends RecyclerView.Adapter<CoachesSessionListAdapter.ViewHolder> {

    List<CoachBooking> list;
    List<Exercise> list2;

    private SharedPreferences preferences;
    private DatabaseReference dbwrite = new FirebaseHelper().getCoachBooking();
    private DatabaseReference dbwrite2 = new FirebaseHelper().getMemberSessionExcercise();
    private SharedPreferences sharedPreferences;

    private DatabaseReference dbSessionExercise = new FirebaseHelper().getMemberSessionExcercise();
    private DatabaseReference db = new FirebaseHelper().getExerciseReference();
    private DatabaseReference dbSelectedExercise= new FirebaseHelper().getWorkoutReference();
    public CoachesSessionListAdapter(List<CoachBooking> list, SharedPreferences sharedPreferences) {
        this.list = list;
        this.sharedPreferences = sharedPreferences;
    }

    @NonNull
    @Override
    public CoachesSessionListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_of_profiles_sessionitem,parent,false);
        CoachesSessionListAdapter.ViewHolder viewHolder = new CoachesSessionListAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String userid = sharedPreferences.getString("userid", "");
        String userfullname = sharedPreferences.getString("fullname", "");

        CoachBooking coachBooking = list.get(position);
        list2 = new ArrayList<>();

        //CoachBooking coachBooking = list.get(position);

        holder.fullname.setText("Coach: "+coachBooking.coachName+" "+coachBooking.coach) ;
        holder.dateTime.setText(coachBooking.date +" @ "+ coachBooking.time );

//        holder.callBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new SIMHelper(v.getContext()).callNumber(coach.phone);
//            }
//        });
//        holder.callBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Get the current date
//                Calendar calendar = Calendar.getInstance();
//                int year = calendar.get(Calendar.YEAR);
//                int month = calendar.get(Calendar.MONTH);
//                int day = calendar.get(Calendar.DAY_OF_MONTH);
//
//                // Create a DatePickerDialog to pick a date
//                DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
//                        // Use the selected date
//                        String selectedDate = selectedYear + "-" + String.format(Locale.getDefault(), "%02d", (selectedMonth + 1)) + "-" + String.format(Locale.getDefault(), "%02d", selectedDay);
//
//// Get the current time
//                        Calendar currentTime = Calendar.getInstance();
//                        int hour = currentTime.get(Calendar.HOUR_OF_DAY);
//                        int minute = currentTime.get(Calendar.MINUTE);
//
//// Create a TimePickerDialog to pick a time
//                        TimePickerDialog timePickerDialog = new TimePickerDialog(v.getContext(), new TimePickerDialog.OnTimeSetListener() {
//                            @Override
//                            public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
//                                // Use the selected time
//                                String formattedHour = String.format(Locale.getDefault(), "%02d", selectedHour);
//                                String formattedMinute = String.format(Locale.getDefault(), "%02d", selectedMinute);
//
//                                // Combine date and time
//                                String selectedDateTime = selectedDate + " " + formattedHour + ":" + formattedMinute;
//
//                                // Show the selected date and time in a dialog or perform any other action
//                                showDialogWithDateTime(userid,coachBooking.uid,coachBooking.coach,coachBooking.gym,selectedDate
//                                        , formattedHour + ":" + formattedMinute, v.getContext(),userfullname);
//                            }
//                        }, hour, minute, false);
//                        timePickerDialog.show(); // false for 12-hour format, true for 24-hour format
//                        timePickerDialog.show();
//                    }
//                }, year, month, day);
//                datePickerDialog.show();
//            }
//        });

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCurrentActivity(coachBooking.user,coachBooking.uid,coachBooking.coach,coachBooking.gym, v.getContext(),coachBooking.userFullname);

            }
        });

    }
    // Method to show the selected date and time in a dialog
    private void showCurrentActivity(String userid,String coach,String coachName,String gym, Context context,String userFullname) {
        // Create an array of items for the list
        List<String> itemsList = new ArrayList<>();
        List<String> itemsListID = new ArrayList<>();
        dbSessionExercise.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
               try {
                   itemsList.clear();
                   itemsListID.clear();
                   for ( DataSnapshot snap : snapshot.getChildren() ) {
                       if (coachName.equals(snap.child("coach_id").getValue(String.class)) ){
                           Log.i("",snap.child("excerciseName").getValue(String.class));
                           itemsList.add(snap.child("excerciseName").getValue(String.class));
                           itemsListID.add(snap.child("exerciseid").getValue(String.class));
                       }
                   }
               }catch (Exception e){
                   System.out.println(e);
               }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Log.d("FIREBASE ERR", error.getMessage());
            }
        });

        // Create an ArrayAdapter for the ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, itemsList);

        // Set up the ListView
        ListView listView = new ListView(context);
        listView.setAdapter(adapter);

        // Set up the AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Current Activity")
                .setView(listView)
                .setPositiveButton("Add Excercise", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Handle positive button click
                        db.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                list2.clear();
                                for ( DataSnapshot snap : snapshot.getChildren() ) {
                                    Log.i("",snap.child("exerciseid").getValue(String.class));
//                                    if (
//                                            snap.child("coach_id").getValue(String.class).equals(userid)
//                                                    && !snap.child("deleted").getValue(Boolean.class)
//                                    ) {
//                                        Log.i("",snap.child("exerciseid").getValue(String.class));
//
//                                    }
                                    list2.add(new Exercise(
                                            snap.child("exerciseid").getValue(String.class),
                                            coachName,
                                            snap.child("name").getValue(String.class),
                                            snap.child("description").getValue(String.class),
                                            snap.child("deleted").getValue(Boolean.class)
                                    ));
                                }

                                // Display the list in AlertDialog
                                showExerciseListDialog(list2,context,userid,userFullname);
                            }

                            @Override
                            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                                Log.d("FIREBASE ERR", error.getMessage());
                            }
                        });
                    }
                })
                .setNegativeButton("Cancel Session", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Handle negative button click
                        showToast("Negative button clicked",context);
                    }
                })
                .setNeutralButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Handle neutral button click
                        showToast("Neutral button clicked",context);
                    }
                });

        // Create and show the AlertDialog
        final AlertDialog dialog = builder.create();

        // Set up item click listener for the ListView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Handle item click
                showToast("Selected: " + itemsListID.get(position),context);
                showSelectedActivity(itemsListID.get(position).toString(),itemsList.get(position).toString(),context);
                dialog.dismiss(); // Dismiss the dialog when an item is selected
            }
        });
// Set the custom style for buttons
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                dialog.getButton(DialogInterface.BUTTON_POSITIVE).setBackgroundResource(R.drawable.btn_pos);
                dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setBackgroundResource(R.drawable.btn_neg);
                //dialog.getButton(DialogInterface.BUTTON_NEUTRAL).setBackgroundResource(R.drawable.custom_button_background);
            }
        });
        // Show the AlertDialog
        dialog.show();
    }
    private void showSelectedActivity(String excerciseid,String excerciseName,Context context) {
        // Create an array of items for the list
        List<String> itemsList = new ArrayList<>();
        List<String> itemsListID = new ArrayList<>();
        dbSelectedExercise.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                itemsList.clear();
                itemsListID.clear();
                for ( DataSnapshot snap : snapshot.getChildren() ) {
                    Log.i("",snap.child("name").getValue(String.class));
                    if(snap.child("exerciseid").getValue(String.class).equals(excerciseid)){
                        itemsList.add(snap.child("name").getValue(String.class));
                        itemsListID.add(snap.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Log.d("FIREBASE ERR", error.getMessage());
            }
        });

        // Create an ArrayAdapter for the ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, itemsList);

        // Set up the ListView
        ListView listView = new ListView(context);
        listView.setAdapter(adapter);

        // Set up the AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Exerceise: "+excerciseName)
                .setView(listView)
                .setPositiveButton("Add Excercise", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Handle positive button click
//                        db.addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
//                                list2.clear();
//                                for ( DataSnapshot snap : snapshot.getChildren() ) {
//                                    Log.i("",snap.child("exerciseid").getValue(String.class));
////                                    if (
////                                            snap.child("coach_id").getValue(String.class).equals(userid)
////                                                    && !snap.child("deleted").getValue(Boolean.class)
////                                    ) {
////                                        Log.i("",snap.child("exerciseid").getValue(String.class));
////
////                                    }
//                                    list2.add(new Exercise(
//                                            snap.child("exerciseid").getValue(String.class),
//                                            snap.child("coach_id").getValue(String.class),
//                                            snap.child("name").getValue(String.class),
//                                            snap.child("description").getValue(String.class),
//                                            snap.child("deleted").getValue(Boolean.class)
//                                    ));
//                                }
//
//                                // Display the list in AlertDialog
//                                showExerciseListDialog(list2,context,userid,userFullname);
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull @NotNull DatabaseError error) {
//                                Log.d("FIREBASE ERR", error.getMessage());
//                            }
//                        });
                    }
                })
                .setNegativeButton("Cancel Session", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Handle negative button click
                        showToast("Negative button clicked",context);
                    }
                })
                .setNeutralButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Handle neutral button click
                        showToast("Neutral button clicked",context);
                    }
                });

        // Create and show the AlertDialog
        final AlertDialog dialog = builder.create();

        // Set up item click listener for the ListView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Handle item click
                showToast("Selected: " + itemsListID.get(position),context);
                dialog.dismiss(); // Dismiss the dialog when an item is selected
            }
        });
// Set the custom style for buttons
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                dialog.getButton(DialogInterface.BUTTON_POSITIVE).setBackgroundResource(R.drawable.btn_pos);
                dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setBackgroundResource(R.drawable.btn_neg);
                //dialog.getButton(DialogInterface.BUTTON_NEUTRAL).setBackgroundResource(R.drawable.custom_button_background);
            }
        });
        // Show the AlertDialog
        dialog.show();
    }

    private void showExerciseListDialog(List<Exercise> exerciseList,Context context,String userid,String userfullname) {
        // Create an AlertDialog.Builder


        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.coach_custom_exercise_dialog, null);
        builder.setView(dialogView);

        // Get the ListView from the custom layout
        ListView listView = dialogView.findViewById(R.id.exercisesListView);

        // Create an ArrayAdapter for the ListView
        //ArrayAdapter<Exercise> adapter = new ArrayAdapter<>(context, R.layout.coach_custom_exercise_dialog, exerciseList);
        ArrayAdapter<Exercise> adapter = new ArrayAdapter<Exercise>(context, 0, exerciseList) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                // Check if the convertView is null, if so, inflate a new layout
                if (convertView == null) {
                    convertView = LayoutInflater.from(context).inflate(R.layout.coach_custom_exercise_items, parent, false);
                }

                // Get the TextViews from the custom layout
                TextView textViewExerciseId = convertView.findViewById(R.id.textViewExerciseId);
                TextView textViewExerciseDescription = convertView.findViewById(R.id.textViewExerciseDescription);

                // Set the text for each TextView based on the Exercise object
                Exercise exercise = exerciseList.get(position);
                textViewExerciseId.setText("ID: " + exercise.exerciseid.toString());
                textViewExerciseDescription.setText("Exercise: " + exercise.name);

                return convertView;
            }
        };

        // Set the adapter for the ListView
        listView.setAdapter(adapter);

        // Set up item click listener for the ListView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Exercise selectedExercise = exerciseList.get(position);
                String uuid = dbwrite.push().getKey();
                dbwrite2.child(uuid).setValue(new ExerciseSesion(
                        userid,
                        selectedExercise.coach_id,
                        selectedExercise.description,
                        false,
                        selectedExercise.exerciseid,
                        userfullname,
                        selectedExercise.name

                )).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
//                        progress.dismiss();
//                        Toast.makeText(AddExerciseActivity.this,"Exercise Added Successfully.",Toast.LENGTH_SHORT).show();
//                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("FIREBASE ERROR",""+ e.getMessage());
                        //Toast.makeText(getBaseContext(),"Exercise Adding Failed. Please Try Again",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        // Create and show the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

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
    private void showToast(String message, Context context) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
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
                                0,userfullname

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
        public ImageButton view,approved;
        public ViewHolder(View itemView) {
            super(itemView);
            fullname = itemView.findViewById(R.id.list_of_profiles_button_fullname);

            dateTime = itemView.findViewById(R.id.dateTime);
            view = itemView.findViewById(R.id.list_of_profiles_button_call);
            approved = itemView.findViewById(R.id.list_of_profiles_button_text);
        }

    }
}
