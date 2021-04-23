package com.example.admingoodfood;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

import pl.droidsonroids.gif.GifImageView;

public class ThongTinCaNhan_DonHang_TichLuy extends AppCompatActivity {

    private FirebaseFirestore database;
    private ArrayList<LichSuDon> list_LichSuDon = new ArrayList<>();
    private GifImageView img_loading_watting;
    SharedPreferences sharedPreferences;
    private StorageReference mStorageRef;
    String taikhoan = "";
    boolean reload = false;
    boolean reset_list = true;
    LichSuDonAdapter adapter;
    String currentab = "thông tin";

    private LinearLayout area_thongtin, area_donhang, area_tichluy;
    private TextView txt_thongtin, txt_donhang, txt_tichluy, line_thongtin, line_donhang, line_tichluy, txt_noresult;
    private ListView listview_donhang;

    private LinearLayout thongtin, tichluy;
    private ImageView img_avatar;
    private GifImageView img_loading_avatar;
    private EditText txt_SDT, txt_HoTen, txt_Email, txt_DiaChi, txt_Matkhau;
    private Button btn_capnhat;
    String link_avatar = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thongtincanhan_donhang_tichluy);

        database = FirebaseFirestore.getInstance();
        sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        taikhoan = sharedPreferences.getString("sdt", "");

        img_avatar = findViewById(R.id.img_avatar);
        img_loading_avatar = findViewById(R.id.img_loading_avatar);
        thongtin = findViewById(R.id.thongtin);
        tichluy = findViewById(R.id.tichluy);
        txt_SDT = findViewById(R.id.txt_SDT);
        txt_HoTen = findViewById(R.id.txt_HoTen);
        txt_Email = findViewById(R.id.txt_Email);
        txt_DiaChi = findViewById(R.id.txt_DiaChi);
        txt_Matkhau = findViewById(R.id.txt_Matkhau);

        listview_donhang = findViewById(R.id.listview_donhang);
        img_loading_watting = findViewById(R.id.img_loading_watting);
        txt_noresult = findViewById(R.id.txt_noresult);

        txt_thongtin = findViewById(R.id.txt_thongtin);
        txt_donhang = findViewById(R.id.txt_donhang);
        txt_tichluy = findViewById(R.id.txt_tichluy);
        line_thongtin = findViewById(R.id.line_thongtin);
        line_donhang = findViewById(R.id.line_donhang);
        line_tichluy = findViewById(R.id.line_tichluy);

        area_thongtin = findViewById(R.id.area_thongtin);
        area_thongtin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!currentab.equals("thông tin"))
                {
                    img_loading_watting.setVisibility(View.VISIBLE);
                    txt_noresult.setVisibility(View.INVISIBLE);
                }
                currentab = "thông tin";

                listview_donhang.setVisibility(View.INVISIBLE);
                tichluy.setVisibility(View.INVISIBLE);
                txt_thongtin.setTypeface(null, Typeface.BOLD);
                txt_donhang.setTypeface(null, Typeface.NORMAL);
                txt_tichluy.setTypeface(null, Typeface.NORMAL);
                line_thongtin.setVisibility(View.VISIBLE);
                line_donhang.setVisibility(View.INVISIBLE);
                line_tichluy.setVisibility(View.INVISIBLE);

                thongtin.setVisibility(View.VISIBLE);
            }
        });

        area_donhang = findViewById(R.id.area_donhang);
        area_donhang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!currentab.equals("đơn hàng"))
                {
                    img_loading_watting.setVisibility(View.VISIBLE);
                }
                currentab = "đơn hàng";

                thongtin.setVisibility(View.INVISIBLE);
                tichluy.setVisibility(View.INVISIBLE);
                txt_thongtin.setTypeface(null, Typeface.NORMAL);
                txt_donhang.setTypeface(null, Typeface.BOLD);
                txt_tichluy.setTypeface(null, Typeface.NORMAL);
                line_thongtin.setVisibility(View.INVISIBLE);
                line_donhang.setVisibility(View.VISIBLE);
                line_tichluy.setVisibility(View.INVISIBLE);

                if (list_LichSuDon.size()>0)
                {
                    listview_donhang.setVisibility(View.VISIBLE);
                    img_loading_watting.setVisibility(View.INVISIBLE);
                }
                else
                {
                    img_loading_watting.setImageResource(R.drawable.ic_fail_loading);
                    txt_noresult.setVisibility(View.VISIBLE);
                }
            }
        });

        area_tichluy = findViewById(R.id.area_tichluy);
        area_tichluy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!currentab.equals("tích lũy"))
                    {
                    img_loading_watting.setVisibility(View.VISIBLE);
                    txt_noresult.setVisibility(View.INVISIBLE);
                }
                currentab = "tích lũy";

                listview_donhang.setVisibility(View.INVISIBLE);
                thongtin.setVisibility(View.INVISIBLE);
                txt_thongtin.setTypeface(null, Typeface.NORMAL);
                txt_donhang.setTypeface(null, Typeface.NORMAL);
                txt_tichluy.setTypeface(null, Typeface.BOLD);
                line_thongtin.setVisibility(View.INVISIBLE);
                line_donhang.setVisibility(View.INVISIBLE);
                line_tichluy.setVisibility(View.VISIBLE);

                if (list_LichSuDon.size()>0)
                {
                    listview_donhang.setVisibility(View.VISIBLE);
                    img_loading_watting.setVisibility(View.INVISIBLE);
                }
                else
                {
                    img_loading_watting.setImageResource(R.drawable.ic_fail_loading);
                    txt_noresult.setVisibility(View.VISIBLE);
                }
            }
        });



        ThongTin();
        DonHang();
    }


    void ThongTin()
    {
        final DocumentReference docRef = database.collection("Users").document(taikhoan);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }

                Object[] obj_key = (Object[]) snapshot.getData().keySet().toArray();
                Object[] obj_value = (Object[]) snapshot.getData().values().toArray();

                txt_SDT.setText(snapshot.getId());
                for (int i = 0; i < obj_key.length; i++)
                {
                    if (obj_key[i].toString().equals("name")) txt_HoTen.setText(obj_value[i].toString());
                    if (obj_key[i].toString().equals("email")) txt_Email.setText(obj_value[i].toString());
                    if (obj_key[i].toString().equals("address")) txt_DiaChi.setText(obj_value[i].toString());
                    if (obj_key[i].toString().equals("password")) txt_Matkhau.setText(obj_value[i].toString());
                    if (obj_key[i].toString().equals("link_avatar"))
                    {
                        link_avatar = obj_value[i].toString();
                        if (!link_avatar.equals(""))
                        {
                            mStorageRef = FirebaseStorage.getInstance().getReference().child("users_pic/" + link_avatar);
                            try
                            {
                                final File file_local = File.createTempFile("users_pic", link_avatar.substring(link_avatar.length()-3));
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
                                        Toast.makeText(ThongTinCaNhan_DonHang_TichLuy.this, "Tải hình thất bại", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            catch (Exception ex)
                            {
                                Toast.makeText(ThongTinCaNhan_DonHang_TichLuy.this, "Lỗi kết nối !!!", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            img_avatar.setImageResource(R.drawable.ic_avatar_default);
                            img_loading_avatar.setVisibility(View.INVISIBLE);
                        }
                    }
                }
                thongtin.setVisibility(View.VISIBLE);
            }
        });


    }


    void DonHang()
    {
        //database.collection("Users").orderBy("name", Query.Direction.DESCENDING).limit(3);
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
                            String Madon = "", Tinhtrang = "", Thoi_gian_dat = "", Thoi_gian_nhan = "", Soluong = "", TongTien = "", DiaChi = "";

                            Madon = document.getDocument().getId();
                            for (int i = 0; i < obj_key.length; i++)
                            {
                                if (obj_key[i].toString().equals("tinh_trang")) Tinhtrang = obj_value[i].toString();
                                if (obj_key[i].toString().equals("thoi_gian_dat")) Thoi_gian_dat = obj_value[i].toString();
                                if (obj_key[i].toString().equals("thoi_gian_nhan")) Thoi_gian_nhan = obj_value[i].toString();
                                if (obj_key[i].toString().equals("sl_mon")) Soluong = obj_value[i].toString();
                                if (obj_key[i].toString().equals("tong_tien")) TongTien = obj_value[i].toString();
                                if (obj_key[i].toString().equals("dia_chi")) DiaChi = obj_value[i].toString();
                            }



                            if (reload)
                            {
                                for (int i = list_LichSuDon.size()-1; i >= 0; i--)
                                {
                                    if (list_LichSuDon.get(i).getMadon().equals(Madon))
                                    {
                                        list_LichSuDon.set(i,new LichSuDon(Madon,Tinhtrang,Thoi_gian_dat,Thoi_gian_nhan,Soluong,TongTien,DiaChi));
                                        adapter.notifyDataSetChanged();
                                        return;
                                    }
                                }
                            }
                            else
                            {
                                list_LichSuDon.add(new LichSuDon(Madon,Tinhtrang,Thoi_gian_dat,Thoi_gian_nhan,Soluong,TongTien,DiaChi));
                            }
                        }

                        Collections.sort(list_LichSuDon, new Comparator<LichSuDon>() {
                            @Override
                            public int compare(LichSuDon o1, LichSuDon o2) {
                                Date currentTime = Calendar.getInstance().getTime();
                                final SimpleDateFormat dateFormat_txt_var = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                                final String dateFormat_txt = dateFormat_txt_var.format(currentTime);

                                Date d1 = null, d2 = null;
                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                                try {
                                    d1 = sdf.parse(o1.getThoi_gian_dat());
                                    d2 = sdf.parse(o2.getThoi_gian_dat());

                                } catch (ParseException ex) {
                                    ex.printStackTrace();
                                }
                                return d1.compareTo(d2);
                            }
                        });
                        Collections.reverse(list_LichSuDon);


                        ViewGroup.LayoutParams params_listview_donhang = listview_donhang.getLayoutParams();
                        params_listview_donhang.height = params_listview_donhang.height * list_LichSuDon.size();
                        listview_donhang.setLayoutParams(params_listview_donhang);
                        adapter =  new LichSuDonAdapter(ThongTinCaNhan_DonHang_TichLuy.this, R.layout.custom_listview_donhang, list_LichSuDon);
                        listview_donhang.setAdapter(adapter);
                        reload = true;
                    }
                });
    }
}