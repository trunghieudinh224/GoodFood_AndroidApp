package com.example.goodfood;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
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

import java.util.HashMap;
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
                    alert_show("Vui l??ng nh???p ?????y ????? th??ng tin");
                    return;
                }
                else if (Password.length() <6 || ConfirmPassword.length() < 6)
                {
                    alert_show("M???t kh???u ph???i c?? ??t nh???t 6 k?? t???");
                    return;
                }
                else if (!Password.equals(ConfirmPassword))
                {
                    alert_show("M???t kh???u ph???i c?? ??t nh???t 6 k?? t???");
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
                    txt_noidung_noti.setText("GoodFood ??ang ki???m tra d??? li???u");
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
                                    txt_loading_noti.setText("TH???T B???I");
                                    txt_noidung_noti.setText("T??i kho???n ???? t???n t???i, vui l??ng nh???p l???i!");
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
                                    Map<String, Object> user = new HashMap<>();
                                    user.put("name", Name);
                                    user.put("email", "");
                                    user.put("address", DiaChi);
                                    user.put("password", Password);
                                    user.put("link_avatar", "");
                                    user.put("type_member", "Member");

                                    db.collection("Users").document(Phone).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            gif_loading_chung.setImageResource(R.drawable.ic_success_loading);
                                            txt_loading_noti.setText("TH??NH C??NG");
                                            txt_noidung_noti.setText("GoodFood ch??o m???ng b???n");
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
                                Toast.makeText(DangKy.this, "L???i k???t n???i !!!", Toast.LENGTH_SHORT).show();
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
        txt_loading_noti.setText(noti);
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
