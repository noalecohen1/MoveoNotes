package com.noalecohen1.moveonotes.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import android.os.Build;
import android.os.Bundle;
import com.noalecohen1.moveonotes.R;
import com.noalecohen1.moveonotes.fragments.SplashFragment;
import com.noalecohen1.moveonotes.models.Model;

public class MainActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onStart() {
        super.onStart();
        Model.instance.initializeDB(this);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.mainactivity_toReplace, new SplashFragment())
                .commit();
    }
}