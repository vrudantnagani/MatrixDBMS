package com.example.matrixdbms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.Manifest.permission.CAMERA;

public class QRCodeScanner extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private static final int REQUEST_CAMERA = 1;
    private ZXingScannerView scannerView;

    private FirebaseAuth auth;

    private FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkPermission()) {
                Toast.makeText(QRCodeScanner.this, "Permission is granted!", Toast.LENGTH_LONG).show();
            } else {
                requestPermission();
            }
        }
    }

    private boolean checkPermission() {
        return (ContextCompat.checkSelfPermission(QRCodeScanner.this, CAMERA) == PackageManager.PERMISSION_GRANTED);
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{CAMERA}, REQUEST_CAMERA);
    }

    public void onRequestPermissionsResult(int requestcode, String permission[], int grantResults[]) {
        switch (requestcode){
            case REQUEST_CAMERA :
                if(grantResults.length>0){
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if(cameraAccepted){
                        Toast.makeText(QRCodeScanner.this, "Permission Granted", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(QRCodeScanner.this, "Permission Denied", Toast.LENGTH_LONG).show();
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                        {
                            if(shouldShowRequestPermissionRationale(CAMERA)){
                                displayAlertMessage("You need to allow access for both permissions", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        requestPermissions(new String[]{CAMERA}, REQUEST_CAMERA);
                                    }
                                });
                                return;
                            }
                        }
                    }
                }
                break;
        }
    }

    @Override
    public void onResume() {

        super.onResume();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkPermission()) {
                if(scannerView == null){
                    scannerView = new ZXingScannerView(this);
                    setContentView(scannerView);
                }
                scannerView.setResultHandler(this);
                scannerView.startCamera();
            }
            else{
                requestPermission();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        scannerView.stopCamera();
    }

    public void displayAlertMessage(String message, DialogInterface.OnClickListener listener) {
        new AlertDialog.Builder(QRCodeScanner.this)
                .setMessage(message)
                .setPositiveButton("Rescan", listener)
                .setNegativeButton("CANCEL", null)
                .create()
                .show();
    }

    @Override
    public void handleResult(final Result result) {
        final String scanResult = result.getText();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Scan Result");
        builder.setPositiveButton("Rescan", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                scannerView.resumeCameraPreview(QRCodeScanner.this);
            }
        });

        builder.setNeutralButton("View Part", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String id = result.getText().toString();

                auth = FirebaseAuth.getInstance();
                user = auth.getCurrentUser();

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("matrix").child(id);
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int a = 0;

                        /*boolean flag=false;
                        for(DataSnapshot ds : dataSnapshot.getChildren()) {
                            String partid = ds.getKey();
                            flag = id.equals(partid);
                            if(flag == true) {
                                break;
                            }
                        }*/
                        if(dataSnapshot.exists()) {
                            Intent intent = new Intent(QRCodeScanner.this, ParticularPart.class);
                            intent.putExtra("id", id);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(QRCodeScanner.this,"NO SUCH PART EXITS",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(QRCodeScanner.this, MainMenu.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                /*Intent intent = new Intent(QRCodeScanner.this, ParticularPart.class);
                intent.putExtra("id", id);
                startActivity(intent);*/
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(QRCodeScanner.this,MainMenu.class));
        finish();
    }
}
