package com.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.Global.AppConfig;
import com.android.Global.AppPreferences;
import com.android.Global.GlobalStaticData;
import com.android.Models.UserMember;
import com.android.RetrofitServices.Models_R.Api_Utils;
import com.android.RetrofitServices.Models_R.ResponeServices;
import com.android.RetrofitServices.Models_R.WeaService;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ngoc Vu on 12/18/2017.
 */

public class Login extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener  {
    DatabaseReference databaseReference;
    private GoogleApiClient googleApiClient;
    private WeaService weaService;
    private SignInButton signInButton;
    public static final int SIGN_IN_CODE = 777;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener fireAuthListener;
    EditText edtuser;
    EditText edtpass;
    private String n;
    TextView txtLogin,txtforget,txtdangki,txtloca,txttemp;
    //dialog wait
    SpotsDialog progressDialog;

    AppPreferences appPreferences;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //dialog
        progressDialog = new SpotsDialog(this,R.style.CustomAlertDialog);
        appPreferences = AppPreferences.getInstance(this);

        edtuser = findViewById(R.id.edtLoginName);
        edtpass=findViewById(R.id.edtPass);
        txtloca=findViewById(R.id.txtlocation);
        txttemp=findViewById(R.id.txttemp);
        txtLogin=findViewById(R.id.textViewButtonLogin);
        txtdangki=findViewById(R.id.txtregister);
        weaService=Api_Utils.getWeaservice();
        loadRespone();
        txtdangki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this,Register.class);
                startActivity(intent);
            }
        });
        txtforget=findViewById(R.id.textViewForget);
        txtforget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this,ForgetPassword.class);
                startActivity(intent);
            }
        });
        databaseReference = FirebaseDatabase.getInstance().getReference();
        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login(edtuser.getText().toString(),edtpass.getText().toString());
            }
        });
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();
        signInButton = (SignInButton)findViewById(R.id.signin);
        setButtonText(signInButton,"Sign in with Google Account");
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent,SIGN_IN_CODE);
            }
        });
        firebaseAuth = FirebaseAuth.getInstance();
        fireAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user!=null)
                {
                    goMainScreen();
                }
            }
        };

    }
    private void loadRespone()
    {
        weaService.getAnswers().enqueue(new Callback<ResponeServices>() {
            @Override
            public void onResponse(Call<ResponeServices> call, Response<ResponeServices> response) {
                if (response.isSuccessful())
                {
                    txtloca.setText(response.body().getQuery().getResults().getChannel().getTitle());
                    String t=response.body().getQuery().getResults().getChannel().getItem().getCondition().getTemp();
                    int k=Integer.parseInt(t);
                    int temp=(k-32)*5/9;
                    txttemp.setText(String.valueOf(temp)+"°C "+ response.body().getQuery().getResults().getChannel().getItem().getCondition().getText());
                }
            }

            @Override
            public void onFailure(Call<ResponeServices> call, Throwable t) {

            }
        });
    }
    protected void setButtonText (SignInButton googleSinOutButton, String buttonText) {
        for (int i = 0; i < signInButton.getChildCount (); i++) {
            View v = signInButton.getChildAt (i);
            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setText (buttonText);
                return;
            }
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(fireAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        revokeAccess();
        if (fireAuthListener != null)
        {
            firebaseAuth.removeAuthStateListener(fireAuthListener);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode ==SIGN_IN_CODE)
        {
            GoogleSignInResult result= Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }
    private void  handleSignInResult(GoogleSignInResult result)
    {

        if (result.isSuccess())
        {
            Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show();
            firebaseAuthWithGoogle(result.getSignInAccount());
            //FirebaseUser user = firebaseAuth.getCurrentUser();
            /*if (user!=null)
            {
                appPreferences.setUserId(user.getUid());
                appPreferences.setLogin(true);
                appPreferences.setLoginWithGoogle(true);
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                String currentDateandTime = sdf.format(new Date());
                UserMember newuser = new UserMember("", currentDateandTime,user.getEmail(),user.getPhotoUrl().toString(),user.getEmail(),user.getDisplayName(),"","","",user.getUid());
                databaseReference.child(AppConfig.FIREBASE_FIELD_USERMEMBERS).child(user.getUid()).setValue(newuser, new DatabaseReference.CompletionListener(){
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference1) {
                        databaseReference1.child("userId").setValue(databaseReference1.getKey());
                    }
                });
                Toast.makeText(this, "Login Success2", Toast.LENGTH_SHORT).show();
            }*/
            appPreferences.setUserId(result.getSignInAccount().getId());
            appPreferences.setLogin(true);
            appPreferences.setLoginWithGoogle(true);
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String currentDateandTime = sdf.format(new Date());
            UserMember newuser = new UserMember("", currentDateandTime,result.getSignInAccount().getEmail(),result.getSignInAccount().getPhotoUrl().toString(),result.getSignInAccount().getEmail(),result.getSignInAccount().getDisplayName(),"","","",result.getSignInAccount().getId());
            databaseReference.child(AppConfig.FIREBASE_FIELD_USERMEMBERS).child(result.getSignInAccount().getId()).setValue(newuser);
        }
        else
        {
            Toast.makeText(this, result.getStatus().toString(), Toast.LENGTH_SHORT).show();
            Toast.makeText(this,"failed",Toast.LENGTH_SHORT).show();
        }
    }
    private void firebaseAuthWithGoogle( GoogleSignInAccount signInAccount)
    {
        AuthCredential credential = GoogleAuthProvider.getCredential(signInAccount.getIdToken(),null);
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
             if (!task.isSuccessful())
             {
                 Toast.makeText(getApplicationContext(),"Fail",Toast.LENGTH_SHORT).show();
             }
             else
             {
                 Toast.makeText(getApplicationContext(), "Login Success3", Toast.LENGTH_SHORT).show();
             }
            }
        });
    }
    private void goMainScreen ()
    {
        Intent intent = new Intent(this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent );
    }
    private void Login(final String UserLoginName,final String password)
    {
        progressDialog.show();
        databaseReference.child(AppConfig.FIREBASE_FIELD_USERMEMBERS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean fSuccess=false;
                for(DataSnapshot dataPost : dataSnapshot.getChildren())
                {
                    if(dataPost.getValue(UserMember.class).getLoginName().contentEquals(UserLoginName))
                    {
                        if(dataPost.getValue(UserMember.class).getPassword().contentEquals(password))
                        {

                            appPreferences.setLogin(true);
                            appPreferences.setLoginWithGoogle(false);
                            appPreferences.setUserId(dataPost.getKey());
                            sendLoginResult(dataPost.getValue(UserMember.class));
                            progressDialog.dismiss();
                            fSuccess=true;
                        }
                        else{
                            break;
                        }
                    }
                }
                if(!fSuccess){
                    Toast.makeText(Login.this,"LoginName hoặc Password không chính xác", Toast.LENGTH_SHORT).show();
                    edtpass.setText(null);
                    progressDialog.dismiss();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void sendLoginResult(UserMember user){
        GlobalStaticData.setCurrentUser(user);
        Intent intent = new Intent();
        intent.putExtra(AppConfig.USER,user);
        setResult(AppConfig.RESULT_CODE_LOGIN,intent);
        finish();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    private void revokeAccess()
    {
        firebaseAuth.signOut();
    }
}

