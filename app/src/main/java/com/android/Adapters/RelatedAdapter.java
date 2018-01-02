package com.android.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.Activity_Fragment.PostDetailActivity;
import com.android.Global.AppConfig;
import com.android.Models.Post;
import com.android.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ngoc Vu on 12/18/2017.
 */

public class RelatedAdapter extends RecyclerView.Adapter<RelatedAdapter.ViewHolder>{
    Context context;
    List<Post> listPost = new ArrayList<>();

    public RelatedAdapter(Context context, List<Post> listPost) {
        this.context = context;
        this.listPost = listPost;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_related,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position){
        final Post postModel = listPost.get(position);
        holder.textViewTitile.setText(postModel.getTitle());
        holder.linearLayoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PostDetailActivity.class);
                intent.putExtra(AppConfig.POST,postModel);
                context.startActivity(intent);
            }
        });
        try{
            Glide.with(context).load(postModel.getImg()).into(holder.imageViewCover);
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(listPost.get(position).getPostId());
    }


    @Override
    public int getItemCount() {
        return listPost ==null ? 0: listPost.size();
    }


    public void setData(List<Post> listPost){
        this.listPost.clear();
        this.listPost.addAll(listPost);
        notifyDataSetChanged();
    }


    static class ViewHolder extends RecyclerView.ViewHolder{
        View v;
        LinearLayout linearLayoutItem;
        ImageView imageViewCover;
        TextView textViewTitile;
        public ViewHolder( View v) {
            super(v);
            this.v = v;
            linearLayoutItem = (LinearLayout) v.findViewById(R.id.linearLayoutItem);
            imageViewCover = (ImageView) v.findViewById(R.id.imageViewCover);
            textViewTitile = (TextView) v.findViewById(R.id.textViewBarName);
        }
    }
}
