package com.example.netflix.mainscreens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.netflix.R;
import com.example.netflix.adapters.MainRecyclerAdapter;
import com.example.netflix.databinding.ActivityMoviesBinding;
import com.example.netflix.models.AllCategory;
import com.example.netflix.retrofit.RetrofitClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class Movies extends AppCompatActivity {
ActivityMoviesBinding binding;
    MainRecyclerAdapter adapter;
    List<AllCategory> allCategories;
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
        binding.moviesrecyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));
        adapter=new MainRecyclerAdapter(Movies.this,allCategories);
        binding.moviesrecyclerview.setAdapter(adapter);
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
                        Toast.makeText(Movies.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                })

        );
    }
}