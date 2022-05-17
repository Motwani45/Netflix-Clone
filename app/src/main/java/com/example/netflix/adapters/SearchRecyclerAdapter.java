package com.example.netflix.adapters;

import android.content.Context;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.netflix.R;
import com.example.netflix.models.AllCategory;
import com.example.netflix.models.CategoryItemList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchRecyclerAdapter extends RecyclerView.Adapter<SearchRecyclerAdapter.ViewHolder> implements Filterable {
    Context context;
    List<AllCategory> allCategoryList;
    List<CategoryItemList> itemLists=new ArrayList<>();
    List<AllCategory> fullist;
    public SearchRecyclerAdapter(Context context, List<AllCategory> allCategoryList) {
        this.context = context;
        this.allCategoryList = allCategoryList;
        this.fullist = new ArrayList<>(allCategoryList);
        for (AllCategory i:
             allCategoryList) {
            itemLists.addAll(i.getCategoryItemList());
        }
    }
    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.searchrecyclerlayout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textView.setText(allCategoryList.get(position).getCategoryTitle());
        setItemRecycler(holder.itemRecycler,allCategoryList.get(position).getCategoryItemList());
    }



    @Override
    public int getItemCount() {
        return allCategoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RecyclerView itemRecycler;
        TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.searchitemcategory);
            itemRecycler=itemView.findViewById(R.id.searchlayoutrecyclerview);
        }
    }
    private Filter exampleFilter=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<AllCategory> filteredList= new ArrayList<>();
            if(charSequence==null|| charSequence.length()==0){
                filteredList.addAll(fullist);

            }
            else{
                String filterpattern = charSequence.toString().toLowerCase().trim();
                for(AllCategory item:fullist){
                    if(item.getCategoryTitle().toLowerCase().contains(filterpattern)){
                        filteredList.add(item);
                    }
                }
//                for(CategoryItemList item:itemLists){
//                   if(item.getMovieName().toLowerCase().contains(filterpattern)){
//                       filteredList.add(item);
//                   }
//                }


            }
            FilterResults results=new FilterResults();
            results.values=filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            allCategoryList.clear();
            allCategoryList.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };
    public void setItemRecycler(RecyclerView recyclerView,List<CategoryItemList> categoryItem){
        ItemRecyclerAdapter itemRecyclerAdapter=new ItemRecyclerAdapter(context,categoryItem);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL,false));
        recyclerView.setAdapter(itemRecyclerAdapter);

    }
}
