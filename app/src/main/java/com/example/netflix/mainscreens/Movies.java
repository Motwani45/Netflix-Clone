package com.example.netflix.mainscreens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.netflix.R;
import com.example.netflix.databinding.ActivityMoviesBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Movies extends AppCompatActivity {
ActivityMoviesBinding binding;
Menu menu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        binding=ActivityMoviesBinding.inflate(getLayoutInflater());
        binding.bottomnaviagtionbar.getMenu().getItem(0).setChecked(false);
        setContentView(binding.getRoot());
        binding.bottomnaviagtionbar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemid=item.getItemId();
                switch (itemid){
                    case R.id.homeicon:
                        Intent intent2=new Intent(Movies.this,MainScreen.class);
                        startActivity(intent2);
                        break;
                    case R.id.searchicon:
                        Intent intent=new Intent(Movies.this,Search.class);
                        startActivity(intent);
                        break;
                    case R.id.settingsicon:
                        Intent intent1=new Intent(Movies.this,Settings.class);
                        startActivity(intent1);
                        break;
                }

                return false;
            }
        });
    }
}