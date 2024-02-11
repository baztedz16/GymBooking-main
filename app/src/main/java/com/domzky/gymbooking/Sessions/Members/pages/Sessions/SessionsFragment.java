package com.domzky.gymbooking.Sessions.Members.pages.Sessions;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.domzky.gymbooking.R;

public class SessionsFragment extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_member_sessions, container, false);



        return view;
    }
}