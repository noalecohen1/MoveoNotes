package com.noalecohen1.moveonotes.fragments;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.noalecohen1.moveonotes.R;
import com.noalecohen1.moveonotes.models.Model;

public class MainScreenFragment extends Fragment implements View.OnClickListener {

    Fragment[] tabs = new Fragment[2];
    int selected = 0;

    public MainScreenFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_main_screen, container, false);
        Button notes = view.findViewById(R.id.mainScreen_listModeBtn);
        Button map = view.findViewById(R.id.mainScreen_mapModeBtn);
        Button logout = view.findViewById(R.id.mainScreen_logoutBtn);
        FloatingActionButton add = view.findViewById(R.id.mainScreen_addButton);

        add.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.mainactivity_toReplace, AddEditNoteFragment.newInstance(null))
                        .addToBackStack("addedit")
                        .commit();
            }
        });

        notes.setOnClickListener(this);
        map.setOnClickListener(this);
        logout.setOnClickListener(this);

        tabs[0] = new NotesListFragment();
        tabs[1] = new MapsFragment();

        notes.setTag(0);
        map.setTag(1);
        logout.setTag(2);

        TextView message = view.findViewById(R.id.mainScreen_indicator);

        if(Model.instance.getAllNotes().isEmpty()){
            message.setVisibility(View.VISIBLE);
        }
        else{
            message.setVisibility(View.GONE);
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.mainScreen_container, tabs[this.selected]);
            transaction.commit();
        }

        return view;
    }

    @Override
    public void onClick(View view) {
        this.selected = (int) view.getTag();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if(this.selected == 2){
            Model.instance.logout();
            transaction.replace(R.id.mainactivity_toReplace, new LoginFragment()).commit();
        }else{
            transaction.replace(R.id.mainScreen_container, tabs[this.selected]);
            transaction.commit();
        }
    }
}