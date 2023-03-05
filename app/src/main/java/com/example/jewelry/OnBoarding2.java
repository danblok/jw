package com.example.jewelry;

import static com.example.jewelry.Constants.IS_NETWORK_AVAILABLE_EXTRA;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.example.jewelry.databinding.ActivityOnBoarding2Binding;

public class OnBoarding2 extends AppCompatActivity {
    private boolean isNetworkAvailable;
    private float endX;
    private ActivityOnBoarding2Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOnBoarding2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupListeners();
        isNetworkAvailable = getIntent().getExtras().getBoolean(IS_NETWORK_AVAILABLE_EXTRA);
    }

    private void setupListeners() {
        binding.buttonSignIn.setOnClickListener(view -> {
            startActivity(SignInActivity.newIntent(this));
        });
        binding.buttonSignUp.setOnClickListener(view -> {
            startActivity(SignUpActivity.newIntent(this));
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!isNetworkAvailable) {
            binding.textViewSkipAuthorization.setVisibility(View.VISIBLE);
            binding.textViewSkipAuthorization.setOnClickListener(view -> {
                // TODO implement skip logic
            });
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                endX = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                float endX = event.getX();
                if (this.endX < endX) {
                    startActivity(OnBoarding1.newIntent(this, isNetworkAvailable));
                }
                break;
        }
        return false;
    }

    public static Intent newIntent(Context context, boolean isNetworkAvailable) {
        Intent intent = new Intent(context, OnBoarding2.class);
        intent.putExtra(IS_NETWORK_AVAILABLE_EXTRA, isNetworkAvailable);
        return intent;
    }
}