package com.example.jewelry;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;

public class LaunchActivity extends AppCompatActivity {
    private static final String TAG = "LaunchActivity";
    private LaunchViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewModel = new ViewModelProvider(this).get(LaunchViewModel.class);

        if (isNetworkAvailable()) {
            observeViewModel();
            viewModel.compareLocalVersionsToRemote();
        } else {
            startActivity(OnBoarding1.newIntent(this, false));
        }
    }

    private void observeViewModel() {
        viewModel.getIsLoading().observe(this, isLoading -> {
            if (!isLoading) {
                startActivity(OnBoarding1.newIntent(this, true));
            } else {
                viewModel.refreshDishes();
            }
        });
        viewModel.getError().observe(this, error -> {
            Log.e(TAG, error);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        /* TODO if the internet isn't available then just start the OnBoarding1 activity
           TODO otherwise check if the menu version is equal to the locally saved one and
           TODO if not then fetch all dishes from the DB by the api and save it to the Local DB */

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager != null ?
                connectivityManager.getActiveNetworkInfo() : null;
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}