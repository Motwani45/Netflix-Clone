package com.example.netflix.mainscreens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
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
import com.example.netflix.adapters.SearchRecyclerAdapter;
import com.example.netflix.databinding.ActivitySearchBinding;
import com.example.netflix.databinding.ActivitySettingsBinding;
import com.example.netflix.models.AllCategory;
import com.example.netflix.retrofit.RetrofitClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class Search extends AppCompatActivity {
    ActivitySearchBinding binding;
    SearchRecyclerAdapter adapter;
    List<AllCategory> allCategories;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySearchBinding.inflate(getLayoutInflater());
        binding.bottomnaviagtionbar.getMenu().getItem(1).setChecked(true);
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        binding.searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                return false;
            }
        });
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
        binding.searchrecyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));
        adapter=new SearchRecyclerAdapter(Search.this,allCategories);
        binding.searchrecyclerview.setAdapter(adapter);
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
                        Toast.makeText(Search.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                })

        );
    }
}