package com.android.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.Interface.IOnClickCategory;
import com.android.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ngoc Vu on 12/18/2017.
 */

public class CategoryAdapter  extends RecyclerView.Adapter<CategoryAdapter.ViewHolder>{
    Context context;
    List<String> listCategory = new ArrayList<>();
    IOnClickCategory iOnClickCategory;
    DatabaseReference databaseReference;

    public CategoryAdapter(Context context, List<String> listCategory) {
        this.context = context;
        this.listCategory = listCategory;
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public void setiOnClickCategory(IOnClickCategory iOnClickCategory){
        this.iOnClickCategory=iOnClickCategory;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_category,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final String category = listCategory.get(position);
        holder.textViewCategory.setText(category);
        holder.textViewCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iOnClickCategory.onClickCategory(category);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listCategory==null?0:listCategory.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void setData(List<String> listData)
    {
        listCategory.clear();
        listCategory.addAll(listData);
        notifyDataSetChanged();
    }


    static  class ViewHolder extends RecyclerView.ViewHolder{
        View itemView;
        TextView textViewCategory;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            textViewCategory = (TextView) itemView.findViewById(R.id.textViewCategory);
        }
    }
}
