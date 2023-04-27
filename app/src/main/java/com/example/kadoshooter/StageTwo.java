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

//Deuxième niveau
public class StageTwo extends AbstractStage {

    private int nbClick = 0;
    private ArrayList<Ennemi> ennemies = new ArrayList<>();
    private int displayWidth;
    private int displayHeight;

    private TextView timer;
    private RelativeLayout ecran;
    private TextView accueil;

    // TIMER
    private int sec = 16;
    private Timer countdown;

    private MediaPlayer theme2;

    //Création du niveau
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getSupportActionBar().hide();

        setContentView(R.layout.activity_stage_two);
        
        //Création du timer
        timer = findViewById(R.id.timer2);
        createTimer();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        displayWidth = displayMetrics.widthPixels;
        displayHeight = displayMetrics.heightPixels;

        ecran = (RelativeLayout) findViewById(R.id.ecran);

        theme2 = MediaPlayer.create(this, R.raw.theme2);
        theme2.start();
        
        //Création des 15 goombas
        for (int i=0; i < 15; i++) {
            MediaPlayer goombaDeath = MediaPlayer.create(this, R.raw.goomba_death);
            int x = (int)(Math.random() * (displayWidth-300));
            int y = (int)(Math.random() * (displayHeight-300));

            Ennemi ennemi = createEnnemi(300, 300, x, y, R.drawable.goomba, 5, this);
            
            //Définition des actions menées lorsque l'on clique sur un Goomba
            ennemi.getGif().setOnClickListener(v -> {
                ViewGroup parentView = (ViewGroup) v.getParent();
                parentView.removeView(v);
                goombaDeath.start();
                ennemies.remove(ennemi);
                
                //Création de 2 plus petits goombas (simulation d'une division en 2 du goomba initial)
                Ennemi ennemi1 = createEnnemi(150, 150, ennemi.getGif().getX(), ennemi.getGif().getY(), R.drawable.goomba, 5, this);
                ecran.addView(ennemi1.getGif());
                ennemies.add(ennemi1);
                Ennemi ennemi2 = createEnnemi(150, 150, ennemi.getGif().getX(), ennemi.getGif().getY(), R.drawable.goomba, 5, this);
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
                    accueil.setText("Win \n\n Retour a l'ecran titre");
                    accueil.setTextSize(40);
                    accueil.setOnClickListener(w -> {
                        this.finish();
                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                    });
                }
            });

            ecran.addView(ennemi.getGif());
            ennemies.add(ennemi);
        }


        //Runnable permettant de mettre à jour la position des ennemis
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
                timer.setText(""+seti());
            }
        }, del, per);
        //set interval
    }
    
    //Mise à jour du timer
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

    //Fin de la partie
    private void endGame() {
        if (ennemies.size() != 0) {
            for (Ennemi ennemi : ennemies) {
                ecran.removeView(ennemi.getGif());
            }
        }
        countdown.cancel();
        timer.setText("");
        ImageView logo = new ImageView(this);
        logo.setImageResource(R.drawable.kado_logo);
        ecran.addView(logo);
        TextView accueil = findViewById(R.id.accueil2);
        accueil.setText("Game Over \n\n Retour a l'ecran titre");
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
        theme2.stop();
    }
}
