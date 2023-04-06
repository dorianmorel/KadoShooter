package com.example.kadoshooter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.PorterDuff;
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

import com.example.kadoshooter.Ennemi;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import pl.droidsonroids.gif.GifImageView;

public class StageThree extends AppCompatActivity {

    private int nbClick = 0;
    private ImageView button;
    private ArrayList<GifImageView> gifs = new ArrayList<>();
    private ArrayList<Double> directions = new ArrayList<>();
    private ArrayList<Ennemi> ennemies = new ArrayList<>();
    private int displayWidth;
    private int displayHeight;


    private Integer if1noFilter;

    private Runnable hit;
    private ScheduledExecutorService executorHit;

    private MediaPlayer theme3;

    private Float speed = (float) 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getSupportActionBar().hide();

        setContentView(R.layout.activity_stage_three);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        displayWidth = displayMetrics.widthPixels;
        displayHeight = displayMetrics.heightPixels;

        RelativeLayout ecran = (RelativeLayout) findViewById(R.id.ecran);

        theme3 = MediaPlayer.create(this, R.raw.theme3);
        theme3.start();

        for (int i=0; i < 1; i++) {


            GifImageView gif = new GifImageView(this);
            gif.setImageResource(R.drawable.bowser);
            gif.setLayoutParams(new RelativeLayout.LayoutParams(300,300));
            double direction = degree2Radian(Math.random() * 360); // angle aléatoire entre 0 et 360°
            int x = (int)(Math.random() * (displayWidth-700));
            int y = (int)(Math.random() * (displayHeight-700));

            Ennemi ennemi = createEnnemi(700, 700, x, y);

            ennemi.getGif().setOnClickListener(v -> {
                //ennemi.getGif().setColorFilter(ContextCompat.getColor(this,R.color.orange));
                if1noFilter = 0;
                hit = new Runnable() {
                    @Override
                    public void run() {
                        if (if1noFilter == 0) {
                            ennemi.getGif().setColorFilter(0xFFFF0000, PorterDuff.Mode.MULTIPLY);
                        }
                        else if (if1noFilter == 1) {
                            Log.i("RUNHIT","YES");
                            clearBowserFilter();
                            cancelExecutor();
                        }
                        if1noFilter++;
                    }

                    public void clearBowserFilter() {
                        ennemi.getGif().clearColorFilter();
                    }
                };
                executorHit = Executors.newScheduledThreadPool(1);
                executorHit.scheduleAtFixedRate(hit, 0, 100, TimeUnit.MILLISECONDS);

                nbClick++;
                if(nbClick==100){
                    ViewGroup parentView = (ViewGroup) v.getParent();
                    parentView.removeView(v);
                    ennemies.remove(ennemi);
                }
                if(nbClick%5==0){
                    speed += (float) 5;
                }

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
        gif.setImageResource(R.drawable.bowser);
        gif.setLayoutParams(new RelativeLayout.LayoutParams(width,height));
        double direction = degree2Radian(Math.random() * 360); // angle aléatoire entre 0 et 360°
        gif.setX(x);
        gif.setY(y);
        Ennemi ennemi = new Ennemi(this, gif, direction);
        return ennemi;
    }

        private void updateMovements(){
            for (int i = 0; i < ennemies.size(); i++) {

                GifImageView gif = ennemies.get(i).getGif();
                double direction = ennemies.get(i).getDirection();

                float x = gif.getX();
                float y = gif.getY();

                x += Math.cos(direction) * speed;
                y += Math.sin(direction) * speed;

                //gif.setX(x);
                //gif.setY(y);
                ennemies.get(i).setGif(x,y);

            if (x > displayWidth-ennemies.get(i).getGif().getWidth() || x < 0)
                //directions.set(i, degree2Radian(180) - direction);
                ennemies.get(i).setDirection(degree2Radian(180) - ennemies.get(i).getDirection());
            else if (y > displayHeight-ennemies.get(i).getGif().getHeight() || y < 0)
                //directions.set(i, -direction);
                ennemies.get(i).setDirection(-ennemies.get(i).getDirection());
        }
    }

    private void cancelExecutor() {
        executorHit.shutdown();
    }

    @Override
    protected void onPause() {
        super.onPause();  // Always call the superclass method first
        theme3.stop();
    }
}