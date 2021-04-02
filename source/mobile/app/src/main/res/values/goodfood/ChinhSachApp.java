package com.example.goodfood;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class ChinhSachApp extends AppCompatActivity {
    private ImageView btn_back_TTTK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chinh_sach_app);

        btn_back_TTTK = findViewById(R.id.btn_back_TTTK);

        btn_back_TTTK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}