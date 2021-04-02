package com.example.goodfood;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import pl.droidsonroids.gif.GifImageView;

public class ThongTinTaiKhoan extends AppCompatActivity {

    private ImageView btn_back, btn_logout, img_notshow_password_tttk;
    private GifImageView img_show_password_tttk;
    private TextView txt_diachi_tttk;
    private EditText txt_SDT_tttk, txt_matkhau_tttk, txt_hoten_tttk, txt_email_tttk;
    private TextView txt_tieude_acti;
    private Button btn_capnhat_tttk;

    SharedPreferences sharedPreferences;
    private FirebaseFirestore database;

    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_tin_tai_khoan);

        database = FirebaseFirestore.getInstance();
        sharedPreferences = getSharedPreferences("datalogin", MODE_PRIVATE);
        final String taikhoan = sharedPreferences.getString("Phone", "");

        LayoutInflater inflater = LayoutInflater.from(ThongTinTaiKhoan.this);
        View viewalert = inflater.inflate(R.layout.alert_thongbaochung, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(ThongTinTaiKhoan.this).setView(viewalert).create();
        alertDialog.setCanceledOnTouchOutside(false);
        final TextView txt_loading_noti = viewalert.findViewById(R.id.txt_loading_noti);
        final TextView txt_noidung_noti = viewalert.findViewById(R.id.txt_noidung_noti);
        final GifImageView gif_loading_chung = viewalert.findViewById(R.id.gif_loading_chung);
        final Button btn_accept = viewalert.findViewById(R.id.btn_accept);
        final Button btn_not_accept = viewalert.findViewById(R.id.btn_not_accept);


        btn_back = findViewById(R.id.btn_back);
        btn_logout = findViewById(R.id.btn_logout);
        btn_logout.setVisibility(View.VISIBLE);
        btn_capnhat_tttk = findViewById(R.id.btn_capnhat_tttk);
        txt_tieude_acti = findViewById(R.id.txt_tieude_acti);
        txt_diachi_tttk = findViewById(R.id.txt_diachi_tttk);
        txt_SDT_tttk = findViewById(R.id.txt_SDT_tttk);
        txt_matkhau_tttk = findViewById(R.id.txt_matkhau_tttk);
        txt_hoten_tttk = findViewById(R.id.txt_hoten_tttk);
        txt_email_tttk = findViewById(R.id.txt_email_tttk);

        txt_tieude_acti.setText("Thông tin tài khoản");

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Boolean value = extras.getBoolean("focus_address");
            if (value)
            {
                txt_diachi_tttk.requestFocus();
            }
        }

        database.collection("Users").document(taikhoan).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful())
                {
                    DocumentSnapshot document = task.getResult();
                    Object[] Users_key = (Object[]) document.getData().keySet().toArray();
                    Object[] Users_value = (Object[]) document.getData().values().toArray();
                    txt_SDT_tttk.setText(taikhoan);
                    for (int i = 0; i < Users_key.length; i++)
                    {
                        if (Users_key[i].toString().equals("name")) txt_hoten_tttk.setText(Users_value[i].toString());
                        if (Users_key[i].toString().equals("email")) txt_email_tttk.setText(Users_value[i].toString());
                        if (Users_key[i].toString().equals("password")) txt_matkhau_tttk.setText(Users_value[i].toString());
                        if (Users_key[i].toString().equals("address")) txt_diachi_tttk.setText(Users_value[i].toString());
                    }
                }
                else
                {
                    Toast.makeText(ThongTinTaiKhoan.this, "Lỗi kết nối !!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt_loading_noti.setText("THÔNG BÁO");
                txt_noidung_noti.setText("Bạn muốn đăng xuất khỏi GoodFood ?");
                btn_not_accept.setVisibility(View.VISIBLE);
                btn_accept.setVisibility(View.VISIBLE);
                alertDialog.show();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                btn_accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        Check_input(gif_loading_chung, R.drawable.ic_success_loading, txt_loading_noti, txt_noidung_noti,alertDialog, "ĐĂNG XUẤT", "Hẹn gặp lại bạn nhé !");
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("Phone", "");
                        editor.commit();

//                        Intent resultIntent = new Intent();
//                        resultIntent.putExtra("clear",true);
//                        setResult(Activity.RESULT_OK, resultIntent);
//                        finish();


                        Intent intent = new Intent(ThongTinTaiKhoan.this, DangNhap.class);
                        intent.putExtra("new", true);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                });

                btn_not_accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                }
        });

        img_notshow_password_tttk = findViewById(R.id.img_notshow_password_tttk);
        img_notshow_password_tttk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img_notshow_password_tttk.setVisibility(View.INVISIBLE);
                img_show_password_tttk.setVisibility(View.VISIBLE);
                txt_matkhau_tttk.setInputType(InputType.TYPE_CLASS_TEXT);
            }
        });
        img_show_password_tttk = findViewById(R.id.img_show_password_tttk);
        img_show_password_tttk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img_show_password_tttk.setVisibility(View.INVISIBLE);
                img_notshow_password_tttk.setVisibility(View.VISIBLE);
                txt_matkhau_tttk.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
        });





        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btn_capnhat_tttk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_capnhat_tttk.setEnabled(false);

                gif_loading_chung.setImageResource(R.drawable.ic_loading_goodfood);
                txt_loading_noti.setText("LOADING");
                txt_noidung_noti.setText("Bạn vui lòng đợi xíu nhé");
                alertDialog.show();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                final String name = txt_hoten_tttk.getText().toString().trim();
                final String address = txt_diachi_tttk.getText().toString().trim();
                final String password = txt_matkhau_tttk.getText().toString().trim();
                final String email = txt_email_tttk.getText().toString().trim();
                final String correct_email_address = "@gmail.com";

                if (name.equals("") || address.equals("")  || password.equals(""))
                {
                    Check_input(gif_loading_chung, R.drawable.ic_fail_loading, txt_loading_noti, txt_noidung_noti,alertDialog, "THẤT BẠI", "Vui lòng nhập đầy đủ thông tin !");
                }
                else if (email.length() <= correct_email_address.length() || (!correct_email_address.equals(email.substring(email.length()-correct_email_address.length(), email.length()))))
                {
                    Check_input(gif_loading_chung, R.drawable.ic_fail_loading, txt_loading_noti, txt_noidung_noti, alertDialog, "THẤT BẠI", "Email chưa đúng định dạng, xin nhập lại !");
                }
                else
                {
                    Map<String, Object> user = new HashMap<>();
                    user.put("name", name);
                    user.put("email", email);
                    user.put("type_member", "Member");
                    user.put("address", address);
                    user.put("password", password);
                    user.put("link_avatar", "");

                    database.collection("Users").document(taikhoan).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                                gif_loading_chung.setImageResource(R.drawable.ic_success_loading);
                            txt_loading_noti.setText("THÀNH CÔNG");
                            txt_noidung_noti.setText("Dữ liệu đã được cập nhật");
                            timer = new Timer();
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    alertDialog.dismiss();
                                }
                            }, 1000);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            String error = e.getMessage();
                            Toast.makeText(ThongTinTaiKhoan.this, "Lỗi kết nối !!!" + error, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                btn_capnhat_tttk.setEnabled(true);
            }
        });

        txt_SDT_tttk.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String value = txt_SDT_tttk.getText().toString().trim();

            }
        });
    }

    void Check_input(GifImageView gifImageView, int hinh, TextView txt_noti, TextView txt_noidung_noti, final AlertDialog alertDialog, String noti, String noidung_noti)
    {
        gifImageView.setImageResource(hinh);
        txt_noti.setText(noti);
        txt_noidung_noti.setText(noidung_noti);
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                alertDialog.dismiss();
            }
        }, 1400);
    }


}