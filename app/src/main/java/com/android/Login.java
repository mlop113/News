package com.android;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.Global.AppConfig;
import com.android.Models.UserMember;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Ngoc Vu on 12/18/2017.
 */

public class Login extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener  {
    DatabaseReference databaseReference;
    private GoogleApiClient googleApiClient;
    private SignInButton signInButton;
    public static final int SIGN_IN_CODE = 777;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener fireAuthListener;
    EditText edtuser;
    EditText edtpass;
    TextView txtLogin,txtforget,txtdangki;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edtuser = findViewById(R.id.edtLoginName);
        edtpass=findViewById(R.id.edtPass);
        txtLogin=findViewById(R.id.textViewButtonLogin);
        txtdangki=findViewById(R.id.txtregister);
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
            firebaseAuthWithGoogle(result.getSignInAccount());
        }
        else
        {
            Toast.makeText(this,"fail",Toast.LENGTH_SHORT).show();
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
        databaseReference.child(AppConfig.FIREBASE_FIELD_USERMEMBERS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dataPost : dataSnapshot.getChildren())
                {
                    if(dataPost.getValue(UserMember.class).getLoginName().contentEquals(UserLoginName))
                    {
                        if(dataPost.getValue(UserMember.class).getPassword().contentEquals(password))
                        {
                            Intent intentSearch = new Intent(Login.this,MainActivity.class);
                            startActivity(intentSearch);
                        }
                        else
                        {
                            Toast.makeText(Login.this,"Password hoặc Loginname không chính xác", Toast.LENGTH_SHORT).show();
                            edtpass.setText(null);
                        }
                    }

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    private void revokeAccess()
    {
        firebaseAuth.signOut();

    }
}

