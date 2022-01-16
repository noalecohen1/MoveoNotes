package com.noalecohen1.moveonotes.fragments;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.noalecohen1.moveonotes.R;
import com.noalecohen1.moveonotes.models.Model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginFragment extends Fragment {

    EditText email;
    EditText pass;
    Button submitBtn;
    Switch switchBtn;
    TextView welcome;

    public LoginFragment() {
        // Required empty public constructor
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_login, container, false);
        submitBtn = view.findViewById(R.id.loginFragment_submitBtn);
        switchBtn = view.findViewById(R.id.loginFragment_switchBtn);
        email = view.findViewById(R.id.loginFragment_email);
        pass = view.findViewById(R.id.loginFragment_password);
        welcome = view.findViewById(R.id.loginFragment_welcome);

        switchBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    loginView();
                }else{
                    submitBtn.setText("register");
                    registerView();
                }
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isValidEmail(email.getText()) || !isValidPassword(String.valueOf(pass.getText()))){
                    Snackbar.make(view, "invalid email or password", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                }else{
                    boolean isSignIn = switchBtn.isChecked();

                    OnCompleteListener<AuthResult> listener = new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in/up success, update UI with the signed-in user's information
                                FirebaseUser user = Model.instance.getCurrentUser();
                                Model.instance.setUserId(user.getUid());
                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                FragmentTransaction transaction = fragmentManager.beginTransaction();
                                transaction.replace(R.id.mainactivity_toReplace, new MainScreenFragment()).commit();
                            } else {
                                // If sign in/up fails, display a message to the user.
                                Snackbar.make(view, task.getException().getLocalizedMessage(), Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }
                        }
                    };

                    if(isSignIn){
                        Model.instance.signIn(email.getText().toString(), pass.getText().toString(),listener);
                    }else{
                        Model.instance.createUser(email.getText().toString(), pass.getText().toString(),listener);
                    }
                }
            }
        });
        return view;
    }

    public boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
    public boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "(?=.*[0-9a-zA-Z]).{6,}";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();
    }

    public void loginView(){
        email.setHintTextColor(getResources().getColor(R.color.purple_200));
        pass.setHintTextColor(getResources().getColor(R.color.purple_200));
        //switchBtn.setTextColor(getResources().getColor(R.color.purple_200));
        submitBtn.setText("login");
    }

    @SuppressLint("ResourceType")
    public void registerView(){ //#B86262
        email.setHintTextColor(Color.rgb(184, 98, 98));
        pass.setHintTextColor(Color.rgb(184, 98, 98));
        //switchBtn.setTextColor(Color.rgb(184, 98, 98));
        submitBtn.setText("register");
    }

}