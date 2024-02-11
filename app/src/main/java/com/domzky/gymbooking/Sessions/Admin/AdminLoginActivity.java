package com.domzky.gymbooking.Sessions.Admin;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class AdminLoginActivity extends AppCompatActivity {


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


        progress = new ProgressDialog(AdminLoginActivity.this);
        progress.setCancelable(false);
        progress.setMessage("Logging In");

        loginbanner = findViewById(R.id.login_text_banner);

        loginbanner.setText("Administrator Login");

        usernameField = findViewById(R.id.login_field_username);
        passwordField = findViewById(R.id.login_field_password);
        loginBtn = findViewById(R.id.login_button_submit);
        signupBtn = findViewById(R.id.login_button_signup);

        signupBtn.setVisibility(View.GONE);

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

                login = new FirebaseHelper().getUserReference("Admins");

                login.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String uid = loginGetUid(snapshot,username,password);
                        if (uid==null) {
                            Toast.makeText(getApplicationContext(), "Invalid Username or Password", Toast.LENGTH_SHORT).show();
                            Log.d("BAWAL",username + ":" + password +" - Login Failed");
                        } else {
                            SharedPreferences preferences;
                            SharedPreferences.Editor editor;
                            preferences = getSharedPreferences("admin",MODE_PRIVATE);
                            editor = preferences.edit();
                            editor.putString("userid",uid);
                            editor.putString("username",snapshot.child(uid).child("username").getValue(String.class));
                            editor.putString("password",snapshot.child(uid).child("password").getValue(String.class));
                            editor.putString("fullname",snapshot.child(uid).child("fullname").getValue(String.class));
                            editor.putString("phone",snapshot.child(uid).child("phone").getValue(String.class));
                            editor.putString("email",snapshot.child(uid).child("email").getValue(String.class));
                            editor.commit();

                            logSession();

                            Log.d("PASOK",username + ":" + password +" - Login SUCCESS");

                            startActivity(new Intent(AdminLoginActivity.this, AdminSessionActivity.class));
                            finishAffinity();
                        }
                        progress.dismiss();
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
        Log.d("userid",getSharedPreferences("admin",MODE_PRIVATE).getString("userid",""));
        Log.d("username",getSharedPreferences("admin",MODE_PRIVATE).getString("username",""));
        Log.d("password",getSharedPreferences("admin",MODE_PRIVATE).getString("password",""));
        Log.d("fullname",getSharedPreferences("admin",MODE_PRIVATE).getString("fullname",""));
        Log.d("email",getSharedPreferences("admin",MODE_PRIVATE).getString("email",""));
        Log.d("phone",getSharedPreferences("admin",MODE_PRIVATE).getString("phone",""));
    }

}