package com.example.testmovement;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import pl.droidsonroids.gif.GifImageView;

public class MainActivity extends AppCompatActivity {

    private int nbClick = 0;
    private ImageView button;
    //private ArrayList<GifImageView> imgs = new ArrayList<>();
    private GifImageView[] gifs = new GifImageView[50];
    private String[][] directions = new String[50][2];

    private String[] topbot = new String[2];
    private String[] leftright = new String[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int displayWidth = displayMetrics.widthPixels;
        int displayHeight = displayMetrics.heightPixels;

        RelativeLayout ecran = (RelativeLayout) findViewById(R.id.ecran);

        topbot[0] = "TOP";
        topbot[1] = "BOT";
        leftright[0] = "LEFT";
        leftright[1] = "RIGHT";

        for (int i=0; i < 50; i++) {

            GifImageView gif = new GifImageView(this);
            gif.setImageResource(R.drawable.koopa);
            gif.setLayoutParams(new RelativeLayout.LayoutParams(300,300));

            gif.setX((int)(Math.random() * displayWidth));
            gif.setY((int)(Math.random() * displayHeight));

            gif.setOnClickListener(v -> {

                System.out.println("CLICK");
                v.setVisibility(View.GONE);
            });

            ecran.addView(gif);
            gifs[i] = gif;

            int randX = new Random().nextInt(leftright.length);
            int randY = new Random().nextInt(topbot.length);
            directions[i][0] = leftright[randX];
            directions[i][1] = topbot[randY];
        }


        Runnable movements = new Runnable() {
            public void run() {
                for (int i=0; i<50; i++) {
                    if (directions[i][0] == "RIGHT" && directions[i][1] == "BOT") {
                        if (gifs[i].getX()+1 == displayWidth) {
                            directions[i][0] = "LEFT";
                            gifs[i].setX(gifs[i].getX() - 1);
                            gifs[i].setY(gifs[i].getY() + 1);
                        }
                        else if (gifs[i].getY()+1 == displayHeight) {
                            directions[i][1] = "TOP";
                            gifs[i].setX(gifs[i].getX() + 1);
                            gifs[i].setY(gifs[i].getY() - 1);
                        }
                        else {
                            gifs[i].setX(gifs[i].getX() + 1);
                            gifs[i].setY(gifs[i].getY() + 1);
                        }
                    }
                    if (directions[i][0] == "RIGHT" && directions[i][1] == "TOP") {
                        if (gifs[i].getX()+1 == displayWidth) {
                            directions[i][0] = "LEFT";
                            gifs[i].setX(gifs[i].getX() - 1);
                            gifs[i].setY(gifs[i].getY() + 1);
                        }
                        else if (gifs[i].getY()+1 == 0) {
                            directions[i][1] = "BOT";
                            gifs[i].setX(gifs[i].getX() + 1);
                            gifs[i].setY(gifs[i].getY() + 1);
                        }
                        else {
                            gifs[i].setX(gifs[i].getX() + 1);
                            gifs[i].setY(gifs[i].getY() - 1);
                        }
                    }
                    if (directions[i][0] == "LEFT" && directions[i][1] == "BOT") {
                        if (gifs[i].getX()+1 == 0) {
                            directions[i][0] = "RIGHT";
                            gifs[i].setX(gifs[i].getX() + 1);
                            gifs[i].setY(gifs[i].getY() + 1);
                        }
                        else if (gifs[i].getY()+1 == displayHeight) {
                            directions[i][1] = "TOP";
                            gifs[i].setX(gifs[i].getX() - 1);
                            gifs[i].setY(gifs[i].getY() - 1);
                        }
                        else {
                            gifs[i].setX(gifs[i].getX() - 1);
                            gifs[i].setY(gifs[i].getY() + 1);
                        }
                    }
                    if (directions[i][0] == "LEFT" && directions[i][1] == "TOP") {
                        if (gifs[i].getX()+1 == 0) {
                            directions[i][0] = "RIGHT";
                            gifs[i].setX(gifs[i].getX() + 1);
                            gifs[i].setY(gifs[i].getY() - 1);
                        }
                        else if (gifs[i].getY()+1 == 0) {
                            directions[i][1] = "BOT";
                            gifs[i].setX(gifs[i].getX() - 1);
                            gifs[i].setY(gifs[i].getY() + 1);
                        }
                        else {
                            gifs[i].setX(gifs[i].getX() - 1);
                            gifs[i].setY(gifs[i].getY() - 1);
                        }
                    }
                }
            }
        };

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(movements, 0, 10, TimeUnit.MILLISECONDS);
    }

    public void move(View v) {

        ViewGroup parentView = (ViewGroup) v.getParent();
        parentView.removeView(v);
    }
}