package com.domzky.gymbooking.Sessions.GymCoach.pages.MembersList.ProgramForMember;

import android.annotation.SuppressLint;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.viewpager2.widget.ViewPager2;
import com.domzky.gymbooking.R;
import com.google.android.material.tabs.TabLayout;

public class ProgramForMemberActivity extends AppCompatActivity {

    private ProgramsViewPagerAdapter adapter;
    private ViewPager2 viewPager;
    private TabLayout tabLayout;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_member_programs);

        adapter = new ProgramsViewPagerAdapter(this);
        viewPager = findViewById(R.id.member_dashboard_viewpager);
        tabLayout = findViewById(R.id.member_dashboard_tablayout);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        viewPager.setAdapter(adapter);
        viewPager.setUserInputEnabled(false);

        
    }
}