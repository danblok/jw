package com.example.jewelry;

import static com.example.jewelry.Constants.IS_NETWORK_AVAILABLE_EXTRA;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;

public class OnBoarding1 extends AppCompatActivity {
    private float startX;
    private boolean isNetworkAvailable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding1);
        isNetworkAvailable = getIntent().getExtras().getBoolean(IS_NETWORK_AVAILABLE_EXTRA);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                float endX = event.getX();
                if (startX > endX) {
                    startActivity(OnBoarding2.newIntent(this, isNetworkAvailable));
                }
                break;
        }
        return false;
    }

    public static Intent newIntent(Context context, boolean isNetworkAvailable) {
        Intent intent = new Intent(context, OnBoarding1.class);
        intent.putExtra(IS_NETWORK_AVAILABLE_EXTRA, isNetworkAvailable);
        return intent;
    }
}