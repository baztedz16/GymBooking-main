package com.domzky.gymbooking.Sessions.Members.pages.Programs;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.domzky.gymbooking.Sessions.Members.pages.Programs.tabs.BMI.BMIFragment;
import com.domzky.gymbooking.Sessions.Members.pages.Programs.tabs.Diet.DietFragment;
import com.domzky.gymbooking.Sessions.Members.pages.Programs.tabs.Exercises.ExercisesFragment;
import com.domzky.gymbooking.Sessions.Members.pages.Programs.tabs.WorkoutLogs.WorkoutLogsFragment;

import org.jetbrains.annotations.NotNull;

public class ProgramsViewPagerAdapter extends FragmentStateAdapter {

    public ProgramsViewPagerAdapter(@NotNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NotNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new ExercisesFragment();
            case 1:
                return new BMIFragment();
            case 2:
                return new DietFragment();
            case 3:
                return new WorkoutLogsFragment();
            default:
                return new WorkoutLogsFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
