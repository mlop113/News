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

import com.android.Activity_Fragment.FeedbackCommentActivity;
import com.android.Global.AppConfig;
import com.android.Global.AppPreferences;
import com.android.Global.GlobalFunction;
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

public class CommentAdapter extends  RecyclerView.Adapter<CommentAdapter.ViewHolder>{
    Context context;
    Post post;
    List<Comment> listComment = new ArrayList<>();
    DatabaseReference databaseReference;
    AppPreferences appPreferences;

    public CommentAdapter(Context context,Post post, List<Comment> listComment) {
        this.context = context;
        this.post = post;
        this.listComment = listComment;
        databaseReference = FirebaseDatabase.getInstance().getReference();
        appPreferences = AppPreferences.getInstance(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_comment,parent,false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Comment comm = listComment.get(position);
        databaseReference.child(AppConfig.FIREBASE_FIELD_USERMEMBERS).child(comm.getUserOfCommentId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserMember userMember = dataSnapshot.getValue(UserMember.class);
                holder.textViewUsername.setText(userMember.getName());
                try{
                    Glide.with(context).load(userMember.getImg()).into(holder.imageViewUserComment);
                }catch (IllegalArgumentException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        holder.textViewContent.setText(comm.getMessage());
        holder.textViewTimeAgo.setText(GlobalFunction.calculateTimeAgo(comm.getDateCreate()));
        holder.textViewReplyComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(context,FeedbackCommentActivity.class);
                intent.putExtra(AppConfig.COMMENT,comm);
                intent.putExtra(AppConfig.POST,post);
                intent.putExtra(AppConfig.ACTION,AppConfig.COMMENT);
                context.startActivity(intent);
            }
        });

        List<ReplyComment> listFeedback = new ArrayList<>();
        if((comm.getReplyComments()!=null)) {
            listFeedback = new ArrayList<>(comm.getReplyComments().values());
            if ( listFeedback.size() > 0) {
                holder.linearLayoutViewFeedback.setVisibility(View.VISIBLE);
                holder.linearLayoutViewFeedback.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, FeedbackCommentActivity.class);
                        intent.putExtra(AppConfig.COMMENT, comm);
                        intent.putExtra(AppConfig.POST,post);
                        context.startActivity(intent);
                    }
                });
                if (listFeedback.size() > 1) {
                    holder.textViewViewFeedback.setVisibility(View.VISIBLE);
                    holder.textViewViewFeedback.setText("Xem " + String.valueOf(listFeedback.size()) + " câu trả lời trước");
                }
                ReplyComment feedbackComment = listFeedback.get(0);
                databaseReference.child(AppConfig.FIREBASE_FIELD_USERMEMBERS).child(feedbackComment.getUserReplyId()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        UserMember userMember = dataSnapshot.getValue(UserMember.class);
                        holder.textViewFeedbackUsername.setText(userMember.getName());
                        try{
                            Glide.with(context).load(userMember.getImg()).into(holder.imageViewUserReply);
                        }catch (IllegalArgumentException e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                holder.textViewFeedbackContent.setText(feedbackComment.getContent());
            }
        }
        //holder.textViewLikeNumber.setText(comm.getUserLikeIds().size());
        databaseReference.child(AppConfig.FIREBASE_FIELD_POSTS).child(post.getPostId()).child(AppConfig.FIREBASE_FIELD_COMMENTS).child(comm.getCommentId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                holder.textViewLikeNumber.setText(String.valueOf(dataSnapshot.child(AppConfig.FIREBASE_FIELD_USERLIKEIDS).getChildrenCount()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        checkLiked(comm,holder.textViewLike);

        holder.textViewLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickLikeComment(post,comm,holder.textViewLike);
            }
        });

    }

    public void addComment(Comment comment) {
        this.listComment.add(comment);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(listComment.get(position).getCommentId());
    }

    @Override
    public int getItemCount() {
        return listComment==null?0:listComment.size();
    }

    public void setData(List<Comment> listComment)
    {
        this.listComment.clear();
        this.listComment.addAll(listComment);
        notifyDataSetChanged();
    }

    private void checkLiked(final Comment comment, final TextView textViewLike) {
        databaseReference.child(AppConfig.FIREBASE_FIELD_POSTS).child(post.getPostId()).child(AppConfig.FIREBASE_FIELD_COMMENTS).child(comment.getCommentId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(AppConfig.FIREBASE_FIELD_USERLIKEIDS)) {
                    String userId = appPreferences.getUserId();
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

    private void onClickLikeComment(final Post post,Comment comment, final TextView textViewLike) {
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
        else{
            Intent intentLogin = new Intent(context,Login.class);
            context.startActivity(intentLogin);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        View v;
        TextView textViewUsername;
        TextView textViewContent;
        TextView textViewReplyComment;
        ImageView imageViewUserComment;
        //View feedback
        LinearLayout linearLayoutViewFeedback;
        TextView textViewViewFeedback;
        TextView textViewFeedbackUsername;
        TextView textViewFeedbackContent;
        TextView textViewTimeAgo;
        TextView textViewLike;
        TextView textViewLikeNumber;
        ImageView imageViewUserReply;
        public ViewHolder(View itemView) {
            super(itemView);
            this.v = itemView;
            textViewUsername = (TextView) v.findViewById(R.id.textViewUsername);
            textViewContent = (TextView) v.findViewById(R.id.textViewContent);
            textViewReplyComment = (TextView) v.findViewById(R.id.textViewReplyComment);
            textViewTimeAgo = (TextView) v.findViewById(R.id.textViewTimeAgo);
            imageViewUserComment = v.findViewById(R.id.imageViewUserComment);

            linearLayoutViewFeedback = (LinearLayout) v.findViewById(R.id.linearLayoutViewFeedback);
            textViewViewFeedback = (TextView) v.findViewById(R.id.textViewViewFeedback);
            textViewFeedbackUsername = (TextView) v.findViewById(R.id.textViewFeedbackUsername);
            textViewFeedbackContent = (TextView) v.findViewById(R.id.textViewFeedbackContent);
            textViewLike = (TextView) v.findViewById(R.id.textViewLike);
            textViewLikeNumber = (TextView) v.findViewById(R.id.textViewLikeNumber);
            imageViewUserReply = v.findViewById(R.id.imageViewUserReply);

        }
    }

}
