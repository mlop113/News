package com.android.Global;

import com.android.Models.Post;
import com.android.Models.Tag;
import com.android.Models.UserMember;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ngoc Vu on 12/18/2017.
 */

public class GlobalStaticData {
    static int currentPage=0;
    public static UserMember currentUser  = new UserMember("asdasd", "asdasdsad", "asdasdsad", "asdasdsad", "asdasdsad", "asdasdsad", "asdasdsad", "asdasdsad", "asdasdsad", "-KyUeplvVdI5vI4ghU01");;
    public static List<Post> listPostOnReQuest = new ArrayList<>();
    public static List<Post> listPostOnReQuest_Tag = new ArrayList<>();
    public static List<Post> listPostHome = new ArrayList<>();
    public static Map<String,List<Post>> listPostOfCategory = new HashMap<>();
    public static List<UserMember> members = new ArrayList<>();
    public static List<String> listCategoryHome = new ArrayList<>();
    public static List<String> listBookmark = new ArrayList<>();
    public static List<String> listTag = new ArrayList<>();




    static List<Tag> tags = new ArrayList<>();

    public static int getCurrentPage() {
        return currentPage;
    }

    public static void setCurrentPage(int currentPage) {
        GlobalStaticData.currentPage = currentPage;
    }

    public static UserMember getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(UserMember currentUser) {
        GlobalStaticData.currentUser = currentUser;
    }


}
