package com.example.matrixdbms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UpdatePart extends AppCompatActivity {

    private EditText Part_editTxt;
    private EditText Manufacturer_editTxt;
    private EditText Installation_editTxt;
    private EditText Modification_editTxt;
    private EditText Operator_editTxt;
    private Spinner Part_categories_spinner;
    private Button Update_btn;

    private FirebaseAuth auth;

    private FirebaseUser user;

    private String part;
    private String manufacturer;
    private String category;
    private String installation;
    private String modification;
    private String operator;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_part);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        id = getIntent().getStringExtra("id");

        Part_editTxt = (EditText) findViewById(R.id.part_editTxt);
        Manufacturer_editTxt = (EditText) findViewById(R.id.manufacturer_editTxt);
        Part_categories_spinner = (Spinner) findViewById(R.id.part_categories_spinner);
        Installation_editTxt = (EditText) findViewById(R.id.installation_editTxt);
        Modification_editTxt = (EditText) findViewById(R.id.modification_editTxt);
        Operator_editTxt = (EditText) findViewById(R.id.operator_editTxt);

        Update_btn = (Button) findViewById(R.id.up_btn);

        /*part = getIntent().getStringExtra("part");
        manufacturer = getIntent().getStringExtra("manufacturer");
        category = getIntent().getStringExtra("category");
        installation = getIntent().getStringExtra("installation");
        modification = getIntent().getStringExtra("modification");
        operator = getIntent().getStringExtra("operator");

        Part_editTxt = (EditText) findViewById(R.id.part_editTxt);
        Part_editTxt.setText(part);
        Manufacturer_editTxt = (EditText) findViewById(R.id.manufacturer_editTxt);
        Manufacturer_editTxt.setText(manufacturer);
        Installation_editTxt = (EditText) findViewById(R.id.installation_editTxt);
        Installation_editTxt.setText(installation);
        Modification_editTxt = (EditText) findViewById(R.id.modification_editTxt);
        Modification_editTxt.setText(modification);
        Operator_editTxt = (EditText) findViewById(R.id.operator_editTxt);
        Operator_editTxt.setText(operator);
        Part_categories_spinner = (Spinner) findViewById(R.id.part_categories_spinner);
        Part_categories_spinner.setSelection(getIndex_SpinnerItem(Part_categories_spinner, category));*/
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("matrix").child(id.toString());

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
                Part_editTxt.setText(part);
                Manufacturer_editTxt.setText(manufacturer);
                Part_categories_spinner.setSelection(getIndex_SpinnerItem(Part_categories_spinner, category));
                Installation_editTxt.setText(installation_date);
                Modification_editTxt.setText(modification_date);
                Operator_editTxt.setText(operator_name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        Part_categories_spinner.setOnItemSelectedListener(listener);

        Update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String id = getIntent().getStringExtra("id");
                String part = Part_editTxt.getText().toString().trim();
                String manufacturer = Manufacturer_editTxt.getText().toString().trim();
                String category = Part_categories_spinner.getSelectedItem().toString();
                String installation_date = Installation_editTxt.getText().toString().trim();
                String modification_date = Modification_editTxt.getText().toString().trim();
                String operator_name = Operator_editTxt.getText().toString().trim();

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("matrix").child(id.toString());

                MatrixDBMS matrixDBMS = new MatrixDBMS(id, part, manufacturer, category, installation_date, modification_date, operator_name);
                databaseReference.setValue(matrixDBMS);

                Toast.makeText(UpdatePart.this,"The part record has been updated successfully",Toast.LENGTH_LONG).show();

                //updatePart(id, part, manufacturer, category, installation_date, modification_date,operator_name);

                Intent intent = new Intent(UpdatePart.this, ParticularPart.class);
                intent.putExtra("id", id);
                startActivity(intent);

            }
        });

    }

    /*private boolean updatePart(String id, String part, String manufacturer, String category, String installation_date, String modification_date, String operator_name) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("matrix").child(id);

        MatrixDBMS matrixDBMS = new MatrixDBMS(id, part, manufacturer, category, installation_date, modification_date, operator_name);

        databaseReference.setValue(matrixDBMS);

        Toast.makeText(UpdatePart.this,"The part record has been updated successfully",Toast.LENGTH_LONG).show();

        return true;
    }*/

    private int getIndex_SpinnerItem(Spinner spinner, String item) {
        int index = 0;
        for(int i = 0; i<spinner.getCount(); i++) {
            if(spinner.getItemAtPosition(i).equals(item)) {
                index = i;
                break;
            }
        }
        return index;
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
        super.onBackPressed();
        Intent intent = new Intent(UpdatePart.this, ParticularPart.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }

}
