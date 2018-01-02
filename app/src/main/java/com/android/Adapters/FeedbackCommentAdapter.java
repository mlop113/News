package com.android.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.Global.AppConfig;
import com.android.Global.AppPreferences;
import com.android.Global.GlobalFunction;
import com.android.Interface.IOnClickFeedback;
import com.android.Login;
import com.android.Models.Comment;
import com.android.Models.Post;
import com.android.Models.ReplyComment;
import com.android.Models.UserMember;
import com.android.R;
import com.bumptech.glide.Glide;
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

public class FeedbackCommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int ITEMVIEWTYPE_COMMENT=0;
    private final int ITEMVIEWTYPE_FEEDBACK=1;
    Context context;
    Post post;
    Comment comment;
    List<ReplyComment> listFeedback = new ArrayList<>();
    DatabaseReference databaseReference;
    IOnClickFeedback iOnClickFeedback;
    AppPreferences appPreferences;
    public FeedbackCommentAdapter(Context context,Post post, Comment comment) {
        this.context = context;
        this.post = post;
        this.comment = comment;
        this.listFeedback = new ArrayList<>();
        if(comment.getReplyComments()!=null)
            this.listFeedback = new ArrayList<>(comment.getReplyComments().values());
        databaseReference = FirebaseDatabase.getInstance().getReference();
        appPreferences = AppPreferences.getInstance(context);

    }

    public void setiOnClickFeedback(IOnClickFeedback iOnClickFeedback){
        this.iOnClickFeedback = iOnClickFeedback;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType){
            case ITEMVIEWTYPE_COMMENT:
                view = LayoutInflater.from(context).inflate(R.layout.item_comment,parent,false);
                return new CommentViewholder(view);
            case ITEMVIEWTYPE_FEEDBACK:
                view = LayoutInflater.from(context).inflate(R.layout.item_feedbackcomment,parent,false);
                return new FeedbackViewholer(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        switch (holder.getItemViewType()){
            case ITEMVIEWTYPE_COMMENT:
                final CommentViewholder commentViewholder = (CommentViewholder) holder;
                databaseReference.child(AppConfig.FIREBASE_FIELD_USERMEMBERS).child(comment.getUserOfCommentId()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        UserMember userMember = dataSnapshot.getValue(UserMember.class);
                        commentViewholder.textViewUsername.setText(userMember.getName());
                        try{
                            Glide.with(context).load(userMember.getImg()).into(commentViewholder.imageViewUserComment);
                        }catch (IllegalArgumentException e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                commentViewholder.textViewContent.setText(comment.getMessage());
                commentViewholder.textViewTimeAgo.setText(GlobalFunction.calculateTimeAgo(comment.getDateCreate()));
                checkLiked(comment,commentViewholder.textViewLike);
                commentViewholder.textViewLike.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClickLikeComment(post,comment,commentViewholder.textViewLike);
                    }
                });
                databaseReference.child(AppConfig.FIREBASE_FIELD_POSTS).child(post.getPostId()).child(AppConfig.FIREBASE_FIELD_COMMENTS).child(comment.getCommentId()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        commentViewholder.textViewLikeNumber.setText(String.valueOf(dataSnapshot.child(AppConfig.FIREBASE_FIELD_USERLIKEIDS).getChildrenCount()));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                commentViewholder.textViewReplyComment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        iOnClickFeedback.onClickFeedback();
                    }
                });
                break;
            case ITEMVIEWTYPE_FEEDBACK:
                final ReplyComment feedbackComment = listFeedback.get(position-1);
                final FeedbackViewholer feedbackViewholer = (FeedbackViewholer) holder;
                databaseReference.child(AppConfig.FIREBASE_FIELD_USERMEMBERS).child(feedbackComment.getUserReplyId()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        UserMember userMember = dataSnapshot.getValue(UserMember.class);
                        feedbackViewholer.textViewFeedbackUsername.setText(userMember.getName());
                        try{
                            Glide.with(context).load(userMember.getImg()).into(feedbackViewholer.imageViewUserReply);
                        }catch (IllegalArgumentException e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                feedbackViewholer.textViewFeedbackContent.setText(feedbackComment.getContent());
                feedbackViewholer.textViewTimeAgo.setText(GlobalFunction.calculateTimeAgo(feedbackComment.getDateCreate()));
                break;
        }
    }

    public void addFeebackComment(ReplyComment feedbackComment){
        listFeedback.add(feedbackComment);
        notifyDataSetChanged();
    }





    @Override
    public int getItemCount() {
        return listFeedback.size()<=0?1 : (listFeedback.size()+1);
    }

    @Override
    public int getItemViewType(int position) {
        if(position==0)
            return ITEMVIEWTYPE_COMMENT;
        else
            return ITEMVIEWTYPE_FEEDBACK;
    }

    private void checkLiked(final Comment comment, final TextView textViewLike) {
        databaseReference.child(AppConfig.FIREBASE_FIELD_POSTS).child(post.getPostId()).child(AppConfig.FIREBASE_FIELD_COMMENTS).child(comment.getCommentId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(AppConfig.FIREBASE_FIELD_USERLIKEIDS)) {
                    String userId =appPreferences.getUserId();
                    Comment comment1 = dataSnapshot.getValue(Comment.class);
                    List<String> userLikeId = comment1.getUserLikeIds();
                    long count = dataSnapshot.child(AppConfig.FIREBASE_FIELD_USERLIKEIDS).getChildrenCount();
                    if (userLikeId != null && count > 0) {
                        if (userLikeId.contains(userId)) {
                            textViewLike.setTextColor(context.getResources().getColor(R.color.likedcomment));
                        } else {
                            textViewLike.setTextColor(context.getResources().getColor(R.color.black));
                        }
                    } else {
                        textViewLike.setTextColor(context.getResources().getColor(R.color.black));
                    }
                }
                else {
                    textViewLike.setTextColor(context.getResources().getColor(R.color.black));
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void onClickLikeComment(final Post post, Comment comment, final TextView textViewLike) {
        if(appPreferences.isLogin()) {
            String userId = appPreferences.getUserId();
            //get from user_post
            if (comment.getUserLikeIds() != null && comment.getUserLikeIds().size() > 0) {
                //if user liked this post
                if (comment.getUserLikeIds().contains(userId)) {
                    comment.getUserLikeIds().remove(userId);
                    databaseReference.child(AppConfig.FIREBASE_FIELD_POSTS).child(post.getPostId())
                            .child(AppConfig.FIREBASE_FIELD_COMMENTS).child(comment.getCommentId()).child(AppConfig.FIREBASE_FIELD_USERLIKEIDS).setValue(comment.getUserLikeIds());
                } else {
                    comment.getUserLikeIds().add(userId);
                    databaseReference.child(AppConfig.FIREBASE_FIELD_POSTS).child(post.getPostId())
                            .child(AppConfig.FIREBASE_FIELD_COMMENTS).child(comment.getCommentId()).child(AppConfig.FIREBASE_FIELD_USERLIKEIDS).setValue(comment.getUserLikeIds());
                }
            } else {
                comment.setUserLikeIds(new ArrayList<String>());
                comment.getUserLikeIds().add(userId);
                databaseReference.child(AppConfig.FIREBASE_FIELD_POSTS).child(post.getPostId())
                        .child(AppConfig.FIREBASE_FIELD_COMMENTS).child(comment.getCommentId()).child(AppConfig.FIREBASE_FIELD_USERLIKEIDS).setValue(comment.getUserLikeIds());
            }
        }
        else
        {
            Intent intentLogin = new Intent(context,Login.class);
            context.startActivity(intentLogin);
        }
    }

    public void setData(Comment comment){
        this.comment = comment;
        this.listFeedback = new ArrayList<>();
        if(comment.getReplyComments()!=null)
            this.listFeedback = new ArrayList<>(comment.getReplyComments().values());
        notifyDataSetChanged();
    }

    static class CommentViewholder extends RecyclerView.ViewHolder{
        TextView textViewUsername;
        TextView textViewContent;
        TextView textViewTimeAgo;
        TextView textViewLike;
        TextView textViewLikeNumber;
        TextView textViewReplyComment;
        ImageView imageViewUserComment;
        public CommentViewholder(View itemView) {
            super(itemView);
            textViewUsername = (TextView) itemView.findViewById(R.id.textViewUsername);
            textViewContent = (TextView) itemView.findViewById(R.id.textViewContent);
            textViewTimeAgo = (TextView) itemView.findViewById(R.id.textViewTimeAgo);
            textViewLike = (TextView) itemView.findViewById(R.id.textViewLike);
            textViewLikeNumber = (TextView) itemView.findViewById(R.id.textViewLikeNumber);
            textViewReplyComment = (TextView) itemView.findViewById(R.id.textViewReplyComment);
            imageViewUserComment = itemView.findViewById(R.id.imageViewUserComment);
        }
    }

    static class FeedbackViewholer extends RecyclerView.ViewHolder{
        TextView textViewFeedbackUsername;
        TextView textViewFeedbackContent;
        TextView textViewTimeAgo;
        ImageView imageViewUserReply;
        public FeedbackViewholer(View itemView) {
            super(itemView);
            textViewFeedbackUsername = (TextView) itemView.findViewById(R.id.textViewFeedbackUsername);
            textViewFeedbackContent = (TextView) itemView.findViewById(R.id.textViewFeedbackContent);
            textViewTimeAgo = (TextView) itemView.findViewById(R.id.textViewTimeAgo);
            imageViewUserReply = itemView.findViewById(R.id.imageViewUserReply);
        }
    }
}
