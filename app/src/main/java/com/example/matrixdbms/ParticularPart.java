package com.example.matrixdbms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ParticularPart extends AppCompatActivity {
    /*private static final String TAG = "ParticularPart";

    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;*/
    DatabaseReference reference;

    private TextView mPart;
    private TextView mManufacturer;
    private TextView mCategory;
    private TextView mInstallation;
    private TextView mModification;
    private TextView mOperator;
    private Button mUpdate;

    private FirebaseAuth auth;

    private FirebaseUser user;

    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_particular_part);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();


        //keyvalue.setText(getIntent().getStringExtra("id"));
        //String key = keyvalue.getText().toString();

        mPart = (TextView) findViewById(R.id.part_txtView);
        mManufacturer = (TextView) findViewById(R.id.manufacturer_txtView);
        mCategory = (TextView) findViewById(R.id.category_txtView);
        mInstallation = (TextView) findViewById(R.id.installation_txtView);
        mModification = (TextView) findViewById(R.id.modification_txtView);
        mOperator = (TextView) findViewById(R.id.operator_txtView);

        mUpdate = (Button) findViewById(R.id.update_btn);

        id = getIntent().getStringExtra("id");

        //auth = FirebaseAuth.getInstance();
        //firebaseDatabase = FirebaseDatabase.getInstance();
        reference = FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("matrix").child(id.toString());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //Log.d(TAG,"onDataChanges invoked ="+dataSnapshot.toString());
                String part = dataSnapshot.child("part").getValue().toString();
                String manufacturer = dataSnapshot.child("manufacturer").getValue().toString();
                String category = dataSnapshot.child("category").getValue().toString();
                String installation_date = dataSnapshot.child("installation_date").getValue().toString();
                String modification_date = dataSnapshot.child("modification_date").getValue().toString();
                String operator_name = dataSnapshot.child("operator_name").getValue().toString();
                //MatrixDBMS matrixDBMS = (MatrixDBMS) dataSnapshot.getValue(MatrixDBMS.class);
                mPart.setText(part);
                mManufacturer.setText(manufacturer);
                mCategory.setText(category);
                mInstallation.setText(installation_date);
                mModification.setText(modification_date);
                mOperator.setText(operator_name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ParticularPart.this, UpdatePart.class);
                intent.putExtra("id",id);
                /*intent.putExtra("part", mPart.getText().toString());
                intent.putExtra("manufacturer",mManufacturer.getText().toString());
                intent.putExtra("category",mCategory.getText().toString());
                intent.putExtra("installation",mInstallation.getText().toString());
                intent.putExtra("modification",mModification.getText().toString());
                intent.putExtra("operator",mOperator.getText().toString());*/
                startActivity(intent);
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ParticularPart.this, MainMenu.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
