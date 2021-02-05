package com.example.matrixdbms;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NewUser extends AppCompatActivity {

    private EditText nemail;
    private EditText npassword;
    private Button signup;

    private ProgressBar mProgress_bar;

    private FirebaseAuth mAuth;

    private FirebaseUser firebaseUser;

    private DatabaseReference rootReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        mAuth = FirebaseAuth.getInstance();

        rootReference = FirebaseDatabase.getInstance().getReference();

        nemail = (EditText) findViewById(R.id.nemail_editText);
        npassword = (EditText) findViewById(R.id.npassword_editText);

        signup = (Button) findViewById(R.id.nsignup_btn);

        mProgress_bar = (ProgressBar) findViewById(R.id.loading_progressBar);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isEmpty())   return;
                inProgress(true);
                mAuth.createUserWithEmailAndPassword(nemail.getText().toString(),npassword.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(NewUser.this, "User registered successfully!", Toast.LENGTH_LONG).show();

                        //Inserting user id as node in table

                        firebaseUser = mAuth.getCurrentUser();
                        User user = new User(nemail.getText().toString(), npassword.getText().toString());
                        rootReference.child(firebaseUser.getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    finish();return;
                                    //or start activity if any
                                }
                            }
                        });


                        inProgress(false);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        inProgress(false);
                        Toast.makeText(NewUser.this, "Registration failed!"+e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

    }

    private void inProgress(boolean x) {
        if(x) {
            mProgress_bar.setVisibility(View.VISIBLE);
            signup.setEnabled(false);
        }else {
            mProgress_bar.setVisibility(View.GONE);
            signup.setEnabled(true);
        }
    }

    private boolean isEmpty() {
        if(TextUtils.isEmpty(nemail.getText().toString())) {
            nemail.setError("REQUIRED!");
            return true;
        }
        if(TextUtils.isEmpty(npassword.getText().toString())) {
            npassword.setError("REQUIRED!");
            return true;
        }
        return false;
    }
}
