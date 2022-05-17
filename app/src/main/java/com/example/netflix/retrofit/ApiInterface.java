package com.example.netflix.retrofit;

import static com.example.netflix.retrofit.RetrofitClient.BASE_URL;

import com.example.netflix.models.AllCategory;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiInterface {
    @GET(BASE_URL)
    Observable<List<AllCategory>> getAllCategoryMovies();
}
