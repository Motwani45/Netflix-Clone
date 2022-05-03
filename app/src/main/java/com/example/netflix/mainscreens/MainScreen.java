package com.example.netflix.mainscreens;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.netflix.R;
import com.example.netflix.databinding.ActivityMainScreenBinding;
import com.example.netflix.databinding.MainscreentoolbarBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainScreen extends AppCompatActivity {
    ActivityMainScreenBinding binding;
    MainscreentoolbarBinding mainscreentoolbarBinding;
    Menu menu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        binding=ActivityMainScreenBinding.inflate(getLayoutInflater());
        mainscreentoolbarBinding=MainscreentoolbarBinding.bind(binding.getRoot());
        menu=binding.bottomnaviagtionbar.getMenu();
//        menu.getItem(0).setCheckable(true);
        setContentView(binding.getRoot());
        mainscreentoolbarBinding.moviestooltext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainScreen.this,Movies.class);
                startActivity(intent);
            }
        });
        mainscreentoolbarBinding.tvseriestooltext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainScreen.this,Tvseries.class);
                startActivity(intent);
            }
        });
        binding.bottomnaviagtionbar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemid=item.getItemId();
                switch (itemid){
                    case R.id.homeicon:
                        break;
                    case R.id.searchicon:
                        Intent intent=new Intent(MainScreen.this,Search.class);
                        startActivity(intent);
                        break;
                    case R.id.settingsicon:
                        Intent intent1=new Intent(MainScreen.this,Settings.class);
                        startActivity(intent1);
                        break;
                }

                return false;
            }
        });

    }
}