package com.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.Global.AppConfig;
import com.android.Models.UserMember;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Ngoc Vu on 12/28/2017.
 */

public class Register extends AppCompatActivity {
    private EditText edtloginname,edtemail,edtpass,edtaddress,edtfullname,edtphone;
    private RadioButton rdnam,rdnu;
    private DatabaseReference databaseReference;
    private String gioitinh;
    private RadioGroup rdg;
    private CardView cd1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        edtloginname = findViewById(R.id.input_loginname);
        edtemail = findViewById(R.id.input_email);
        edtpass = findViewById(R.id.input_password);
        cd1 = findViewById(R.id.cardregister);
        rdg= findViewById(R.id.radiog);
        edtfullname = findViewById(R.id.input_name);
        edtaddress = findViewById(R.id.input_address);
        edtphone = findViewById(R.id.input_phone);
        rdnam = findViewById(R.id.rdnam);
        rdnu = findViewById(R.id.rdnu);
        databaseReference = FirebaseDatabase.getInstance().getReference();

        cd1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rdnam.isChecked())
                {
                    rdnu.setChecked(false);
                    gioitinh="Nam";
                }
                if (rdnu.isChecked())
                {
                    rdnam.setChecked(false);
                    gioitinh="Nữ";
                }
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                String currentDateandTime = sdf.format(new Date());
                CreateUser(edtaddress.getText().toString(),currentDateandTime,edtemail.getText().toString(),"",edtloginname.getText().toString(),edtfullname.getText().toString(),edtpass.getText().toString(),edtphone.getText().toString(),gioitinh);
            }
        });

    }
    private void CreateUser(final  String address,final String dateCreate, final String email,final String img,final String loginName,final String name,final String password,final String phone,final String sex)
    {
        DatabaseReference mdata=FirebaseDatabase.getInstance().getReference();
        mdata.child(AppConfig.FIREBASE_FIELD_USERMEMBERS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean fLoginName = false;
                for(DataSnapshot datauser : dataSnapshot.getChildren())
                {
                    if((!datauser.getValue(UserMember.class).getLoginName().contentEquals(loginName))&&password!=null) {

                    }
                    else
                    {
                        Toast.makeText(Register.this,"tên đăng nhập đã tồn tại", Toast.LENGTH_SHORT).show();
                        fLoginName = true;
                    }
                }
                if(!fLoginName){
                    writeNewUser(address, dateCreate, email, "https://firebasestorage.googleapis.com/v0/b/news-daeeb.appspot.com/o/icon_user.png?alt=media&token=73d0513e-c2d7-4eee-bfb9-114b222be49b", loginName, name, password, phone, sex,"");
                    Toast.makeText(Register.this, "Đăng kí thành công", Toast.LENGTH_SHORT).show();
                    Intent intentSearch = new Intent(Register.this, Login.class);
                    startActivity(intentSearch);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
       /* final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("");
        DatabaseReference userMemberssRef = ref.child("UserMembers");

            userMemberssRef.push().setValue(new UserMember(address, dateCreate,email,"https://www.facebook.com/photo.php?fbid=1928810227435906&set=pcb.1928813470768915&type=3",loginName,name,password,phone,sex,userMemberssRef.push().getKey().toString() ));
*/
    }

    private void writeNewUser(final  String address,final String dateCreate, final String email,final String img,final String loginName,final String name,final String password,final String phone,final String sex,final String userid) {
        UserMember user = new UserMember(address, dateCreate,email,img,loginName,name,password,phone,sex,userid);
        databaseReference.child("UserMembers").push().setValue(user, new DatabaseReference.CompletionListener(){
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference1) {
                databaseReference1.child("userId").setValue(databaseReference1.getKey());
            }
        });
        finish();
    //    CreateUser(address, dateCreate,email,img,loginName,name,password,phone,sex);
    }
}
