package com.example.netflix.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.example.netflix.databinding.ActivitySplashScreenBinding;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreen extends AppCompatActivity {
    ActivitySplashScreenBinding binding;
    static int counter=0;
    static int duration=5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        binding=ActivitySplashScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        progress();
        start();
    }
    public void progress(){
        final Timer timer=new Timer();
        TimerTask timerTask=new TimerTask() {
            @Override
            public void run() {
                while(counter<=duration){
                    counter++;
                    binding.progressbar.setProgress(counter);
                }
                timer.cancel();
            }
        };
        timer.schedule(timerTask,0,100);
    }
    public void start(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                binding.progressbar.setVisibility(View.VISIBLE);
                Intent intent=new Intent(SplashScreen.this,SignInActivity.class);
                startActivity(intent);
                finish();
            }
        },duration);
    }
}