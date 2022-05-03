package com.example.netflix.mainscreens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.netflix.R;
import com.example.netflix.databinding.ActivityTvseriesBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Tvseries extends AppCompatActivity {
    ActivityTvseriesBinding binding;
    Menu menu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        binding=ActivityTvseriesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.bottomnaviagtionbar.getMenu().getItem(0).setChecked(false);
        binding.bottomnaviagtionbar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemid=item.getItemId();
                switch (itemid){
                    case R.id.homeicon:
                        Intent intent2=new Intent(Tvseries.this,MainScreen.class);
                        startActivity(intent2);
                        break;
                    case R.id.searchicon:
                        Intent intent=new Intent(Tvseries.this,Search.class);
                        startActivity(intent);
                        break;
                    case R.id.settingsicon:
                        Intent intent1=new Intent(Tvseries.this,Settings.class);
                        startActivity(intent1);
                        break;
                }

                return false;
            }
        });
    }
}