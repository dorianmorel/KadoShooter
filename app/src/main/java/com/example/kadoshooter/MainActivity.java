package com.example.kadoshooter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;


public class MainActivity extends AppCompatActivity {

    private MediaPlayer mainTheme;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainTheme = MediaPlayer.create(this, R.raw.main_theme);
        mainTheme.start();

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getSupportActionBar().hide();

        setContentView(R.layout.activity_main);


    }

    public void onClickStage1(View view) {
        Intent intent = new Intent(this, StageOne.class);
        startActivity(intent);
        this.finish();
    }
    public void onClickStage2(View view) {
        Intent intent = new Intent(this, StageTwo.class);
        startActivity(intent);
        this.finish();
    }
    public void onClickStage3(View view) {
        Intent intent = new Intent(this, StageThree.class);
        startActivity(intent);
        this.finish();
    }

    @Override
    protected void onPause() {
        super.onPause();  // Always call the superclass method first
        mainTheme.stop();
    }


}
