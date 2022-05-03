package com.example.netflix.mainscreens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.netflix.R;
import com.example.netflix.databinding.ActivitySearchBinding;
import com.example.netflix.databinding.ActivitySettingsBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Search extends AppCompatActivity {
    ActivitySearchBinding binding;
    Menu menu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySearchBinding.inflate(getLayoutInflater());
        binding.bottomnaviagtionbar.getMenu().getItem(1).setChecked(true);
        setContentView(binding.getRoot());
        binding.bottomnaviagtionbar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemid=item.getItemId();
                switch (itemid){
                    case R.id.homeicon:
                        Intent intent2=new Intent(Search.this,MainScreen.class);
                        startActivity(intent2);
                        break;
                    case R.id.searchicon:
                        break;
                    case R.id.settingsicon:
                        Intent intent1=new Intent(Search.this,Settings.class);
                        startActivity(intent1);
                        break;
                }

                return false;
            }
        });
    }
}