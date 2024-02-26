package com.bilir.trivialand;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {

    ImageView emblem;
    TextView splashTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        emblem = findViewById(R.id.imageViewSplash);
        splashTitle = findViewById(R.id.logoTitleSplash);

        // Animasyon kaynağımızı Java tarafına bağladık
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animation_spash);

        // Objemize animasyonu ekledik
        splashTitle.startAnimation(animation);

        // startAnimation (diğer işlemlerin yplması için) bitmeyi beklemez. Ondan dolayı delay.
        new Handler().postDelayed(new Runnable() { // Normalde bu da beklemez. run() a yazmalı.
            @Override
            public void run() {

                Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, 1500); // delay (ms)
    }
}