package com.example.testmovement;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import pl.droidsonroids.gif.GifImageView;

public class MainActivity extends AppCompatActivity {

    private int nbClick = 0;
    private ImageView button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RelativeLayout ecran = (RelativeLayout) findViewById(R.id.ecran);
        GifImageView gif1 = new GifImageView(this);
        gif1.setImageResource(R.drawable.koopa);
        gif1.setLayoutParams(new RelativeLayout.LayoutParams(300,300));
        RelativeLayout.LayoutParams layoutParams1 =
                (RelativeLayout.LayoutParams)gif1.getLayoutParams();
        layoutParams1.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        layoutParams1.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        gif1.setLayoutParams(layoutParams1);
        gif1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                v.setVisibility(View.GONE);
            }
        });

        GifImageView gif2 = new GifImageView(this);
        gif2.setImageResource(R.drawable.koopa);
        gif2.setLayoutParams(new RelativeLayout.LayoutParams(300,300));
        RelativeLayout.LayoutParams layoutParams2 =
                (RelativeLayout.LayoutParams)gif2.getLayoutParams();
        layoutParams2.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        layoutParams2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        gif2.setLayoutParams(layoutParams2);
        gif2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                v.setVisibility(View.GONE);
            }
        });

        GifImageView gif3 = new GifImageView(this);
        gif3.setImageResource(R.drawable.koopa);
        gif3.setLayoutParams(new RelativeLayout.LayoutParams(300,300));
        RelativeLayout.LayoutParams layoutParams3 =
                (RelativeLayout.LayoutParams)gif3.getLayoutParams();
        layoutParams3.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        layoutParams3.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        gif3.setLayoutParams(layoutParams3);
        gif3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                v.setVisibility(View.GONE);
            }
        });

        GifImageView gif4 = new GifImageView(this);
        gif4.setImageResource(R.drawable.koopa);
        gif4.setLayoutParams(new RelativeLayout.LayoutParams(300,300));
        RelativeLayout.LayoutParams layoutParams4 =
                (RelativeLayout.LayoutParams)gif4.getLayoutParams();
        layoutParams4.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        layoutParams4.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        gif4.setLayoutParams(layoutParams4);
        gif4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                v.setVisibility(View.GONE);
            }
        });

        ecran.addView(gif1);
        ecran.addView(gif2);
        ecran.addView(gif3);
        ecran.addView(gif4);

        Runnable helloRunnable = new Runnable() {
            public void run() {
                gif1.setTop(gif1.getTop()+1);
                gif1.setBottom(gif1.getBottom()+1);
                gif1.setLeft(gif1.getLeft()+1);
                gif1.setRight(gif1.getRight()+1);

                gif2.setTop(gif2.getTop()+1);
                gif2.setBottom(gif2.getBottom()+1);
                gif2.setLeft(gif2.getLeft()-1);
                gif2.setRight(gif2.getRight()-1);

                gif3.setTop(gif3.getTop()-1);
                gif3.setBottom(gif3.getBottom()-1);
                gif3.setLeft(gif3.getLeft()-1);
                gif3.setRight(gif3.getRight()-1);

                gif4.setTop(gif4.getTop()-1);
                gif4.setBottom(gif4.getBottom()-1);
                gif4.setLeft(gif4.getLeft()+1);
                gif4.setRight(gif4.getRight()+1);
            }
        };

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(helloRunnable, 0, 50, TimeUnit.MILLISECONDS);
    }

    public void move(View v) {
        System.out.println("CLICK");

        ViewGroup parentView = (ViewGroup) v.getParent();
        parentView.removeView(v);
    }
}