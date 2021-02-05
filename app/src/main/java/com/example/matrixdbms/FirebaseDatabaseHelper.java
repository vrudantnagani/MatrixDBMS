package com.example.matrixdbms;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseDatabaseHelper {
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReferenceParts;
    private List<MatrixDBMS> matrixDBMS = new ArrayList<>();

    private FirebaseAuth auth;

    private FirebaseUser user;

    public interface DataStatus{
        void DataIsLoaded(List<MatrixDBMS> matrixDBMS, List<String> keys);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();
    }
    public FirebaseDatabaseHelper() {

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        //mDatabase = FirebaseDatabase.getInstance();
        //mReferenceParts = mDatabase.getReference("matrix");

        mReferenceParts = FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("matrix");
    }

    public void readParts(final DataStatus dataStatus) {
        mReferenceParts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                matrixDBMS.clear();
                List<String> keys = new ArrayList<>();
                for(DataSnapshot keyNode: dataSnapshot.getChildren()) {
                    keys.add(keyNode.getKey());
                    MatrixDBMS mat = keyNode.getValue(MatrixDBMS.class);
                    matrixDBMS.add(mat);
                }
                dataStatus.DataIsLoaded(matrixDBMS, keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void addPart(MatrixDBMS matrixDBMS, final DataStatus dataStatus) {
        String key = mReferenceParts.push().getKey();
        mReferenceParts.child(key).setValue(matrixDBMS).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dataStatus.DataIsInserted();
            }
        });
    }

    public void  updatePart(String key, MatrixDBMS matrixDBMS, final DataStatus dataStatus) {
        mReferenceParts.child(key).setValue(matrixDBMS).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dataStatus.DataIsUpdated();
            }
        });
    }

    public void deletePart(String key, final DataStatus dataStatus) {
        mReferenceParts.child(key).setValue(null).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dataStatus.DataIsDeleted();
            }
        });
    }
}
