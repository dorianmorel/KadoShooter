package com.example.kadoshooter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import pl.droidsonroids.gif.GifImageView;

public class StageTwo extends AppCompatActivity {

    private int nbClick = 0;
    private ImageView button;
    private ArrayList<GifImageView> gifs = new ArrayList<>();
    private ArrayList<Double> directions = new ArrayList<>();
    private ArrayList<Ennemi> ennemies = new ArrayList<>();
    private int displayWidth;
    private int displayHeight;

    private TextView timer;
    private RelativeLayout ecran;
    private TextView accueil;

    // TIMER
    private int sec = 21;
    private Timer countdown;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getSupportActionBar().hide();

        setContentView(R.layout.activity_stage_two);

        timer = findViewById(R.id.timer2);
        createTimer();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        displayWidth = displayMetrics.widthPixels;
        displayHeight = displayMetrics.heightPixels;

        ecran = (RelativeLayout) findViewById(R.id.ecran);

        for (int i=0; i < 15; i++) {
            MediaPlayer goombaDeath = MediaPlayer.create(this, R.raw.goomba_death);

            GifImageView gif = new GifImageView(this);
            gif.setImageResource(R.drawable.goomba);
            gif.setLayoutParams(new RelativeLayout.LayoutParams(300,300));
            double direction = degree2Radian(Math.random() * 360); // angle aléatoire entre 0 et 360°
            int x = (int)(Math.random() * (displayWidth-300));
            int y = (int)(Math.random() * (displayHeight-300));

            Ennemi ennemi = createEnnemi(300, 300, x, y);

            ennemi.getGif().setOnClickListener(v -> {
                ViewGroup parentView = (ViewGroup) v.getParent();
                parentView.removeView(v);
                goombaDeath.start();
                ennemies.remove(ennemi);
                Ennemi ennemi1 = createEnnemi(150, 150, ennemi.getGif().getX(), ennemi.getGif().getY());
                ecran.addView(ennemi1.getGif());
                ennemies.add(ennemi1);
                Ennemi ennemi2 = createEnnemi(150, 150, ennemi.getGif().getX(), ennemi.getGif().getY());
                ecran.addView(ennemi2.getGif());
                ennemies.add(ennemi2);
                ennemi1.getGif().setOnClickListener(z -> {
                    goombaDeath.start();
                    ecran.removeView(z);
                    ennemies.remove(ennemi1);
                    if (ennemies.size() == 0)
                        endGame();
                        });
                ennemi2.getGif().setOnClickListener(r -> {
                    goombaDeath.start();
                    ecran.removeView(r);
                    ennemies.remove(ennemi2);
                    if (ennemies.size() == 0)
                        endGame();
                });

                if (ennemies.size() == 0) {
                    ImageView logo = new ImageView(this);
                    logo.setImageResource(R.drawable.kado_logo);
                    ecran.addView(logo);
                    accueil = findViewById(R.id.accueil2);
                    accueil.setGravity(Gravity.CENTER);
                    accueil.setText("Retour à l'écran titre");
                    accueil.setTextSize(40);
                    accueil.setOnClickListener(w -> {
                        this.finish();
                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
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

                try {
                    updateMovements();
                }
                catch (ConcurrentModificationException exception) {
                    // erreur si l'exec prend plus de 20ms
                }

            }
        };

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(movements, 0, 20, TimeUnit.MILLISECONDS);
    }

    private double degree2Radian(double degree) {
        return degree * Math.PI / 180;
    }

    private Ennemi createEnnemi(int width, int height, float x, float y) {
        GifImageView gif = new GifImageView(this);
        gif.setImageResource(R.drawable.goomba);
        gif.setLayoutParams(new RelativeLayout.LayoutParams(width,height));
        double direction = degree2Radian(Math.random() * 360); // angle aléatoire entre 0 et 360°
        gif.setX(x);
        gif.setY(y);
        Ennemi ennemi = new Ennemi(this, gif, direction);
        return ennemi;
    }

    private void createTimer() {
        //set delay and period as 1000
        int del = 500;
        int per = 1000;
        countdown = new Timer();
        timer.setTextSize(200);
        //System.out.println(sec);
        //performs the specifiedd task at certain intervals
        countdown.scheduleAtFixedRate(new TimerTask()
        {
            //task to be performed
            public void run()
            {
                timer.setText(""+seti());
            }
        }, del, per);
        //set interval
    }

    private final int seti() {
        //if interval is 1, cancel
        if (sec == 1) {

            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    endGame();
                }
            });
            //endGame();
        }

        return --sec;
    }

    private void updateMovements(){
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
    private void endGame() {
        Log.i("ENDGAME","TEST");
        if (ennemies.size() != 0) {
            for (Ennemi ennemi : ennemies) {
                Log.i("ENDGAME", "OUI");
                ecran.removeView(ennemi.getGif());
            }
        }
        countdown.cancel();
        timer.setText("");
        ImageView logo = new ImageView(this);
        logo.setImageResource(R.drawable.kado_logo);
        ecran.addView(logo);
        TextView accueil = findViewById(R.id.accueil2);
        accueil.setText("Retour à l'écran titre");
        accueil.setTextSize(40);
        accueil.setOnClickListener(w -> {
            this.finish();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });
    }
}