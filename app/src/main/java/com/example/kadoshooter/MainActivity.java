package com.example.kadoshooter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getSupportActionBar().hide();

        setContentView(R.layout.activity_main);
    }

    public void onClickStage1(View view) {
        Intent intent = new Intent(this, StageOne.class);
        startActivity(intent);
    }
    public void onClickStage2(View view) {
        Intent intent = new Intent(this, StageTwo.class);
        startActivity(intent);
    }
    public void onClickStage3(View view) {
        Intent intent = new Intent(this, StageThree.class);
        startActivity(intent);
    }


}
