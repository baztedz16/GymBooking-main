package com.domzky.gymbooking.Sessions.GymStaff;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.domzky.gymbooking.Helpers.Firebase.FirebaseHelper;
import com.domzky.gymbooking.R;
import com.domzky.gymbooking.Sessions.DeactivationActivity;
import com.domzky.gymbooking.Sessions.GymCoach.CoachLoginActivity;
import com.domzky.gymbooking.Sessions.GymCoach.CoachSessionActivity;
import com.domzky.gymbooking.Sessions.GymStaff.StaffLoginActivity;
import com.domzky.gymbooking.Sessions.GymStaff.StaffSessionActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class StaffLoginActivity extends AppCompatActivity {

    private DatabaseReference login;

    private TextView loginbanner;
    private EditText usernameField,passwordField;
    private Button loginBtn,signupBtn;
    private CheckBox showPass;

    private String username,password;

    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        progress = new ProgressDialog(StaffLoginActivity.this);
        progress.setCancelable(false);
        progress.setMessage("Logging In");

        loginbanner = findViewById(R.id.login_text_banner);

        loginbanner.setText("Gym Staff Login");

        usernameField = findViewById(R.id.login_field_username);
        passwordField = findViewById(R.id.login_field_password);
        loginBtn = findViewById(R.id.login_button_submit);
        signupBtn = findViewById(R.id.login_button_signup);

        showPass = findViewById(R.id.login_checkbtn_password);

        showPass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (!checked) {
                    passwordField.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    passwordField.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = usernameField.getText().toString().trim();
                password = passwordField.getText().toString().trim();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(getApplicationContext(),"Please Enter Form.",Toast.LENGTH_SHORT).show();
                    return;
                }

                progress.show();

                login = new FirebaseHelper().getRootReference();

                login.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snap) {
                        DataSnapshot snapshot = snap.child("Users").child("Staffs");
                        DataSnapshot snapGym = snap.child("Gyms");
                        String uid = loginGetUid(snapshot,username,password);
                        progress.dismiss();
                        if (uid==null) {
                            Toast.makeText(getApplicationContext(), "Invalid Username or Password", Toast.LENGTH_SHORT).show();
                            Log.d("BAWAL",username + ":" + password +" - Login Failed");
                        } else {
                            SharedPreferences preferences;
                            SharedPreferences.Editor editor;
                            preferences = getSharedPreferences("staff",MODE_PRIVATE);
                            editor = preferences.edit();

                            String gym_id = snapshot.child(uid).child("gym_id").getValue(String.class);

                            editor.putString("userid",uid);
                            editor.putString("gymid",snapshot.child(uid).child("gym_id").getValue(String.class));
                            editor.putString("gymname",snapGym.child(gym_id).child("gym_name").getValue(String.class));
                            editor.putString("gymaddress",snapGym.child(gym_id).child("gym_address").getValue(String.class));
                            editor.putBoolean("gymactivated",snapGym.child(gym_id).child("gym_activated").getValue(Boolean.class));
                            editor.putBoolean("gymstatus",snapGym.child(gym_id).child("gym_status").getValue(Boolean.class));
                            editor.putString("username",snapshot.child(uid).child("username").getValue(String.class));
                            editor.putString("password",snapshot.child(uid).child("password").getValue(String.class));
                            editor.putString("fullname",snapshot.child(uid).child("fullname").getValue(String.class));
                            editor.putString("phone",snapshot.child(uid).child("phone").getValue(String.class));
                            editor.putString("email",snapshot.child(uid).child("email").getValue(String.class));
                            editor.putBoolean("activated",snapshot.child(uid).child("activated").getValue(Boolean.class));
                            editor.commit();

                            logSession();

                            Log.d("PASOK",username + ":" + password +" - Login SUCCESS");

                            if (snapshot.child(uid).child("activated").getValue(Boolean.class)) {
                                startActivity(new Intent(StaffLoginActivity.this, StaffSessionActivity.class));
                            } else {
                                startActivity(new Intent(StaffLoginActivity.this, DeactivationActivity.class)
                                        .putExtra("UserType","Staff")
                                );
                            }

//                            startActivity(new Intent(StaffLoginActivity.this, StaffSessionActivity.class));
                            finishAffinity();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getApplicationContext(), "Failed to login. Please Try again.", Toast.LENGTH_LONG).show();
                        Log.e("Login ERROR",""+ error.getMessage());
                        progress.dismiss();
                    }
                });
            }
        });

        signupBtn.setVisibility(View.GONE);


    }


    private String loginGetUid(DataSnapshot snapshot,String username,String password) {
        for (DataSnapshot snap: snapshot.getChildren()) {
            String uid = snap.getKey();
            if (snap.child("username").getValue(String.class).equals(username) &&
                    snap.child("password").getValue(String.class).equals(password) ) {
                return uid;
            }
        }
        return null;
    }

    private void logSession() {
        Log.d("userid",getSharedPreferences("staff",MODE_PRIVATE).getString("userid",""));
        Log.d("gymname",getSharedPreferences("staff",MODE_PRIVATE).getString("gymname",""));
        Log.d("gymaddress",getSharedPreferences("staff",MODE_PRIVATE).getString("gymaddress",""));
        Log.d("username",getSharedPreferences("staff",MODE_PRIVATE).getString("username",""));
        Log.d("password",getSharedPreferences("staff",MODE_PRIVATE).getString("password",""));
        Log.d("fullname",getSharedPreferences("staff",MODE_PRIVATE).getString("fullname",""));
        Log.d("email",getSharedPreferences("staff",MODE_PRIVATE).getString("email",""));
        Log.d("phone",getSharedPreferences("staff",MODE_PRIVATE).getString("phone",""));
    }
}