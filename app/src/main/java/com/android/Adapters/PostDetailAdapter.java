package com.android.Adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.Global.AppConfig;
import com.android.Global.GlobalFunction;
import com.android.Models.Comment;
import com.android.Models.Post;
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

public class PostDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int ITEMVIEWTYPE_CONTENT=0;
    private final int ITEMVIEWTYPE_RECYCLERVIEWTAG=1;
    private final int ITEMVIEWTYPE_RECYCLERVIEWRELATED=2;
    private final int ITEMVIEWTYPE_RECYCLERVIEWCOMMENT=3;
    private Context context;
    private Post post;

    TagAdapter tagAdapter;
    List<Post> relatedPosts=new ArrayList<>();
    RelatedAdapter related_adapterCategory;
    CommentAdapter comment_adapter;

    DatabaseReference databaseReference;
    public PostDetailAdapter(Context context, Post post) {
        this.context = context;
        this.post = post;
        this.tagAdapter = new TagAdapter(context,post.getTags());
        this.related_adapterCategory = new RelatedAdapter(context,relatedPosts);

        if(post.getComments()!=null && post.getComments().size()>0)
            comment_adapter = new CommentAdapter(context,post, new ArrayList<>(post.getComments().values()));
        else
            comment_adapter = new CommentAdapter(context,post,new ArrayList<Comment>());
        databaseReference  = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=null;
        switch (viewType){
            case ITEMVIEWTYPE_CONTENT:
                view = LayoutInflater.from(context).inflate(R.layout.item_postdetail_content,parent,false);
                return new ContentViewHolder(view);
            case ITEMVIEWTYPE_RECYCLERVIEWTAG:
                view = LayoutInflater.from(context).inflate(R.layout.item_recyclerviewtag,parent,false);
                return new TagViewHolder(view);
            case ITEMVIEWTYPE_RECYCLERVIEWRELATED:
                view = LayoutInflater.from(context).inflate(R.layout.item_postdetail_recyclerviewrelated,parent,false);
                return new RelatedViewHolder(view);
            case ITEMVIEWTYPE_RECYCLERVIEWCOMMENT:
                view = LayoutInflater.from(context).inflate(R.layout.item_postdetail_recyclerviewcomment,parent,false);
                return new CommentViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        switch (holder.getItemViewType()){
            case ITEMVIEWTYPE_CONTENT:
                final ContentViewHolder contentViewHolder = (ContentViewHolder) holder;
                try{
                    Glide.with(context).load(post.getImg()).into(contentViewHolder.imageViewCover);
                }catch (IllegalArgumentException e){
                    e.printStackTrace();
                }
                contentViewHolder.textViewTitile.setText(post.getTitle());
                contentViewHolder.textViewCategory.setText(post.getcategory());
                contentViewHolder.textViewDescription.setText(post.getDescription());
                contentViewHolder.textViewAuthor.setText(post.getAuthor());
                contentViewHolder.textViewTimeago.setText(GlobalFunction.calculateTimeAgo(post.getDateCreate()));
                contentViewHolder.textViewContent.setText(post.getContent());
                databaseReference.child(AppConfig.FIREBASE_FIELD_POSTS).child(post.getPostId()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        post = dataSnapshot.getValue(Post.class);
                        try {
                            Glide.with(context).load(post.getImg()).into(contentViewHolder.imageViewCover);
                        }catch (IllegalArgumentException e){
                            e.printStackTrace();
                        }
                        contentViewHolder.textViewTitile.setText(post.getTitle());
                        contentViewHolder.textViewCategory.setText(post.getcategory());
                        contentViewHolder.textViewDescription.setText(post.getDescription());
                        contentViewHolder.textViewAuthor.setText(post.getAuthor());
                        contentViewHolder.textViewTimeago.setText(GlobalFunction.calculateTimeAgo(post.getDateCreate()));
                        contentViewHolder.textViewContent.setText(post.getContent());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                break;
            case ITEMVIEWTYPE_RECYCLERVIEWTAG:
                final TagViewHolder tagViewHolder = (TagViewHolder)holder;
                LinearLayoutManager linearLayoutManagerTag = new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,true);
                linearLayoutManagerTag.setStackFromEnd(true);
                tagViewHolder.recyclerViewTag.setLayoutManager(linearLayoutManagerTag);
                tagViewHolder.recyclerViewTag.setAdapter(tagAdapter);
                databaseReference.child(AppConfig.FIREBASE_FIELD_POSTS).child(post.getPostId()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        post = dataSnapshot.getValue(Post.class);
                        tagAdapter.setData(post.getTags());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                break;
            case ITEMVIEWTYPE_RECYCLERVIEWRELATED:
                RelatedViewHolder relatedViewHolder = (RelatedViewHolder)holder;
                LinearLayoutManager linearLayoutManagerCategory = new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,true);
                linearLayoutManagerCategory.setStackFromEnd(true);
                relatedViewHolder.recyclerViewRelated.setLayoutManager(linearLayoutManagerCategory);
                relatedViewHolder.recyclerViewRelated.setAdapter(related_adapterCategory);
                databaseReference.child(AppConfig.FIREBASE_FIELD_POSTS).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        relatedPosts = new ArrayList<Post>();
                        for(DataSnapshot dataPost:dataSnapshot.getChildren()) {
                            Post p = dataPost.getValue(Post.class);
                            if (p.getcategory().equals(post.getcategory())) {
                                relatedPosts.add(p);
                            }
                        }
                        related_adapterCategory.setData(relatedPosts);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                break;
            case ITEMVIEWTYPE_RECYCLERVIEWCOMMENT:
                CommentViewHolder commentViewHolder = (CommentViewHolder) holder;
                commentViewHolder.recyclerViewComment.setLayoutManager(new LinearLayoutManager(context));

                commentViewHolder.recyclerViewComment.setAdapter(comment_adapter);

                databaseReference.child(AppConfig.FIREBASE_FIELD_POSTS).child(post.getPostId()).child(AppConfig.FIREBASE_FIELD_COMMENTS).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<Comment> listComment = new ArrayList<Comment>();
                        for(DataSnapshot dataComment:dataSnapshot.getChildren()) {
                            listComment.add(dataComment.getValue(Comment.class));
                            comment_adapter.setData(listComment);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                break;

        }
    }


    @Override
    public int getItemCount() {
        return 4;
    }

    @Override
    public int getItemViewType(int position) {
        switch (position){
            case 0:
                return ITEMVIEWTYPE_CONTENT;
            case 1:
                return ITEMVIEWTYPE_RECYCLERVIEWTAG;
            case 2:
                return ITEMVIEWTYPE_RECYCLERVIEWRELATED;
            case 3:
                return ITEMVIEWTYPE_RECYCLERVIEWCOMMENT;
        }
        return position;
    }

    static class ContentViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageViewCover;
        private TextView textViewTitile;
        private TextView textViewCategory;
        private TextView textViewDescription;
        private TextView textViewAuthor;
        private TextView textViewTimeago;
        private TextView textViewContent;
        public ContentViewHolder(View itemView) {
            super(itemView);
            imageViewCover = (ImageView) itemView.findViewById(R.id.imageViewCover);
            textViewTitile = (TextView) itemView.findViewById(R.id.textViewTitile);
            textViewCategory = (TextView) itemView.findViewById(R.id.textViewCategory);
            textViewDescription = (TextView) itemView.findViewById(R.id.textViewDescription);
            textViewAuthor = (TextView) itemView.findViewById(R.id.textViewAuthor);
            textViewTimeago = (TextView) itemView.findViewById(R.id.textViewTimeago);
            textViewContent = (TextView) itemView.findViewById(R.id.textViewContent);
        }
    }

    static class TagViewHolder extends RecyclerView.ViewHolder{
        private RecyclerView recyclerViewTag;
        public TagViewHolder(View itemView) {
            super(itemView);
            recyclerViewTag = (RecyclerView) itemView.findViewById(R.id.recyclerViewTag);
        }
    }

    static class RelatedViewHolder extends RecyclerView.ViewHolder{
        private RecyclerView recyclerViewRelated;
        public RelatedViewHolder(View itemView) {
            super(itemView);
            recyclerViewRelated = (RecyclerView) itemView.findViewById(R.id.recyclerViewRelated);
        }
    }

    static class CommentViewHolder extends RecyclerView.ViewHolder{
        private RecyclerView recyclerViewComment;
        public CommentViewHolder(View itemView) {
            super(itemView);
            recyclerViewComment = (RecyclerView) itemView.findViewById(R.id.recyclerViewComment);
        }
    }




    public void addComment(Comment comment){
        comment_adapter.addComment(comment);
        notifyDataSetChanged();
    }
}
