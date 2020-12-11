package com.dylansalim.qrmenuapp.ui.onboarding;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;
import com.dylansalim.qrmenuapp.R;

public class SplashscreenActivity extends AppCompatActivity {
    ImageView splashLogo;
    LottieAnimationView lottieSplash;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);


        splashLogo = findViewById(R.id.splashLogo);
        lottieSplash = findViewById(R.id.lottieSplash);
        lottieSplash.setAnimation(R.raw.splashscreen_lottie);


        splashLogo.animate().translationY(-4000).setDuration(1300).setStartDelay(1700);
        lottieSplash.animate().translationY(4000).setDuration(1300).setStartDelay(1700);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final Intent splashIntent = new Intent(SplashscreenActivity.this, OnboardingActivity.class);
                SplashscreenActivity.this.startActivity(splashIntent);
                SplashscreenActivity.this.finish();
                overridePendingTransition(R.anim.onboarding_fade_anim, 0);

            }
        }, 2700);


    }


}