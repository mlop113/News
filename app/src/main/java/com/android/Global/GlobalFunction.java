package com.android.Global;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.android.Activity_Fragment.BookMarkActivity;
import com.android.Activity_Fragment.PostsOnRequestActivity;
import com.android.Models.Post;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import dmax.dialog.SpotsDialog;

/**
 * Created by Ngoc Vu on 12/18/2017.
 */

public class GlobalFunction {
    static DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    public static String calculateTimeAgo(String date)
    {

        Date today=new Date(System.currentTimeMillis());
        DateFormat timeFormat= SimpleDateFormat.getDateTimeInstance();
        Date datepost=new Date();
        Calendar calendar =Calendar.getInstance();
        //Log.d("currentdate",String.valueOf(timeFormat.format(today)));
        try {
            datepost = timeFormat.parse(date);
            //Log.d("datepost",String.valueOf(timeFormat.format(datepost)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long distance = (today.getTime()-datepost.getTime());
        long minute = TimeUnit.MINUTES.convert(distance, TimeUnit.MILLISECONDS);

        if(minute>=60)
        {
            long hour = TimeUnit.HOURS.convert(distance, TimeUnit.MILLISECONDS);
            if(hour>=24)
            {
                long day = TimeUnit.DAYS.convert(distance, TimeUnit.MILLISECONDS);
                if(day>=7)
                {
                    if(day>=30)
                    {
                        return String.valueOf(day / 30) + " tháng trước";
                    }
                    return String.valueOf(day / 7) + " tuần " + String.valueOf(day % 7) + " ngày trước";
                }
                return String.valueOf(day) +" ngày trước";
            }
            return String.valueOf(hour) +" giờ trước";

        }
        else if(minute<=1)
            return  "vừa xong";
        return  String.valueOf(minute) +" phút trước";
    }

    public static List<Post> getFilterListPostByDate(List<Post> listPost, String date){
        List<Post> listPostResult = new ArrayList<>();
        DateFormat timeFormat= SimpleDateFormat.getDateTimeInstance();
        Date dateFilter=new Date();
        Date datePost = new Date();
        try {
            dateFilter = timeFormat.parse(date);
            Log.d("dateFilter",dateFilter.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        for (Post p:listPost) {
            try {
                datePost = timeFormat.parse(p.getDateCreate());
                Log.d("datePost",datePost.toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long distance = (datePost.getTime()-dateFilter.getTime());
            Log.d("distance",String.valueOf(distance));
            if(distance>=0)
                listPostResult.add(p);
        }
        return  listPostResult;
    }

    public static boolean filter( Post p, String date){
        DateFormat timeFormat= SimpleDateFormat.getDateTimeInstance();
        Date dateFilter=new Date();
        Date datePost = new Date();
        try {
            dateFilter = timeFormat.parse(date);
            Log.d("dateFilter",dateFilter.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            datePost = timeFormat.parse(p.getDateCreate());
            Log.d("datePost",datePost.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long distance = (datePost.getTime()-dateFilter.getTime());
        Log.d("distance",String.valueOf(distance));
        if(distance>=0)
            return  true;

        return  false;
    }

    //function get list post by all
    /*public static List<Post> getListPost() {
        final List<Post> list = new ArrayList<>();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child(AppConfig.FIREBASE_FIELD_POSTS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot child:dataSnapshot.getChildren()){
                    Post post =child.getValue(Post.class);
                    list.add(post);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return list;
    }

    public static List<Tag> getListTag(){
        final List<Tag> list = new ArrayList<>();
        DatabaseReference  databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child(AppConfig.FIREBASE_FIELD_TAGS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot child:dataSnapshot.getChildren()){
                    Tag tag =child.getValue(Tag.class);
                    list.add(tag);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return list;
    }

    public static List<Post> getListPostByTagName(String tagName){
        List<Post> listPost = new ArrayList<>();
        for (Post p:GlobalStaticData.getListPost()) {
            if(p.getTags().contains(tagName))
                listPost.add(p);
        }
        return listPost;
    }

    public static List<Category> getlistCategory(){
        final List<Category> list = new ArrayList<>();
        DatabaseReference  databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child(AppConfig.FIREBASE_FIELD_CATEGORIES).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot child:dataSnapshot.getChildren()){
                    Category category =child.getValue(Category.class);
                    list.add(category);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return list;
    }

    public static UserMember LoginByPhoneNumber(String phonenumber, String password){
        UserMember user=null;
        return  user;
    }

    public static List<Post> getListPostByCategoryId(String categoryId){
        List<Post> listPost = new ArrayList<>();

        for (Post p:GlobalStaticData.getListPost()) {
                if(p.getCategoryId().equals(categoryId))
                    listPost.add(p);
        }
        return  listPost;
    }

    public static Category getCategoryById(String categoryId)
    {

        return null;
    }



    public static ArrayList<PostModel> ListBookmark = new ArrayList<>();

    public static ArrayList<PostModel> getListBookmark() {
        return ListBookmark;
    }

    public static void setListBookmark(ArrayList<PostModel> listBookmark) {
        ListBookmark = listBookmark;
    }

    public static ArrayList<PostModel> getListBookmarkByUserId(String userId){
        ArrayList<PostModel> listPost = new ArrayList<>();
        return listPost;
    }*/

    public static void onClickViewBookMark(final Activity activity, final SpotsDialog progressDialog){
        progressDialog.show();
        GlobalStaticData.listPostOnReQuest= new ArrayList<>();
        databaseReference.child(AppConfig.FIREBASE_FIELD_BOOKMARKS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(GlobalStaticData.currentUser.getUserId()))
                {
                    databaseReference.child(AppConfig.FIREBASE_FIELD_BOOKMARKS).child(GlobalStaticData.currentUser.getUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(final DataSnapshot dataSnapshot) {
                            int i = 0;
                            final int count = (int) dataSnapshot.getChildrenCount();
                            for(DataSnapshot dataBookmark:dataSnapshot.getChildren()){
                                i++;

                                GlobalStaticData.listBookmark.add(dataBookmark.getValue().toString());

                                final int finalI = i;
                                databaseReference.child(AppConfig.FIREBASE_FIELD_POSTS).child(dataBookmark.getValue().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot2) {
                                        GlobalStaticData.listPostOnReQuest.add(dataSnapshot2.getValue(Post.class));
                                        Log.d("bookmarks",String.valueOf(dataSnapshot2.getKey()));
                                        if(finalI ==count) {
                                            Intent intent = new Intent(activity,BookMarkActivity.class);
                                            intent.putExtra(AppConfig.BARNAME, AppConfig.FIREBASE_FIELD_BOOKMARKS);
                                            intent.putExtra(AppConfig.LISTPOST, (ArrayList) GlobalStaticData.listPostOnReQuest);
                                            intent.putExtra(AppConfig.ACTION,AppConfig.FIREBASE_FIELD_BOOKMARKS);
                                            activity.startActivity(intent);
                                            progressDialog.dismiss();
                                        }
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
                }
                else
                {
                    Intent intent = new Intent(activity,PostsOnRequestActivity.class);
                    intent.putExtra(AppConfig.BARNAME, AppConfig.FIREBASE_FIELD_BOOKMARKS);
                    GlobalStaticData.listPostOnReQuest = new ArrayList<Post>();
                    intent.putExtra(AppConfig.LISTPOST, (ArrayList) GlobalStaticData.listPostOnReQuest);
                    intent.putExtra(AppConfig.ACTION,AppConfig.FIREBASE_FIELD_BOOKMARKS);
                    activity.startActivity(intent);
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
