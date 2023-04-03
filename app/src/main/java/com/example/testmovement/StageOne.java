package com.example.testmovement;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import pl.droidsonroids.gif.GifImageView;

public class StageOne extends AppCompatActivity {

    private int nbClick = 0;
    private ImageView button;
    private ArrayList<GifImageView> gifs = new ArrayList<>();
    private ArrayList<Double> directions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getSupportActionBar().hide();

        setContentView(R.layout.activity_stage_one);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int displayWidth = displayMetrics.widthPixels;
        int displayHeight = displayMetrics.heightPixels;

        RelativeLayout ecran = (RelativeLayout) findViewById(R.id.ecran);

        for (int i=0; i < 5; i++) {

            GifImageView gif = new GifImageView(this);
            gif.setImageResource(R.drawable.koopa);
            gif.setLayoutParams(new RelativeLayout.LayoutParams(200,200));

            gif.setX((int)(Math.random() * (displayWidth-200)));
            gif.setY((int)(Math.random() * (displayHeight-200)));
            double direction = degree2Radian(Math.random() * 360); // angle aléatoire entre 0 et 360°


            gif.setOnClickListener(v -> {
                Log.i("AAAA", "CLICK");
                //v.setVisibility(View.GONE);
                ViewGroup parentView = (ViewGroup) v.getParent();
                parentView.removeView(v);
                gifs.remove(gif);
                directions.remove(direction);
                Log.i("INFOARR", String.valueOf(gifs.isEmpty()) + " AND " + String.valueOf(gifs.size()));
                if (gifs.size() == 0) {
                    ImageView logo = new ImageView(this);
                    logo.setImageResource(R.drawable.kado_logo);
                    ecran.addView(logo);
                }
            });

            //double direction = degree2Radian(45);

            ecran.addView(gif);
            gifs.add(gif);
            directions.add(direction);
        }


        Runnable movements = new Runnable() {
            public void run() {

                for (int i = 0; i < gifs.size(); i++) {

                    GifImageView gif = gifs.get(i);
                    double direction = directions.get(i);

                    float x = gif.getX();
                    float y = gif.getY();

                    float speed = 10;

                    x += Math.cos(direction) * speed;
                    y += Math.sin(direction) * speed;

                    gif.setX(x);
                    gif.setY(y);

                    if (x >= displayWidth-200 || x <= 0)
                        directions.set(i, degree2Radian(180) - direction);
                    else if (y >= displayHeight-200 || y <= 0)
                        directions.set(i, -direction);
                }
            }
        };

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(movements, 0, 20, TimeUnit.MILLISECONDS);
    }

    private double degree2Radian(double degree) {
        return degree * Math.PI / 180;
    }

}