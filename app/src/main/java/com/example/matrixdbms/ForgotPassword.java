package com.example.matrixdbms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    private EditText resetEmail;
    private Button sendEmail;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        auth = FirebaseAuth.getInstance();

        resetEmail = (EditText) findViewById(R.id.resetmal_editTxt);
        sendEmail = (Button) findViewById(R.id.send_btn);

        sendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String useremail = resetEmail.getText().toString();

                if(TextUtils.isEmpty(resetEmail.getText().toString())){
                    resetEmail.setError("REQUIRED!");
                }
                else {
                    auth.sendPasswordResetEmail(useremail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getApplicationContext(), "Please check your email to reset your password!",Toast.LENGTH_SHORT).show();
                                finish(); return;
                            }
                            else{
                                String message = task.getException().getMessage();
                                Toast.makeText(getApplicationContext(), "Error occured: " +message,Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}
