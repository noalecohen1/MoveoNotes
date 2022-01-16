package com.noalecohen1.moveonotes.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseUser;
import com.noalecohen1.moveonotes.R;
import com.noalecohen1.moveonotes.models.Model;


public class SplashFragment extends Fragment {

    ProgressBar progressBar;

    public SplashFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_splash, container, false);
        progressBar = view.findViewById(R.id.splash_progressBar);
        progressBar.animate();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = Model.instance.getCurrentUser();
        if(currentUser != null){
            Model.instance.setUserId(currentUser.getUid());
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.mainactivity_toReplace, new MainScreenFragment())
                    .commit();
        }else{
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.mainactivity_toReplace, new LoginFragment())
                    .commit();
        }
    }
}