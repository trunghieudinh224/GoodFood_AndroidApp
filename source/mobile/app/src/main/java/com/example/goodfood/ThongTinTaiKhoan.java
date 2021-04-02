package com.example.goodfood;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import pl.droidsonroids.gif.GifImageView;

public class ThongTinTaiKhoan extends AppCompatActivity {

    private ImageView btn_back, btn_logout, img_notshow_password_tttk;
    private GifImageView img_show_password_tttk, img_loading_avatar;
    private TextView txt_diachi_tttk;
    private EditText txt_SDT_tttk, txt_matkhau_tttk, txt_hoten_tttk, txt_email_tttk;
    private TextView txt_tieude_acti;
    private Button btn_capnhat_tttk;
    private ImageButton btn_upload_avatar;
    private CircularImageView img_avatar_TTTK;
    private final int CODE_IMG_GALLERY = 1;
    public Uri imgUri;
    private String taikhoan;
    private String link_hinh_ava = "";

    SharedPreferences sharedPreferences;
    private FirebaseFirestore database;
    private StorageReference mStorageRef;
    private StorageTask mUploadTask;

    LayoutInflater inflater;
    View viewalert;
    AlertDialog alertDialog;
    TextView txt_loading_noti, txt_noidung_noti;
    GifImageView gif_loading_chung;
    Button btn_accept, btn_not_accept;


    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_tin_tai_khoan);

        database = FirebaseFirestore.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference("users_pic");
        sharedPreferences = getSharedPreferences("datalogin", MODE_PRIVATE);
        taikhoan = sharedPreferences.getString("Phone", "");

        inflater = LayoutInflater.from(ThongTinTaiKhoan.this);
        viewalert = inflater.inflate(R.layout.alert_thongbaochung, null);
        alertDialog = new AlertDialog.Builder(ThongTinTaiKhoan.this).setView(viewalert).create();
        alertDialog.setCanceledOnTouchOutside(false);
        txt_loading_noti = viewalert.findViewById(R.id.txt_loading_noti);
        txt_noidung_noti = viewalert.findViewById(R.id.txt_noidung_noti);
        gif_loading_chung = viewalert.findViewById(R.id.gif_loading_chung);
        btn_accept = viewalert.findViewById(R.id.btn_accept);
        btn_not_accept = viewalert.findViewById(R.id.btn_not_accept);


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

        img_loading_avatar = findViewById(R.id.img_loading_avatar);
        img_avatar_TTTK = findViewById(R.id.img_avatar_TTTK);
        btn_upload_avatar = findViewById(R.id.btn_upload_avatar);
        btn_upload_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(Intent.createChooser(new Intent().setAction(Intent.ACTION_GET_CONTENT).setType("image/*"), "Ảnh"), CODE_IMG_GALLERY);
            }
        });

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
                        if (Users_key[i].toString().equals("link_avatar")) {
                            link_hinh_ava = Users_value[i].toString();
                            if (!link_hinh_ava.equals(""))
                            {
                                mStorageRef = FirebaseStorage.getInstance().getReference().child("users_pic/" + link_hinh_ava);
                                try
                                {
                                    final File file_local = File.createTempFile("avatar", link_hinh_ava.substring(link_hinh_ava.length()-3));
                                    mStorageRef.getFile(file_local).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                            Bitmap bitmap = BitmapFactory.decodeFile(file_local.getAbsolutePath());
                                            img_avatar_TTTK.setImageBitmap(bitmap);
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
                                    Toast.makeText(ThongTinTaiKhoan.this, "Lỗi khi admin xóa hình", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

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


                        Check_input(R.drawable.ic_success_loading,"ĐĂNG XUẤT", "Hẹn gặp lại bạn nhé !");
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
                Intent resultIntent = new Intent();
                resultIntent.putExtra("Avatar_change",true);
                setResult(Activity.RESULT_OK, resultIntent);
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

                if (name.equals("") || password.equals(""))
                {
                    Check_input(R.drawable.ic_fail_loading,"THẤT BẠI", "Vui lòng nhập đầy đủ thông tin !");
                }
                else if (!email.equals(""))
                {
                    if (email.length() <= correct_email_address.length() || (!correct_email_address.equals(email.substring(email.length()-correct_email_address.length(), email.length()))))
                    {
                        Check_input(R.drawable.ic_fail_loading,"THẤT BẠI", "Email chưa đúng định dạng, xin nhập lại !");
                    }
                }
                else
                {
                    update_info(name,email,address,password, taikhoan,gif_loading_chung,txt_loading_noti,txt_noidung_noti, alertDialog);
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

    void Check_input(int hinh, String noti, String noidung_noti)
    {
        gif_loading_chung.setImageResource(hinh);
        txt_loading_noti.setText(noti);
        txt_noidung_noti.setText(noidung_noti);
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                alertDialog.dismiss();
            }
        }, 1400);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CODE_IMG_GALLERY && resultCode == RESULT_OK)
        {
            imgUri = data.getData();
            if (imgUri != null)
            {
                img_avatar_TTTK.setImageURI(imgUri);
                img_loading_avatar.setVisibility(View.INVISIBLE);
                if (!link_hinh_ava.substring(0,taikhoan.length()).equals(taikhoan))
                {
                    link_hinh_ava = taikhoan + "_avatar" + "." + getFile(imgUri);
                    final DocumentReference DocRef = database.collection("Users").document(taikhoan);
                    database.runTransaction(new Transaction.Function<Void>() {
                        @Override
                        public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                            transaction.update(DocRef, "link_avatar", link_hinh_ava);
                            return null;
                        }
                    }).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ThongTinTaiKhoan.this, "Lỗi kết nối !!!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                upload_avatar();
            }
        }
    }

    private String getFile(Uri uri)
    {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }


    void upload_avatar()
    {
        StorageReference fileReference = FirebaseStorage.getInstance().getReference().child("users_pic/" + link_hinh_ava);
        mUploadTask = fileReference.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(ThongTinTaiKhoan.this, "Đã thay đổi ảnh đại diện", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ThongTinTaiKhoan.this, "Lỗi kết nối !!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void update_info(final String name, final String email, final String address, final String password, final String taikhoan, final GifImageView gif_loading_chung, final TextView txt_loading_noti, final TextView txt_noidung_noti, final AlertDialog alertDialog)
    {
        final DocumentReference DocRef = database.collection("Users").document(taikhoan);
        database.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                transaction.update(DocRef, "name", name);
                transaction.update(DocRef, "email", email);
                transaction.update(DocRef, "address", address);
                transaction.update(DocRef, "password", password);
                return null;
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
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
                }, 1500);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ThongTinTaiKhoan.this, "Lỗi kết nối !!!", Toast.LENGTH_SHORT).show();
            }
        });



    }

    @Override
    public void onBackPressed() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("Avatar_change",true);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}