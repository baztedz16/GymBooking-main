package com.domzky.gymbooking.Sessions.GymOwner.pages.Membership.ShowJoined;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;

import com.domzky.gymbooking.R;

public class ShowJoinedmembershipActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private String bannerName,membershipUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_show_joinedmembership);

        bannerName = getIntent().getStringExtra("membership_name");
        membershipUid = getIntent().getStringExtra("membership_uid");

        toolbar = findViewById(R.id.joined_adapter_toolbar);
        toolbar.setTitle(bannerName + " Membership");
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_baseline_arrow_back_24));
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishAffinity();
            }
        });



    }
}