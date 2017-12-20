package com.android.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.android.Activity_Fragment.PostsOnRequestActivity;
import com.android.Effect.Blur;
import com.android.Global.AppConfig;
import com.android.Global.GlobalFunction;
import com.android.Global.GlobalStaticData;
import com.android.Models.Post;
import com.android.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ngoc Vu on 12/18/2017.
 */

public class SummaryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int ITEMVIEWTYPE_HEADER = 0;
    private final int ITEMVIEWTYPE_ITEM = 1;
    private final int ITEMVIEWTYPE_CATEGORY = 2;
    Context context;
    Animation animation0to180, animation180to0;
    List<Post> listPost = new ArrayList<>();
    List<String> listCategory = new ArrayList<>();
    Animation hyperspaceJumpAnimation;
    DatabaseReference databaseReference;

    public SummaryAdapter(Context context, List<Post> listPost, List<String> listCategory) {
        this.context = context;
        this.listPost = listPost;
        this.listCategory = listCategory;
        animation0to180 = AnimationUtils.loadAnimation(context, R.anim.rotate_iconexpand_0to180);
        animation180to0 = AnimationUtils.loadAnimation(context, R.anim.rotate_iconexpand_180to0);
        hyperspaceJumpAnimation = AnimationUtils.loadAnimation(context, R.anim.animlike);
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType) {
            case ITEMVIEWTYPE_HEADER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_summary_header, parent, false);
                return new HeaderViewHolder(view);
            case ITEMVIEWTYPE_ITEM:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_summary, parent, false);
                return new ItemViewHolder(view);
            case ITEMVIEWTYPE_CATEGORY:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_summary_category, parent, false);
                return new CategoryViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        switch (holder.getItemViewType()) {
            case ITEMVIEWTYPE_HEADER:
                /*databaseReference.child(AppConfig.FIREBASE_FIELD_POSTS).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild(listPost.get(position).getPostId()))
                        {
                            databaseReference.child(AppConfig.FIREBASE_FIELD_POSTS).child(listPost.get(position).getPostId()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    final Post postHeader = dataSnapshot.getValue(Post.class);
                                    final HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
                                    headerViewHolder.imageViewCover.setImageResource(R.drawable.bogiaothong);
                                    headerViewHolder.textViewTitile.setText(postHeader.getTitle());
                                    Blur blur = new Blur(context);
                                    blur.applyBlur(headerViewHolder.imageViewCover, headerViewHolder.textViewTitile);
                                    databaseReference.child(AppConfig.FIREBASE_FIELD_CATEGORIES).child(postHeader.getCategoryId()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            headerViewHolder.textViewCategory.setText(dataSnapshot.getValue(Category.class).getName());
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                    headerViewHolder.textViewDescription.setText(postHeader.getDescription());
                                    checkLiked(postHeader, headerViewHolder.imageViewLike, headerViewHolder.textViewLike);
                                    headerViewHolder.textViewComment.setText(String.valueOf(postHeader.getComments().size()));
                                    headerViewHolder.textViewShare.setText(String.valueOf(postHeader.getUserShareIds().size()));
                                    headerViewHolder.textViewTimeAgo.setText(GlobalFunction.calculateTimeAgo(postHeader.getDateCreate()));

                                    headerViewHolder.linearLayoutSummary.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(context, PostDetailActivity.class);
                                            intent.putExtra(AppConfig.POST, postHeader);
                                            context.startActivity(intent);
                                        }
                                    });

                                    headerViewHolder.linearLayoutLike.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            onClickLikePost(postHeader, headerViewHolder.imageViewLike);
                                        }
                                    });

                                    headerViewHolder.linearLayoutComment.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(context, PostDetailActivity.class);
                                            intent.putExtra(AppConfig.POST, postHeader);
                                            intent.putExtra(AppConfig.ACTION, AppConfig.COMMENT);
                                            context.startActivity(intent);
                                        }
                                    });

                                    headerViewHolder.linearLayoutShare.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });*/


                final Post postHeader = listPost.get(position);
                final HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
                headerViewHolder.imageViewCover.setImageResource(R.drawable.bogiaothong);
                headerViewHolder.textViewTitile.setText(postHeader.getTitle());
                Blur blur = new Blur(context);
                blur.applyBlur(headerViewHolder.imageViewCover, headerViewHolder.textViewTitile);
                headerViewHolder.textViewCategory.setText(postHeader.getcategory());
                headerViewHolder.textViewDescription.setText(postHeader.getDescription());
                checkLiked(postHeader, headerViewHolder.imageViewLike, headerViewHolder.textViewLike);

                if(postHeader.getComments()!=null && postHeader.getComments().size()>0)
                    headerViewHolder.textViewComment.setText(String.valueOf(postHeader.getComments().size()));
                else
                    headerViewHolder.textViewComment.setText("0");

                if(postHeader.getUserShareIds()!=null && postHeader.getUserShareIds().size()>0)
                    headerViewHolder.textViewShare.setText(String.valueOf(postHeader.getUserShareIds().size()));
                else
                    headerViewHolder.textViewShare.setText("0");
                headerViewHolder.textViewTimeAgo.setText(GlobalFunction.calculateTimeAgo(postHeader.getDateCreate()));

                headerViewHolder.linearLayoutSummary.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, PostDetailActivity.class);
                        intent.putExtra(AppConfig.POST, postHeader);
                        context.startActivity(intent);
                    }
                });

                headerViewHolder.linearLayoutLike.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClickLikePost(postHeader, headerViewHolder.imageViewLike);
                    }
                });

                headerViewHolder.linearLayoutComment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, PostDetailActivity.class);
                        intent.putExtra(AppConfig.POST, postHeader);
                        intent.putExtra(AppConfig.ACTION, AppConfig.COMMENT);
                        context.startActivity(intent);
                    }
                });

                headerViewHolder.linearLayoutShare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                break;
            case ITEMVIEWTYPE_ITEM:
                databaseReference.child(AppConfig.FIREBASE_FIELD_POSTS).child(listPost.get(position).getPostId()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final Post post = dataSnapshot.getValue(Post.class);
                        final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
                        //imageCover
                        itemViewHolder.imageViewCover.setImageResource(R.drawable.bogiaothong);
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

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                break;
            case ITEMVIEWTYPE_CATEGORY:

                final String category = listCategory.get(position - listPost.size());
                final CategoryViewHolder categoryViewHolder = (CategoryViewHolder) holder;

                //listCategory home

                databaseReference.child(AppConfig.FIREBASE_FIELD_POSTS).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final List<Post> listPostOfCategory = new ArrayList<Post>();
                        GenericTypeIndicator<HashMap<String, Post>> objectsGTypeInd = new GenericTypeIndicator<HashMap<String, Post>>() {
                        };
                        Map<String, Post> objectHashMap = dataSnapshot.getValue(objectsGTypeInd);
                        List<Post> listPost = new ArrayList<Post>(objectHashMap.values());
                        for (Post p : listPost) {
                            if (p.getcategory().equals(category)) {
                                listPostOfCategory.add(p);
                            }
                        }
                        Log.d("listPostOfCategory", String.valueOf(listPostOfCategory.size()));
                        categoryViewHolder.relativeLayoutCategoryName.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(context, PostsOnRequestActivity.class);
                                intent.putExtra(AppConfig.LISTPOST, (ArrayList) listPostOfCategory);
                                intent.putExtra(AppConfig.BARNAME, category);
                                context.startActivity(intent);
                            }
                        });

                        categoryViewHolder.textViewCateGoryName.setText(category);

                        if (listPostOfCategory != null && listPostOfCategory.size() > 0) {
                            final Post post1 = listPostOfCategory.get(0);
                            categoryViewHolder.linearLayoutPost1.setVisibility(View.VISIBLE);
                            categoryViewHolder.imageViewPost1.setImageResource(R.drawable.bogiaothong);
                            categoryViewHolder.textViewTitlePost1.setText(post1.getTitle());
                            categoryViewHolder.linearLayoutPost1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    openPostDetail(post1);
                                }
                            });
                            if (listPostOfCategory.size() > 1) {
                                final Post post2 = listPostOfCategory.get(1);
                                categoryViewHolder.linearLayoutPost2.setVisibility(View.VISIBLE);
                                categoryViewHolder.imageViewPost2.setImageResource(R.drawable.bogiaothong);
                                categoryViewHolder.textViewDescriptionPost2.setText(post2.getDescription());
                                categoryViewHolder.linearLayoutPost2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        openPostDetail(post2);
                                    }
                                });
                            }
                            if (listPostOfCategory.size() > 2) {
                                final Post post3 = listPostOfCategory.get(2);
                                categoryViewHolder.linearLayoutPost3.setVisibility(View.VISIBLE);
                                categoryViewHolder.imageViewPost3.setImageResource(R.drawable.bogiaothong);
                                categoryViewHolder.textViewDescriptionPost3.setText(post3.getDescription());
                                categoryViewHolder.linearLayoutPost3.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        openPostDetail(post3);
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                /*final List<Post> listPostOfCategory = GlobalStaticData.listPostOfCategory.get(category.getCategoryId());
                Log.d("listPostOfCategory",String.valueOf(listPostOfCategory.size()));
                //Log.d("listPostOfCategory",String.valueOf(listPostOfCategory.size()));
                categoryViewHolder.relativeLayoutCategoryName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, PostsOnRequestActivity.class);
                        intent.putExtra(AppConfig.LISTPOST, (ArrayList) listPostOfCategory);
                        intent.putExtra(AppConfig.BARNAME, category.getName());
                        context.startActivity(intent);
                    }
                });

                categoryViewHolder.textViewCateGoryName.setText(category.getName());

                if (listPostOfCategory != null && listPostOfCategory.size() > 0) {
                    final Post post1 = listPostOfCategory.get(0);
                    categoryViewHolder.linearLayoutPost1.setVisibility(View.VISIBLE);
                    categoryViewHolder.imageViewPost1.setImageResource(R.drawable.bogiaothong);
                    categoryViewHolder.textViewTitlePost1.setText(post1.getTitle());
                    categoryViewHolder.linearLayoutPost1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openPostDetail(post1);
                        }
                    });
                    if (listPostOfCategory.size() > 1) {
                        final Post post2 = listPostOfCategory.get(1);
                        categoryViewHolder.linearLayoutPost2.setVisibility(View.VISIBLE);
                        categoryViewHolder.imageViewPost2.setImageResource(R.drawable.bogiaothong);
                        categoryViewHolder.textViewDescriptionPost2.setText(post2.getDescription());
                        categoryViewHolder.linearLayoutPost2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                openPostDetail(post2);
                            }
                        });
                    }
                    if (listPostOfCategory.size() > 2) {
                        final Post post3 = listPostOfCategory.get(2);
                        categoryViewHolder.linearLayoutPost3.setVisibility(View.VISIBLE);
                        categoryViewHolder.imageViewPost3.setImageResource(R.drawable.bogiaothong);
                        categoryViewHolder.textViewDescriptionPost3.setText(post3.getDescription());
                        categoryViewHolder.linearLayoutPost3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                openPostDetail(post3);
                            }
                        });
                    }
                }*/
                break;
        }
    }

    private static int LIKE_TIME_OUT = 1500;

    private void checkLiked(final Post post, final ImageView imageViewLike, final TextView textViewLike) {

        databaseReference.child(AppConfig.FIREBASE_FIELD_POSTS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String userId = GlobalStaticData.currentUser.getUserId();
                long count = dataSnapshot.child(post.getPostId()).child(AppConfig.FIREBASE_FIELD_USERLIKEIDS).getChildrenCount();
                if (post.getUserLikeIds() != null && count > 0) {
                    if (post.getUserLikeIds().contains(userId)) {
                        imageViewLike.setImageResource(R.drawable.ic_liked);
                    } else {
                        imageViewLike.setImageResource(R.drawable.ic_like);
                    }
                    textViewLike.setText(String.valueOf(count));
                } else {
                    imageViewLike.setImageResource(R.drawable.ic_like);
                    textViewLike.setText("0");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void onClickLikePost(final Post post, final ImageView imageViewLike) {
        //get from user_post
        String userId = GlobalStaticData.currentUser.getUserId();
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
                    .child(AppConfig.FIREBASE_FIELD_USERLIKEIDS).child("0").setValue(String.valueOf(userId), new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    imageViewLike.startAnimation(hyperspaceJumpAnimation);
                }
            });
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (position >= 0 && position < listPost.size()) {
            if (position == 0)
                return ITEMVIEWTYPE_HEADER;
            return ITEMVIEWTYPE_ITEM;
        }
        if (position >= listPost.size())
            return ITEMVIEWTYPE_CATEGORY;
        return position;
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(listPost.get(position).getPostId());
    }


    @Override
    public int getItemCount() {
        return listPost == null && listCategory == null ? 0 : listPost.size() + listCategory.size();
    }


    public void setListPost(List<Post> listPost) {
        this.listPost.clear();
        this.listPost.addAll(listPost);
        notifyDataSetChanged();
    }

    public void setListCategory(List<String> listCategory) {
        this.listCategory.clear();
        this.listCategory.addAll(listCategory);
        notifyDataSetChanged();
    }

    public void addAll(ArrayList<Post> listPost) {
        int startIndex = this.listPost.size();
        this.listPost.addAll(startIndex, listPost);
        notifyItemRangeInserted(startIndex, listPost.size());
    }

    private void openPostDetail(Post post) {
        Intent intent = new Intent(context, PostDetailActivity.class);
        intent.putExtra(AppConfig.POST, post);
        context.startActivity(intent);
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout relativeLayoutSummary;
        ImageView imageViewCover;
        TextView textViewTitile, textViewCategory, textViewDescription;
        LinearLayout linearLayoutLike;
        ImageView imageViewLike;
        TextView textViewLike;
        LinearLayout linearLayoutComment;
        TextView textViewComment;
        LinearLayout linearLayoutShare;
        TextView textViewShare;
        TextView textViewTimeAgo;


        public ItemViewHolder(View itemView) {
            super(itemView);
            this.relativeLayoutSummary = (RelativeLayout) itemView.findViewById(R.id.relativeLayoutSummary);
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
            textViewShare = (TextView) itemView.findViewById(R.id.textViewShare);
            textViewTimeAgo = (TextView) itemView.findViewById(R.id.textViewTimeAgo);
        }
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayoutSummary;
        ImageView imageViewCover;
        TextView textViewTitile, textViewCategory, textViewDescription;
        LinearLayout linearLayoutLike;
        ImageView imageViewLike;
        TextView textViewLike;
        LinearLayout linearLayoutComment;
        TextView textViewComment;
        LinearLayout linearLayoutShare;
        TextView textViewShare;
        TextView textViewTimeAgo;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            linearLayoutSummary = (LinearLayout) itemView.findViewById(R.id.linearLayoutSummary);
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
            textViewShare = (TextView) itemView.findViewById(R.id.textViewShare);
            textViewTimeAgo = (TextView) itemView.findViewById(R.id.textViewTimeAgo);
        }
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        View v;
        //CategoryName
        RelativeLayout relativeLayoutCategoryName;
        TextView textViewCateGoryName;
        RelativeLayout linearLayoutPost1;
        ImageView imageViewPost1;
        TextView textViewTitlePost1;
        LinearLayout linearLayoutPost2;
        ImageView imageViewPost2;
        TextView textViewDescriptionPost2;
        LinearLayout linearLayoutPost3;
        ImageView imageViewPost3;
        TextView textViewDescriptionPost3;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            this.v = itemView;
            relativeLayoutCategoryName = (RelativeLayout) v.findViewById(R.id.relativeLayoutCategoryName);
            textViewCateGoryName = (TextView) v.findViewById(R.id.textViewCateGoryName);
            linearLayoutPost1 = (RelativeLayout) v.findViewById(R.id.linearLayoutPost1);
            imageViewPost1 = (ImageView) v.findViewById(R.id.imageViewPost1);
            textViewTitlePost1 = (TextView) v.findViewById(R.id.textViewTitlePost1);
            linearLayoutPost2 = (LinearLayout) v.findViewById(R.id.linearLayoutPost2);
            imageViewPost2 = (ImageView) v.findViewById(R.id.imageViewPost2);
            textViewDescriptionPost2 = (TextView) v.findViewById(R.id.textViewDescriptionPost2);
            linearLayoutPost3 = (LinearLayout) v.findViewById(R.id.linearLayoutPost3);
            imageViewPost3 = (ImageView) v.findViewById(R.id.imageViewPost3);
            textViewDescriptionPost3 = (TextView) v.findViewById(R.id.textViewDescriptionPost3);
        }
    }
}