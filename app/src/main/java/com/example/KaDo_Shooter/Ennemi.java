package com.example.KaDo_Shooter;

import android.content.Context;

import pl.droidsonroids.gif.GifImageView;

public class Ennemi {
    private GifImageView gif;
    private Double direction;
    private Context context;

    public Ennemi(Context context, GifImageView file, Double direction){
        this.context=context;

        this.gif = file;

        this.direction = direction;
    }

    public GifImageView getGif() {
        return this.gif;
    }
    public Double getDirection() {
        return this.direction;
    }

    public void setGif(Float x, Float y) {
        this.gif.setX(x);
        this.gif.setY(y);
    }

    public void setDirection(Double direction) {
        this.direction = direction;
    }
}
