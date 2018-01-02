package com.android.Activity_Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.Adapters.PostDetailAdapter;
import com.android.CustomView.CustomSnackbar;
import com.android.Global.AppConfig;
import com.android.Global.AppPreferences;
import com.android.Global.GlobalFunction;
import com.android.Login;
import com.android.Models.Comment;
import com.android.Models.Post;
import com.android.Models.ReplyComment;
import com.android.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import dmax.dialog.SpotsDialog;

/**
 * Created by Ngoc Vu on 12/18/2017.
 */

public class PostDetailActivity extends AppCompatActivity implements View.OnClickListener{
    AppPreferences appPreferences;
    InputMethodManager inputMethodManager;
    //animation
    Animation animlike;
    Animation animshake;
    //back
    LinearLayout linearLayoutBack;
    //bookmark
    LinearLayout linearLayoutBookmark;
    //data Post
    RecyclerView recyclerViewPostDetail;
    PostDetailAdapter postDetailAdapter;
    Intent intent;
    Post post;

    //bottom
    LinearLayout linearLayoutLike;
    ImageView imageViewLike;
    EditText editTextComment;
    LinearLayout linearLayoutSend;
    ImageView imageViewSend;

    DatabaseReference databaseReference;

    //dialog wait
    SpotsDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        appPreferences = AppPreferences.getInstance(this);
        //connect firebase
        //fireBase();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        overridePendingTransitionEnter();
        inputMethodManager = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        //animation
        animlike = AnimationUtils.loadAnimation(this, R.anim.animlike);
        animshake = AnimationUtils.loadAnimation(this, R.anim.animshake);

        intent = getIntent();
        if(intent!=null)
        {
            post = (Post) intent.getSerializableExtra(AppConfig.POST);
            if(post==null)
            {
                Toast.makeText(this, "Error data transfer", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
        else
        {
            Toast.makeText(this, "Error data transfer", Toast.LENGTH_SHORT).show();
            finish();
        }
        mapping();
        initView();
        event();


    }

    private void mapping() {
        //back
        linearLayoutBack = (LinearLayout) findViewById(R.id.linearLayoutBack);
        //bookmark
        linearLayoutBookmark = (LinearLayout) findViewById(R.id.linearLayoutBookmark);
        //recyclerView contant content,tag,related and comment
        recyclerViewPostDetail = (RecyclerView) findViewById(R.id.recyclerViewPostDetail);

        //bottom
        linearLayoutLike = (LinearLayout) findViewById(R.id.linearLayoutLike);
        imageViewLike = (ImageView) findViewById(R.id.imageViewLike);
        editTextComment = (EditText) findViewById(R.id.editTextComment);
        linearLayoutSend = (LinearLayout) findViewById(R.id.linearLayoutSend);
        imageViewSend = (ImageView) findViewById(R.id.imageViewSend);
    }

    private void initView() {
        postDetailAdapter = new PostDetailAdapter(this,post);
        recyclerViewPostDetail.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewPostDetail.setAdapter(postDetailAdapter);
        progressDialog = new SpotsDialog(this,R.style.CustomAlertDialog);
        checkLiked(imageViewLike);
    }

    private void event() {
        //back
        linearLayoutBack.setOnClickListener(this);
        //bookmark
        linearLayoutBookmark.setOnClickListener(this);
        //bottom
        linearLayoutLike.setOnClickListener(this);
        linearLayoutSend.setOnClickListener(this);
    }

    private void checkAction(){
        if(intent!=null){
            if(intent.getStringExtra(AppConfig.ACTION)!=null) {
                String action = intent.getStringExtra(AppConfig.ACTION);
                if (action.equals(AppConfig.COMMENT)) {
                    editTextComment.requestFocus();
                    inputMethodManager.showSoftInput(editTextComment, InputMethodManager.SHOW_IMPLICIT);
                }
            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.linearLayoutBack:
                finish();
                break;
            case R.id.linearLayoutBookmark:
                onClickBookMark();
                break;
            //bottom
            case R.id.linearLayoutLike:
                onClickLikePost(imageViewLike);
                break;
            case R.id.linearLayoutSend:
                if(appPreferences.isLogin()) {
                    Date myDate = new Date();
                    if (!TextUtils.isEmpty(editTextComment.getText().toString().trim())) {
                        Comment comment = new Comment("312321", new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(myDate),
                                editTextComment.getText().toString(), new HashMap<String, ReplyComment>(),
                                new ArrayList<String>(), appPreferences.getUserId());
                        databaseReference.child(AppConfig.FIREBASE_FIELD_POSTS).child(post.getPostId()).child(AppConfig.FIREBASE_FIELD_COMMENTS)
                                .push().setValue(comment, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                databaseReference.child("commentId").setValue(databaseReference.getKey());
                                Toast.makeText(PostDetailActivity.this, "sent", Toast.LENGTH_SHORT).show();
                                editTextComment.clearFocus();
                                editTextComment.setText("");
                                inputMethodManager.hideSoftInputFromWindow(editTextComment.getWindowToken(), 0);
                                imageViewSend.startAnimation(animshake);
                                recyclerViewPostDetail.smoothScrollToPosition(View.FOCUS_DOWN);
                            }
                        });

                    }
                }
                else{
                    Intent intentLogin = new Intent(this,Login.class);
                    startActivityForResult(intentLogin,AppConfig.REQUEST_CODE_LOGIN);
                }
                break;
        }
    }

    private void checkLiked(final ImageView imageViewLike) {

        databaseReference.child(AppConfig.FIREBASE_FIELD_POSTS).child(post.getPostId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String userId = appPreferences.getUserId();
                post = dataSnapshot.getValue(Post.class);
                if (post.getUserLikeIds() != null && post.getUserLikeIds().size() > 0) {
                    if (post.getUserLikeIds().contains(userId)) {
                        imageViewLike.setImageResource(R.drawable.ic_liked);
                    } else {
                        imageViewLike.setImageResource(R.drawable.ic_like);
                    }
                } else {
                    imageViewLike.setImageResource(R.drawable.ic_like);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void onClickLikePost( final ImageView imageViewLike) {
        if(appPreferences.isLogin()) {
            //get from user_post
            if (post.getUserLikeIds() != null && post.getUserLikeIds().size() > 0) {
                String userId = appPreferences.getUserId();
                //if user liked this post
                if (post.getUserLikeIds().contains(userId)) {
                    int index = post.getUserLikeIds().indexOf(userId);
                    databaseReference.child(AppConfig.FIREBASE_FIELD_POSTS).child(post.getPostId())
                            .child(AppConfig.FIREBASE_FIELD_USERLIKEIDS).child(String.valueOf(index)).removeValue();
                } else {
                    databaseReference.child(AppConfig.FIREBASE_FIELD_POSTS).child(post.getPostId())
                            .child(AppConfig.FIREBASE_FIELD_USERLIKEIDS).child(String.valueOf(post.getUserLikeIds().size())).setValue(String.valueOf(userId));
                    imageViewLike.startAnimation(animlike);
                }
            } else {
                String userId = appPreferences.getUserId();
                databaseReference.child(AppConfig.FIREBASE_FIELD_POSTS).child(post.getPostId())
                        .child(AppConfig.FIREBASE_FIELD_USERLIKEIDS).child("0").setValue(String.valueOf(userId));
                imageViewLike.startAnimation(animlike);
            }
        }
        else {
            Intent intentLogin = new Intent(this,Login.class);
            startActivityForResult(intentLogin,AppConfig.REQUEST_CODE_LOGIN);
        }
    }

    private void onClickBookMark(){
        if(appPreferences.isLogin()) {
            progressDialog.show();
            databaseReference.child(AppConfig.FIREBASE_FIELD_BOOKMARKS).child(appPreferences.getUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    final List<String> listBookmark = new ArrayList<String>();
                    //have not been save bookmark
                    if (dataSnapshot.getValue() != null) {
                        for (DataSnapshot dataBookmark : dataSnapshot.getChildren()) {
                            listBookmark.add(dataBookmark.getValue().toString());
                        }
                        if (listBookmark.contains(post.getPostId())) {
                            listBookmark.remove(post.getPostId());
                            databaseReference.child(AppConfig.FIREBASE_FIELD_BOOKMARKS).child(appPreferences.getUserId()).setValue(listBookmark).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(PostDetailActivity.this, getString(R.string.removebookmark), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            listBookmark.add(post.getPostId());
                            databaseReference.child(AppConfig.FIREBASE_FIELD_BOOKMARKS).child(appPreferences.getUserId()).setValue(listBookmark).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    CustomSnackbar customSnackbar = CustomSnackbar.make(linearLayoutBookmark, 1);
                                    customSnackbar.setDuration(CustomSnackbar.LENGTH_LONG);
                                    customSnackbar.setText(getString(R.string.savebookmark));
                                    customSnackbar.setAction("Xem " + getString(R.string.action_bookmarks), new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            GlobalFunction.onClickViewBookMark(PostDetailActivity.this, progressDialog);
                                        }
                                    });
                                    customSnackbar.show();
                                }
                            });
                        }
                    } else {
                        listBookmark.add(post.getPostId());
                        databaseReference.child(AppConfig.FIREBASE_FIELD_BOOKMARKS).child(appPreferences.getUserId()).setValue(listBookmark).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                CustomSnackbar customSnackbar = CustomSnackbar.make(linearLayoutBookmark, 1);
                                customSnackbar.setDuration(CustomSnackbar.LENGTH_LONG);
                                customSnackbar.setText(getString(R.string.savebookmark));
                                customSnackbar.setAction("Xem " + getString(R.string.action_bookmarks), new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        GlobalFunction.onClickViewBookMark(PostDetailActivity.this, progressDialog);
                                    }
                                });
                                customSnackbar.show();
                            }
                        });
                    }
                    if (BookMarkActivity.listPost != null) {
                        databaseReference.child(AppConfig.FIREBASE_FIELD_POSTS).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                BookMarkActivity.listPost = new ArrayList<Post>();
                                for (String pId : listBookmark) {
                                    BookMarkActivity.listPost.add(dataSnapshot.child(pId).getValue(Post.class));
                                }
                                if (BookMarkActivity.postsOnRequestAdapter != null)
                                    BookMarkActivity.postsOnRequestAdapter.setData(BookMarkActivity.listPost);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                    progressDialog.dismiss();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        else{
            Intent intentLogin = new Intent(this,Login.class);
            startActivityForResult(intentLogin,AppConfig.REQUEST_CODE_LOGIN);
        }
    }


    @Override
    public void finish() {
        super.finish();
        inputMethodManager.hideSoftInputFromWindow(editTextComment.getWindowToken(),0);
        overridePendingTransitionExit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkAction();
        postDetailAdapter.notifyDataSetChanged();
    }

    /**
     * Overrides the pending Activity transition by performing the "Enter" animation.
     */
    protected void overridePendingTransitionEnter() {
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    /**
     * Overrides the pending Activity transition by performing the "Exit" animation.
     */
    protected void overridePendingTransitionExit() {
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data==null) {
            return;
        }

        if(requestCode==AppConfig.REQUEST_CODE_LOGIN && resultCode==AppConfig.RESULT_CODE_LOGIN)
        {
            postDetailAdapter.notifyDataSetChanged();
        }
    }


}
