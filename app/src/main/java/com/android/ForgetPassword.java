package com.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.Global.AppConfig;
import com.android.Models.UserMember;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Ngoc Vu on 12/21/2017.
 */

public class ForgetPassword extends AppCompatActivity {
    private DatabaseReference mdata;
    EditText editusername, editemail, editphone;
    TextView txtdisplay,displaypass;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        editusername = findViewById(R.id.forgetusername);
        editemail = findViewById(R.id.forgetemail);
        editphone = findViewById(R.id.forgetphone);
        txtdisplay = findViewById(R.id.Getpass);
        displaypass=findViewById(R.id.Dislaypass);
         mdata = FirebaseDatabase.getInstance().getReference();
        txtdisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                     Create(editusername.getText().toString(), editemail.getText().toString(), editphone.getText().toString());


            }
        });
    }

    private void Create(final String Username, final String Email, final String Phone) {
        mdata.child(AppConfig.FIREBASE_FIELD_USERMEMBERS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dataPost : dataSnapshot.getChildren())
                {
                    if(dataPost.getValue(UserMember.class).getLoginName().contentEquals(Username)) {
                            if (dataPost.getValue(UserMember.class).getEmail().contentEquals(Email)) {
                                if (dataPost.getValue(UserMember.class).getPhone().contentEquals(Phone)) {
                                    String p = String.valueOf(dataPost.getValue(UserMember.class).getPassword());
                                    displaypass.setText(p);
                                } else {
                                    Toast.makeText(ForgetPassword.this, "Thông tin không chính xác", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(ForgetPassword.this, "Thông tin không chính xác", Toast.LENGTH_SHORT).show();
                            }

                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}
