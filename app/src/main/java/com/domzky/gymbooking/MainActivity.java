package com.domzky.gymbooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.domzky.gymbooking.Helpers.FieldSyntaxes.DateTimeHelper;
import com.domzky.gymbooking.Helpers.Firebase.FirebaseHelper;
import com.domzky.gymbooking.Sessions.Admin.AdminSignupActivity;
import com.domzky.gymbooking.Sessions.UsersActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    DatabaseReference checkAdmin = new FirebaseHelper().getUserReference("Admins");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        Log.d("Today's Date", Calendar.getInstance().getTime().toString());
        Log.d("System's Date", String.valueOf(System.currentTimeMillis()));
        Log.d("Instance Date",new DateTimeHelper().getNowDateTime());

        checkAdmin.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    startActivity(new Intent(MainActivity.this, UsersActivity.class));
                    finishAffinity();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Application Setup");
                    builder.setMessage("This requires an Admin to setup the Platform.\nPlease Sign Up as Admin to Continue.");
                    builder.setCancelable(false);
                    builder.setNegativeButton("Sign In", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startActivity(new Intent(MainActivity.this, AdminSignupActivity.class));
                            finishAffinity();
                        }
                    });
                    builder.create().show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FIREBASE ERR",""+error.getMessage());
                Toast.makeText(getApplicationContext(),"Application Failed to Open.",Toast.LENGTH_SHORT).show();
                finishAffinity();
            }
        });
    }
}