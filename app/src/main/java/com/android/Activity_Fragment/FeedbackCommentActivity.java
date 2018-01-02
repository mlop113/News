package com.android.Activity_Fragment;

import android.content.Intent;
import android.os.Bundle;
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

import com.android.Adapters.FeedbackCommentAdapter;
import com.android.Global.AppConfig;
import com.android.Global.AppPreferences;
import com.android.Interface.IOnClickFeedback;
import com.android.Login;
import com.android.Models.Comment;
import com.android.Models.Post;
import com.android.Models.ReplyComment;
import com.android.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Ngoc Vu on 12/18/2017.
 */

public class FeedbackCommentActivity extends AppCompatActivity implements View.OnClickListener,IOnClickFeedback {
    InputMethodManager inputMethodManager;
    //animation
    Animation animlike;
    Animation animshake;

    //back
    LinearLayout linearLayoutback;

    //post
    Post post;

    //comm
    Comment comment;

    Intent intent;
    //feedbackcomment
    RecyclerView recyclerViewFeedbackComment;
    FeedbackCommentAdapter feedbackCommentAdapter;

    //bottom
    LinearLayout linearLayoutLike;
    ImageView imageViewLike;
    EditText editTextComment;
    LinearLayout linearLayoutSend;
    ImageView imageViewSend;

    DatabaseReference databaseReference;
    AppPreferences appPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_comment);
        appPreferences = AppPreferences.getInstance(this);
        databaseReference = FirebaseDatabase.getInstance().getReference();

        inputMethodManager = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);

        //animation
        animlike = AnimationUtils.loadAnimation(this, R.anim.animlike);
        animshake = AnimationUtils.loadAnimation(this, R.anim.animshake);

        intent = getIntent();
        if(intent!=null)
        {
            comment = (Comment) intent.getSerializableExtra(AppConfig.COMMENT);
            post = (Post) intent.getSerializableExtra(AppConfig.POST);
        }
        else {
            finish();
            Toast.makeText(this, "Error data tranfer", Toast.LENGTH_SHORT).show();
        }
        mapping();
        initView();
        event();
    }

    private void mapping() {
        //back
        linearLayoutback = (LinearLayout) findViewById(R.id.linearLayoutBack);
        //
        recyclerViewFeedbackComment = (RecyclerView) findViewById(R.id.recyclerViewFeedbackComment);

        //bottom
        linearLayoutLike = (LinearLayout) findViewById(R.id.linearLayoutLike);
        imageViewLike = (ImageView) findViewById(R.id.imageViewLike);
        editTextComment = (EditText) findViewById(R.id.editTextComment);
        linearLayoutSend = (LinearLayout) findViewById(R.id.linearLayoutSend);
        imageViewSend = (ImageView) findViewById(R.id.imageViewSend);
    }

    private void initView() {
        feedbackCommentAdapter = new FeedbackCommentAdapter(this,post,comment);
        feedbackCommentAdapter.setiOnClickFeedback(this);
        recyclerViewFeedbackComment.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewFeedbackComment.setAdapter(feedbackCommentAdapter);
        checkLiked();
        databaseReference.child(AppConfig.FIREBASE_FIELD_POSTS).child(post.getPostId())
                .child(AppConfig.FIREBASE_FIELD_COMMENTS).child(comment.getCommentId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Comment comment = dataSnapshot.getValue(Comment.class);
                feedbackCommentAdapter.setData(comment);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void event() {
        linearLayoutback.setOnClickListener(this);

        //bottom
        linearLayoutLike.setOnClickListener(this);
        linearLayoutSend.setOnClickListener(this);
    }

    boolean isLiked=false;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.linearLayoutBack:
                finish();
                break;
            //bottom
            case R.id.linearLayoutLike:
                onClickLikeComment();
                break;
            case R.id.linearLayoutSend:
                if(appPreferences.isLogin()) {
                    Date myDate = new Date();
                    if (!TextUtils.isEmpty(editTextComment.getText().toString().trim())) {
                        final ReplyComment replyComment = new ReplyComment(editTextComment.getText().toString().trim()
                                , new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(myDate), appPreferences.getUserId());
                        databaseReference.child(AppConfig.FIREBASE_FIELD_POSTS).child(post.getPostId()).child(AppConfig.FIREBASE_FIELD_COMMENTS)
                                .child(comment.getCommentId()).child(AppConfig.FIREBASE_FIELD_REPLYCOMMENTS).push().setValue(replyComment, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                Toast.makeText(FeedbackCommentActivity.this, "sent", Toast.LENGTH_SHORT).show();
                                editTextComment.clearFocus();
                                editTextComment.setText("");
                                inputMethodManager.hideSoftInputFromWindow(editTextComment.getWindowToken(), 0);
                                imageViewSend.startAnimation(animshake);
                                recyclerViewFeedbackComment.smoothScrollToPosition(View.FOCUS_DOWN);
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


    @Override
    public void finish() {
        super.finish();
        inputMethodManager.hideSoftInputFromWindow(editTextComment.getWindowToken(),0);
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkAction();
        feedbackCommentAdapter.notifyDataSetChanged();
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
    public void onClickFeedback() {
        editTextComment.requestFocus();
        inputMethodManager.showSoftInput(editTextComment, InputMethodManager.SHOW_IMPLICIT);
    }


    private void checkLiked() {
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
                            imageViewLike.setImageResource(R.drawable.ic_liked);
                        } else {
                            imageViewLike.setImageResource(R.drawable.ic_like);
                        }
                    } else {
                        imageViewLike.setImageResource(R.drawable.ic_like);
                    }
                }
                else {
                    imageViewLike.setImageResource(R.drawable.ic_like);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void onClickLikeComment() {
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
                    imageViewLike.startAnimation(animlike);
                }
            } else {
                comment.setUserLikeIds(new ArrayList<String>());
                comment.getUserLikeIds().add(userId);
                databaseReference.child(AppConfig.FIREBASE_FIELD_POSTS).child(post.getPostId())
                        .child(AppConfig.FIREBASE_FIELD_COMMENTS).child(comment.getCommentId()).child(AppConfig.FIREBASE_FIELD_USERLIKEIDS).setValue(comment.getUserLikeIds());
                imageViewLike.startAnimation(animlike);
            }
        }
        else{
            Intent intentLogin = new Intent(this,Login.class);
            startActivityForResult(intentLogin,AppConfig.REQUEST_CODE_LOGIN);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data==null) {
            return;
        }

        if(requestCode==AppConfig.REQUEST_CODE_LOGIN && resultCode==AppConfig.RESULT_CODE_LOGIN)
        {
            feedbackCommentAdapter.notifyDataSetChanged();
        }
    }
}
