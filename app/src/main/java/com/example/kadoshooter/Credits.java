package com.example.kadoshooter;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Window;

//Cette activité correspond à la page des Crédits du jeu
public class Credits extends AppCompatActivity {
    
    private MediaPlayer creditsTheme;
    @Override
    
    
    //On crée l'écran et on lance la musique
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        creditsTheme = MediaPlayer.create(this, R.raw.credits);
        creditsTheme.start();

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getSupportActionBar().hide();

        setContentView(R.layout.activity_credits);
    }
    
    //Met en pause la musique si on quitte l'activité
    @Override
    protected void onPause() {
        super.onPause();
        creditsTheme.stop();
    }
}
