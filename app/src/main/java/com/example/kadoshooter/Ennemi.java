package com.example.kadoshooter;

import android.content.Context;

import pl.droidsonroids.gif.GifImageView;

public class Ennemi {
    private GifImageView gif;
    private Double direction;
    private Context context;
    private float speed;

    public Ennemi(Context context, GifImageView file, Double direction, float speed){
        this.context=context;

        this.gif = file;

        this.direction = direction;

        this.speed = speed;
    }

    public GifImageView getGif() {
        return this.gif;
    }
    public Double getDirection() {
        return this.direction;
    }

    public void setGif(float x, float y) {
        this.gif.setX(x);
        this.gif.setY(y);
    }

    public void setDirection(Double direction) {
        this.direction = direction;
    }

    public String toString() {
        return "[Ennemi X="+this.gif.getX()+" Y="+this.gif.getY()+"]";
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
    public float getSpeed() { return this.speed; }
}
