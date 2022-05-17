package com.example.netflix.mainscreens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.netflix.R;
import com.example.netflix.databinding.ActivityMovieDetailsBinding;

public class MovieDetails extends AppCompatActivity {
    ActivityMovieDetailsBinding binding;
    String name,image,fileurl,moviesid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMovieDetailsBinding.inflate(getLayoutInflater());
        getSupportActionBar().hide();
        setContentView(binding.getRoot());
        moviesid=getIntent().getStringExtra("movieId");
        name=getIntent().getStringExtra("movieName");
        image=getIntent().getStringExtra("movieImageUrl");
        fileurl=getIntent().getStringExtra("movieFile");
        Glide.with(this).load(image).into(binding.imagedeatils);
        binding.moviename.setText(name);
        binding.playbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(MovieDetails.this,VideoPlayer.class);
                i.putExtra("url",fileurl);
                startActivity(i);
            }
        });
    }
}