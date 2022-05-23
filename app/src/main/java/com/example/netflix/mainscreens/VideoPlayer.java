package com.example.netflix.mainscreens;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;

import com.example.netflix.R;
import com.example.netflix.databinding.ActivityVideoPlayerBinding;
import com.example.netflix.databinding.CustomControllerBinding;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DeviceInfo;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.MediaMetadata;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.TracksInfo;
import com.google.android.exoplayer2.audio.AudioAttributes;
import com.google.android.exoplayer2.extractor.Extractor;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.text.Cue;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionParameters;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.VideoSize;

import java.util.List;

public class VideoPlayer extends AppCompatActivity {
    ActivityVideoPlayerBinding binding;
    CustomControllerBinding customControllerBinding;
    ExoPlayer exoplayer;
    boolean flag = false;
    boolean lock=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVideoPlayerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        customControllerBinding = CustomControllerBinding.bind(binding.getRoot());
        binding.progressBar.setVisibility(View.GONE);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        setUpExoplayer(getIntent().getStringExtra("url"));
//        LoadControl loadControl = new DefaultLoadControl();

    }

    private void setUpExoplayer(String url) {
        exoplayer = new ExoPlayer.Builder(getApplicationContext()).build();
        binding.exoplayer.setPlayer(exoplayer);
        MediaItem mediaItem = MediaItem.fromUri(url);
        exoplayer.setMediaItem(mediaItem);
        exoplayer.prepare();
//        exoplayer.play();
        exoplayer.setPlayWhenReady(true);
        exoplayer.addListener(new Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int playbackState) {
                Player.Listener.super.onPlaybackStateChanged(playbackState);
                if (playbackState == Player.STATE_BUFFERING) {
//                    binding.progressBar.setVisibility(View.VISIBLE);
                } else if (playbackState == Player.STATE_READY) {
//                    binding.progressBar.setVisibility(View.GONE);
                }
            }
        });
        customControllerBinding.btFullscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag) {
                    customControllerBinding.btFullscreen.setImageDrawable(getDrawable(R.drawable.ic_baseline_fullscreen_24));
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    flag = false;
                } else {
                    customControllerBinding.btFullscreen.setImageDrawable(getDrawable(R.drawable.ic_baseline_fullscreen_exit_24));
                    flag = true;
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                }
            }
        });
        customControllerBinding.exoFfwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exoplayer.seekTo(exoplayer.getCurrentPosition()+10000);
            }
        });customControllerBinding.exoRew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exoplayer.seekTo(exoplayer.getCurrentPosition()-10000);
            }
        });
        customControllerBinding.exoLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             if(!lock){
                 customControllerBinding.exoLock.setImageDrawable(getDrawable(R.drawable.ic_baseline_lock_24));
                 lock=true;
                 customControllerBinding.llcontroller.setVisibility(View.GONE);
                 customControllerBinding.lltimestamp.setVisibility(View.GONE);
                 customControllerBinding.exoProgress.setVisibility(View.GONE);

             }
             else{
                 lock=false;
                 customControllerBinding.exoLock.setImageDrawable(getDrawable(R.drawable.ic_baseline_lock_open_24));
                 customControllerBinding.llcontroller.setVisibility(View.VISIBLE);
                 customControllerBinding.lltimestamp.setVisibility(View.VISIBLE);
                 customControllerBinding.exoProgress.setVisibility(View.VISIBLE);
             }

            }
        });
    }

    private void lockScreen(boolean lock) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        exoplayer.setPlayWhenReady(false);
        exoplayer.getPlaybackState();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        exoplayer.setPlayWhenReady(false);
        exoplayer.getPlaybackState();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        exoplayer.release();
    }
}