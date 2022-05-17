package com.example.netflix.mainscreens;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.netflix.R;
import com.example.netflix.adapters.MainRecyclerAdapter;
import com.example.netflix.databinding.ActivityMainScreenBinding;
import com.example.netflix.databinding.MainscreentoolbarBinding;
import com.example.netflix.models.AllCategory;
import com.example.netflix.retrofit.RetrofitClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class MainScreen extends AppCompatActivity {
    ActivityMainScreenBinding binding;
    MainRecyclerAdapter adapter;
    List<AllCategory> allCategories;
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
        ConnectivityManager connectivityManager=(ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        if(networkInfo==null||!networkInfo.isConnected()||!networkInfo.isAvailable()){
            AlertDialog.Builder builder= new AlertDialog.Builder(this);
            builder.setTitle("No Internet Connection");
            builder.setMessage("Please turn on your internet connection to continue.");
            builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    recreate();
                }
            });
            AlertDialog alertDialog=builder.create();
            alertDialog.show();
            alertDialog.setCanceledOnTouchOutside(false);
        }
        else{
            allCategories=new ArrayList<>();
            getAllMovieData();
        }
    }
    public void setMainRecyclerView(List<AllCategory> allCategories){
        binding.mainrecyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));
        adapter=new MainRecyclerAdapter(MainScreen.this,allCategories);
        binding.mainrecyclerview.setAdapter(adapter);
    }

    private void getAllMovieData() {
        CompositeDisposable disposable=new CompositeDisposable();
        disposable.add(RetrofitClient.getRetrofitClient().getAllCategoryMovies().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<AllCategory>>(){

                    @Override
                    public void onNext(List<AllCategory> allCategoryList) {
                        setMainRecyclerView(allCategoryList);

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(MainScreen.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                })

        );
    }
}