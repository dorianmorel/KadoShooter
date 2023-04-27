package com.example.kadoshooter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
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

//Premier niveau
public class StageOne extends AbstractStage {

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

    private Context context;

    //Création du niveau
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        context = this;

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getSupportActionBar().hide();

        setContentView(R.layout.activity_stage_one);

        //Lancement du timer
        timer = findViewById(R.id.timer);
        createTimer();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        displayWidth = displayMetrics.widthPixels;
        displayHeight = displayMetrics.heightPixels;

        ecran = (RelativeLayout) findViewById(R.id.ecran);

        theme1 = MediaPlayer.create(this, R.raw.theme1);
        theme1.start();

        //Petit EasterEgg qui, avec une chance sur 5, remplace les Koopas par un gif d'Aubin EBANO, élève de DDIM
        int eskeOb1 = (int) (Math.random() * 5);
        int drawable;
        if (eskeOb1 == 1)
            drawable = R.drawable.ob1;
        else
            drawable = R.drawable.koopa;
        
        //Création et placement des ennemis
        for (int i=0; i < 20; i++) {

            MediaPlayer koopaDeath = MediaPlayer.create(this, R.raw.koopa_death);

            int x = (int)(Math.random() * (displayWidth-200));
            int y = (int)(Math.random() * (displayHeight-200));

            Ennemi koopa = createEnnemi(200, 200, x, y, drawable, 10, this);

            koopa.getGif().setOnClickListener(v -> {
                ViewGroup parentView = (ViewGroup) v.getParent();
                koopaDeath.start();
                parentView.removeView(v);
                ennemies.remove(koopa);
                if (ennemies.size() == 0) {
                    countdown.cancel();
                    timer.setText("");
                    ImageView logo = new ImageView(this);
                    logo.setImageResource(R.drawable.kado_logo);
                    ecran.addView(logo);
                    accueil = findViewById(R.id.accueil);
                    accueil.setText("Win \n\n Retour a l'ecran titre");
                    accueil.setTextSize(40);
                    accueil.setOnClickListener(w -> {
                        this.finish();
                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                    });

                }
            });
            ecran.addView(koopa.getGif());
            ennemies.add(koopa);
        }
        
        //Runnable permettant d'actualiser les mouvements toutes les 20 millisecondes
        Runnable movements = new Runnable() {
            public void run() {
                try {
                    updateMovements(ennemies, displayWidth, displayHeight);
                }
                catch (ConcurrentModificationException exception) {
                    // erreur si l'exec prend plus de 20ms
                }
            }
        };
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(movements, 0, 20, TimeUnit.MILLISECONDS);
    }

    //Création du timer
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
                    timer.setText(""+seti(countdown));
                }
            }, del, per);
    }
    
    //Fonction liée au timer, permettant de le mettre à jour
    private final int seti(Timer countdown) {
        //if interval is 1, cancel
        if (sec == 1) {
            countdown.cancel();

            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    endGame(ennemies, ecran, timer, context);
                    TextView accueil = findViewById(R.id.accueil);
                    accueil.setText("Game Over \n\n Retour a l'ecran titre");
                    accueil.setTextSize(40);
                    accueil.setOnClickListener(w -> {
                        finish();
                        Intent intent = new Intent(context, MainActivity.class);
                        startActivity(intent);
                    });
                }
            });
            //endGame();
        }
        return --sec;
    }


    @Override
    protected void onPause() {
        super.onPause();  // Always call the superclass method first
        theme1.stop();
    }
}
