package com.example.netflix.mainscreens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.netflix.R;
import com.example.netflix.databinding.ActivitySettingsBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Settings extends AppCompatActivity {
    ActivitySettingsBinding binding;
    Menu menu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySettingsBinding.inflate(getLayoutInflater());
        binding.bottomnaviagtionbar.getMenu().getItem(2).setChecked(true);
        getSupportActionBar().hide();
        setContentView(binding.getRoot());
        binding.bottomnaviagtionbar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemid=item.getItemId();
                switch (itemid){
                    case R.id.homeicon:
                        Intent intent2=new Intent(Settings.this,MainScreen.class);
                        startActivity(intent2);
                        break;
                    case R.id.searchicon:
                        Intent intent=new Intent(Settings.this,Search.class);
                        startActivity(intent);
                        break;
                    case R.id.settingsicon:
                        break;
                }

                return false;
            }
        });
    }
}