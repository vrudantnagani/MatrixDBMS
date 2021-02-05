package com.example.matrixdbms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class NewPartActivity extends AppCompatActivity {

    private EditText mPart_editTxt;
    private EditText mManufacturer_editTxt;
    private EditText mInstallation_editTxt;
    private EditText mModification_editTxt;
    private EditText mOperator_editTxt;
    private Spinner mPart_categories_spinner;
    private Button mAdd_btn;
    private Button mGenerate_btn;

    private FirebaseAuth auth;

    private FirebaseUser user;

    private String inputValue;

    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYS0123456789";
    public static String randomAlphaNumeric(int count) {
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }

    String id;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_part);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        inputValue = randomAlphaNumeric(7);

        databaseReference = FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("matrix");

        mPart_editTxt = (EditText) findViewById(R.id.part_editTxt);
        mManufacturer_editTxt = (EditText) findViewById(R.id.manufacturer_editTxt);
        mInstallation_editTxt = (EditText) findViewById(R.id.installation_editTxt);
        mModification_editTxt = (EditText) findViewById(R.id.modification_editTxt);
        mOperator_editTxt = (EditText) findViewById(R.id.operator_editTxt);
        mPart_categories_spinner = (Spinner) findViewById(R.id.part_categories_spinner);

        mAdd_btn = (Button) findViewById(R.id.add_btn);
        mGenerate_btn = (Button) findViewById(R.id.generate_button);

        mPart_categories_spinner.setOnItemSelectedListener(listener);

        mAdd_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String part = mPart_editTxt.getText().toString().trim();
                String manufacturer = mManufacturer_editTxt.getText().toString().trim();
                String category = mPart_categories_spinner.getSelectedItem().toString();
                String installation_date = mInstallation_editTxt.getText().toString().trim();
                String modification_date = mModification_editTxt.getText().toString().trim();
                String operator_name = mOperator_editTxt.getText().toString().trim();

                if(!TextUtils.isEmpty(part)) {
                    id = inputValue;
                    MatrixDBMS matrixDBMS = new MatrixDBMS(id, part, manufacturer, category, installation_date, modification_date, operator_name);
                    databaseReference.child(id).setValue(matrixDBMS);

                    Toast.makeText(NewPartActivity.this, "The part record has been inserted successfully", Toast.LENGTH_LONG).show();
                }
            }
        });

        mGenerate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(id!=null) {
                    Intent intent = new Intent(NewPartActivity.this, QRCode_Generator.class);
                    intent.putExtra("id", id);
                    startActivity(intent);
                } else
                {
                    Toast.makeText(NewPartActivity.this,"Add new part to generate",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.white));
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(NewPartActivity.this, MainMenu.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(intent);
    }
}
