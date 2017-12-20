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

        // initDataInFireBase();
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

                GenericTypeIndicator<HashMap<String, Post>> objectsGTypeInd = new GenericTypeIndicator<HashMap<String, Post>>() {};
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
                            GenericTypeIndicator<HashMap<String, Post>> objectsGTypeInd = new GenericTypeIndicator<HashMap<String, Post>>() {};
                            Map<String, Post> objectHashMap = dataSnapshot.getValue(objectsGTypeInd);
                            List<Post> listPost = new ArrayList<Post>(objectHashMap.values());
                            GlobalStaticData.listPostOfCategory.put(category,listPost);
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

   /* private void initDataInFireBase(){
        List<Post> listPost = new ArrayList<>();
        List<String> listTag1 = new ArrayList<>();
        List<String> listTag2 = new ArrayList<>();
        List<String> listTag3 = new ArrayList<>();
        List<String> listTag4 = new ArrayList<>();
        List<String> listTag5 = new ArrayList<>();
        List<String> listTag6 = new ArrayList<>();
        List<String> listTag7 = new ArrayList<>();
        List<String> listTag8 = new ArrayList<>();
        List<String> listTag9 = new ArrayList<>();
        List<String> listTag10 = new ArrayList<>();
        List<String> listTag11 = new ArrayList<>();
        List<String> listTag12 = new ArrayList<>();
        List<String> listTag13 = new ArrayList<>();
        List<String> listTag14 = new ArrayList<>();
        List<String> listTag15 = new ArrayList<>();
        List<String> listTag16 = new ArrayList<>();
        List<String> listTag17 = new ArrayList<>();
        List<String> listTag18 = new ArrayList<>();


        listTag1.add("Android");
        listTag1.add("mobile");
        listTag1.add("the gioi di dong");
        listTag2.add("Dell");
        listTag2.add("laptop");
        listTag2.add("viet nam");
        listTag3.add("dior");
        listTag3.add("thoi trang");
        listTag4.add("ky duyen");
        listTag4.add("fasion show");
        listTag4.add("hoa hau");
        Post post1 = new Post("Anh Thi Trần","Công Nghệ",new HashMap<String,Comment>()
                , "Smartphone mới của Google không có độ bền như một số sản phẩm cùng tầm giá. Nó có thể dễ dàng bị xước, bẻ cong và cắt thành từng mảng. Mới đây, kênh YouTube của JerrRigEverything, phổ biến với những thử nghiệm “phá hoại” các loại điện thoại đã đăng tải clip về Pixel 2. Theo đó, smartphone đến từ Google bị tra tấn bằng cách đốt, khắc hình thậm chí cắt bằng dao cắt giấy. Đầu tiên, JerryRigEverything thử nghiệm chống xước của màn hình. Phải ở mức 6 với lực tì tay mạnh, Google Pixel 2 mới bắt đầu xuất hiện những vết xước nhỏ. Tiếp theo đó, trang YouTube này bắt đầu bẻ cong sản phẩm. Không giống như những sản phẩm cao cấp khác, smartphone của Google bị bẻ cong khá dễ dàng. Có thể thấy, nếu so sánh với dòng Galaxy S8 hay iPhone 8 mà JerrRigEverything từng thử nghiệm, sản phẩm không được bền như vậy. Có thể nói, sản phẩm Google Pixel 2 không có độ bền giống như các smartphone khác. Tuy nhiên, nó mang nhiều tính năng mới để có thể cạnh tranh trực tiếp với iPhone hay dòng S của Samsung."
                , "02-09-2017 14:00:01", "Smartphone mới của Google không có độ bền như một số sản phẩm cùng tầm giá."
                , "https://znews-photo-td.zadn.vn/w660/Uploaded/OFH_oazszstq/2017_10_23/Screen_Shot_20171023_at_211412.jpg",
                "postId2",listTag1,"Google Pixel 2 dễ dàng bị bẻ cong trong thử nghiệm?",new ArrayList<String>(),new ArrayList<String>());
        Post post2 = new Post("Local","Công Nghệ",new HashMap<String,Comment>()
                , "Dell vừa giới thiệu tại Việt Nam chiếc XPS 13 9365 với độ mỏng 8 mn. Đây được xem là laptop 13 inch mỏng nhất thế giới. Dell XPS 9365 có kích thước màn hình 13 inch, thiết kế nhôm nguyên khối chắc chắn với viền màn hình 5,2 mm, độ mỏng của máy 8 mm và trọng lượng 1,2 kg. Vì được trang bị màn hình cảm ứng độ phân giải QHD+ và tương thích bút cảm ứng Active nên hãng gọi đây là mẫu laptop 2 trong 1, có khả năng hoạt động như một chiếc tablet. Vì được trang bị màn hình cảm ứng độ phân giải QHD+ và tương thích bút cảm ứng Active nên hãng gọi đây là mẫu laptop 2 trong 1, có khả năng hoạt động như một chiếc tablet. Về cấu hình máy được trang bị vi xử lý Intel Core i7, RAM 16 GB LPDDR3 và ổ cứng có thể nâng cấp lên SSD 512 GB. Thời lượng pin của máy đạt 18 giờ hoạt động liên tục, theo hãng sản xuất. XPS 13 9365 có giá sau thuế là 54,9 triệu đồng tại thị trường Việt Nam. Bên cạnh XPS 9365, Dell còn giới thiệu mẫu laptop Inspiron 7373, mẫu laptop đầu tiên của dòng 7000 có khả năng xoay lật. Dell trang bị cho Inspiron 7373 cấu hình gồm vi xử lý Intel thế hệ thứ 8, card đồ họa rồi NVIDIA GeForce 940MX, ổ cứng SSD 256 GB, HDD 1 TB và màn hình Full HD. Giá bán sản phẩm này ở mức 27,49 triệu đồng."
                , "02-09-2017 14:00:01", "Dell XPS 9365 có kích thước màn hình 13 inch, thiết kế nhôm nguyên khối chắc chắn với viền màn hình 5,2 mm"
                , "https://znews-photo-td.zadn.vn/w660/Uploaded/ofh_jgmzfuqz/2017_10_24/xt_zing42.jpg",
                "postId3",listTag2,"Laptop 13 inch mỏng nhất thế giới về VN",new ArrayList<String>(),new ArrayList<String>());
        Post post3 = new Post("Anh Thi Trần","Thời trang",new HashMap<String,Comment>()
                , "Mà hay ho nhất là trong loạt lần bóc mẽ đó có cả sự tham gia nhiệt tình từ fan của Angela Baby. Từ sau khi trở thành đại sứ của Dior, Triệu Lệ Dĩnh đã liên tục trở thành cái tên bị xì xào bán tán khắp các trang mạng xã hội, người khen không ít mà kẻ chê cũng chẳng thiếu. Mới đây nhất, cô nàng nàng này lại vướng vào một lùm xùm do chính các fan của mình gây ra. Chuyện là, các fan của cô Triệu nô nức rủ nhau đi mua đồ của Dior để thể hiện sự ủng hộ dành cho thần tượng. Tuy nhiên, bên cạnh những fan chấp nhận chịu chi, thì lại có không ít fan mua hàng fake hoặc 'mượn' ảnh của người khác rồi nói đó là đồ họ mua. Đỉnh điểm là mới đây, một fan của Angela Baby đã lên tiếng tố giác fan Triệu Lệ Dĩnh ăn cắp ảnh của cô. Cả Angela Baby và Triệu Lệ Dĩnh cùng là đại sứ của thương hiệu Dior tại Trung Quốc, bởi vậy mà sự việc đã thu hút rất nhiều sự chú ý."
                , "02-09-2017 14:00:01", "Từ sau khi trở thành đại sứ của Dior, Triệu Lệ Dĩnh đã liên tục trở thành cái tên bị xì xào bán tán khắp các trang mạng xã hội"
                , "https://kenh14cdn.com/2017/3-1508854846849.jpg",
                "postId4",listTag3,"Khoe mua đồ Dior để ủng hộ thần tượng, fan Triệu Lệ Dĩnh hết bị tố ăn cắp ảnh trên mạng lại bị nghi mua hàng fake",new ArrayList<String>(),new ArrayList<String>());
        Post post4 = new Post("Phạm Thị Thảo","Thời trang",new HashMap<String,Comment>()
                , "Show diễn Her Legend - Huyền Thoại Người Con Gái, chương trình kỉ niệm 10 năm trong nghề thiết kế của Adrian Anh Tuấn là một trong những sự kiện nổi bật trong tuần này. Trước giờ G nhiều sao Việt đã kéo đến của hàng của nhà thiết kế để thử các thiết kế mới nhất chuẩn bị cho show diễn. Hoa hậu Phạm Hương, 'nàng thơ' từng xuất hiện ở BST mùa hè 2017 'Away We Wow' chia sẻ, điểm thu hút cô ở BST lần này là sau những lần bùng nổ cùng các mảng màu tươi tắn, NTK Adrian Anh Tuấn đã lấy sắc trắng - sắc màu của sự trung hoà, vẻ đẹp kinh điển và thuần khiết, làm gam màu chủ đạo cho BST lần này. Người xem cũng sẽ phát hiện ra sự xuất hiện của những mẫu váy đính kết đầy quyến rũ, cùng các chi tiết được đính kết hoàn toàn bằng tay. Với BST mùa đông lần này, Adrian Anh Tuấn cũng muốn phối nhiều loại chất liệu với nhau bằng cách mặc nhiều layers khác nhau một cách thời trang nhất."
                , "02-09-2017 14:00:01", "Show diễn Her Legend - Huyền Thoại Người Con Gái,"
                , "https://kenh14cdn.com/2017/3-1508854846849.jpg",
                "postId5",listTag4,"Kỳ Duyên ngày một ra dáng fashion icon, đọ sắc với Phạm Hương trong buổi thử đồ của NTK Adrian Anh Tuấn",new ArrayList<String>(),new ArrayList<String>());


        listPost.add(post1);
        listPost.add(post2);
        listPost.add(post3);
        listPost.add(post4);
        for(final Post post:listPost){
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
                                    if(!listCategory.contains(post.getcategory()))
                                    {
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
                                    for(String tag: post.getTags()){
                                        if(!listTag.contains(tag)){
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
    }*/
}
