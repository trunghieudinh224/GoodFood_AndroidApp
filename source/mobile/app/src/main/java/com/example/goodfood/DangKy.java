package com.example.goodfood;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import pl.droidsonroids.gif.GifImageView;


public class DangKy extends AppCompatActivity {

    private EditText txtPhone, txtName, txtDiaChi, txtPassword, txtConfirmPassword;
    private ImageView img_notshow_password_dangky, img_notshow_confirm_password_dangky;
    private GifImageView img_show_password_dangky, img_show_confirm_password_dangky;
    private Button btnRegister;

    private FirebaseFirestore db;
    Timer timer;
    boolean show_pass = true;
    boolean show_confirm = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky);

        db = FirebaseFirestore.getInstance();

        txtPhone = findViewById(R.id.txtPhone);
        txtName = findViewById(R.id.txtName);
        txtDiaChi = findViewById(R.id.txtDiaChi);
        txtPassword = findViewById(R.id.txtPassword);
        txtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        txtConfirmPassword = findViewById(R.id.txtConfirmPassword);
        txtConfirmPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        btnRegister = findViewById(R.id.btnRegister);

        img_notshow_password_dangky = findViewById(R.id.img_notshow_password_dangky);
        img_notshow_password_dangky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show_pass = true;
                img_notshow_password_dangky.setVisibility(View.INVISIBLE);
                img_show_password_dangky.setVisibility(View.VISIBLE);
                txtPassword.setInputType(InputType.TYPE_CLASS_TEXT);
            }
        });
        img_show_password_dangky = findViewById(R.id.img_show_password_dangky);
        img_show_password_dangky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                img_show_password_dangky.setVisibility(View.INVISIBLE);
                img_notshow_password_dangky.setVisibility(View.VISIBLE);
                txtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
        });

        img_notshow_confirm_password_dangky = findViewById(R.id.img_notshow_confirm_password_dangky);
        img_notshow_confirm_password_dangky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img_notshow_confirm_password_dangky.setVisibility(View.INVISIBLE);
                img_show_confirm_password_dangky.setVisibility(View.VISIBLE);
                txtConfirmPassword.setInputType(InputType.TYPE_CLASS_TEXT);
            }
        });
        img_show_confirm_password_dangky = findViewById(R.id.img_show_confirm_password_dangky);
        img_show_confirm_password_dangky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img_show_confirm_password_dangky.setVisibility(View.INVISIBLE);
                img_notshow_confirm_password_dangky.setVisibility(View.VISIBLE);
                txtConfirmPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
        });



        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String Phone = txtPhone.getText().toString().trim();
                final String Name = txtName.getText().toString().trim();
                final String DiaChi = txtDiaChi.getText().toString().trim();
                final String Password = txtPassword.getText().toString().trim();
                String ConfirmPassword = txtConfirmPassword.getText().toString().trim();

                if (Phone.equals("") || Name.equals("") || DiaChi.equals("") || Password.equals("") || ConfirmPassword.equals(""))
                {
                    alert_show("Vui lòng nhập đầy đủ thông tin");
                    return;
                }
                else if (Password.length() <6 || ConfirmPassword.length() < 6)
                {
                    alert_show("Mật khẩu phải có ít nhất 6 kí tự");
                    return;
                }
                else if (!Password.equals(ConfirmPassword))
                {
                    alert_show("Vui lòng kiểm tra lại mật khẩu");
                    return;
                }
                else
                {
                    LayoutInflater inflater = LayoutInflater.from(DangKy.this);
                    View viewalert = inflater.inflate(R.layout.alert_thongbaochung, null);
                    final AlertDialog alertDialog = new AlertDialog.Builder(DangKy.this).setView(viewalert).create();
                    final TextView txt_loading_noti = viewalert.findViewById(R.id.txt_loading_noti);
                    txt_loading_noti.setText("LOADING");
                    final TextView txt_noidung_noti = viewalert.findViewById(R.id.txt_noidung_noti);
                    txt_noidung_noti.setText("GoodFood đang kiểm tra dữ liệu");
                    final GifImageView gif_loading_chung = viewalert.findViewById(R.id.gif_loading_chung);

                    alertDialog.show();
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    btnRegister.setEnabled(false);
                    DocumentReference docRef = db.collection("Users").document(Phone);
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    gif_loading_chung.setImageResource(R.drawable.ic_fail_loading);
                                    txt_loading_noti.setText("THẤT BẠI");
                                    txt_noidung_noti.setText("Tài khoản đã tồn tại, vui lòng nhập lại!");
                                    timer = new Timer();
                                    timer.schedule(new TimerTask() {
                                        @Override
                                        public void run() {
                                            alertDialog.dismiss();
                                        }
                                    }, 1400);
                                }
                                else
                                {
                                    Date currentTime = Calendar.getInstance().getTime();
                                    final SimpleDateFormat dateFormat_full_var = new SimpleDateFormat("MM", Locale.getDefault());
                                    final String thang_hientai = dateFormat_full_var.format(currentTime);
                                    Map<String, Object> user = new HashMap<>();
                                    user.put("name", Name);
                                    user.put("email", "");
                                    user.put("address", DiaChi);
                                    user.put("password", Password);
                                    user.put("link_avatar", "avatar_default.png");
                                    user.put("type_member", "Member");
                                    user.put("so_don_dat_hang", "0");
                                    user.put("so_don_tich_luy", "0");
                                    user.put("tich_luy_thang", thang_hientai);
                                    user.put("so_voucher_tich_luy", "0");

                                    db.collection("Users").document(Phone).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Map<String, Object> fv_food = new HashMap<>();
                                            String[] list = {"Cà phê chồn", "Cà phê sữa thượng hạng", "Cơm gà xối mỡ"};
                                            for (int i = 0; i < list.length ; i++)
                                            {
                                                db.collection("Users").document(Phone).collection("fv_food").document(list[i]).set(fv_food).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @SuppressLint("ResourceType")
                                                    @Override
                                                    public void onSuccess(Void aVoid) {

                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(DangKy.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }


                                            gif_loading_chung.setImageResource(R.drawable.ic_success_loading);
                                            txt_loading_noti.setText("THÀNH CÔNG");
                                            txt_noidung_noti.setText("GoodFood chào mừng bạn");
                                            timer = new Timer();
                                            timer.schedule(new TimerTask() {
                                                @Override
                                                public void run() {
                                                    alertDialog.dismiss();
                                                    Intent resultIntent = new Intent();
                                                    resultIntent.putExtra("Phone",Phone);
                                                    resultIntent.putExtra("Password",Password);
                                                    setResult(Activity.RESULT_OK, resultIntent);
                                                    finish();
                                                }
                                            }, 1000);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            String error = e.getMessage();
                                            Toast.makeText(DangKy.this, "Error:" + error, Toast.LENGTH_SHORT).show();
                                        }
                                    });


                                }
                            }
                            else {
                                Toast.makeText(DangKy.this, "Lỗi kết nối !!!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    btnRegister.setEnabled(true);
                }
            }
        });
    }

    void alert_show(final String noti)
    {
        LayoutInflater inflater = LayoutInflater.from(DangKy.this);
        View viewalert = inflater.inflate(R.layout.alert_thongbaochung, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(DangKy.this).setView(viewalert).create();
        final TextView txt_loading_noti = viewalert.findViewById(R.id.txt_loading_noti);
        final GifImageView gif_loading_chung = viewalert.findViewById(R.id.gif_loading_chung);
        txt_loading_noti.setText("THẤT BẠI");
        final TextView txt_noidung_noti = viewalert.findViewById(R.id.txt_noidung_noti);
        txt_noidung_noti.setText(noti);
        gif_loading_chung.setImageResource(R.drawable.ic_error_loading);

        alertDialog.show();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                alertDialog.dismiss();
            }
        }, 1500);
    }
}
