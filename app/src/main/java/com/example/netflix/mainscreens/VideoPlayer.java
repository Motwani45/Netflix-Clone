package com.example.netflix.mainscreens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.example.netflix.R;
import com.example.netflix.databinding.ActivityVideoPlayerBinding;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.Extractor;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class VideoPlayer extends AppCompatActivity {
    ActivityVideoPlayerBinding binding;
    ExoPlayer exoplayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityVideoPlayerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        setUpExoplayer(getIntent().getStringExtra("url"));
    }

    private void setUpExoplayer(String url) {
        exoplayer=new ExoPlayer.Builder(getApplicationContext()).build();
        binding.exoplayer.setPlayer(exoplayer);
        MediaItem mediaItem = MediaItem.fromUri(url);
        exoplayer.setMediaItem(mediaItem);
        exoplayer.prepare();
        exoplayer.play();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        exoplayer.release();
    }
}