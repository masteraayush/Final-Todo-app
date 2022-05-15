package com.aayush.journeyjournal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashScreen extends AppCompatActivity {

    // setting time for the screen to be loaded after splash screen to 3s
    private  static int SPLASH_SCREEN = 3000;

    // declaring variables
    Animation topAnim, bottomAnim;
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash_screen);

        //Animations for Splash Screen
        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation);

        //Hooks for Animation in Splash Screen
        image = findViewById(R.id.imageView);

        //Assigning animations to image and text in Splash Screen
        image.setAnimation(topAnim);

        // calling intent to create a Splash Screen
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this,Login.class); // calling the next activity i.e. Homepage/Dashboard
                startActivity(intent);
                finish();
            }
        }, SPLASH_SCREEN);
    }
}