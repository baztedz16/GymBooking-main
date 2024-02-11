package com.domzky.gymbooking.Sessions.GymCoach.pages.MembersList.ProgramForMember;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.domzky.gymbooking.Sessions.GymCoach.pages.MembersList.ProgramForMember.tabs.BMI.BMIFragment;
import com.domzky.gymbooking.Sessions.GymCoach.pages.MembersList.ProgramForMember.tabs.Diet.DietFragment;
import com.domzky.gymbooking.Sessions.GymCoach.pages.MembersList.ProgramForMember.tabs.Exercises.ExercisesFragment;
import org.jetbrains.annotations.NotNull;

public class ProgramsViewPagerAdapter extends FragmentStateAdapter {

    public ProgramsViewPagerAdapter(@NotNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @NotNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1:
                return new BMIFragment();
            case 2:
                return new ExercisesFragment();
            case 3:
                return new DietFragment();
            default:
                return new BMIFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
