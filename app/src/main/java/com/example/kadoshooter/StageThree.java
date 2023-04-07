package com.example.kadoshooter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
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

    private float bowserSpeed = (float) 5;
    private float speed = (float) 5;

    private Ennemi bowser;

    Context context = this;

    private RelativeLayout ecran;

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

        ecran = (RelativeLayout) findViewById(R.id.ecran);

        theme3 = MediaPlayer.create(this, R.raw.theme3);
        theme3.start();

        for (int i=0; i < 1; i++) {


            GifImageView gif = new GifImageView(this);
            gif.setImageResource(R.drawable.bowser);
            gif.setLayoutParams(new RelativeLayout.LayoutParams(300,300));
            double direction = degree2Radian(Math.random() * 360); // angle aléatoire entre 0 et 360°
            int x = (int)(Math.random() * (displayWidth-700));
            int y = (int)(Math.random() * (displayHeight-700));

            bowser = createEnnemi(700, 700, x, y, R.drawable.bowser, bowserSpeed);

            bowser.getGif().setOnClickListener(v -> {
                if1noFilter = 0;
                hit = new Runnable() {
                    @Override
                    public void run() {
                        if (if1noFilter == 0) {
                            bowser.getGif().setColorFilter(0xFFFF0000, PorterDuff.Mode.MULTIPLY);
                        }
                        else if (if1noFilter == 1) {
                            Log.i("RUNHIT","YES");
                            clearBowserFilter();
                            cancelExecutor();
                        }
                        if1noFilter++;
                    }

                    public void clearBowserFilter() {
                        bowser.getGif().clearColorFilter();
                    }
                };
                executorHit = Executors.newScheduledThreadPool(1);
                executorHit.scheduleAtFixedRate(hit, 0, 100, TimeUnit.MILLISECONDS);

                nbClick++;
                if(nbClick==100){
                    ViewGroup parentView = (ViewGroup) v.getParent();
                    parentView.removeView(v);
                    ennemies.remove(bowser);
                }
                if(nbClick%5==0){
                    bowser.setSpeed(bowser.getSpeed() + 4);
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
            ecran.addView(bowser.getGif());
            ennemies.add(bowser);
        }


        // A CHANGER
        Runnable movements = new Runnable() {
            private int counter = 0;

            public void run() {
                counter+=20;
                try {
                    if (counter%2000 == 0) {
                        if (ennemies.get(0).getSpeed() != 5)
                            createGoomba();
                    }


                    updateMovements();
                }
                catch (ConcurrentModificationException exception) {
                    // erreur si l'exec prend plus de 20ms
                }
                catch (Exception exception) {
                }

            }
        };

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(movements, 0, 20, TimeUnit.MILLISECONDS);
    }

    private double degree2Radian(double degree) {
        return degree * Math.PI / 180;
    }

    private Ennemi createEnnemi(int width, int height, float x, float y, int file, float spd) {
        GifImageView gif = new GifImageView(this);
        gif.setImageResource(file);
        gif.setLayoutParams(new RelativeLayout.LayoutParams(width,height));
        double direction = degree2Radian(Math.random() * 360); // angle aléatoire entre 0 et 360°
        gif.setX(x);
        gif.setY(y);
        Ennemi ennemi = new Ennemi(this, gif, direction, spd);
        return ennemi;
    }

    private void updateMovements(){
        //Log.i("RUNNING","JE RUN");
        for (int i = 0; i < ennemies.size(); i++) {

            GifImageView gif = ennemies.get(i).getGif();
            double direction = ennemies.get(i).getDirection();

            float x = gif.getX();
            float y = gif.getY();

            x += Math.cos(direction) * ennemies.get(i).getSpeed();
            y += Math.sin(direction) * ennemies.get(i).getSpeed();

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

    private void cancelExecutor() {
        executorHit.shutdown();
    }

    @Override
    protected void onPause() {
        super.onPause();  // Always call the superclass method first
        theme3.stop();
    }

    private void goBackToMenu() {
        this.finish();
        Intent intent = new Intent(context, MainActivity.class);
        startActivity(intent);
    }

    private void createGoomba(){
        Log.i("ENNEMI","CREATE");

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                createGoombaOnUIThread();
            }
        });
    }

    private void createGoombaOnUIThread() {
        Ennemi goomba = createEnnemi(300, 300, bowser.getGif().getX(), bowser.getGif().getY(), R.drawable.goomba, speed);

        ecran.addView(goomba.getGif());

        ennemies.add(goomba);
        Log.i("ENNEMI",ennemies.toString());
        goomba.getGif().setOnClickListener(v -> {
            ViewGroup parentView = (ViewGroup) v.getParent();
            parentView.removeView(v);
            ennemies.remove(goomba);
            if (ennemies.size() == 0) {
                ImageView logo = new ImageView(context);
                logo.setImageResource(R.drawable.kado_logo);
                ecran.addView(logo);
                TextView accueil = new TextView(context);
                accueil.setGravity(Gravity.CENTER);
                accueil.setText("Retour à l'écran titre");
                accueil.setTextSize(40);
                ecran.addView(accueil);
                accueil.setOnClickListener(w -> {
                    goBackToMenu();
                });
            }
        });
    }
}