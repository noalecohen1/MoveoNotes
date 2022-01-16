package com.noalecohen1.moveonotes.models;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Model {

    public final static Model instance = new Model();
    ArrayList<Note> data = null;
    String userId;
    Firebase firebase = new Firebase();
    DBHelper db;

    private Model(){}

    public void initializeDB(Context context){
        db = new DBHelper(context);
    }

    public List<Note> getAllNotes() {
        if(data == null) {
            data = db.getAllNotesByUserId(userId);
        }
        return data;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Note getNoteById(String id){
        return data.stream().filter(n -> n.getId().equals(id)).findFirst().orElse(null);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private boolean doesIdExist(UUID id){
        return data.stream().anyMatch(n -> n.getId().equals(id.toString()));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void addNote(Note note){
        //generate unique user id
        UUID uuid = UUID.randomUUID();
        while (doesIdExist(uuid)){
            uuid = UUID.randomUUID();
        }

        if(note != null) {
            note.id = uuid.toString();
            sortAdd(note);
            db.addNote(note, userId);
        }
    }

    public void deleteNote(Note note){
        if(note != null){
            data.remove(note);
            db.deleteNote(note.getId());
        }
    }

    public void updateNote(Note note) {
        data.remove(note);
        sortAdd(note);
        db.updateNote(note);
    }

    //adding the note at the correct position in the list sorted by date
    public void sortAdd(Note note){
        int j;
        for(j=0; j<data.size(); j++){
            if(data.get(j).compareTo(note) < 0){
                data.add(j, note);
                break;
            }
        }
        if(j == data.size()){
            data.add(note);
        }
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
        data = db.getAllNotesByUserId(userId);
    }

    public FirebaseUser getCurrentUser(){
        return firebase.getCurrentUser();
    }

    public void createUser(String email, String password, OnCompleteListener<AuthResult> listener){
        firebase.createUser(email, password, listener);
    }

    public void signIn(String email, String password, OnCompleteListener<AuthResult> listener){
        firebase.signIn(email, password, listener);
    }

    public void logout(){
        firebase.logout();
    }

}
