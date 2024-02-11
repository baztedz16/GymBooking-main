package com.domzky.gymbooking.Sessions;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.domzky.gymbooking.R;
import com.domzky.gymbooking.Sessions.Members.MemberLoginActivity;

public class DeactivationActivity extends AppCompatActivity {

    private TextView textBanner;
    private Button gobackBtn;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deactivation);

        textBanner = findViewById(R.id.deactivated_banner_text);
        gobackBtn = findViewById(R.id.deactivated_button_go_back);

        if (getIntent().getStringExtra("UserType").equals("Coach")) {
            textBanner.setText("Account Deactivated. Please Contact the Gym Owner for Requesting Access.");
        } else if (getIntent().getStringExtra("UserType").equals("Staff")) {
            textBanner.setText("Account Deactivated. Please Contact the Gym Owner for Requesting Access.");
        } else if (getIntent().getStringExtra("UserType").equals("Owner")) {
            textBanner.setText("Account Deactivated. Please Contact the Administrator for Requesting Access.");
        } else if (getIntent().getStringExtra("UserType").equals("Member")) {
            textBanner.setText("Account Deactivated. Please Wait for the Member Account to Activate.");
        } else {
            textBanner.setText("Account Deactivated. Unknown User Detected,");
        }


        gobackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DeactivationActivity.this, UsersActivity.class));
                finish();
            }
        });

    }
}