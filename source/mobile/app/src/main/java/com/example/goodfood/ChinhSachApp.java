package com.example.goodfood;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ChinhSachApp extends AppCompatActivity {
    private ImageView btn_back;
    private TextView txt_tieude_acti;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chinh_sach_app);

        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        txt_tieude_acti = findViewById(R.id.txt_tieude_acti);
        txt_tieude_acti.setText("Chính sách App");

    }
}