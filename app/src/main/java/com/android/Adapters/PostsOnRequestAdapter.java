package com.android.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.Activity_Fragment.PostDetailActivity;
import com.android.Global.AppConfig;
import com.android.Global.AppPreferences;
import com.android.Global.GlobalFunction;
import com.android.Login;
import com.android.Models.Post;
import com.android.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

public class PostsOnRequestAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    Animation animation0to180,animation180to0;
    List<Post> listPost = new ArrayList<>();
    Animation hyperspaceJumpAnimation;
    DatabaseReference databaseReference;
    AppPreferences appPreferences;
    public PostsOnRequestAdapter(Context context, List<Post> listPost) {
        this.context = context;
        this.listPost = listPost;
        animation0to180 = AnimationUtils.loadAnimation(context, R.anim.rotate_iconexpand_0to180);
        animation180to0 = AnimationUtils.loadAnimation(context, R.anim.rotate_iconexpand_180to0);
        hyperspaceJumpAnimation = AnimationUtils.loadAnimation(context, R.anim.animlike);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        appPreferences = AppPreferences.getInstance(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=null;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_summary,parent,false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        final Post post = listPost.get(position);
        final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        //imageCover
        Glide.with(context).load(post.getImg()).into(itemViewHolder.imageViewCover);
        itemViewHolder.textViewTitile.setText(post.getTitle());

        itemViewHolder.textViewCategory.setText(post.getcategory());

        itemViewHolder.textViewDescription.setText(post.getDescription());
        checkLiked(post, itemViewHolder.imageViewLike, itemViewHolder.textViewLike);

        if(post.getComments()!=null && post.getComments().size()>0)
            itemViewHolder.textViewComment.setText(String.valueOf(post.getComments().size()));
        else
            itemViewHolder.textViewComment.setText("0");

        if(post.getUserShareIds()!=null && post.getUserShareIds().size()>0)
            itemViewHolder.textViewShare.setText(String.valueOf(post.getUserShareIds().size()));
        else
            itemViewHolder.textViewShare.setText("0");
        itemViewHolder.textViewTimeAgo.setText(GlobalFunction.calculateTimeAgo(post.getDateCreate()));

        itemViewHolder.relativeLayoutSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PostDetailActivity.class);
                intent.putExtra(AppConfig.POST, post);
                context.startActivity(intent);
            }
        });


        itemViewHolder.relativeLayoutSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PostDetailActivity.class);
                intent.putExtra(AppConfig.POST, post);
                context.startActivity(intent);
            }
        });


        itemViewHolder.linearLayoutLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickLikePost(post, itemViewHolder.imageViewLike);
            }
        });
        itemViewHolder.linearLayoutComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PostDetailActivity.class);
                intent.putExtra(AppConfig.POST, post);
                intent.putExtra(AppConfig.ACTION, AppConfig.COMMENT);
                context.startActivity(intent);
            }
        });
        itemViewHolder.linearLayoutShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    private void checkLiked(final Post post, final ImageView imageViewLike, final TextView textViewLike){
        databaseReference.child(AppConfig.FIREBASE_FIELD_POSTS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String userId = appPreferences.getUserId();
                long count  = dataSnapshot.child(post.getPostId()).child(AppConfig.FIREBASE_FIELD_USERLIKEIDS).getChildrenCount();
                if(post.getUserLikeIds()!=null && count>0) {
                    if (post.getUserLikeIds().contains(userId)) {
                        imageViewLike.setImageResource(R.drawable.ic_liked);
                    } else {
                        imageViewLike.setImageResource(R.drawable.ic_like);
                    }
                    textViewLike.setText(String.valueOf(count));
                }
                else {
                    imageViewLike.setImageResource(R.drawable.ic_like);
                    textViewLike.setText("0");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void onClickLikePost(final Post post, final ImageView imageViewLike){
        if(appPreferences.isLogin()) {
            //get from user_post
            String userId = appPreferences.getUserId();
            if (post.getUserLikeIds() != null && post.getUserLikeIds().size() > 0) {
                //if user liked this post
                if (post.getUserLikeIds().contains(userId)) {
                    post.getUserLikeIds().remove(userId);
                    databaseReference.child(AppConfig.FIREBASE_FIELD_POSTS).child(post.getPostId())
                            .child(AppConfig.FIREBASE_FIELD_USERLIKEIDS).setValue(post.getUserLikeIds());
                } else {
                    post.getUserLikeIds().add(userId);
                    databaseReference.child(AppConfig.FIREBASE_FIELD_POSTS).child(post.getPostId())
                            .child(AppConfig.FIREBASE_FIELD_USERLIKEIDS).setValue(post.getUserLikeIds());
                }
            } else {
                post.setUserLikeIds(new ArrayList<String>());
                post.getUserLikeIds().add(userId);
                databaseReference.child(AppConfig.FIREBASE_FIELD_POSTS).child(post.getPostId())
                        .child(AppConfig.FIREBASE_FIELD_USERLIKEIDS).child("0").setValue(String.valueOf(userId)).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        imageViewLike.startAnimation(hyperspaceJumpAnimation);
                    }
                });
            }
        }
        else{
            Intent intentLogin = new Intent(context,Login.class);
            context.startActivity(intentLogin);
        }

    }

    @Override
    public int getItemCount() {
        return listPost ==null ? 0: listPost.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }



    public void setData(List<Post> listPost) {
        this.listPost.clear();
        this.listPost.addAll(listPost);
        notifyDataSetChanged();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder{
        View v;
        RelativeLayout relativeLayoutSummary;
        ImageView imageViewCover;
        TextView textViewTitile,textViewCategory,textViewDescription;
        LinearLayout linearLayoutLike;
        ImageView imageViewLike;
        TextView textViewLike;
        LinearLayout linearLayoutComment;
        TextView textViewComment;
        LinearLayout linearLayoutShare;
        TextView textViewShare;
        TextView textViewTimeAgo;


        public ItemViewHolder( View v) {
            super(v);
            this.v = v;
            this.relativeLayoutSummary = (RelativeLayout) v.findViewById(R.id.relativeLayoutSummary);
            imageViewCover = (ImageView) itemView.findViewById(R.id.imageViewCover);
            textViewTitile = (TextView) itemView.findViewById(R.id.textViewBarName);
            textViewCategory = (TextView) itemView.findViewById(R.id.textViewCategory);
            textViewDescription = (TextView) itemView.findViewById(R.id.textViewDescription);
            linearLayoutLike = (LinearLayout) itemView.findViewById(R.id.linearLayoutLike);
            imageViewLike = (ImageView) itemView.findViewById(R.id.imageViewLike);
            textViewLike = (TextView) itemView.findViewById(R.id.textViewLike);
            linearLayoutComment = (LinearLayout) itemView.findViewById(R.id.linearLayoutComment);
            textViewComment = (TextView) itemView.findViewById(R.id.textViewComment);
            linearLayoutShare = (LinearLayout) itemView.findViewById(R.id.linearLayoutShare);
            textViewShare= (TextView) itemView.findViewById(R.id.textViewShare);
            textViewTimeAgo = (TextView) itemView.findViewById(R.id.textViewTimeAgo);
        }
    }


}
