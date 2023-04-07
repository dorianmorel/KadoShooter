package com.example.kadoshooter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

public class StageOne extends AppCompatActivity {

    private int nbClick = 0;
    private ImageView button;
    private ArrayList<GifImageView> gifs = new ArrayList<>();
    private ArrayList<Double> directions = new ArrayList<>();
    private ArrayList<Ennemi> ennemies = new ArrayList<>();

    private TextView timer;
    private RelativeLayout ecran;
    private int displayWidth;
    private int displayHeight;
    // TIMER
    private int sec = 11;
    private Timer countdown;

    private TextView accueil;
    private MediaPlayer theme1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getSupportActionBar().hide();

        setContentView(R.layout.activity_stage_one);

        timer = findViewById(R.id.timer);
        createTimer();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        displayWidth = displayMetrics.widthPixels;
        displayHeight = displayMetrics.heightPixels;

        ecran = (RelativeLayout) findViewById(R.id.ecran);

        theme1 = MediaPlayer.create(this, R.raw.theme1);
        theme1.start();

        for (int i=0; i < 20; i++) {

            MediaPlayer koopaDeath = MediaPlayer.create(this, R.raw.koopa_death);
            GifImageView gif = new GifImageView(this);
            gif.setImageResource(R.drawable.koopa);
            gif.setLayoutParams(new RelativeLayout.LayoutParams(200,200));
            double direction = degree2Radian(Math.random() * 360); // angle aléatoire entre 0 et 360°
            gif.setX((int)(Math.random() * (displayWidth-200)));
            gif.setY((int)(Math.random() * (displayHeight-200)));

            Ennemi ennemi = new Ennemi(this, gif, direction, 5);

            ennemi.getGif().setOnClickListener(v -> {
                //Log.i("AAAA", "CLICK");
                //v.setVisibility(View.GONE);
                ViewGroup parentView = (ViewGroup) v.getParent();
                koopaDeath.start();
                parentView.removeView(v);
                ennemies.remove(ennemi);
                //Log.i("INFOARR", String.valueOf(gifs.isEmpty()) + " AND " + String.valueOf(gifs.size()));
                if (ennemies.size() == 0) {
                    countdown.cancel();
                    timer.setText("");
                    ImageView logo = new ImageView(this);
                    logo.setImageResource(R.drawable.kado_logo);
                    ecran.addView(logo);
                    accueil = findViewById(R.id.accueil);
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
            countdown.cancel();

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

            if (x > displayWidth-ennemies.get(i).getGif().getWidth()) {
                ennemies.get(i).setDirection(degree2Radian(180) - ennemies.get(i).getDirection());
                gif.setX(displayWidth-ennemies.get(i).getGif().getWidth()-1);
            } else if (x < 0) {
                ennemies.get(i).setDirection(degree2Radian(180) - ennemies.get(i).getDirection());
                gif.setX(1);
            } else if (y > displayHeight-ennemies.get(i).getGif().getHeight()) {
                ennemies.get(i).setDirection(-ennemies.get(i).getDirection());
                gif.setY(displayHeight-ennemies.get(i).getGif().getHeight()-1);
            } else if (y < 0) {
                ennemies.get(i).setDirection(-ennemies.get(i).getDirection());
                gif.setY(1);
            }
        }
    }
    private void endGame() {
        Log.i("ENDGAME","TEST");
        for (Ennemi ennemi : ennemies) {
            Log.i("ENDGAME","OUI");
            ecran.removeView(ennemi.getGif());
        }
        timer.setText("");
        ImageView logo = new ImageView(this);
        logo.setImageResource(R.drawable.kado_logo);
        ecran.addView(logo);
        TextView accueil = findViewById(R.id.accueil);
        accueil.setText("Retour à l'écran titre");
        accueil.setTextSize(40);
        accueil.setOnClickListener(w -> {
            this.finish();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onPause() {
        super.onPause();  // Always call the superclass method first
        theme1.stop();
    }

}