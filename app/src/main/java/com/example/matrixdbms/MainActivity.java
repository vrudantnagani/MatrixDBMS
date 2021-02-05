package com.example.matrixdbms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private long backPressedTime;
    private Toast backToast;

    private EditText mEmail_editTxt;
    private EditText mPassword_editTxt;

    private TextView mSignup_txtView;
    private TextView mForgot_txtView;

    private Button mSignIn_btn;

    private ProgressBar mProgress_bar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        mEmail_editTxt = (EditText) findViewById(R.id.email_editText);
        mPassword_editTxt = (EditText) findViewById(R.id.password_editText);

        mSignup_txtView = (TextView) findViewById(R.id.signup_txtView);
        mForgot_txtView = (TextView) findViewById(R.id.forgot_txtView);

        mSignIn_btn= (Button) findViewById(R.id.signin_btn);

        mProgress_bar = (ProgressBar) findViewById(R.id.loading_progressBar);

        mSignIn_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isEmpty())   return;
                inProgress(true);
                mAuth.signInWithEmailAndPassword(mEmail_editTxt.getText().toString(),mPassword_editTxt.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(MainActivity.this, "User signed in", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(MainActivity.this, MainMenu.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish(); return;
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        inProgress(false);
                        Toast.makeText(MainActivity.this, "Sign in failed!"+e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        mSignup_txtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, NewUser.class));
            }
        });

        mForgot_txtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ForgotPassword.class));
            }
        });
    }

    private void inProgress(boolean x) {
        if(x) {
            mProgress_bar.setVisibility(View.VISIBLE);
            mSignIn_btn.setEnabled(false);
        }else {
            mProgress_bar.setVisibility(View.GONE);
            mSignIn_btn.setEnabled(true);
        }
    }

    private boolean isEmpty() {
        if(TextUtils.isEmpty(mEmail_editTxt.getText().toString())) {
            mEmail_editTxt.setError("REQUIRED!");
            return true;
        }
        if(TextUtils.isEmpty(mPassword_editTxt.getText().toString())) {
            mPassword_editTxt.setError("REQUIRED!");
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {

        if(backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
            super.onBackPressed();
            return;
        } else {
            backToast = Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT);
            backToast.show();
        }

        backPressedTime = System.currentTimeMillis();
    }
}