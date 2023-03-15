package com.example.testmovement;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private int nbClick = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void move(View v) {
        System.out.println("CLICK");
        Button button = findViewById(R.id.button);
        nbClick+=1;

        if (nbClick == 1) {
            Runnable helloRunnable = new Runnable() {
                public void run() {
                    button.setTop(button.getTop()-1);
                    button.setBottom(button.getBottom()-1);
                    button.setLeft(button.getLeft()+1);
                    button.setRight(button.getRight()+1);
                }
            };

            ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
            executor.scheduleAtFixedRate(helloRunnable, 0, 50, TimeUnit.MILLISECONDS);
        }

        else if (nbClick == 2) {
            ViewGroup parentView = (ViewGroup) button.getParent();
            parentView.removeView(button);
        }
    }
}