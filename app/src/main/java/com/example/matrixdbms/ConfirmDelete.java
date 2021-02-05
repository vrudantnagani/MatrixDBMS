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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ConfirmDelete extends AppCompatActivity {

    private EditText Confpsd;
    private Button ConDel;

    private String key;
    private String cuser;

    private FirebaseAuth auth;

    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_delete);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        key = getIntent().getStringExtra("key");

        Confpsd = (EditText) findViewById(R.id.confpsd);

        ConDel = (Button) findViewById(R.id.delete);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(user.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cuser = dataSnapshot.child("username").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ConDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(isEmpty())   return;
                    auth.signInWithEmailAndPassword(cuser,Confpsd.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            new FirebaseDatabaseHelper().deletePart(key, new FirebaseDatabaseHelper.DataStatus() {
                                @Override
                                public void DataIsLoaded(List<MatrixDBMS> matrixDBMS, List<String> keys) {

                                }

                                @Override
                                public void DataIsInserted() {

                                }

                                @Override
                                public void DataIsUpdated() {

                                }

                                @Override
                                public void DataIsDeleted() {
                                    Toast.makeText(ConfirmDelete.this, "Part record has been deleted successfully",Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(ConfirmDelete.this, MatrixDBMSMainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish(); return;
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ConfirmDelete.this, "Delete process failed: "+e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    });
            }
        });

    }


    private boolean isEmpty() {
        if(TextUtils.isEmpty(Confpsd.getText().toString())) {
            Confpsd.setError("REQUIRED!");
            return true;
        }
        return false;
    }
}
