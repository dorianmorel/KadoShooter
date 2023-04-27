package com.example.kadoshooter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import pl.droidsonroids.gif.GifImageView;

public abstract class AbstractStage extends AppCompatActivity {
    
    // Fonction permettant de créer un ennemi par la création d'une ImageView, mais avec un gif
    public static Ennemi createEnnemi(int width, int height, float x, float y, int file, float spd, Context context) {
        GifImageView gif = new GifImageView(context);
        gif.setImageResource(file);
        gif.setLayoutParams(new RelativeLayout.LayoutParams(width,height));
        double direction = degree2Radian(Math.random() * 360); // angle aléatoire entre 0 et 360°
        gif.setX(x);
        gif.setY(y);
        Ennemi ennemi = new Ennemi(context, gif, direction, spd);
        return ennemi;
    }
    
    // Fonction qui convertit un angle radian, en degré
    public static double degree2Radian(double degree) {
        return degree * Math.PI / 180;
    }
    
    //Fonction qui met à jour la position d'un ennemi, et la direction dans laquelle il se dirige, si nécessaire de la changer (collisions comprises)
    public static void updateMovements(ArrayList<Ennemi> ennemies, int displayWidth, int displayHeight){
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
    
    //Fonction permettant de lancer le processus de fin d'un niveau, lorsque l'on perd / gagne
    public static void endGame(ArrayList<Ennemi> ennemies, RelativeLayout ecran, TextView timer, Context context) {

        for (Ennemi ennemi : ennemies) {
            ecran.removeView(ennemi.getGif());
        }
        timer.setText("");
        ImageView logo = new ImageView(context);
        logo.setImageResource(R.drawable.kado_logo);
        ecran.addView(logo);
    }

}
