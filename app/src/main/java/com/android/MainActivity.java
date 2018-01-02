package com.android;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.aigestudio.wheelpicker.widgets.WheelDayPicker;
import com.aigestudio.wheelpicker.widgets.WheelMonthPicker;
import com.aigestudio.wheelpicker.widgets.WheelYearPicker;
import com.android.Activity_Fragment.Hot_Fragment;
import com.android.Activity_Fragment.New_Fragment;
import com.android.Activity_Fragment.PostsOnRequestActivity;
import com.android.Activity_Fragment.Profile_Activity;
import com.android.Adapters.CategoryAdapter;
import com.android.Adapters.MyFragmentPagerAdapter;
import com.android.Global.AppConfig;
import com.android.Global.AppPreferences;
import com.android.Global.GlobalFunction;
import com.android.Global.GlobalStaticData;
import com.android.Interface.IOnClickCategory;
import com.android.Interface.IOnClickFilter;
import com.android.Models.Post;
import com.android.Models.UserMember;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.droidparts.widget.ClearableEditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class MainActivity extends AppCompatActivity implements IOnClickCategory, ViewPager.OnPageChangeListener,TabHost.OnTabChangeListener,View.OnClickListener{
    AppPreferences appPreferences;
    InputMethodManager inputMethodManager;
    //actionbar
    ImageButton imageButtonPlus;
    DrawerLayout drawer;
    //dialogFilter
    Dialog dialogFilter;

    //drawer and listCategory
    private RecyclerView recyclerViewCategory;
    RelativeLayout relativeLayoutUser;
    ImageView imageViewUserAction;
    List<String> listCategory = new ArrayList<>();
    CategoryAdapter categoryAdapter;
    ClearableEditText editTextSearch;

    //main
    Fragment hot_fragment = new Hot_Fragment();
    Fragment new_fragment= new New_Fragment();
    List<Fragment> fragmentList;

    MyFragmentPagerAdapter myFragmentPagerAdapter;
    //tag hot & view pager
    TabHost tabHost;

    ViewPager viewPager;
    //WheelDPicker
    WheelDayPicker wheelDayPicker;
    WheelMonthPicker wheelMonthPicker;
    WheelYearPicker wheelYearPicker;

    //interface click filter
    public static IOnClickFilter iOnClickFilterHot;
    public static IOnClickFilter iOnClickFilterNew;
    public static IOnClickFilter iOnClickClearFilterHot;
    public static IOnClickFilter iOnClickClearFilterNew;
    boolean isFilterHot=false;
    boolean isFilterNew=false;
    private String  p,u,i;
    DatabaseReference databaseReference;

    //intent
    Intent intentPostsOnReQuest;

    //dialog wait
    SpotsDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appPreferences  = AppPreferences.getInstance(this);
        //connect firebase
        //fireBase();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        setContentView(R.layout.activity_main);
        inputMethodManager = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        //set tollbar and displaytitile
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //drawer contain layoutuser and listCategory
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                inputMethodManager.hideSoftInputFromWindow(editTextSearch.getWindowToken(),0);
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        //tạo viewpager
        initViewPager();
        //tạo tabhot
        inittabhot();
        //show menu "+"
        initMenuplus();
        //dialog filter
        initdialogFilter();

        initIntent();
        mappings();
        initView();
        event();

    }

    private void initIntent(){
        intentPostsOnReQuest = new Intent(this,PostsOnRequestActivity.class);
    }

    private void mappings() {
        //header contean Layoutuser
        relativeLayoutUser = (RelativeLayout) findViewById(R.id.relativeLayoutUser);
        imageViewUserAction = findViewById(R.id.imageViewUserAction);
        editTextSearch = (ClearableEditText) findViewById(R.id.editTextSearch);
        //listCategory
        recyclerViewCategory= (RecyclerView) findViewById(R.id.recyclerViewCategory);
    }

    private void initView() {

        listCategory = GlobalStaticData.listCategoryHome;
        categoryAdapter = new CategoryAdapter(this, listCategory);
        categoryAdapter.setiOnClickCategory(this);
        recyclerViewCategory.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCategory.setAdapter(categoryAdapter);
        progressDialog = new SpotsDialog(this,R.style.CustomAlertDialog);

        databaseReference.child(AppConfig.FIREBASE_FIELD_CATEGORIES).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listCategory = new ArrayList<String>();
                for (DataSnapshot dataCategory : dataSnapshot.getChildren()) {
                    final String category = (String) dataCategory.getValue();
                    listCategory.add(category);
                    categoryAdapter.setData(listCategory);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void event() {
        relativeLayoutUser.setOnClickListener(this);
        imageViewUserAction.setOnClickListener(this);
        editTextSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                searchPost(editTextSearch.getText().toString());
                return true;
            }
        });

    }
    private void loadCurrentUser(){
        if(appPreferences.isLogin()) {
            AppPreferences appPreferences = AppPreferences.getInstance(this);
            if (appPreferences.isLogin()) {
                if (appPreferences.isLoginWithGoogle()) {

                } else {

                }

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                databaseReference.child(AppConfig.FIREBASE_FIELD_USERMEMBERS).child(appPreferences.getUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        UserMember currentUser = dataSnapshot.getValue(UserMember.class);
                        GlobalStaticData.setCurrentUser(currentUser);
                        ImageView imageViewNotLogin = (ImageView) findViewById(R.id.imageViewNotLogin);
                        ImageView selectableRIVAvatar =  findViewById(R.id.selectableRIVAvatar);
                        Glide.with(MainActivity.this).load(currentUser.getImg()).into(selectableRIVAvatar);
                        TextView textViewUsername = (TextView) findViewById(R.id.textViewUsername);
                        imageViewNotLogin.setVisibility(View.GONE);
                        selectableRIVAvatar.setVisibility(View.VISIBLE);
                        textViewUsername.setText(currentUser.getName());
                        if(android.os.Build.VERSION.SDK_INT >= 21){
                            imageViewUserAction.setImageDrawable(getResources().getDrawable(R.drawable.ic_logout, getTheme()));
                        } else {
                            imageViewUserAction.setImageDrawable(getResources().getDrawable(R.drawable.ic_logout));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        }
    }

    private void logout(){
        appPreferences.setUserId("");
        Toast.makeText(MainActivity.this, "Đã đăng xuất tài khoản!", Toast.LENGTH_SHORT).show();
        appPreferences.setLogin(false);
        ImageView imageViewNotLogin = (ImageView) findViewById(R.id.imageViewNotLogin);
        ImageView selectableRIVAvatar = findViewById(R.id.selectableRIVAvatar);
        TextView textViewUsername = (TextView) findViewById(R.id.textViewUsername);
        imageViewNotLogin.setVisibility(View.VISIBLE);
        selectableRIVAvatar.setVisibility(View.GONE);
        textViewUsername.setText(getString(R.string.login));
        if(android.os.Build.VERSION.SDK_INT >= 21){
            imageViewUserAction.setImageDrawable(getResources().getDrawable(R.drawable.ic_expand_right, getTheme()));
        } else {
            imageViewUserAction.setImageDrawable(getResources().getDrawable(R.drawable.ic_expand_right));
        }

    }
    private void searchPost(final String strSearch){
        final List<Post> listPostSearch = new ArrayList<>();
        databaseReference.child(AppConfig.FIREBASE_FIELD_POSTS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dataPost : dataSnapshot.getChildren())
                {
                    if(dataPost.getValue(Post.class).getTitle().contains(strSearch))
                        listPostSearch.add(dataPost.getValue(Post.class));
                }
                Intent intentSearch = new Intent(MainActivity.this,PostsOnRequestActivity.class);
                intentSearch.putExtra(AppConfig.LISTPOST,(ArrayList)listPostSearch);
                intentSearch.putExtra(AppConfig.BARNAME,strSearch);
                startActivity(intentSearch);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        /*ArrayList<Post> listPost = new ArrayList<>();
        for(PostModel p:GlobalFunction.getListPost()){
            if(standandString(p.getTitile().trim()).contains(standandString(strSearch)))
            {
                listPost.add(p);
            }
        }
        Intent intentSearch = new Intent(this,PostsOnRequestActivity.class);
        intentSearch.putExtra(AppConfig.LISTPOST,listPost);
        intentSearch.putExtra(AppConfig.BARNAME,strSearch);
        startActivity(intentSearch);*/
    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    //tạo viewpager
    private void initViewPager()
    {
        ///tạo list pager
        fragmentList = new ArrayList<>();
        fragmentList.add(hot_fragment);
        fragmentList.add(new_fragment);
        myFragmentPagerAdapter = new MyFragmentPagerAdapter(
                getSupportFragmentManager(),fragmentList);

        viewPager = (ViewPager) findViewById(R.id.home_view_pager);

        //đưa list payger vào viewpayger
        viewPager.setAdapter(myFragmentPagerAdapter);
        //băt sự kiện trượt viewpager
        viewPager.addOnPageChangeListener(this);

    }
    //tạo tabhost
    private void inittabhot()
    {
        tabHost = (TabHost)findViewById(R.id.tabhost);
        tabHost.setup();
        //tạo số lượng và tên tabhot
        String[] tabnames = {"",""};
        for(String t:tabnames){
            TabHost.TabSpec tabSpec;
            tabSpec = tabHost.newTabSpec(t);
            tabSpec.setContent(new FakeContent(this));
            TextView tv = new TextView(this);
            tv.setWidth(200);
            tv.setHeight(200);
            tv.setTextSize(11);
            tabSpec.setIndicator(tv);
            tabHost.addTab(tabSpec);
        }
        //bắt sự kiện click thay đổi tab
        tabHost.setOnTabChangedListener(this);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data==null) {
            return;
        }
        if(requestCode==AppConfig.REQUEST_CODE_LOGIN_FROM_LinearLayoutLogin && resultCode==AppConfig.RESULT_CODE_LOGIN)
        {
            UserMember user = (UserMember) data.getSerializableExtra(AppConfig.USER);
            Toast.makeText(this, "Login Success!", Toast.LENGTH_SHORT).show();
            if(Hot_Fragment.summary_adapter!=null){
                Hot_Fragment.summary_adapter.notifyDataSetChanged();
            }
            if(New_Fragment.postsOnRequestAdapter!=null){
                New_Fragment.postsOnRequestAdapter.notifyDataSetChanged();
            }

        }

        if(resultCode==AppConfig.RESULT_CODE_LOGOUT)
        {
            logout();
            if(Hot_Fragment.summary_adapter!=null){
                Hot_Fragment.summary_adapter.notifyDataSetChanged();
            }
            if(New_Fragment.postsOnRequestAdapter!=null){
                New_Fragment.postsOnRequestAdapter.notifyDataSetChanged();
            }
        }
    }


    //funtion of Interface implement click item
    @Override
    public void onClickCategory(final String category) {
        //close drawer contain listcategory
        //DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //drawer.closeDrawer(GravityCompat.START);
        //Toast.makeText(this, "clicked category id="+category.getName(), Toast.LENGTH_SHORT).show();

        //PostsOnRequestActivity.listPost.clear();
        //GlobalStaticData.listPost.clear();
        progressDialog.show();
        GlobalStaticData.listPostOnReQuest.clear();

        databaseReference.child(AppConfig.FIREBASE_FIELD_POSTS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dataPost:dataSnapshot.getChildren()) {
                    Post post = dataPost.getValue(Post.class);
                    if (post.getcategory().equals(category)) {
                        GlobalStaticData.listPostOnReQuest.add(post);
                    }
                }

                intentPostsOnReQuest.putExtra(AppConfig.BARNAME,category);
                intentPostsOnReQuest.putExtra(AppConfig.LISTPOST,(ArrayList)GlobalStaticData.listPostOnReQuest);
                startActivity(intentPostsOnReQuest);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    //thiết lập tab của viewpager để trượt qua
    public class FakeContent implements TabHost.TabContentFactory{
        Context context;
        public FakeContent(Context mcontext){
            context=mcontext;
        }

        @Override
        public View createTabContent(String tag) {
            View fakeview = new View(context);
            fakeview.setMinimumHeight(0);
            fakeview.setMinimumWidth(0);
            return fakeview;
        }
    }
    /*
        Viewpager listener
     */

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }
    //khi trượt thay đổi trang hiện tại của viewpager thì tab hiện tại của tabhot cũng thay đổi theo
    @Override
    public void onPageSelected(int position) {
        tabHost.setCurrentTab(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
    /*
            Tabhost listener
            bắt sự kiện thay đổi tab
         */
    @Override
    public void onTabChanged(String tabId) {
        TextView textViewNew = (TextView) findViewById(R.id.textViewNew);
        TextView textViewHot = (TextView)findViewById(R.id.textViewHot);

        int selectedPage = tabHost.getCurrentTab();
        //sét các màu của tab widget
        if(selectedPage==0) {
            textViewHot.setBackgroundResource(R.drawable.tabs_pressed_left);
            textViewHot.setTextColor(getResources().getColor(R.color.textcolor_selected));

            textViewNew.setBackgroundResource(R.drawable.tabs_normal_right);
            textViewNew.setTextColor(getResources().getColor(R.color.white));
        }else{
            textViewHot.setBackgroundResource(R.drawable.tabs_normal_left);
            textViewHot.setTextColor(getResources().getColor(R.color.white));

            textViewNew.setBackgroundResource(R.drawable.tabs_pressed_right);
            textViewNew.setTextColor(getResources().getColor(R.color.textcolor_selected));
        }
        viewPager.setCurrentItem(selectedPage);
        GlobalStaticData.setCurrentPage(selectedPage);
    }

    //tạo Diaglog ic dấu "+"
    private void initMenuplus(){
        imageButtonPlus = (ImageButton)findViewById(R.id.imageButtonPlus);
        imageButtonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(MainActivity.this, imageButtonPlus);
                popupMenu.inflate(R.menu.main);
                switch (GlobalStaticData.getCurrentPage()){
                    case 0:
                        if(isFilterHot)
                            popupMenu.getMenu().findItem(R.id.action_clearfilter).setEnabled(true);
                        else
                            popupMenu.getMenu().findItem(R.id.action_clearfilter).setEnabled(false);
                        break;
                    case 1:
                        if(isFilterNew)
                            popupMenu.getMenu().findItem(R.id.action_clearfilter).setEnabled(true);
                        else
                            popupMenu.getMenu().findItem(R.id.action_clearfilter).setEnabled(false);
                        break;
                }


                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_filter:
                                dialogFilter.show();
                                break;
                            case R.id.action_clearfilter:
                                clearFilter();
                                break;
                            case R.id.action_bookmarks:
                                GlobalFunction.onClickViewBookMark(MainActivity.this,progressDialog);
                                return true;

                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });

    }

    private void initdialogFilter(){
        Date today=new Date(System.currentTimeMillis());
        DateFormat timeFormat= SimpleDateFormat.getDateTimeInstance();
        // khởi tạo dialog
        dialogFilter = new Dialog(this,R.style.NoTitleDialog);
        // xét layout cho dialog
        dialogFilter.setContentView(R.layout.dialogfilter);

        //day
        wheelDayPicker = (WheelDayPicker) dialogFilter.findViewById(R.id.wheel_date_picker_day);
        wheelDayPicker.setCyclic(true);
        wheelDayPicker.setItemTextColor(R.color.whellpicker_ItemTextColor);
        wheelDayPicker.setIndicator(true);
        wheelDayPicker.setIndicatorColor(R.color.whellpicker_IndicatorColor);
        wheelDayPicker.setAtmospheric(true);
        wheelDayPicker.setCurved(true);
        //month
        wheelMonthPicker = (WheelMonthPicker) dialogFilter.findViewById(R.id.wheel_date_picker_month);
        wheelMonthPicker.setCyclic(true);
        wheelMonthPicker.setItemTextColor(R.color.whellpicker_ItemTextColor);
        wheelMonthPicker.setIndicator(true);
        wheelMonthPicker.setIndicatorColor(R.color.whellpicker_IndicatorColor);
        wheelMonthPicker.setAtmospheric(true);
        wheelMonthPicker.setCurved(true);
        //year
        wheelYearPicker = (WheelYearPicker) dialogFilter.findViewById(R.id.wheel_date_picker_year);
        wheelYearPicker.setCyclic(true);
        wheelYearPicker.setItemTextColor(R.color.whellpicker_ItemTextColor);
        wheelYearPicker.setIndicatorColor(R.color.whellpicker_IndicatorColor);
        wheelYearPicker.setIndicator(true);
        wheelYearPicker.setAtmospheric(true);
        wheelYearPicker.setCurved(true);

        TextView textViewCancel = (TextView) dialogFilter.findViewById(R.id.textViewCancel);
        TextView textViewOk = (TextView) dialogFilter.findViewById(R.id.textViewOk);
        textViewCancel.setOnClickListener(this);
        textViewOk.setOnClickListener(this);

    }
    //funtion clickView: LayoutUser, dialog
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.relativeLayoutUser:
                if(appPreferences.isLogin()){
                    //user profile
                    Intent intent=new Intent(MainActivity.this, Profile_Activity.class);
                    intent.putExtra("userid",appPreferences.getUserId());
                    startActivityForResult(intent,1);
                }
                else {
                    Intent intent = new Intent(this, Login.class);
                    startActivityForResult(intent, AppConfig.REQUEST_CODE_LOGIN_FROM_LinearLayoutLogin);
                }

                break;
            case R.id.imageViewUserAction:
                if(appPreferences.isLogin()){
                    AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(MainActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                    } else {
                        builder = new AlertDialog.Builder(MainActivity.this);
                    }
                    builder.setTitle("Đăng xuất")
                            .setMessage("Bạn có chắc muốn đăng xuất tài khoản?")
                            .setPositiveButton("Đăng xuất", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                    logout();
                                    if(Hot_Fragment.summary_adapter!=null){
                                        Hot_Fragment.summary_adapter.notifyDataSetChanged();
                                    }
                                    if(New_Fragment.postsOnRequestAdapter!=null){
                                        New_Fragment.postsOnRequestAdapter.notifyDataSetChanged();
                                    }
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();

                }
                else {
                    Intent intent = new Intent(this, Login.class);
                    startActivityForResult(intent, AppConfig.REQUEST_CODE_LOGIN_FROM_LinearLayoutLogin);
                }
                break;
            case R.id.textViewCancel:
                dialogFilter.dismiss();
                break;
            case R.id.textViewOk:
                switch (GlobalStaticData.getCurrentPage()){
                    case 0:
                        iOnClickFilterHot.onClickFilter(wheelDayPicker.getCurrentDay()+"-"+wheelMonthPicker.getCurrentMonth()+"-"
                                +wheelYearPicker.getCurrentYear()+" 00:00:00");
                        isFilterHot=true;
                        break;
                    case 1:
                        iOnClickFilterNew.onClickFilter(wheelDayPicker.getCurrentDay()+"-"+wheelMonthPicker.getCurrentMonth()+"-"
                                +wheelYearPicker.getCurrentYear()+" 00:00:00");
                        isFilterNew=true;
                        break;

                }
                dialogFilter.dismiss();

                break;
        }
    }

    private void clearFilter(){
        switch (GlobalStaticData.getCurrentPage()){
            case 0:
                iOnClickFilterHot.onClickClearFilter();
                isFilterHot=false;
                break;
            case 1:
                iOnClickFilterNew.onClickClearFilter();
                isFilterNew=false;
                break;

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                if(GlobalStaticData.getCurrentPage()==0)
                    Hot_Fragment.recyclerViewSummary.smoothScrollToPosition(0);
                else
                    New_Fragment.recyclerViewSummary.smoothScrollToPosition(0);

            }
        });
        loadCurrentUser();
        if(Hot_Fragment.summary_adapter!=null){
            Hot_Fragment.summary_adapter.notifyDataSetChanged();
        }
        if(New_Fragment.postsOnRequestAdapter!=null){
            New_Fragment.postsOnRequestAdapter.notifyDataSetChanged();
        }
    }

}
