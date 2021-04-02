package com.example.goodfood;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import pl.droidsonroids.gif.GifImageView;

public class LichSuDonHang extends AppCompatActivity {

    private ScrollView scrollview_lichsudon;
    private ImageView btn_back;
    private GridView gridview_lichsudonhang;
    private TextView txt_tieude_acti, txt_loadingmore_lichsudonhang, txt_khong_donhang;
    private LinearLayout area_loading_lichsudonhang;
    private RelativeLayout area_loadingmore_lichsudonhang;
    private GifImageView gif_loading;
    ArrayList<LichSuDon> MangLichSuDon;
    ArrayList<LichSuDon> mang_getdefault;
    LichSuDonAdapter adapter;

    SharedPreferences sharedPreferences;
    private FirebaseFirestore database;
    int chieucao_gridview_macdinh = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lich_su_don_hang);

        database = FirebaseFirestore.getInstance();
        sharedPreferences = getSharedPreferences("datalogin", MODE_PRIVATE);
        final String taikhoan = sharedPreferences.getString("Phone", "");

        btn_back = findViewById(R.id.btn_back);
        txt_tieude_acti = findViewById(R.id.txt_tieude_acti);
        gridview_lichsudonhang = findViewById(R.id.gridview_lichsudonhang);
        area_loading_lichsudonhang = findViewById(R.id.area_loading_lichsudonhang);
        txt_loadingmore_lichsudonhang = findViewById(R.id.txt_loadingmore_lichsudonhang);
        area_loadingmore_lichsudonhang = findViewById(R.id.area_loadingmore_lichsudonhang);
        txt_khong_donhang = findViewById(R.id.txt_khong_donhang);
        gif_loading = findViewById(R.id.gif_loading);

        txt_tieude_acti.setText("Lịch sử đơn hàng");
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Load(taikhoan);


        txt_loadingmore_lichsudonhang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (int  i = mang_getdefault.size(); i<MangLichSuDon.size(); i++)
                {
                    String ma_don = MangLichSuDon.get(i).getMadon();
                    String tinh_trang = MangLichSuDon.get(i).getTinhtrang();
                    String thoi_gian_dat = MangLichSuDon.get(i).getThoi_gian_dat();
                    String thoi_gian_nhan = MangLichSuDon.get(i).getThoi_gian_nhan();
                    String sl_mon = MangLichSuDon.get(i).getSoluong();
                    String tong_tien = MangLichSuDon.get(i).getTongTien();
                    String dia_chi = MangLichSuDon.get(i).getDiaChi();
                    mang_getdefault.add(new LichSuDon(ma_don,tinh_trang,thoi_gian_dat,thoi_gian_nhan,sl_mon,tong_tien,dia_chi));
                    if (i == mang_getdefault.size()+5) break;
                }
                adapter.notifyDataSetChanged();

                if (mang_getdefault.size() == MangLichSuDon.size())
                {
                    area_loadingmore_lichsudonhang.setVisibility(View.INVISIBLE);
                    area_loading_lichsudonhang.setEnabled(false);

                    ViewGroup.LayoutParams params_area_loadingmore_lichsudonhang = area_loadingmore_lichsudonhang.getLayoutParams();
                    params_area_loadingmore_lichsudonhang.height = 0;
                    area_loadingmore_lichsudonhang.setLayoutParams(params_area_loadingmore_lichsudonhang);
                }

                ViewGroup.LayoutParams params_gridview = gridview_lichsudonhang.getLayoutParams();
                params_gridview.height = mang_getdefault.size() * chieucao_gridview_macdinh;
                gridview_lichsudonhang.setLayoutParams(params_gridview);
            }
        });
    }


    boolean reload = false;
    void Load(String taikhoan)
    {
        MangLichSuDon = new ArrayList<LichSuDon>();
        database.collection("Users").document(taikhoan).collection("Bill")
        .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {

                if (snapshots.getDocumentChanges().size() == 0)
                {
                    txt_khong_donhang.setVisibility(View.VISIBLE);
                    gif_loading.setImageResource(R.drawable.ic_fail_loading);
                    return;
                }

                for (DocumentChange document : snapshots.getDocumentChanges()) {
                    Object[] DonHang_array_key = (Object[]) document.getDocument().getData().keySet().toArray();
                    Object[] DonHang_array_value = (Object[]) document.getDocument().getData().values().toArray();

                    String madon_hang= "", tinh_trang = "", thoi_gian_dat = "", thoi_gian_nhan = "", sl_mon = "", tong_tien = "", dia_chi = "";
                    madon_hang = document.getDocument().getId();
                    for (int i = 0; i < DonHang_array_key.length; i++)
                    {
                        if (DonHang_array_key[i].toString().equals("tinh_trang")) tinh_trang = DonHang_array_value[i].toString();
                        if (DonHang_array_key[i].toString().equals("thoi_gian_dat")) thoi_gian_dat = DonHang_array_value[i].toString();
                        if (DonHang_array_key[i].toString().equals("thoi_gian_nhan")) thoi_gian_nhan = DonHang_array_value[i].toString();
                        if (DonHang_array_key[i].toString().equals("sl_mon")) sl_mon = DonHang_array_value[i].toString();
                        if (DonHang_array_key[i].toString().equals("tong_tien")) tong_tien = DonHang_array_value[i].toString();
                        if (DonHang_array_key[i].toString().equals("dia_chi")) dia_chi = DonHang_array_value[i].toString();
                    }

                    if (reload)
                    {
                        for (int i = MangLichSuDon.size()-1; i >= 0; i--)
                        {
                            if (MangLichSuDon.get(i).getMadon().equals(madon_hang))
                            {
                                mang_getdefault.set(i,new LichSuDon(madon_hang,tinh_trang,thoi_gian_dat,thoi_gian_nhan,sl_mon,tong_tien,dia_chi));
                                adapter.notifyDataSetChanged();
                                return;
                            }
                        }
                    }
                    else
                    {
                        MangLichSuDon.add(new LichSuDon(madon_hang,tinh_trang,thoi_gian_dat,thoi_gian_nhan,sl_mon,tong_tien,dia_chi));
                    }
                }

                Collections.sort(MangLichSuDon, new Comparator<LichSuDon>() {
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

                area_loading_lichsudonhang.setVisibility(View.INVISIBLE);
                gridview_lichsudonhang.setVisibility(View.VISIBLE);
                Collections.reverse(MangLichSuDon);

                mang_getdefault = new ArrayList<>(MangLichSuDon);
                if (mang_getdefault.size() > 5)
                {
                    mang_getdefault.subList(5,mang_getdefault.size()).clear();
                    area_loadingmore_lichsudonhang.setVisibility(View.VISIBLE);
                    area_loading_lichsudonhang.setEnabled(false);
                }
                else
                {
                    ViewGroup.LayoutParams params_area_loadingmore_lichsudonhang = area_loadingmore_lichsudonhang.getLayoutParams();
                    params_area_loadingmore_lichsudonhang.height = 0;
                    area_loadingmore_lichsudonhang.setLayoutParams(params_area_loadingmore_lichsudonhang);
                }

                adapter =  new LichSuDonAdapter(LichSuDonHang.this, R.layout.custom_gridview_lichsudonhang, mang_getdefault);
                gridview_lichsudonhang.setAdapter(adapter);


                ViewGroup.LayoutParams params_gridview = gridview_lichsudonhang.getLayoutParams();
                chieucao_gridview_macdinh = params_gridview.height;
                params_gridview.height = params_gridview.height * mang_getdefault.size();
                gridview_lichsudonhang.setLayoutParams(params_gridview);
                reload = true;
            }
        });
    }
}