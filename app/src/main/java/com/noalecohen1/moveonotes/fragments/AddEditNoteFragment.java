package com.noalecohen1.moveonotes.fragments;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.noalecohen1.moveonotes.R;
import com.noalecohen1.moveonotes.models.Model;
import com.noalecohen1.moveonotes.models.Note;
import com.google.android.gms.location.FusedLocationProviderClient;

import java.util.Calendar;

public class AddEditNoteFragment extends Fragment {

    EditText date;
    DatePickerDialog datePickerDialog;
    Calendar calendar;
    Calendar temp;

    int selectedYear;
    int selectedMonth;
    int selectedDay;

    Note note;
    EditText title;
    EditText body;
    Button saveBtn;
    Button deleteBtn;
    FloatingActionButton saveFAB;
    FloatingActionButton deleteFAB;

    ProgressBar spinner;

    boolean isAdd = true;

    FusedLocationProviderClient fusedLocationProviderClient;


    public AddEditNoteFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static AddEditNoteFragment newInstance(String noteId) {
        AddEditNoteFragment fragment = new AddEditNoteFragment();
        if(noteId != null) {
            fragment.isAdd = false;
            fragment.note = Model.instance.getNoteById(noteId);
        }else{
            fragment.note = new Note();
            fragment.note.setCalender(Calendar.getInstance());
        }
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_add_edit_note, container, false);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        date = view.findViewById(R.id.addEditFragment_date);
        title = view.findViewById(R.id.addEditFragment_title);
        body = view.findViewById(R.id.addEditFragment_body);
        saveBtn = view.findViewById(R.id.addEditFragment_saveBtn);
        deleteBtn = view.findViewById(R.id.addEditFragment_deleteBtn);
        saveFAB = view.findViewById(R.id.addEditFragment_saveFAB);
        deleteFAB = view.findViewById(R.id.addEditFragment_deleteFAB);
        spinner = view.findViewById(R.id.addEditFragment_spinner);
        spinner.setVisibility(View.GONE);

        deleteFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isAdd){
                    spinner.setVisibility(View.VISIBLE);
                    spinner.animate();
                    Model.instance.deleteNote(note);
                }
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();
            }
        });

        saveFAB.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                spinner.setVisibility(View.VISIBLE);
                spinner.animate();
                note.setTitle(title.getText().toString());
                note.setBody(body.getText().toString());
                note.setCalender(temp);
                //note.setLatitude(Math.random()*90);
                //note.setLongitude(Math.random()*180);
                if(isAdd){
                    setNoteCoordinates();
                }else{
                    Model.instance.updateNote(note);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.popBackStack();
                }
            }
        });

        title.setText(note.getTitle());
        body.setText(note.getBody());
        calendar = note.getCalender();
        temp = Calendar.getInstance();
        temp.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        selectedYear = note.getCalender().get(Calendar.YEAR);
        selectedMonth = note.getCalender().get(Calendar.MONTH);
        selectedDay = note.getCalender().get(Calendar.DAY_OF_MONTH);

        date.setText(new StringBuilder().append(String.format("%02d", selectedDay)).append("/")
                .append(String.format("%02d", selectedMonth+1)).append("/")
                .append(String.format("%04d", selectedYear)).toString());

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int _year = selectedYear;
                int _month = selectedMonth;
                int _day = selectedDay;

                datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        date.setText(new StringBuilder().append(String.format("%02d", day)).append("/")
                                .append(String.format("%02d", month+1)).append("/")
                                .append(String.format("%04d", year)).toString());
                        selectedYear = year;
                        selectedMonth = month;
                        selectedDay = day;
                        temp.set(year, month, day);
                    }
                }, _year, _month, _day);
                datePickerDialog.show();
            }
        });
        return view;
    }

    public void setNoteCoordinates(){
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity()
                    , new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 42);
        }

//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getCurrentLocation(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY,null).addOnCompleteListener(new OnCompleteListener<Location>() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location l = task.getResult();
                    if(l != null){
                        note.setLatitude(l.getLatitude());
                        note.setLongitude(l.getLongitude());
                    }else{
                        note.setLatitude(0);
                        note.setLongitude(0);
                    }
                    Model.instance.addNote(note);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.popBackStack();
                }
            });
        }
    }

}