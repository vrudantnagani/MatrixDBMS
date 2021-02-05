package com.example.matrixdbms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.WriterException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class QRCode_Generator extends AppCompatActivity {

    String TAG = "GenerateQRCode";
    ImageView qrimg;
    Button save;
    String id;
    Bitmap bitmap;
    QRGEncoder qrgEncoder;

    OutputStream outputStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q_r_code__generator);

        qrimg = (ImageView) findViewById(R.id.qrcode_imgView);
        save = (Button) findViewById(R.id.save_button);

        id = getIntent().getStringExtra("id");

        if(id.length()>0) {
            WindowManager manager = (WindowManager)getSystemService(WINDOW_SERVICE);
            Display display = manager.getDefaultDisplay();
            Point point = new Point();
            display.getSize(point);
            int width = point.x;
            int height = point.y;
            int smallerdim = width < height ? width : height;
            smallerdim = smallerdim*3/4;
            qrgEncoder = new QRGEncoder(id, null, QRGContents.Type.TEXT, smallerdim);
            try {
                bitmap = qrgEncoder.encodeAsBitmap();
                qrimg.setImageBitmap(bitmap);
             }
            catch (WriterException e) {
                Log.v(TAG, e.toString());
            }
        }
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BitmapDrawable drawable = (BitmapDrawable) qrimg.getDrawable();
                Bitmap bitmap = drawable.getBitmap();

                File path = getExternalFilesDir(Environment.DIRECTORY_DCIM);
                File dir = new File(path.getAbsolutePath()+"/Demo/");
                dir.mkdirs();
                File file = new File(dir, System.currentTimeMillis()+".jpg");
                try {
                    outputStream = new FileOutputStream(file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                bitmap.compress(Bitmap.CompressFormat.JPEG ,100,outputStream);
                Toast.makeText(getApplicationContext(), "QR Saved to Internal Memory",Toast.LENGTH_SHORT).show();
                try {
                    outputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                startActivity(new Intent(QRCode_Generator.this, MainMenu.class));
                finish();
            }
        });
    }
    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(QRCode_Generator.this, MainMenu.class));
    }
}
