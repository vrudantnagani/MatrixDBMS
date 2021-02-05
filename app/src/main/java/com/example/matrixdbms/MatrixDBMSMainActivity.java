package com.example.matrixdbms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MatrixDBMSMainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matrix_d_b_m_s_main);

        mAuth = FirebaseAuth.getInstance();

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_parts);
        new FirebaseDatabaseHelper().readParts(new FirebaseDatabaseHelper.DataStatus() {
            @Override
            public void DataIsLoaded(List<MatrixDBMS> matrixDBMS, List<String> keys) {
                findViewById(R.id.loading_parts_pb).setVisibility(View.GONE);
                new RecyclerView_Config().setConfig(mRecyclerView, MatrixDBMSMainActivity.this, matrixDBMS, keys);
            }

            @Override
            public void DataIsInserted() {

            }

            @Override
            public void DataIsUpdated() {

            }

            @Override
            public void DataIsDeleted() {

            }
        });
    }

}
