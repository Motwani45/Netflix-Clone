package com.example.netflix.adapters;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.netflix.R;
import com.example.netflix.mainscreens.MovieDetails;
import com.example.netflix.models.AllCategory;
import com.example.netflix.models.CategoryItemList;

import java.util.List;

public class MainRecyclerAdapter extends RecyclerView.Adapter<MainRecyclerAdapter.ViewHolder> {
    Context context;
    List<AllCategory> allCategories;


    public MainRecyclerAdapter(Context context, List<AllCategory> allCategories) {
        this.context = context;
        this.allCategories = allCategories;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.mainrecyclerlayout,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textView.setText(allCategories.get(position).getCategoryTitle());
        setItemRecycler(holder.recyclerView,allCategories.get(position).getCategoryItemList());
    }

    @Override
    public int getItemCount() {
        return allCategories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;
        TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.itemcategory);
            recyclerView=itemView.findViewById(R.id.itemrecyclerview);

        }
    }
    public void setItemRecycler(RecyclerView recyclerView,List<CategoryItemList> categoryItem){
        ItemRecyclerAdapter itemRecyclerAdapter=new ItemRecyclerAdapter(context,categoryItem);
        recyclerView.setLayoutManager(new LinearLayoutManager(context,RecyclerView.HORIZONTAL,false));
        recyclerView.setAdapter(itemRecyclerAdapter);

    }
}
