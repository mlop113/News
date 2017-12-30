package com.android;

/**
 * Created by Ngoc Vu on 12/18/2017.
 */
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.android.Models.UserMember;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.android.Effect.Typewriter;
import com.android.Global.AppConfig;
import com.android.Global.GlobalStaticData;
import com.android.Models.Comment;
import com.android.Models.Post;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class SplashScreenActivity extends AppCompatActivity {

    ArrayList<Integer> arrayImage;
    private RelativeLayout relativeLayout;
    private static int SPLASH_TIME_OUT = 1500;
    private Typewriter typewriterAppname;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        typewriterAppname = (Typewriter) findViewById(R.id.typewriterAppname);
        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);

        arrayImage = new ArrayList<>();
        //arrayImage.add(R.drawable.bg_splashscreen1);
        //arrayImage.add(R.drawable.bg_splashscreen2);
        arrayImage.add(R.drawable.bg_splashscreen3);
        Random random = new Random();
        int p = random.nextInt(arrayImage.size());

        relativeLayout.setBackgroundResource(arrayImage.get(p));

        String appname = getString(R.string.app_name);
        typewriterAppname.setCharacterDelay(100);
        typewriterAppname.animateText(appname);


        databaseReference = FirebaseDatabase.getInstance().getReference();
        // intuser();
        // initDataInFireBase();
    }

    private void intuser() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("");
        DatabaseReference postsRef = ref.child("UserMembers");
        postsRef.push().setValue(new UserMember("03 Lê Văn Việt", "24-01-2107 14:20:00", "ngoc117@gmail.com", "https://www.facebook.com/photo.php?fbid=1928810227435906&set=pcb.1928813470768915&type=3", "ngoc3", "Phan Vu Xuan Ngoc", "12345", "0981418198", "Nam", postsRef.push().getKey().toString()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreenActivity.this, Login.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    private void initData() {
        /*databaseReference.child(AppConfig.FIREBASE_FIELD_USERMEMBERS).child("-KyUeplvVdI5vI4ghU01").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GlobalStaticData.currentUser = dataSnapshot.getValue(UserMember.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
        //listPost home
        databaseReference.child(AppConfig.FIREBASE_FIELD_POSTS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                /*for(DataSnapshot dataPost:dataSnapshot.getChildren()){
                    GlobalStaticData.listPostHome.add(dataPost.getValue(Post.class));
                }*/

                GenericTypeIndicator<HashMap<String, Post>> objectsGTypeInd = new GenericTypeIndicator<HashMap<String, Post>>() {
                };
                Map<String, Post> objectHashMap = dataSnapshot.getValue(objectsGTypeInd);
                GlobalStaticData.listPostHome = new ArrayList<Post>(objectHashMap.values());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //listCategory home
        databaseReference.child(AppConfig.FIREBASE_FIELD_CATEGORIES).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataCategory : dataSnapshot.getChildren()) {
                    final String category = (String) dataCategory.getValue();
                    GlobalStaticData.listCategoryHome.add(category);
                    databaseReference.child(AppConfig.FIREBASE_FIELD_POSTS).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            GenericTypeIndicator<HashMap<String, Post>> objectsGTypeInd = new GenericTypeIndicator<HashMap<String, Post>>() {
                            };
                            Map<String, Post> objectHashMap = dataSnapshot.getValue(objectsGTypeInd);
                            List<Post> listPost = new ArrayList<Post>(objectHashMap.values());
                            GlobalStaticData.listPostOfCategory.put(category, listPost);
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
        });


        //listBookmarks
        databaseReference.child(AppConfig.FIREBASE_FIELD_BOOKMARKS).child(GlobalStaticData.currentUser.getUserId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataBookmark : dataSnapshot.getChildren()) {
                    GlobalStaticData.listBookmark.add(dataBookmark.getValue().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //listTag
        databaseReference.child(AppConfig.FIREBASE_FIELD_TAGS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataTag : dataSnapshot.getChildren()) {
                    GlobalStaticData.listTag.add(dataTag.getValue().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void initDataInFireBase() {
        List<Post> listPost = new ArrayList<>();
        List<String> listTag1 = new ArrayList<>();


        listTag1.add("Android");

        Post post1 = new Post("Anh Thi Trần", "Công Nghệ", new HashMap<String, Comment>()
                , "Smartphone mới của Google không có độ bền như một số sản phẩm cùng tầm giá. Nó có thể dễ dàng bị xước, bẻ cong và cắt thành từng mảng. Mới đây, kênh YouTube của JerrRigEverything, phổ biến với những thử nghiệm “phá hoại” các loại điện thoại đã đăng tải clip về Pixel 2. Theo đó, smartphone đến từ Google bị tra tấn bằng cách đốt, khắc hình thậm chí cắt bằng dao cắt giấy. Đầu tiên, JerryRigEverything thử nghiệm chống xước của màn hình. Phải ở mức 6 với lực tì tay mạnh, Google Pixel 2 mới bắt đầu xuất hiện những vết xước nhỏ. Tiếp theo đó, trang YouTube này bắt đầu bẻ cong sản phẩm. Không giống như những sản phẩm cao cấp khác, smartphone của Google bị bẻ cong khá dễ dàng. Có thể thấy, nếu so sánh với dòng Galaxy S8 hay iPhone 8 mà JerrRigEverything từng thử nghiệm, sản phẩm không được bền như vậy. Có thể nói, sản phẩm Google Pixel 2 không có độ bền giống như các smartphone khác. Tuy nhiên, nó mang nhiều tính năng mới để có thể cạnh tranh trực tiếp với iPhone hay dòng S của Samsung."
                , "02-09-2017 14:00:01", "Smartphone mới của Google không có độ bền như một số sản phẩm cùng tầm giá."
                , "https://znews-photo-td.zadn.vn/w660/Uploaded/OFH_oazszstq/2017_10_23/Screen_Shot_20171023_at_211412.jpg",
                "postId2", listTag1, "Google Pixel 2 dễ dàng bị bẻ cong trong thử nghiệm?", new ArrayList<String>(), new ArrayList<String>());

        listPost.add(post1);
        for (final Post post : listPost) {
            databaseReference.child(AppConfig.FIREBASE_FIELD_POSTS).push().setValue(post, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(final DatabaseError databaseError, final DatabaseReference databaseReference1) {
                    databaseReference1.child("postId").setValue(databaseReference1.getKey()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            databaseReference.child(AppConfig.FIREBASE_FIELD_CATEGORIES).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    List<String> listCategory = (List<String>) dataSnapshot.getValue();
                                    if (!listCategory.contains(post.getcategory())) {
                                        listCategory.add(post.getcategory());
                                        databaseReference.child(AppConfig.FIREBASE_FIELD_CATEGORIES).setValue(listCategory);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });


                            databaseReference.child(AppConfig.FIREBASE_FIELD_TAGS).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    List<String> listTag = (List<String>) dataSnapshot.getValue();
                                    for (String tag : post.getTags()) {
                                        if (!listTag.contains(tag)) {
                                            listTag.add(tag);
                                            databaseReference.child(AppConfig.FIREBASE_FIELD_TAGS).setValue(listTag);
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    });
                }
            });
        }
    }
}

