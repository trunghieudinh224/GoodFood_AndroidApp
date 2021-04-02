package com.example.goodfood;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class OpenApp extends AppCompatActivity {

    Animation top_Animation, bottom_Animation;
    private ImageView img_logoApp;
    private TextView txt_slogan;

    Timer timer;

    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_open_app);

        top_Animation = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottom_Animation = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        img_logoApp = findViewById(R.id.img_logoApp);
        txt_slogan = findViewById(R.id.txt_slogan);

        img_logoApp.setAnimation(top_Animation);
        txt_slogan.setAnimation(bottom_Animation);

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {


                sharedPreferences = getSharedPreferences("datalogin", MODE_PRIVATE);
                String taikhoan = sharedPreferences.getString("Phone", "");


                if (!taikhoan.equals(""))
                {
                    Intent intent = new Intent(OpenApp.this, Home.class);
                    startActivity(intent);
                }
                else
                {
                    Intent intent = new Intent(OpenApp.this, DangNhap.class);
                    startActivity(intent);
                }

                finish();
            }
        }, 3000);
    }
}