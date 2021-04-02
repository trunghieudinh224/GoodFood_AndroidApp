package com.example.goodfood;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.type.DateTime;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import pl.droidsonroids.gif.GifImageView;

public class TichDiem extends AppCompatActivity {

    private ImageView btn_back, ic_step_array_tichdiem1, ic_step_array_tichdiem2, ic_step_array_tichdiem3, ic_step_array_tichdiem4, img_gift_not_open;
    private TextView txt_tieude_acti,txt_sodon_tichluy,txt_step1,txt_step2,txt_step3,txt_step4,txt_step5;
    private CircularImageView img_step1,img_step2,img_step3,img_step4,img_step5;
    private GifImageView gif_position1,gif_position2,gif_position3,gif_position4,gif_position5, img_gift_open, img_loading_gift;
    private LinearLayout area_gift;

    SharedPreferences sharedPreferences;
    private FirebaseFirestore database;
    private StorageReference mStorageRef;

    private String taikhoan = "";
    int so_don_dat_hang = 0; int so_don_tich_luy = 0; String tich_luy_thang = ""; String so_voucher_tich_luy = ""; String link_avatar = "";
    Bitmap bitmap_link_avatar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tich_diem);

        database = FirebaseFirestore.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference("users_pic");
        sharedPreferences = getSharedPreferences("datalogin", MODE_PRIVATE);
        taikhoan = sharedPreferences.getString("Phone", "");

        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        txt_tieude_acti = findViewById(R.id.txt_tieude_acti);
        txt_tieude_acti.setText("Tích điểm ");

        img_loading_gift = findViewById(R.id.img_loading_gift);
        area_gift = findViewById(R.id.area_gift);

        txt_sodon_tichluy = findViewById(R.id.txt_sodon_tichluy);
        img_step1 = findViewById(R.id.img_step1);
        img_step2 = findViewById(R.id.img_step2);
        img_step3 = findViewById(R.id.img_step3);
        img_step4 = findViewById(R.id.img_step4);
        img_step5 = findViewById(R.id.img_step5);

        txt_step1 = findViewById(R.id.txt_step1);
        txt_step2 = findViewById(R.id.txt_step2);
        txt_step3 = findViewById(R.id.txt_step3);
        txt_step4 = findViewById(R.id.txt_step4);
        txt_step5 = findViewById(R.id.txt_step5);

        ic_step_array_tichdiem1 = findViewById(R.id.ic_step_array_tichdiem1);
        ic_step_array_tichdiem2 = findViewById(R.id.ic_step_array_tichdiem2);
        ic_step_array_tichdiem3 = findViewById(R.id.ic_step_array_tichdiem3);
        ic_step_array_tichdiem4 = findViewById(R.id.ic_step_array_tichdiem4);

        gif_position1 = findViewById(R.id.gif_position1);
        gif_position2 = findViewById(R.id.gif_position2);
        gif_position3 = findViewById(R.id.gif_position3);
        gif_position4 = findViewById(R.id.gif_position4);
        gif_position5 = findViewById(R.id.gif_position5);


        LayoutInflater inflater = LayoutInflater.from(TichDiem.this);
        View viewalert = inflater.inflate(R.layout.alert_tichdiem, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(TichDiem.this).setView(viewalert).create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setCanceledOnTouchOutside(false);
        final RelativeLayout area_info_gift = viewalert.findViewById(R.id.area_info_gift);
        final Button btn_nhanvoucher = viewalert.findViewById(R.id.btn_nhanvoucher);


        btn_nhanvoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });


        img_gift_not_open = findViewById(R.id.img_gift_not_open);
        img_gift_open = findViewById(R.id.img_gift_open);
        img_gift_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DocumentReference DocRef = database.collection("Users").document(taikhoan);
                database.runTransaction(new Transaction.Function<Void>() {
                    @Override
                    public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                        DocumentSnapshot snapshot = transaction.get(DocRef);

                        transaction.update(DocRef, "so_don_tich_luy", "0");

                        int new_so_voucher_tich_luy = Integer.parseInt(snapshot.getString("so_voucher_tich_luy")) + 1;
                        transaction.update(DocRef, "so_voucher_tich_luy", String.valueOf(new_so_voucher_tich_luy));
                        return null;
                    }
                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        img_gift_open.setVisibility(View.INVISIBLE);
                        img_gift_not_open.setVisibility(View.VISIBLE);
                        gif_position5.setVisibility(View.INVISIBLE);
                        alertDialog.show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(TichDiem.this, "Lỗi kết nối !!!", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });



        final DocumentReference docRef = database.collection("Users").document(taikhoan);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {

                Object[] obj_key = (Object[]) snapshot.getData().keySet().toArray();
                Object[] obj_value = (Object[]) snapshot.getData().values().toArray();
                for (int i = 0; i < obj_key.length; i++)
                {
                    if (obj_key[i].toString().equals("so_don_dat_hang")) so_don_dat_hang = Integer.parseInt(obj_value[i].toString());
                    if (obj_key[i].toString().equals("so_don_tich_luy")) so_don_tich_luy = Integer.parseInt(obj_value[i].toString());
                    if (obj_key[i].toString().equals("tich_luy_thang")) tich_luy_thang = obj_value[i].toString();
                    if (obj_key[i].toString().equals("so_voucher_tich_luy")) so_voucher_tich_luy = obj_value[i].toString();
                    if (obj_key[i].toString().equals("link_avatar"))
                    {
                        link_avatar = obj_value[i].toString();
                    }
                }
                if (!link_avatar.equals(""))
                {
                    mStorageRef = FirebaseStorage.getInstance().getReference().child("users_pic/" + link_avatar);
                    try
                    {
                        final File file_local = File.createTempFile("avatar", link_avatar.substring(link_avatar.length()-3));
                        mStorageRef.getFile(file_local).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                bitmap_link_avatar = BitmapFactory.decodeFile(file_local.getAbsolutePath());
                                show_step();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(TichDiem.this, "cc", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    catch (Exception ex)
                    {
                        Toast.makeText(TichDiem.this, ex.toString(), Toast.LENGTH_SHORT).show();
                    }
                }

                txt_sodon_tichluy.setText(String.valueOf(so_don_tich_luy) +"/5");

            }
        });
    }

    private void show_step()
    {
        Date currentTime = Calendar.getInstance().getTime();
        final SimpleDateFormat dateFormat_full_var = new SimpleDateFormat("MM", Locale.getDefault());
        final String thang_hientai = dateFormat_full_var.format(currentTime);
        if (!tich_luy_thang.equals(thang_hientai))
        {
            final DocumentReference DocRef = database.collection("Users").document(taikhoan);
            database.runTransaction(new Transaction.Function<Void>() {
                @Override
                public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                    transaction.update(DocRef, "tich_luy_thang", thang_hientai);
                    transaction.update(DocRef, "so_don_tich_luy", "0");
                    return null;
                }
            }).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    show_gray_img(img_step1,img_step2,img_step3,img_step4,img_step5);
                    show_txt_step(txt_step1,txt_step2,txt_step3,txt_step4,txt_step5);
                    show_gray_array(ic_step_array_tichdiem1, ic_step_array_tichdiem2, ic_step_array_tichdiem3, ic_step_array_tichdiem4);
                    img_loading_gift.setVisibility(View.INVISIBLE);
                    area_gift.setVisibility(View.VISIBLE);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(TichDiem.this, "cập nhật tháng thất bại", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else
        {
            if (so_don_tich_luy == 0)
            {
                show_gray_img(img_step1,img_step2,img_step3,img_step4,img_step5);
                show_txt_step(txt_step1,txt_step2,txt_step3,txt_step4,txt_step5);
                show_gray_array(ic_step_array_tichdiem1, ic_step_array_tichdiem2, ic_step_array_tichdiem3, ic_step_array_tichdiem4);
            }
            else if (so_don_tich_luy == 1)
            {
                show_gray_img(img_step2,img_step2,img_step3,img_step4,img_step5);
                show_txt_step(txt_step2,txt_step2,txt_step3,txt_step4,txt_step5);
                hide_txt_step(txt_step1,txt_step1,txt_step1,txt_step1,txt_step1);
                show_gray_array(ic_step_array_tichdiem1, ic_step_array_tichdiem2, ic_step_array_tichdiem3, ic_step_array_tichdiem4);
                show_finish_img(gif_position1,gif_position2,gif_position3,gif_position4,gif_position5,img_step1,img_step1,img_step1,img_step1,img_step1);
            }
            else if (so_don_tich_luy == 2)
            {
                show_gray_img(img_step3,img_step3,img_step3,img_step4,img_step5);
                show_txt_step(txt_step3,txt_step3,txt_step3,txt_step4,txt_step5);
                hide_txt_step(txt_step1,txt_step2,txt_step2,txt_step2,txt_step2);
                show_gray_array(ic_step_array_tichdiem2, ic_step_array_tichdiem2, ic_step_array_tichdiem3, ic_step_array_tichdiem4);
                show_finish_array(ic_step_array_tichdiem1, ic_step_array_tichdiem1, ic_step_array_tichdiem1, ic_step_array_tichdiem1);
                show_finish_img(gif_position2,gif_position1,gif_position3,gif_position4,gif_position5,img_step1,img_step2,img_step2,img_step2,img_step2);
            }
            else if (so_don_tich_luy == 3)
            {
                show_gray_img(img_step4,img_step4,img_step4,img_step4,img_step5);
                show_txt_step(txt_step4,txt_step4,txt_step4,txt_step4,txt_step5);
                hide_txt_step(txt_step1,txt_step2,txt_step3,txt_step3,txt_step3);
                show_gray_array(ic_step_array_tichdiem3, ic_step_array_tichdiem3, ic_step_array_tichdiem3, ic_step_array_tichdiem4);
                show_finish_array(ic_step_array_tichdiem1, ic_step_array_tichdiem2, ic_step_array_tichdiem2, ic_step_array_tichdiem2);
                show_finish_img(gif_position3,gif_position1,gif_position2,gif_position4,gif_position5,img_step1,img_step2,img_step3,img_step3,img_step3);
            }
            else if (so_don_tich_luy == 4)
            {
                show_gray_img(img_step5,img_step5,img_step5,img_step5,img_step5);
                show_txt_step(txt_step4,txt_step4,txt_step4,txt_step4,txt_step4);
                hide_txt_step(txt_step1,txt_step2,txt_step3,txt_step4,txt_step4);
                show_gray_array(ic_step_array_tichdiem4, ic_step_array_tichdiem4, ic_step_array_tichdiem4, ic_step_array_tichdiem4);
                show_finish_array(ic_step_array_tichdiem1, ic_step_array_tichdiem2, ic_step_array_tichdiem3, ic_step_array_tichdiem3);
                show_finish_img(gif_position4,gif_position1,gif_position2,gif_position3,gif_position5,img_step1,img_step2,img_step3,img_step4,img_step4);
            }
            else if (so_don_tich_luy >= 5)
            {
                hide_txt_step(txt_step1,txt_step2,txt_step3,txt_step4,txt_step5);
                show_finish_array(ic_step_array_tichdiem1, ic_step_array_tichdiem2, ic_step_array_tichdiem3, ic_step_array_tichdiem4);
                show_finish_img(gif_position5,gif_position1,gif_position2,gif_position3,gif_position4,img_step1,img_step2,img_step3,img_step4,img_step5);
                img_gift_open.setVisibility(View.VISIBLE);
                img_gift_not_open.setVisibility(View.INVISIBLE);
            }

            img_loading_gift.setVisibility(View.INVISIBLE);
            area_gift.setVisibility(View.VISIBLE);
        }
    }

    void show_finish_img(GifImageView gif_position1, GifImageView gif_position2, GifImageView gif_position3, GifImageView gif_position4, GifImageView gif_position5, CircularImageView img1, CircularImageView img2, CircularImageView img3, CircularImageView img4, CircularImageView img5)
    {
        gif_position1.setVisibility(View.VISIBLE);
        gif_position2.setVisibility(View.INVISIBLE);
        gif_position3.setVisibility(View.INVISIBLE);
        gif_position4.setVisibility(View.INVISIBLE);
        gif_position5.setVisibility(View.INVISIBLE);

        img1.setImageBitmap(bitmap_link_avatar); img1.setBorderWidth(0);
        img2.setImageBitmap(bitmap_link_avatar); img2.setBorderWidth(0);
        img3.setImageBitmap(bitmap_link_avatar); img3.setBorderWidth(0);
        img4.setImageBitmap(bitmap_link_avatar); img4.setBorderWidth(0);
        img5.setImageBitmap(bitmap_link_avatar); img5.setBorderWidth(0);
    }

    void show_gray_img(CircularImageView img1, CircularImageView img2, CircularImageView img3, CircularImageView img4, CircularImageView img5)
    {
        img1.setImageResource(R.drawable.ic_step_notfinish); img1.setBorderWidth(4);
        img2.setImageResource(R.drawable.ic_step_notfinish); img2.setBorderWidth(4);
        img3.setImageResource(R.drawable.ic_step_notfinish); img3.setBorderWidth(4);
        img4.setImageResource(R.drawable.ic_step_notfinish); img4.setBorderWidth(4);
        img5.setImageResource(R.drawable.ic_step_notfinish); img5.setBorderWidth(4);
    }

    void show_txt_step(TextView txt_step1, TextView txt_step2, TextView txt_step3, TextView txt_step4, TextView txt_step5)
    {
        txt_step1.setVisibility(View.VISIBLE);
        txt_step2.setVisibility(View.VISIBLE);
        txt_step3.setVisibility(View.VISIBLE);
        txt_step4.setVisibility(View.VISIBLE);
        txt_step5.setVisibility(View.VISIBLE);
    }
    void hide_txt_step(TextView txt_step1, TextView txt_step2, TextView txt_step3, TextView txt_step4, TextView txt_step5)
    {
        txt_step1.setVisibility(View.INVISIBLE);
        txt_step2.setVisibility(View.INVISIBLE);
        txt_step3.setVisibility(View.INVISIBLE);
        txt_step4.setVisibility(View.INVISIBLE);
        txt_step5.setVisibility(View.INVISIBLE);
    }


    void show_gray_array(ImageView img1, ImageView img2, ImageView img3, ImageView img4)
    {
        img1.setImageResource(R.drawable.ic_step_tichdiem_not_finish);
        img2.setImageResource(R.drawable.ic_step_tichdiem_not_finish);
        img3.setImageResource(R.drawable.ic_step_tichdiem_not_finish);
        img4.setImageResource(R.drawable.ic_step_tichdiem_not_finish);
    }

    void show_finish_array(ImageView img1, ImageView img2, ImageView img3, ImageView img4)
    {
        img1.setImageResource(R.drawable.ic_step_tichdiem_finish);
        img2.setImageResource(R.drawable.ic_step_tichdiem_finish);
        img3.setImageResource(R.drawable.ic_step_tichdiem_finish);
        img4.setImageResource(R.drawable.ic_step_tichdiem_finish);
    }
}