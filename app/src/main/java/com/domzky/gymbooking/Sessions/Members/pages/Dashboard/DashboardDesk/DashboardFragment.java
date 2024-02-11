package com.domzky.gymbooking.Sessions.Members.pages.Dashboard.DashboardDesk;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.viewpager2.widget.ViewPager2;
import com.domzky.gymbooking.R;
import com.google.android.material.tabs.TabLayout;

public class DashboardFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private DashboardViewPagerAdapter dashboardViewPagerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_member_dashboard, container, false);

        tabLayout = view.findViewById(R.id.member_dashboard_tablayout);
        viewPager = view.findViewById(R.id.member_dashboard_viewpager);

        dashboardViewPagerAdapter = new DashboardViewPagerAdapter(getActivity());

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                Log.i("Selected","Errors");
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        viewPager.setAdapter(dashboardViewPagerAdapter);
        viewPager.setUserInputEnabled(false);

        return view;
    }
}