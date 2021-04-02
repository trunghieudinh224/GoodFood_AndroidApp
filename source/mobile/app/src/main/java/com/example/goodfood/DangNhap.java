package com.example.goodfood;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import pl.droidsonroids.gif.GifImageView;


public class DangNhap extends AppCompatActivity {

    private Button btn_Login;
    private EditText txt_Phone, txt_Password;
    private TextView btn_Register;
    private ImageView img_notshow_password_dangnhap;
    private GifImageView img_show_password_dangnhap;
    private FirebaseFirestore database;
    Timer timer;

    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            boolean value = extras.getBoolean("new", false);
            if (value)
            {
                SystemClock.sleep(1500);
            }
        }

        sharedPreferences = getSharedPreferences("datalogin", MODE_PRIVATE);
        String taikhoan = sharedPreferences.getString("Phone", "");

        database = FirebaseFirestore.getInstance();

        btn_Login = findViewById(R.id.btn_Login);
        txt_Password = findViewById(R.id.txt_Password);
        txt_Password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        txt_Phone = findViewById(R.id.txt_Phone);
        btn_Register = findViewById(R.id.btn_Register);

        img_notshow_password_dangnhap = findViewById(R.id.img_notshow_password_dangnhap);
        img_notshow_password_dangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img_notshow_password_dangnhap.setVisibility(View.INVISIBLE);
                img_show_password_dangnhap.setVisibility(View.VISIBLE);
                txt_Password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            }
        });
        img_show_password_dangnhap = findViewById(R.id.img_show_password_dangnhap);
        img_show_password_dangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img_show_password_dangnhap.setVisibility(View.INVISIBLE);
                img_notshow_password_dangnhap.setVisibility(View.VISIBLE);
                txt_Password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
        });

        btn_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String Phone = txt_Phone.getText().toString().trim();
                final String Password = txt_Password.getText().toString().trim();

                if (Phone.equals(""))
                {
                    txt_Phone.requestFocus();
                    Toast.makeText(DangNhap.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                }
                else if (Password.equals(""))
                {
                    txt_Password.requestFocus();
                    Toast.makeText(DangNhap.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                }
                else if (Phone.length()<10)
                {
                    Toast.makeText(DangNhap.this, "Số điện thoại chưa đúng định dạng", Toast.LENGTH_SHORT).show();
                }
                else if (Password.length() < 6)
                {
                    Toast.makeText(DangNhap.this, "Mật khẩu phải có ít nhất 6 kí tự", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    LayoutInflater inflater = LayoutInflater.from(DangNhap.this);
                    View viewalert = inflater.inflate(R.layout.alert_thongbaochung, null);
                    final AlertDialog alertDialog = new AlertDialog.Builder(DangNhap.this).setView(viewalert).create();
                    final TextView txt_loading_noti = viewalert.findViewById(R.id.txt_loading_noti);
                    txt_loading_noti.setText("LOADING");
                    final TextView txt_noidung_noti = viewalert.findViewById(R.id.txt_noidung_noti);
                    txt_noidung_noti.setText("Bạn vui lòng đợi xíu nhé");
                    final GifImageView gif_loading_chung = viewalert.findViewById(R.id.gif_loading_chung);


                    alertDialog.show();
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    btn_Login.setEnabled(false);
                    database.collection("Users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful())
                            {
                                boolean success = false;
                                for (DocumentSnapshot document : task.getResult())
                                {
                                    Object[] obj_key = (Object[]) document.getData().keySet().toArray();
                                    Object[] obj_value = (Object[]) document.getData().values().toArray();
                                    for (int i = 0; i < obj_key.length; i++)
                                    {
                                        if (obj_key[i].toString().equals("password"))
                                        {
                                            if (document.getId().equals(Phone) && obj_value[i].toString().equals(Password))
                                            {
                                                success = true;
                                                gif_loading_chung.setImageResource(R.drawable.ic_success_loading);

                                                txt_loading_noti.setText("THÀNH CÔNG");
                                                txt_noidung_noti.setText("Chào mừng bạn trở lại với GoodFood");
                                                timer = new Timer();
                                                timer.schedule(new TimerTask() {
                                                    @Override
                                                    public void run() {
                                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                                        editor.putString("Phone", Phone);
                                                        editor.commit();
                                                        finish();

                                                        Intent intent = new Intent(DangNhap.this, Home.class);
                                                        startActivity(intent);
                                                    }
                                                }, 1000);
                                                break;
                                            }
                                        }
                                    }
                                }

                                if (success == false)
                                {
                                    gif_loading_chung.setImageResource(R.drawable.ic_fail_loading);
                                    txt_loading_noti.setText("THẤT BẠI");
                                    txt_noidung_noti.setText("Vui lòng đăng nhập lại");
                                    timer = new Timer();
                                    timer.schedule(new TimerTask() {
                                        @Override
                                        public void run() {
                                            alertDialog.dismiss();
                                        }
                                    }, 1400);

                                }
                                btn_Login.setEnabled(true);
                            }
                            else
                            {
                                Toast.makeText(DangNhap.this, "Lỗi kết nối !!!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                }
            }
        });

        btn_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt_Phone.setText("");
                txt_Password.setText("");

                Intent intent = new Intent(DangNhap.this, DangKy.class);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                if (data.getExtras() != null) {
                    String phone = extras.getString("Phone");
                    txt_Phone.setText(phone);
                    String password = extras.getString("Password");
                    txt_Password.setText(password);
                }
            }
        }
    }

    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Chạm lần nữa để thoát", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}