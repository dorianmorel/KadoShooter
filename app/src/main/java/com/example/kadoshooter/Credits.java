package com.example.kadoshooter;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Window;

public class Credits extends AppCompatActivity {

    private MediaPlayer creditsTheme;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        creditsTheme = MediaPlayer.create(this, R.raw.credits);
        creditsTheme.start();

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getSupportActionBar().hide();

        setContentView(R.layout.activity_credits);
    }

    @Override
    protected void onPause() {
        super.onPause();  // Always call the superclass method first
        creditsTheme.stop();
    }
}