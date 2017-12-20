package com.android.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.Activity_Fragment.PostsOnRequestActivity;
import com.android.Global.AppConfig;
import com.android.Global.GlobalStaticData;
import com.android.Models.Post;
import com.android.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ngoc Vu on 12/18/2017.
 */

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.Viewholder> {
    Context context;
    List<String> listTag = new ArrayList<>();
    DatabaseReference databaseReference;
    public TagAdapter(Context context, List<String> listTag) {
        this.context = context;
        this.listTag = listTag;
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Viewholder(LayoutInflater.from(context).inflate(R.layout.item_tag,parent,false));
    }

    @Override
    public void onBindViewHolder(Viewholder holder, final int position) {
        /*holder.textViewTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "clicked", Toast.LENGTH_SHORT).show();
            }
        });*/
        final String tag = listTag.get(position);
        holder.textViewName.setText("#"+ tag);

        holder.textViewName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child(AppConfig.FIREBASE_FIELD_POSTS).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        GlobalStaticData.listPostOnReQuest_Tag = new ArrayList<Post>();
                        for(DataSnapshot dataPost:dataSnapshot.getChildren()){
                            Post post = dataPost.getValue(Post.class);
                            if(post.getTags().contains(tag))
                                GlobalStaticData.listPostOnReQuest_Tag.add(dataPost.getValue(Post.class));
                        }
                        Intent intent = new Intent(context, PostsOnRequestActivity.class);
                        intent.putExtra(AppConfig.BARNAME,"#"+ tag);
                        intent.putExtra(AppConfig.LISTPOST, (ArrayList)GlobalStaticData.listPostOnReQuest_Tag);
                        context.startActivity(intent);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });
    }

    @Override
    public int getItemCount() {
        return listTag==null?0:listTag.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void setData(List<String> listTag){
        this.listTag.clear();
        this.listTag.addAll(listTag);
        notifyDataSetChanged();
    }

    static class Viewholder extends RecyclerView.ViewHolder{
        View v;
        TextView textViewName;

        public Viewholder(View itemView) {
            super(itemView);
            this.v = itemView;
            textViewName = (TextView) v.findViewById(R.id.textViewName);
        }
    }
}
