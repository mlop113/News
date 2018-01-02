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
         //initDataInFireBase();
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
                Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
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

        Post post1 = new Post("Bá Đô - Võ Hải", "Thời sự", new HashMap<String, Comment>()
                , "Chiều 2/1, ông Đỗ Lai Luật, Phó chủ tịch thường trực UBND huyện Quốc Oai, thông tin đã tìm thấy ôtô cá nhân của Chủ tịch huyện Nguyễn Hồng Lâm, người \"mất tích\" một tuần qua. \n" +
                "\n" +
                "Xe được gửi ở bãi xe trong ngõ 113 Trần Duy Hưng (Cầu Giấy, Hà Nội), kết quả khám xe \"không có gì đặc biệt ngoài một số dụng cụ cá nhân\". Ông Luật cho biết, việc phát hiện, khám xe, đưa xe về gara của UBND huyện đều do cơ quan công an thực hiện với sự chứng kiến của gia đình, UBND huyện.\n" +
                "\n" +
                "Dẫn báo cáo từ công an, ông Luật thông tin một trong những cuộc điện thoại cuối cùng của ông Lâm trước lúc không liêc lạc được là cho con trai vào sáng 26/12/2017. Trong cuộc gọi, ông Lâm nói với con trai \"đang gặp chuyện rắc rối\".\n" +
                "\n" +
                "Cơ quan chức năng đã xác định vị trí cuối cùng số máy điện thoại liên lạc được là ở khu vực hồ Đền Lừ (quận Hoàng Mai). Nhà chức trách sau đó đã tổ chức rà soát, nhưng \"không phát hiện gì\".\n" +
                "\n" +
                "Lãnh đạo huyện Quốc Oai cho biết, một ngày trước khi \"mất tích\" (ngày 25/12), ông Lâm vẫn điều hành công việc của UBND huyện bình thường. Tối cùng ngày, ông dự bữa cơm chia tay một cán bộ ở Phòng Tài chính huyện về hưu.\n" +
                "\n" +
                "\"Lúc khoảng 21h, anh Lâm tự lái xe ra khỏi cơ quan, sau đó đi đâu không ai biết. Đến sáng hôm sau, khoảng 6h35 ngày 26/12, anh Lâm gọi điện xin phép Bí thư Huyện ủy vắng mặt ở cuộc họp của Ban Thường vụ, sau đó không liên lạc được\", Phó chủ tịch Đỗ Lai Luật cho biết.\n" +
                "\n" +
                "Ông Nguyễn Hồng Lâm mới được bầu làm Chủ tịch UBND từ tháng 7/2017. Quá trình hơn 10 năm làm Phó chủ tịch huyện, ông Lâm được người đồng cấp đánh giá là \"rất thận trọng, điềm đạm, đúng mực không có gì đặc biệt về mối quan hệ, cách ứng xử và công tác lãnh đạo, quản lý cũng không có vấn đề gì\".\n" +
                "\n" +
                "Công an đề nghị người dân thông báo nếu phát hiện ông Lâm\n" +
                "\n" +
                "Phòng Cảnh sát hình sự (Công an Hà Nội) xác định, khoảng 21h ngày 25/12/2017, ông Nguyễn Hồng Lâm lái xe Camry màu đen đi khỏi trụ sở UBND huyện Quốc Oai. Khoảng 21h36, ông gửi xe ở 113 Trần Duy Hưng, sau đó đi bộ ra ngõ 115 Trần Duy Hưng và đi bằng phương tiện gì chưa xác định được.\n" +
                "\n" +
                "Khoảng 21h53 cùng ngày, ông Lâm di chuyển đến khu vực hồ Đền Lừ (Hoàng Mai, Hà Nội). Đến nay gia đình chưa liên lạc được với ông Lâm và chưa xác định ông đi đâu và làm gì.\n" +
                "\n" +
                "Phòng Cảnh sát hình sự đề nghị công dân, cơ quan tổ chức, nếu phát hiện ông Nguyễn Hồng Lâm hoặc có tin tức gì liên quan, liên hệ với phòng qua số điện thoại 0692196451 để phối hợp điều tra."
                , "02-01-2018 20:05:00", "Ôtô của Chủ tịch huyện Quốc Oai được tìm thấy ở bãi xe Trần Duy Hưng (Cầu Giấy, Hà Nội) và đã được về gara của huyện."
                , "https://firebasestorage.googleapis.com/v0/b/news-daeeb.appspot.com/o/xe-cua-ong-Lam-9420-1514894859.jpg?alt=media&token=9eb7ebca-bef5-4ee5-8251-c0e803f22730",
                "postId2", listTag1, "Công an Hà Nội tìm thấy ôtô của Chủ tịch huyện 'mất tích'", new ArrayList<String>(), new ArrayList<String>());

        Post post2 = new Post("Đắc Thành", "Thời sự", new HashMap<String, Comment>()
                , "Chiều 2/1, một lãnh đạo Sở Kế hoạch Đầu tư tỉnh Quảng Nam cho biết, ông Lê Phước Hoài Bảo - Giám đốc Sở, đã đi làm trở lại sau thời gian nghỉ phép.\n" +
                "\n" +
                "Trong lịch làm việc tuần này của Sở Kế hoạch Đầu tư, ông Bảo tham dự một số cuộc họp, cụ thể như: Hội nghị Tỉnh ủy đột xuất để bàn một số nội dung quan trọng theo thẩm quyền; nghe Hội An báo cáo nhu cầu ứng vốn đền bù giải tỏa... \n" +
                "\n" +
                "“Anh Hoài Bảo đi làm từ tuần trước và điều hành công việc bình thường”, nguồn tin nói.\n" +
                "\n" +
                "Tại trụ sở Sở Kế hoạch Đầu tư tỉnh Quảng Nam, khi phóng viên đến liên hệ, xin phỏng vấn ông Lê Phước Hoài Bảo, nhân viên cho biết Giám đốc đi họp; phòng làm việc của ông Bảo ở tầng hai đóng cửa.\n" +
                "\n" +
                "Trước đó tại kỳ họp 20 của Ủy ban Kiểm tra Trung ương, cơ quan này kết luận ông Lê Phước Hoài Bảo - Tỉnh ủy viên, Giám đốc Sở Kế hoạch Đầu tư, không trung thực trong việc kê khai quá trình công tác của bản thân trong hồ sơ, lý lịch và hồ sơ nhân sự ứng cử Ban Chấp hành Đảng bộ tỉnh nhiệm kỳ 2015-2020; ý thức tổ chức kỷ luật kém; vi phạm nguyên tắc tổ chức sinh hoạt của Đảng, bỏ sinh hoạt Đảng nhiều tháng, không chuyển sinh hoạt Đảng theo quy định trong thời gian đi học thạc sĩ tại nước ngoài.\n" +
                "\n" +
                "Ủy Ban kiểm tra yêu cầu Ban thường vụ Tỉnh ủy Quảng Nam phải chỉ đạo tổ chức Đảng và cơ quan có thẩm quyền làm thủ tục xóa tên trong danh sách đảng viên, hủy bỏ các quyết định về công tác cán bộ không đúng đối với ông Lê Phước Hoài Bảo.\n" +
                "\n" +
                "Sau khi có kết luận nêu trên, ngày 18/12/2017, ông Bảo xin nghỉ phép và không nêu lý do cũng như thời gian nghỉ phép đến lúc nào.\n" +
                "\n" +
                "Gần nửa tháng sau, đoàn công tác Ủy ban Kiểm tra Trung ương đã vào làm việc với lãnh đạo tỉnh Quảng Nam để công bố kết luận kiểm tra.\n" +
                "\n" +
                "Ông Nguyễn Chín - Trưởng ban Tuyên giáo Tỉnh ủy Quảng Nam cho biết, sau khi nhận thông báo kết luận, Tỉnh ủy Quảng Nam sẽ triển khai việc xử lý các cá nhân và tập thể sai phạm theo quy định của Đảng và pháp luật của Nhà nước."
                , "02-01-2018 17:00:00", "Giám đốc Sở Kế hoạch Đầu tư tỉnh Quảng Nam xin nghỉ phép từ ngày 18/12/2017, sau khi cơ quan kiểm tra công bố kết luận liên quan đến ông."
                , "https://firebasestorage.googleapis.com/v0/b/news-daeeb.appspot.com/o/IMG-8380-9852-1514887816.jpg?alt=media&token=6f01b7b1-ca11-4c7f-bb91-ae9196a7f860",
                "postId2", listTag1, "Ông Lê Phước Hoài Bảo trở lại nhiệm sở sau thời gian nghỉ phép", new ArrayList<String>(), new ArrayList<String>());
        Post post3 = new Post("Duy Sơn", "Thế giới", new HashMap<String, Comment>()
                , "Nhật Bản tuyên bố sẽ triển khai hai lá chắn phòng thủ tên lửa trên mặt đất Aegis Ashore trị giá 1,8 tỷ USD vào năm 2023 nhằm đối phó với chương trình tên lửa và hạt nhân của Triều Tiên. Tuy nhiên, kế hoạch này của Nhật đã vấp phải phản ứng quyết liệt từ Nga, quốc gia tuyên bố hệ thống Aegis Ashore có thể gây tổn hại quan hệ song phương, theo Popular Mechanics.\n" +
                "\n" +
                "Tokyo đã từng bước thiết lập hệ thống phòng thủ Aegis trên các tàu khu trục hải quân, nhằm đối phó với tên lửa đạn đạo Bình Nhưỡng. Hệ thống này gồm radar mảng pha quét điện tử thụ động AN/SPY-1D và tên lửa SM-3 được thiết kế để bắn hạ tên lửa đạn đạo trong không gian. \n" +
                "\n" +
                "Radar AN/SPY-1D có thể phát hiện và bám bắt nhiều loại mục tiêu, gồm cả tên lửa đạn đạo từ tầm ngắn tới tầm xa. Mỗi tàu chiến lớp Atago và Kongo của Nhật được trang bị 4 đài radar AN/SPY-1D, cho phép chúng theo dõi cùng lúc 800 mục tiêu ở mọi hướng. Hệ thống Aegis cũng có thể lấy dữ liệu từ radar tầm xa AN/TPY-2, một thành phần của Hệ thống phòng thủ tầm cao giai đoạn cuối (THAAD).\n" +
                "\n" +
                "Tokyo hiện sở hữu 6 tổ hợp Aegis trên các khu trục hạm lớp Kongo và Atago tối tân. Tuy nhiên, sự phát triển nhanh chóng của hải quân Trung Quốc buộc Lực lượng Phòng vệ trên biển Nhật Bản (JMSDF) phải phân tán hạm đội tàu chiến, gây suy giảm khả năng bảo vệ đất liền trước mối đe dọa từ tên lửa Triều Tiên. Do đó, Nhật Bản quyết định mua hai hệ thống Aegis mặt đất (Aegis Ashore) từ Mỹ để bảo vệ toàn bộ lãnh thổ."
                , "02-01-2018 19:00:00", "Hệ thống Aegis Ashore có thể phóng tên lửa hành trình tầm xa, loại vũ khí vốn bị cấm theo hiệp ước hạt nhân Nga - Mỹ."
                , "https://firebasestorage.googleapis.com/v0/b/news-daeeb.appspot.com/o/aegis-ashore-5606-1514885156.jpg?alt=media&token=c376f24e-d9ab-44bd-92ad-3b64978676f7",
                "postId2", listTag1, "Lý do khiến Nga lo ngại lá chắn tên lửa mặt đất của Nhật", new ArrayList<String>(), new ArrayList<String>());

        Post post4 = new Post("Minh Thủy", "Xe", new HashMap<String, Comment>()
                , "Từ 1/1/2018, hàng loạt chính sách thuế, phí, kiểm định, luật giao thông có hiệu lực, ảnh hưởng đến giá xe cũng như hành vi của tài xế. Dưới đây là những quy định nổi bật về ôtô có hiệu lực từ 2018 tài xế Việt nên biết.\n" +
                "\n" +
                "Thuế nhập khẩu từ ASEAN về 0%\n" +
                "\n" +
                "Theo cam kết trong Hiệp định thương mại tự do ASEAN (ATIGA), những xe có tỷ lệ nội địa hóa nội khối từ 40% trở lên sẽ được hưởng mức thuế nhập khẩu 0%. Từ 2018, Việt Nam nhập ôtô từ Thái Lan, Indonesia, Malaysia... với thuế 0%, giảm sâu với mức thuế ở 2017 là 30%.\n" +
                "\n" +
                "Xe hơi muốn được giảm giá như vậy, phải đáp ứng đủ hai tiêu chí, sản xuất bởi một nước ASEAN và tỷ lệ nội địa hóa nội khối là 40%. Xe nhập từ ASEAN nhưng tỷ lệ nội địa thấp hơn 40% hoặc xe nhập từ một nước ngoài ASEAN như Hàn Quốc, Nhật, châu Âu... cũng không được hưởng mức ưu đãi này.\n" +
                "\n" +
                "Thuế nhập khẩu linh kiện về 0%\n" +
                "\n" +
                "Theo nghị định 125/2017, các hãng lắp ráp xe trong nước sẽ được hưởng mức thuế nhập khẩu linh kiện là 0% nếu đạt được sản lượng quy định, áp dụng cho xe con dưới 9 chỗ. Nghị định này áp dụng cho khoảng 30 bộ linh kiện chính, mã hải quan từ 98.49.11 đến 98.49.40.\n" +
                "\n" +
                "Trong giai đoạn một, tức nửa đầu 2018, các hãng sản xuất xe con (dưới 9 chỗ, động cơ 2.5 trở xuống) muốn được ưu đãi thuế nhập khẩu linh kiện 0% phải đảm bảo hai điều kiện sản lượng chung từ 8.000 xe trở lên và một mẫu xe cam kết phải từ 3.000 xe trở lên. \n" +
                "\n" +
                "Người ngồi ghế sau ôtô không thắt dây an toàn bị phạt tiền\n" +
                "\n" +
                "Nghị định 46/2016 về Quy định xử phạt vi phạm giao thông viết \"phạt tiền từ 100.000-200.000 đối với người ngồi trên xe ôtô không thắt dây an toàn tại vị trí có trang bị dây an toàn khi xe đang chạy\". \n" +
                "\n" +
                "Điểm mới của nghị định này so với nghị định 171/2013 trước đó là mọi vị trí đều phải thắt dây an toàn, trong khi quy định cũ chỉ bắt buộc điều này với tài xế và khách ngồi ghế trước. Quy định mới được đánh giá là chặt chẽ và cần thiết hơn so với trước đây, giúp hình thành thói quen thắt dây an toàn, ý thức vốn còn thiếu ở Việt Nam.\n" +
                "\n" +
                "Ôtô nhập khẩu khó về Việt Nam vì thiếu giấy tờ\n" +
                "\n" +
                "Nghị định 116/2017 quy định doanh nghiệp muốn nhập ôtô về nước phải có Giấy chứng nhận chất lượng kiểu loại do Tổ chức nước ngoài cấp. Hầu hết các doanh nghiệp liên doanh cho rằng nước ngoài không cấp loại giấy này cho xe nhập khẩu, chỉ cấp cho xe nội địa, vì vậy hãng sẽ không thể nhập xe. \n" +
                "\n" +
                "VAMA đã 4 lần gửi kiến nghị lên Thủ tướng để có những điều chỉnh giúp các hãng dễ dàng nhập khẩu ôtô nhưng đến thời điểm này vẫn chưa có gì thay đổi. Nhiều hãng như Toyota, Honda, Ford cho biết xe chỉ có thể bán đến Tết âm, sau đó chưa biết có xe hay không.\n" +
                "\n" +
                "Tăng thuế nhập khẩu ôtô cũ\n" +
                "\n" +
                "Theo nghị định 125/2017 của Chính phủ ban hành ngày 16/11/2017, mức thuế tuyệt đối áp dụng cho xe có dung tích động cơ không quá một lít và thuế hỗn hợp cho xe trên một lít. \n" +
                "\n" +
                "Ở cách tính cũ, xe được chia thành 4 loại theo dung tích động cơ với các mức thuế khác nhau. Trong cách tính mới, xe chỉ chia làm hai loại là động cơ từ một lít trở xuống và trên một lít. Xe từ một lít trở xuống thì chịu thuế tuyệt đối 10.000 USD, trong khi xe trên một lít áp dụng luôn mức thuế tổng hợp. \n" +
                "\n" +
                "Ở phân khúc xe dưới một lít, chủ yếu các mẫu xe đô thị cỡ nhỏ, giá ôtô cũ nhập lướt tăng hơn gấp đôi so với hiện tại. Với các xe lắp động cơ lớn hơn, mức thuế nhập khẩu có thể độn thêm hàng chục đến hàng trăm nghìn USD, tùy thuộc vào dung tích động cơ và giá trị của xe.\n" +
                "\n" +
                "Ôtô nhỏ phải dán nhãn năng lượng\n" +
                "\n" +
                "Từ ngày 1/1/2018, ôtô từ trên 7 chỗ đến 9 chỗ được sản xuất, lắp ráp từ linh kiện rời, hoàn toàn mới; xe nhập khẩu chưa qua sử dụng phải được dán nhãn năng lượng trước khi đưa ra thị trường.\n" +
                "\n" +
                "Những chính sách mới về ôtô chưa từng có tại Việt Nam - 4\n" +
                "Thông tư này không bắt buộc áp dụng với các trường hợp sau: xe được sản xuất, lắp ráp, nhập khẩu sử dụng trực tiếp vào mục đích quốc phòng, an ninh của Bộ Quốc phòng, Bộ Công an; xe tạm nhập tái xuất; xe quá cảnh, chuyển khẩu; xe của ngoại giao, lãnh sự; xe nhập khẩu đơn chiếc và không vì mục đích kinh doanh xe; xe nhập khẩu theo quy định riêng của Thủ tướng; xe sử dụng nhiên liệu không phải là xăng, điêzen, khí dầu mỏ hóa lỏng (LPG), khí tự nhiên (NG).\n" +
                "\n" +
                "Xe dưới chuẩn khí thải Euro 4 không được đăng kiểm\n" +
                "\n" +
                "Theo Công văn 436/2017 của Thủ tướng chính phủ về việc quy định lộ trình áp dụng tiêu chuẩn khí thải đối với ôtô, môtô lắp ráp và nhập khẩu mới, từ ngày 1/1/2018, cơ quan chức năng sẽ không làm thủ tục đăng kiểm cho xe không đáp ứng tiêu chuẩn khí thải Euro 4.\n" +
                "\n" +
                "Những chính sách mới về ôtô chưa từng có tại Việt Nam - 5\n" +
                "Ôtô đã được Bộ Giao thông vận tải chứng nhận thoả mãn quy định về khí thải mới được thực hiện các thủ tục có liên quan. Trước đó, các doanh nghiệp phải có kế hoạch nhập khẩu, sản xuất ôtô, bảo đảm việc hoàn thành các thủ tục hải quan, đăng kiểm và đưa ra thị trường trước ngày 31/12/2017. Sau thời điểm này, nếu không hoàn thành phải tái xuất hoặc xuất khẩu. \n" +
                "\n" +
                "Ôtô hết niên hạn bị thu hồi\n" +
                "\n" +
                "Theo Quyết định 16/2015 của Thủ tướng về thu hồi, xử lý sản phẩm thải bỏ, các loại ôtô, xe máy hết hạn sử dụng sẽ bị thu hồi vào tháng 1/2018. Theo đó, nhà sản xuất có trách nhiệm tổ chức thu hồi, tiếp nhận sản phẩm thải bỏ do doanh nghiệp đã bán ra thị trường Việt Nam và được hưởng các chính sách ưu đãi, hỗ trợ. Các nhà sản xuất có thể liên kết để cùng thực hiện thu hồi, xử lý sản phẩm thải bỏ.\n" +
                "\n" +
                "Người tiêu dùng được lựa chọn các hình thức như tự chuyển hoặc chuyển giao cho tổ chức, cá nhân thu gom để vận chuyển đến điểm thu hồi và được hưởng quyền lợi theo chính sách của nhà sản xuất..."
                , "02-01-2018 10:15:00", "Các mức thuế suất mới được áp dụng, xe đáp ứng tiêu chuẩn khí thải Euro 4 mới được đăng kiểm, và người ngồi ôtô phải thắt dây an toàn."
                , "",
                "postId2", listTag1, "Những chính sách mới về ôtô chưa từng có tại Việt Nam", new ArrayList<String>(), new ArrayList<String>());
        Post post5 = new Post("Hội An", "Sức khỏe", new HashMap<String, Comment>()
                , ""
                , "02-01-2018 14:15:00", "Các bài tập với tạ tay giúp săn chắc cơ bắp, điều chỉnh tư thế đi đứng cho nam giới, tránh gù lưng, theo Men's Health."
                , "https://firebasestorage.googleapis.com/v0/b/news-daeeb.appspot.com/o/6-bai-tap-co-tay-voi-ta-hieu-qua-nhat-1.jpg?alt=media&token=58aedffc-b030-46ee-a52d-9e3e5dfc81be",
                "postId2", listTag1, "10 phút tập cho dáng đi đàn ông mạnh mẽ", new ArrayList<String>(), new ArrayList<String>());
        Post post6 = new Post("Khánh Hoàng", "Thời sự", new HashMap<String, Comment>()
                , "Mới đầu năm mới thôi mà\n" +
                "Lại nghe tin bão xông nhà dạo chơi\n" +
                "Bão ơi bão đừng đến nơi\n" +
                "Để cho em phải ra khơi cày bừa\n" +
                "Năm rồi.. hẻo lắm chẳng thừa\n" +
                "Ở nhà trốn bão từa lưa doạ hoài\n" +
                "Mất việc thất nghiệp dài dài\n" +
                "Tiền thì rỗng túi,than hoài Gấu yêu\n" +
                "Cơm nguội ngán lắm treo niêu\n" +
                "Nhà vẹo cột méo liêu xiêu Mẹ già\n" +
                "Bão ơi đừng.. quậy nữa mà\n" +
                "Chỉ còn dăm bữa đến là Tết ta\n" +
                "Sắp nhỏ thiếu áo khóc la\n" +
                "Bàn thờ hiu quạnh,thiếu hoa tắt đèn\n" +
                "Xế nổ..lại điếc quèn quèn\n" +
                "Còn..em yêu dấu..lèng phèng thiếu son\n" +
                "Mới vừa..xin việc chỗ..ngon\n" +
                "Phụ hồ trộn vữa..để gom ít hào\n" +
                "Quyết tâm phải..mần ào ào\n" +
                "Mà sao..ông bão lại..bào lòng tui\n" +
                "Doạ gì doạ miết không thôi\n" +
                "Đúng là..rảnh thiệt,lại..chơi nhau hoài\n" +
                "Lạy ông!Ông hãy ở ngoài\n" +
                "Trong đây nghèo lắm chạy dài đừng vô\n" +
                "Bây giờ tui phải chạy sô\n" +
                "\"Phụ hồ lão tướng\"..vẫn đô như thường\n" +
                "Bão nghe thổn thức.. thì thương\n" +
                "Hướng Đông quay lại.. khẩn trương tan nào!\n" +
                "Bão đừng thắc mắc vì sao\n" +
                "Tớ đây.. không rảnh..thôi chào.. về đi!"
                , "02-01-2018 13:29:00", "Áp thấp nhiệt đới được dự báo mạnh lên thành bão hướng vào Nam Bộ, với sức gió tối đa 60 km/h (cấp 7), giật cấp 9."
                , "https://firebasestorage.googleapis.com/v0/b/news-daeeb.appspot.com/o/bao00002901Still001-1514870173-7565-1514870338_180x108.png?alt=media&token=cb951502-08ce-4729-8308-8ab2d3016d13",
                "postId2", listTag1, "Áp thấp nhiệt đới sắp thành bão đang vào Biển Đông", new ArrayList<String>(), new ArrayList<String>());
        Post post7 = new Post("Trần Quang", "Thời sự", new HashMap<String, Comment>()
                , ""
                , "01-01-2018 20:05:00", "Sau kỳ nghỉ Tết dương lịch, chiều 1/1, người dân trở lại thành phố cùng lúc khiến giao thông cửa ngõ thủ đô ùn tắc hàng km."
                , "https://firebasestorage.googleapis.com/v0/b/news-daeeb.appspot.com/o/tacduong-1514812201_180x108.jpg?alt=media&token=1cda8f1d-dca9-44eb-ae30-e75a90c51f4a",
                "postId2", listTag1, "Ùn tắc 4 tiếng ở cửa ngõ Hà Nội", new ArrayList<String>(), new ArrayList<String>());
        Post post8 = new Post("Hồng Hạnh", "Thế giới", new HashMap<String, Comment>()
                , "8 bài Waka (Hòa ca), thể thơ cổ có 31 âm tiết, được Cơ quan Nội chính Hoàng gia Nhật Bản công bố hôm nay, trong đó 5 bài do Nhật hoàng Akihito sáng tác và ba bài do Hoàng hậu Michiko sáng tác, theo Asahi.\n" +
                "\n" +
                "Những tác phẩm này được Nhà vua và Hoàng hậu sáng tác năm ngoái. Trong bài thơ viết sau chuyến thăm Việt Nam hồi tháng 3/2017, Nhật hoàng đã bày tỏ sự ngưỡng mộ với sức mạnh của nhân dân Việt Nam trong cuộc khắc phục hậu quả chiến tranh, cũng như công cuộc phát triển kinh tế của đất nước.\n" +
                "\n" +
                "Sau đây là lược dịch bài thơ \"Thăm Việt Nam\":\n" +
                "\n" +
                "\"Họ đã sống thế nào\n" +
                "\n" +
                "Qua ngần ấy năm chiến đấu và chiến tranh\n" +
                "\n" +
                "Tôi khâm phục những người dân ấy\n" +
                "\n" +
                "Khi tới thăm đất nước này\n" +
                "\n" +
                "Đất nước Việt Nam\".\n" +
                "\n" +
                "Trong những bài thơ khác, Nhà vua Akihito sáng tác thơ về cây bưởi, lễ khai mạc Đại hội Thể thao Quốc gia thứ 72, Công ước về Phát triển Sản xuất Biển Dài hạn, tình bạn với cố vương Thái Lan Bhumibol Adulyadej."
                , "02-01-2018 16:05:00", "Nhà vua Akihito bày tỏ sự ngưỡng mộ với sức mạnh của người dân Việt Nam trong khắc phục hậu quả chiến tranh."
                , "https://firebasestorage.googleapis.com/v0/b/news-daeeb.appspot.com/o/26238798-10156052594247070-373-3467-7521-1514884622.jpg?alt=media&token=8c8dac84-a1d2-46ef-ae9a-0b6bce4b7881",
                "postId2", listTag1, "Nhật hoàng làm thơ về chuyến thăm Việt Nam", new ArrayList<String>(), new ArrayList<String>());
        Post post9 = new Post("Phương Vũ", "Thế giới", new HashMap<String, Comment>()
                , "Ngày 10/10, Lan Anh, 19 tuổi, bắt đầu cuộc sống sinh viên tại Đại học Khoa học Ứng dụng Trung - Đức ở Thiên Tân, Trung Quốc. Điều đặc biệt là trước khi cô gái quê ở Thường Tín, Hà Nội này theo học, ngôi trường không có sinh viên Việt Nam.\n" +
                "\n" +
                "\"Ban đầu em không chọn học ở đây nhưng đã đổi ý khi biết chưa có người Việt nào theo học. Em muốn nâng cao khả năng ngôn ngữ nhanh hơn, nếu không có người Việt thì em sẽ không giao tiếp bằng tiếng Việt nhiều\", Lan Anh cho biết trong cuộc phỏng vấn với VnExpress. \n" +
                "\n" +
                "\"Khi là người Việt đầu tiên đến học, em sẽ được đại diện cho đất nước và sẽ có nhiều cơ hội phát triển bản thân\", cô nói thêm.\n" +
                "\n" +
                "Sự ngưỡng mộ với công ty Alibaba của tỷ phú Jack Ma đã thổi bùng đam mê của Lan Anh với ngành Logistics (Hậu cần). Tuy nhiên, Đại học Thương mại ở Hà Nội - ngôi trường cô theo học, không có ngành này. Lan Anh quyết định đi du học để theo đuổi đam mê và học thêm ngoại ngữ mới ngoài tiếng Anh.\n" +
                "\n" +
                "Tìm hiểu về Đại học Khoa học Ứng dụng Trung - Đức thông qua một trung tâm du học, cô đã xin được học bổng miễn phí tiền học và chi phí ở ký túc xá. Người ứng tuyển phải tốt nghiệp trung học phổ thông với điểm 6,5 trở lên và phải gửi kèm theo bảng điểm thi đại học.\n" +
                "\n" +
                "Lan Anh gặp nhiều khó khăn trong hai tuần đầu tiên ở Trung Quốc. Cô tự làm mọi thứ, từ làm sim điện thoại cho đến tìm hiểu cách đi tàu điện ngầm, cách mua hàng trên trang mạng hay đi mua thiết bị phát wifi và tự cài đặt. Cô còn đánh rơi điện thoại ở cửa hàng tiện lợi của trường nhưng may mắn được trả lại.\n" +
                "\n" +
                "Cô chưa học tiếng Trung trước khi sang Trung Quốc, vì vậy, ngôn ngữ là rào cản rất lớn. \"Khi đi mua cục phát wifi, em nói bằng tiếng Anh nhưng không ai hiểu, em phải tìm hình ảnh để cho họ biết em muốn mua gì\", cô cho biết. Nhà trường sau đó cử một sinh viên Trung Quốc để hỗ trợ nữ sinh Việt. \n" +
                "\n" +
                "Lan Anh chỉ học tiếng Trung trong năm đầu tiên và sang năm thứ hai mới học môn chuyên ngành. Tuy nhiên, cô đã được học thử một số môn như xác suất thống kê và nghe giải thích những khái niệm cơ bản của ngành.\n" +
                "\n" +
                "\"Thầy cô vừa giảng bằng tiếng Anh vừa giảng bằng tiếng Trung và cố gắng nói một cách dễ hiểu nhất. Dù vậy, vấn đề ngôn ngữ vẫn là trở ngại lớn\", cô nói. \"Do xem phim nhiều nên em có thể nghe nói tiếng Trung với trình độ tương đương các bạn học HSK3 (HSK là kỳ thi khảo sát trình độ tiếng Trung gồm 6 cấp độ) còn đọc viết thì em đang học từ từ\"."
                , "02-01-2018 12:03:00", "Khi mới đến Trung Quốc, Lan Anh tự tìm hiểu mọi thứ vì không có đồng hương người Việt đi trước dìu dắt."
                , "https://firebasestorage.googleapis.com/v0/b/news-daeeb.appspot.com/o/du-hoc-sinh-4530-1513264677-1-3220-1514172653.jpg?alt=media&token=9eb699ab-3f41-4878-95fd-ceb9bad6a47c",
                "postId2", listTag1, "Nữ sinh Việt đầu tiên tại đại học khoa học ứng dụng ở Trung Quốc", new ArrayList<String>(), new ArrayList<String>());
        Post post10 = new Post("Trọng Giáp", "Thế giới", new HashMap<String, Comment>()
                , "Cuộc biểu tình dân chủ của hàng nghìn người hôm qua kết thúc bằng những cuộc xô xát và đối đầu với cảnh sát ở quảng trường bên ngoài trụ sở chính quyền Hong Kong, theo SCMP. \n" +
                "\n" +
                "Mặt trận Nhân quyền Dân sự, nhóm tổ chức biểu tình, cho biết 10.000 người xuống đường để bày tỏ quan ngại về điều họ cho là chiến dịch của Bắc Kinh nhằm giảm quyền tự trị của Hong Kong. Cảnh sát thông báo số người biểu tình là 6.200. Người biểu tình hô \"Bảo vệ Hong Kong\" khi dừng trước Đại lộ Tim Mei, bên ngoài quảng trường. \n" +
                "\n" +
                "Chủ đề chính của biểu tình là phản đối việc cơ quan lập pháp Trung Quốc tuần trước chính thức thông qua kế hoạch gây tranh cãi về việc thiết lập trạm kiểm tra chung tại ga West Kowloon của tuyến đường sắt cao tốc sẽ nối Hong Kong với các thành phố Thâm Quyến và Quảng Châu. Những người chỉ trích thỏa thuận lo sợ nó sẽ làm xói mòn quyền tự trị của đặc khu khi cho phép giới chức từ bên kia biên giới lần đầu tiên áp dụng luật của Trung Quốc đại lục trên đất Hong Kong. \n" +
                "\n" +
                "Ga nằm trên bến cảng nổi tiếng của Hong Kong ở Kowloon, không nằm trên biên giới với Trung Quốc đại lục ở phía bắc. \n" +
                "\n" +
                "Cuộc biểu tình đầu năm mới là sự kiện thường niên ở Hong Kong. Số người tham gia năm nay thấp hơn so với hai năm trước. Cảnh sát ước tính số người tham gia biểu tình năm 2017 và 2016 lần lượt là 4.800 và 1.600, trong khi Mặt trận Nhân quyền Dân sự, nhóm tổ chức biểu tình ước tính con số lần lượt là 9.100 và 4.000. \n" +
                "\n"
                , "02-01-2018 11:00:00", "Hàng nghìn người Hong Kong giận dữ phản đối điều họ cho là sự can thiệp của Bắc Kinh trong ngày đầu năm mới. "
                , "Hàng nghìn người Hong Kong giận dữ phản đối điều họ cho là sự can thiệp của Bắc Kinh trong ngày đầu năm mới. ",
                "postId2", listTag1, "Hàng nghìn người Hong Kong biểu tình đầu năm mới", new ArrayList<String>(), new ArrayList<String>());
        listPost.add(post1);
        listPost.add(post2);
        listPost.add(post3);
        listPost.add(post4);
        listPost.add(post5);
        listPost.add(post6);
        listPost.add(post7);
        listPost.add(post8);
        listPost.add(post9);
        listPost.add(post10);
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

