package com.example.matrixdbms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class PartDetailsActivity extends AppCompatActivity {

    private TextView mPart;
    private TextView mManufacturer;
    private TextView mCategory;
    private TextView mInstallation;
    private TextView mModification;
    private TextView mOperator;

    private String key;
    private String part;
    private String manufacturer;
    private String category;
    private String installation;
    private String modification;
    private String operator;

    private Button mDelete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_part_details);

        key = getIntent().getStringExtra("key");
        part = getIntent().getStringExtra("part");
        manufacturer = getIntent().getStringExtra("manufacturer");
        category = getIntent().getStringExtra("category");
        installation = getIntent().getStringExtra("installation");
        modification = getIntent().getStringExtra("modification");
        operator = getIntent().getStringExtra("operator");

        mPart = (TextView) findViewById(R.id.part_txtView);
        mPart.setText(part);
        mManufacturer = (TextView) findViewById(R.id.manufacturer_txtView);
        mManufacturer.setText(manufacturer);
        mCategory = (TextView) findViewById(R.id.category_txtView);
        mCategory.setText(category);
        mInstallation = (TextView) findViewById(R.id.installation_txtView);
        mInstallation.setText(installation);
        mModification = (TextView) findViewById(R.id.modification_txtView);
        mModification.setText(modification);
        mOperator = (TextView) findViewById(R.id.operator_txtView);
        mOperator.setText(operator);

        mDelete = (Button) findViewById(R.id.delete_btn);

        mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PartDetailsActivity.this, ConfirmDelete.class);
                intent.putExtra("key",key);
                startActivity(intent);
                /*new FirebaseDatabaseHelper().deletePart(key, new FirebaseDatabaseHelper.DataStatus() {
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
                        Toast.makeText(PartDetailsActivity.this, "Part record has been deleted successfully",Toast.LENGTH_SHORT).show();
                        finish(); return;
                    }
                });*/
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}
