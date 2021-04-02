package com.example.goodfood;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import pl.droidsonroids.gif.GifImageView;

public class TaiKhoan extends AppCompatActivity {

    private ImageView btn_close_profile;
    private GifImageView img_thongbao_donhang;
    private CardView btn_TTTK, btn_CSA, btn_ViVoucher, btn_LSDH;
    private TextView txt_tenUser, txt_member_type, txt_sldonhang;

    SharedPreferences sharedPreferences;
    private FirebaseFirestore database;
    int sodon = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tai_khoan);

        database = FirebaseFirestore.getInstance();
        sharedPreferences = getSharedPreferences("datalogin", MODE_PRIVATE);
        String taikhoan = sharedPreferences.getString("Phone", "");
        final int SoBillDangThucHien = sharedPreferences.getInt("SoBillDangThucHien", 0);

        img_thongbao_donhang = findViewById(R.id.img_thongbao_donhang);
        txt_sldonhang = findViewById(R.id.txt_sldonhang);
//        if (SoBillDangThucHien > 0)
//        {
//            img_thongbao_donhang.setVisibility(View.VISIBLE);
//            txt_sldonhang.setVisibility(View.VISIBLE);
//            txt_sldonhang.setText(String.valueOf(SoBillDangThucHien));
//        }

        txt_tenUser = findViewById(R.id.txt_tenUser);
        txt_member_type =  findViewById(R.id.txt_member_type);
        database.collection("Users").document(taikhoan).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful())
                {
                    DocumentSnapshot document = task.getResult();
                    Object[] Users_key = (Object[]) document.getData().keySet().toArray();
                    Object[] Users_value = (Object[]) document.getData().values().toArray();
                    for (int i = 0; i < Users_key.length; i++)
                    {
                        if (Users_key[i].toString().equals("name")) txt_tenUser.setText(Users_value[i].toString());
                        if (Users_key[i].toString().equals("type_member")) txt_member_type.setText(Users_value[i].toString());
                    }
                }
                else
                {
                    Toast.makeText(TaiKhoan.this, "Lỗi kết nối !!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        final int[] count = {0};
        database.collection("Users").document(taikhoan).collection("Bill")
        .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }


                for (DocumentChange document : snapshots.getDocumentChanges())
                {
                    Object[] obj_key = (Object[]) document.getDocument().getData().keySet().toArray();
                    Object[] obj_value = (Object[]) document.getDocument().getData().values().toArray();

                    for (int i = 0; i < obj_key.length; i++)
                    {
                        if (obj_key[i].toString().equals("tinh_trang"))
                        {
                            if (!obj_value[i].toString().equals("Hoàn thành"))
                            {
                                count[0]++;
                            }
                        }
                    }
                }
                sodon = count[0];
                if (sodon > 0)
                {
                    img_thongbao_donhang.setVisibility(View.VISIBLE);
                    txt_sldonhang.setVisibility(View.VISIBLE);
                    txt_sldonhang.setText(String.valueOf(sodon));
                }
                else
                {
                    img_thongbao_donhang.setVisibility(View.INVISIBLE);
                    txt_sldonhang.setVisibility(View.INVISIBLE);
                }
                count[0] = 0;
            }
        });




        btn_close_profile = findViewById(R.id.btn_close_profile);
        btn_TTTK = findViewById(R.id.btn_TTTK);
        btn_CSA = findViewById(R.id.btn_CSA);
        btn_ViVoucher = findViewById(R.id.btn_ViVoucher);
        btn_LSDH = findViewById(R.id.btn_LSDH);

        btn_close_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btn_TTTK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TaiKhoan.this, ThongTinTaiKhoan.class);
                startActivityForResult(intent, 1);
            }
        });

        btn_CSA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TaiKhoan.this, ChinhSachApp.class);
                startActivity(intent);
            }
        });

        btn_ViVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TaiKhoan.this, ViVoucher.class);
                startActivity(intent);
            }
        });

        btn_LSDH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TaiKhoan.this, LichSuDonHang.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                if (data != null)
                {
//                    int SoBillDangThucHien_update = sharedPreferences.getInt("SoBillDangThucHien", 0);
//
//                    img_thongbao_donhang = findViewById(R.id.img_thongbao_donhang);
//                    txt_sldonhang = findViewById(R.id.txt_sldonhang);
//                    if (SoBillDangThucHien_update > 0)
//                    {
//                        img_thongbao_donhang.setVisibility(View.VISIBLE);
//                        txt_sldonhang.setVisibility(View.VISIBLE);
//                        txt_sldonhang.setText(String.valueOf(SoBillDangThucHien_update));
//                    }
//                    else
//                    {
//                        img_thongbao_donhang.setVisibility(View.INVISIBLE);
//                        txt_sldonhang.setVisibility(View.INVISIBLE);
//                    }
                }
            }
        }

    }
}