package com.example.testmovement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
    private ArrayList<Ennemi> ennemies = new ArrayList<>();

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

        for (int i=0; i < 20; i++) {

            GifImageView gif = new GifImageView(this);
            gif.setImageResource(R.drawable.koopa);
            gif.setLayoutParams(new RelativeLayout.LayoutParams(200,200));
            double direction = degree2Radian(Math.random() * 360); // angle aléatoire entre 0 et 360°
            gif.setX((int)(Math.random() * (displayWidth-200)));
            gif.setY((int)(Math.random() * (displayHeight-200)));

            Ennemi ennemi = new Ennemi(this, gif, direction);

            ennemi.getGif().setOnClickListener(v -> {
                //Log.i("AAAA", "CLICK");
                //v.setVisibility(View.GONE);
                ViewGroup parentView = (ViewGroup) v.getParent();
                parentView.removeView(v);
                ennemies.remove(ennemi);
                //Log.i("INFOARR", String.valueOf(gifs.isEmpty()) + " AND " + String.valueOf(gifs.size()));
                if (ennemies.size() == 0) {
                    ImageView logo = new ImageView(this);
                    logo.setImageResource(R.drawable.kado_logo);
                    ecran.addView(logo);
                    TextView accueil = new TextView(this);
                    accueil.setGravity(Gravity.CENTER);
                    accueil.setText("Retour à l'écran titre");
                    accueil.setTextSize(40);
                    ecran.addView(accueil);
                    accueil.setOnClickListener(w -> {
                        this.finish();
                    });

                }
            });

            //double direction = degree2Radian(45);

            ecran.addView(ennemi.getGif());
            ennemies.add(ennemi);
            //gifs.add(gif);
            //directions.add(direction);
        }


        // A CHANGER
        Runnable movements = new Runnable() {
            public void run() {

                for (int i = 0; i < ennemies.size(); i++) {

                    GifImageView gif = ennemies.get(i).getGif();
                    double direction = ennemies.get(i).getDirection();

                    float x = gif.getX();
                    float y = gif.getY();

                    float speed = 10;

                    x += Math.cos(direction) * speed;
                    y += Math.sin(direction) * speed;

                    //gif.setX(x);
                    //gif.setY(y);
                    ennemies.get(i).setGif(x,y);

                    if (x >= displayWidth-200 || x <= 0)
                        //directions.set(i, degree2Radian(180) - direction);
                        ennemies.get(i).setDirection(degree2Radian(180) - ennemies.get(i).getDirection());
                    else if (y >= displayHeight-200 || y <= 0)
                        //directions.set(i, -direction);
                        ennemies.get(i).setDirection(-ennemies.get(i).getDirection());
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