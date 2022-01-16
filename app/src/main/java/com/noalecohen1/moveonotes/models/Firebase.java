package com.noalecohen1.moveonotes.models;



import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Firebase {
    private FirebaseAuth mAuth;

    Firebase(){
        mAuth = FirebaseAuth.getInstance();
    }

    public FirebaseUser getCurrentUser(){
        return mAuth.getCurrentUser();
    }

    public void createUser(String email, String password, OnCompleteListener<AuthResult> listener){
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(listener);
    }

    public void signIn(String email, String password, OnCompleteListener<AuthResult> listener){
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(listener);
    }

    public void logout(){
        mAuth.signOut();
    }

}
