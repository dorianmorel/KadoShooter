package com.example.kadoshooter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
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

//Troisième niveau
public class StageThree extends AbstractStage {

    private int nbClick = 0;
    private ImageView button;
    private ArrayList<GifImageView> gifs = new ArrayList<>();
    private ArrayList<Double> directions = new ArrayList<>();
    private ArrayList<Ennemi> ennemies = new ArrayList<>();
    private int displayWidth;
    private int displayHeight;
    private TextView timer;
    private Integer if1noFilter;
    private Runnable hit;
    private ScheduledExecutorService executorHit;
    private MediaPlayer theme3;
    private MediaPlayer bowserDeath;
    private MediaPlayer goombaDeath;
    // TIMER
    private int sec = 26;
    private Timer countdown;
    private float bowserSpeed = (float) 5;
    private float speed = (float) 5;
    private Ennemi bowser;
    Context context = this;
    private RelativeLayout ecran;
    
    //Création du niveau
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_stage_three);
        
        //Lancement du timer
        timer = findViewById(R.id.timer3);
        createTimer();

        // Définition de l'écran
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        displayWidth = displayMetrics.widthPixels;
        displayHeight = displayMetrics.heightPixels;
        ecran = (RelativeLayout) findViewById(R.id.ecran);

        // Musique de fond du niveau
        theme3 = MediaPlayer.create(this, R.raw.theme3);
        theme3.start();
        // Son de mort pour Bowser
        bowserDeath = MediaPlayer.create(this, R.raw.bowser_death);
        // Son de mort pour les goombas
        goombaDeath = MediaPlayer.create(this, R.raw.goomba_death);
        
        //Création du Bowser, boss final
        for (int i=0; i < 1; i++) {
            // angle aléatoire entre 0 et 360°
            int x = (int)(Math.random() * (displayWidth-700));
            int y = (int)(Math.random() * (displayHeight-700));

            // Création du boss en définissant ses paramètres
            bowser = createEnnemi(700, 700, x, y, R.drawable.bowser, bowserSpeed, this);

            //Définition des actions menées lorsque l'on clique sur Bowser
            bowser.getGif().setOnClickListener(v -> {
                if1noFilter = 0;
                hit = new Runnable() {
                    @Override
                    public void run() {
                        if (if1noFilter == 0) {
                            bowser.getGif().setColorFilter(0xFFFF0000, PorterDuff.Mode.MULTIPLY);
                        }
                        else if (if1noFilter == 1) {
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
                
                // Si on clique 100 fois sur Bowser, il meurt
                nbClick++;
                if(nbClick==100){
                    ViewGroup parentView = (ViewGroup) v.getParent();
                    parentView.removeView(v);
                    ennemies.remove(bowser);
                    bowserDeath.start();
                }
                
                // Tous les 5 clics sur Bowser, sa vitesse augmente
                if(nbClick%5==0){
                    bowser.setSpeed(bowser.getSpeed() + 4);
                }

                // Ecran de fin
                if (ennemies.size() == 0) {
                    countdown.cancel();
                    timer.setText("");
                    ImageView logo = new ImageView(this);
                    logo.setImageResource(R.drawable.kado_logo);
                    ecran.addView(logo);
                    TextView accueil = findViewById(R.id.accueil3);
                    accueil.setText("Win \n\n Retour à l'écran titre");
                    accueil.setTextSize(40);
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

        //Création d'un goomba toutes les secondes et demi ainsi que mise à jour de la position des ennemis
        Runnable movements = new Runnable() {
            private int counter = 0;
            public void run() {
                counter+=20;
                try {
                    if (counter%1500 == 0) {
                        if (ennemies.get(0).getSpeed() != 5)
                            createGoomba();
                    }
                    updateMovements(ennemies, displayWidth, displayHeight);
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
    
    private void cancelExecutor() {
        executorHit.shutdown();
    }

    @Override
    protected void onPause() {
        // Arrêter la musique quand on quitte l'activité
        super.onPause();
        theme3.stop();
    }
    
    //On retourne au menu
    private void goBackToMenu() {
        this.finish();
        Intent intent = new Intent(context, MainActivity.class);
        startActivity(intent);
    }
    
    //Creation d'un goomba
    private void createGoomba(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                createGoombaOnUIThread();
            }
        });
    }
    
    //Création d'un goomba, mais dans le thread parent
    private void createGoombaOnUIThread() {
        Ennemi goomba = createEnnemi(300, 300, bowser.getGif().getX(), bowser.getGif().getY(), R.drawable.goomba, speed, this);

        ecran.addView(goomba.getGif());

        ennemies.add(goomba);
        goomba.getGif().setOnClickListener(v -> {
            ViewGroup parentView = (ViewGroup) v.getParent();
            parentView.removeView(v);
            ennemies.remove(goomba);
            goombaDeath.start();
            if (ennemies.size() == 0) {
                countdown.cancel();
                timer.setText("");
                ImageView logo = new ImageView(context);
                logo.setImageResource(R.drawable.kado_logo);
                ecran.addView(logo);
                TextView accueil = findViewById(R.id.accueil3);
                accueil.setText("Win \n\n Retour a l'ecran titre");
                accueil.setTextSize(40);
                accueil.setOnClickListener(w -> {
                    goBackToMenu();
                });
            }
        });
    }
    
    //Création du timer
    private void createTimer() {
        int del = 500;
        int per = 1000;
        countdown = new Timer();
        timer.setTextSize(200);
        countdown.scheduleAtFixedRate(new TimerTask()
        {
            //task to be performed
            public void run()
            {
                timer.setText(""+seti());
            }
        }, del, per);
    }
    
    //Fonction permettant la mise à jour du timer
    private final int seti() {
        //if interval is 1, cancel
        if (sec == 1) {
            countdown.cancel();

            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    endGame(ennemies, ecran, timer, context);
                    TextView accueil = findViewById(R.id.accueil3);
                    accueil.setText("Game Over \n\n Retour a l'ecran titre");
                    accueil.setTextSize(40);
                    accueil.setOnClickListener(w -> {
                        finish();
                        Intent intent = new Intent(context, MainActivity.class);
                        startActivity(intent);
                    });
                }
            });
        }
        return --sec;
    }
}
