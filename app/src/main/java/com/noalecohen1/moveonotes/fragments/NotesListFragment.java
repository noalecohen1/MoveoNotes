package com.noalecohen1.moveonotes.fragments;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.noalecohen1.moveonotes.R;
import com.noalecohen1.moveonotes.adapters.NoteAdapter;
import com.noalecohen1.moveonotes.models.Model;
import com.noalecohen1.moveonotes.models.Note;
import com.noalecohen1.moveonotes.utils.spacingItemDecorator;

import java.util.List;


public class NotesListFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_notes_list, container, false);

        RecyclerView notesList = view.findViewById(R.id.noteslist_frg_recyclerView);
        notesList.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        notesList.setLayoutManager(layoutManager);

        spacingItemDecorator itemDecorator = new spacingItemDecorator(64);
        notesList.addItemDecoration(itemDecorator);

        List<Note> notes = Model.instance.getAllNotes();
        NoteAdapter adapter = new NoteAdapter(notes, getContext());
        notesList.setAdapter(adapter);
        adapter.setListener(new NoteAdapter.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onItemClick(int position) {

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.mainactivity_toReplace, AddEditNoteFragment.newInstance(notes.get(position).getId()))
                        .addToBackStack("addedit")
                        .commit();
            }
        });
        return view;
    }
}