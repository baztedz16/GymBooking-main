package com.domzky.gymbooking.Sessions.GymCoach.pages.AboutGym;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.domzky.gymbooking.R;

public class AboutGymFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_coach_about_gym, container, false);



        return view;
    }
}