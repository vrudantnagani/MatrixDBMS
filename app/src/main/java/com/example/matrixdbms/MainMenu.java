package com.example.matrixdbms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MainMenu extends AppCompatActivity {

    ImageView bgdash;
    Animation bganim, frombottom;
    LinearLayout texthome, menu;

    private long backPressedTime;
    private Toast backToast;

    private Button Scan_btn;
    private Button New_btn;
    private Button View_btn;
    private Button Logout_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        bgdash = (ImageView) findViewById(R.id.bgdash);
        texthome = (LinearLayout) findViewById(R.id.texthome);
        menu = (LinearLayout) findViewById(R.id.menus);

        bganim = AnimationUtils.loadAnimation(this,R.anim.fromtop);
        frombottom = AnimationUtils.loadAnimation(this,R.anim.frombottom);

        bgdash.animate().translationY(-1400).setDuration(800).setStartDelay(300);
        texthome.startAnimation(frombottom);
        menu.startAnimation(frombottom);

        Scan_btn = (Button) findViewById(R.id.scan_qr_btn);
        New_btn = (Button) findViewById(R.id.addpart_btn);
        View_btn = (Button) findViewById(R.id.allpart_btn);
        Logout_btn = (Button) findViewById(R.id.logout_btn);

        Scan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainMenu.this, QRCodeScanner.class));
            }
        });

        New_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainMenu.this, NewPartActivity.class));
            }
        });

        View_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainMenu.this, MatrixDBMSMainActivity.class));
            }
        });

        Logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(getBaseContext(),"User Logged out",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainMenu.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();

            }
        });
    }

    @Override
    public void onBackPressed() {

        if(backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
            super.onBackPressed();
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(getBaseContext(),"User Logged out",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainMenu.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        } else {
            backToast = Toast.makeText(getBaseContext(), "Press back again to logout", Toast.LENGTH_SHORT);
            backToast.show();
        }

        backPressedTime = System.currentTimeMillis();
    }
}
