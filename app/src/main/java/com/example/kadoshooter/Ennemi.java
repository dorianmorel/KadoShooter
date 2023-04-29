package com.example.kadoshooter;

import android.content.Context;

import pl.droidsonroids.gif.GifImageView;

//Classe correspondant à un ennemi, quel qu'il soit
public class Ennemi {
    private GifImageView gif;
    private Double direction;
    private Context context;
    private float speed;

    //Création de l'ennemi grâce au gif associé, à sa direction, et sa vitesse initiale
    public Ennemi(Context context, GifImageView file, Double direction, float speed){
        this.context=context;
        this.gif = file;
        this.direction = direction;
        this.speed = speed;
    }
    
    //Retourne le gif
    public GifImageView getGif() {
        return this.gif;
    }
    
    //Retourne la direction
    public Double getDirection() {
        return this.direction;
    }
    
    //Donne un fichier Gif à l'ennemi
    public void setGif(float x, float y) {
        this.gif.setX(x);
        this.gif.setY(y);
    }
    
    //Donne une direction à l'ennemi
    public void setDirection(Double direction) {
        this.direction = direction;
    }
    
    public String toString() {
        return "[Ennemi X="+this.gif.getX()+" Y="+this.gif.getY()+"]";
    }
    
    //Modification de la vitesse de l'ennemi
    public void setSpeed(float speed) {
        this.speed = speed;
    }
    
    //Retourne la vitesse
    public float getSpeed() { return this.speed; }
}
