package com.android.Activity_Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.Effect.Session;
import com.android.ForgetPassword;
import com.android.Global.AppConfig;
import com.android.Login;
import com.android.Models.UserMember;
import com.android.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Ngoc Vu on 1/2/2018.
 */

public class Profile_Activity extends AppCompatActivity {
    TextView txt_Logout,txtname,txtaddress,txtphone,txtgioitinh;
    private DatabaseReference mdata;
    Session session;
    private String u;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_profile_user);
        session=new Session(this);
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
                    loggout();
            }
        });
        if (u==null)
        {

        }else {
            Profile(u);
        }
    }
    private void loggout()
    {
        session.setLoginin(false);
        finish();
        startActivity(new Intent(Profile_Activity.this, Login.class));
    }
    private void Profile(final String Userid) {
        mdata.child(AppConfig.FIREBASE_FIELD_USERMEMBERS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dataPost : dataSnapshot.getChildren())
                {
                    if(dataPost.getValue(UserMember.class).getUserId().contentEquals(Userid)) {
                                String n = String.valueOf(dataPost.getValue(UserMember.class).getName());
                                txtname.setText(n);
                                String a = String.valueOf(dataPost.getValue(UserMember.class).getAddress());
                                txtaddress.setText(a);
                                String p = String.valueOf(dataPost.getValue(UserMember.class).getPhone());
                                txtphone.setText(p);
                                String g=String.valueOf(dataPost.getValue(UserMember.class).getSex());
                                txtgioitinh.setText(g);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
