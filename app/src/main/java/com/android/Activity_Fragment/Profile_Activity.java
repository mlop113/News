package com.android.Activity_Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.Global.AppConfig;
import com.android.Global.AppPreferences;
import com.android.Models.UserMember;
import com.android.R;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Ngoc Vu on 1/2/2018.
 */

public class Profile_Activity extends AppCompatActivity {
    AppPreferences appPreferences;
    TextView txt_Logout,txtname,txtaddress,txtphone,txtgioitinh;
    ImageView user_profile_photo;
    private DatabaseReference mdata;
    private String u;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_profile_user);
        appPreferences = AppPreferences.getInstance(this);
        Bundle bundle=getIntent().getExtras();
        u=bundle.getString("userid");
        mdata= FirebaseDatabase.getInstance().getReference();
        txt_Logout=findViewById(R.id.profile_logout);
        txtname=findViewById(R.id.user_profile_name);
        txtaddress=findViewById(R.id.user_profile_address);
        txtphone=findViewById(R.id.information_phone);
        txtgioitinh=findViewById(R.id.information_gioitinh);
        txt_Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(Profile_Activity.this, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(Profile_Activity.this);
                }
                builder.setTitle("Đăng xuất")
                        .setMessage("Bạn có chắc muốn đăng xuất tài khoản?")
                        .setPositiveButton("Đăng xuất", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                logout();
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
        });
        user_profile_photo = findViewById(R.id.user_profile_photo);
        user_profile_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        if (u==null)
        {

        }else {
            Profile(u);
        }
    }
    private void logout()
    {
        if(appPreferences.isLoginWithGoogle()){
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.signOut();
        }
        appPreferences.setLogin(false);
        Intent intent = new Intent();
        setResult(AppConfig.RESULT_CODE_LOGOUT,intent);
        finish();
    }
    private void Profile(final String Userid) {
        mdata.child(AppConfig.FIREBASE_FIELD_USERMEMBERS).child(Userid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserMember user = dataSnapshot.getValue(UserMember.class);
                                String n = String.valueOf(user.getName());
                                txtname.setText(n);
                                String a = String.valueOf(user.getAddress());
                                txtaddress.setText(a);
                                String p = String.valueOf(user.getPhone());
                                txtphone.setText(p);
                                String g=String.valueOf(user.getSex());
                                txtgioitinh.setText(g);
                //Glide.with(Profile_Activity.this).load(user.getImg()).into(user_profile_photo);
                /*if(android.os.Build.VERSION.SDK_INT >= 21){
                    user_profile_photo.setImageDrawable(getResources().getDrawable(R.drawable.bg_login, getTheme()));
                } else {
                    user_profile_photo.setImageDrawable(getResources().getDrawable(R.drawable.bg_login));
                }*/
                Glide
                        .with(Profile_Activity.this)
                        .load(user.getImg())
                        .into(user_profile_photo);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
