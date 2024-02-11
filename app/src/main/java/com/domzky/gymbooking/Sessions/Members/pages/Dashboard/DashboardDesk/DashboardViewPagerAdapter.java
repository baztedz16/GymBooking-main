package com.domzky.gymbooking.Sessions.Members.pages.Dashboard.DashboardDesk;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.domzky.gymbooking.Sessions.Members.pages.Dashboard.DashboardDesk.tabs.Booking.BookingFragment;
import com.domzky.gymbooking.Sessions.Members.pages.Dashboard.DashboardDesk.tabs.History.HistoryFragment;
import com.domzky.gymbooking.Sessions.Members.pages.Dashboard.DashboardDesk.tabs.Session.SessionFragment;
import org.jetbrains.annotations.NotNull;

public class DashboardViewPagerAdapter extends FragmentStateAdapter {
    public DashboardViewPagerAdapter(@NotNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NotNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new SessionFragment();
            case 1:
                return new BookingFragment();
            case 2:
                return new HistoryFragment();
            default:
                return new SessionFragment();
        }
    }


    @Override
    public int getItemCount() {
        return 3;
    }
}
