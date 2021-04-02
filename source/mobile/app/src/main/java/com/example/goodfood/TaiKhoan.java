package com.example.goodfood;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import pl.droidsonroids.gif.GifImageView;

public class TaiKhoan extends AppCompatActivity {

    private ImageView btn_close_profile;
    private GifImageView img_thongbao_donhang, img_loading_avatar;
    private CardView btn_TTTK, btn_CSA, btn_ViVoucher, btn_LSDH, btn_TichDiem, btn_HTTT;
    private TextView txt_tenUser, txt_member_type, txt_sldonhang;
    private CircularImageView img_avatar;

    SharedPreferences sharedPreferences;
    private FirebaseFirestore database;
    private StorageReference mStorageRef;
    int sodon = 0;
    String link_avatar = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tai_khoan);

        database = FirebaseFirestore.getInstance();
        sharedPreferences = getSharedPreferences("datalogin", MODE_PRIVATE);
        String taikhoan = sharedPreferences.getString("Phone", "");

        img_thongbao_donhang = findViewById(R.id.img_thongbao_donhang);
        txt_sldonhang = findViewById(R.id.txt_sldonhang);

        img_loading_avatar = findViewById(R.id.img_loading_avatar);
        img_avatar = findViewById(R.id.img_avatar);
        txt_tenUser = findViewById(R.id.txt_tenUser);
        txt_member_type =  findViewById(R.id.txt_member_type);


        final DocumentReference docRef = database.collection("Users").document(taikhoan);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {

                }

                if (snapshot != null && snapshot.exists()) {
                    Object[] Users_key = (Object[]) snapshot.getData().keySet().toArray();
                    Object[] Users_value = (Object[]) snapshot.getData().values().toArray();

                    for (int i = 0; i < Users_key.length; i++)
                    {
                        if (Users_key[i].toString().equals("name")) txt_tenUser.setText(Users_value[i].toString());
                        if (Users_key[i].toString().equals("type_member")) txt_member_type.setText(Users_value[i].toString());
                        if (Users_key[i].toString().equals("link_avatar"))
                        {
                            link_avatar = Users_value[i].toString();
                            if (!link_avatar.equals(""))
                            {
                                mStorageRef = FirebaseStorage.getInstance().getReference().child("users_pic/" + link_avatar);
                                try
                                {
                                    final File file_local = File.createTempFile("avatar", link_avatar.substring(link_avatar.length()-3));
                                    mStorageRef.getFile(file_local).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                            Bitmap bitmap = BitmapFactory.decodeFile(file_local.getAbsolutePath());
                                            img_avatar.setImageBitmap(bitmap);
                                            img_loading_avatar.setVisibility(View.INVISIBLE);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                        }
                                    });
                                }
                                catch (Exception ex)
                                {
                                    Toast.makeText(TaiKhoan.this, ex.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
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
        btn_TichDiem = findViewById(R.id.btn_TichDiem);
        btn_HTTT = findViewById(R.id.btn_HTTT);

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

        btn_TichDiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TaiKhoan.this, TichDiem.class);
                startActivity(intent);
            }
        });

        btn_HTTT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TaiKhoan.this, ThongTinHoTro.class);
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
                    boolean avatar_change = data.getBooleanExtra("Avatar_change", false);
                    if (avatar_change)
                    {
                        mStorageRef = FirebaseStorage.getInstance().getReference().child("users_pic/" + link_avatar);
                        try
                        {
                            final File file_local = File.createTempFile("avatar", link_avatar.substring(link_avatar.length()-3));
                            mStorageRef.getFile(file_local).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    Bitmap bitmap = BitmapFactory.decodeFile(file_local.getAbsolutePath());
                                    img_avatar.setImageBitmap(bitmap);
                                    img_loading_avatar.setVisibility(View.INVISIBLE);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });
                        }
                        catch (Exception ex)
                        {
                            Toast.makeText(TaiKhoan.this, ex.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        }

    }
}