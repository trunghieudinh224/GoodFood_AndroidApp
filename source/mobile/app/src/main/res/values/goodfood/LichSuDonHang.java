package com.example.goodfood;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;

public class LichSuDonHang extends AppCompatActivity {

    private ScrollView scrollview_lichsudon;
    private ImageView btn_back;
    private GridView gridview_lichsudonhang;
    private TextView txt_tieude_acti, txt_loadingmore_lichsudonhang;
    private LinearLayout area_loading_lichsudonhang;
    private RelativeLayout area_loadingmore_lichsudonhang;
    ArrayList<LichSuDon> MangLichSuDon;

    SharedPreferences sharedPreferences;
    private FirebaseFirestore database;
    int item_show = 5;
    int chieucao_gridview_macdinh = 0;
    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lich_su_don_hang);

        database = FirebaseFirestore.getInstance();
        sharedPreferences = getSharedPreferences("datalogin", MODE_PRIVATE);
        final String taikhoan = sharedPreferences.getString("Phone", "");

        btn_back = findViewById(R.id.btn_back);
        txt_tieude_acti = findViewById(R.id.txt_tieude_acti);
        scrollview_lichsudon = findViewById(R.id.scrollview_lichsudon);
        gridview_lichsudonhang = findViewById(R.id.gridview_lichsudonhang);
        area_loading_lichsudonhang = findViewById(R.id.area_loading_lichsudonhang);
        txt_loadingmore_lichsudonhang = findViewById(R.id.txt_loadingmore_lichsudonhang);
        area_loadingmore_lichsudonhang = findViewById(R.id.area_loadingmore_lichsudonhang);


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

                item_show = item_show + 5;
                int item_thucte = 0;
                ArrayList<LichSuDon> mang_getmore = new ArrayList<>();
                for (int  i = 0; i<item_show; i++)
                {

                    if (i <= MangLichSuDon.size()-1)
                    {
                        item_thucte++;
                        String ma_don = MangLichSuDon.get(i).getMadon();
                        String tinh_trang = MangLichSuDon.get(i).getTinhtrang();
                        String ngay_dat = MangLichSuDon.get(i).getNgaydat();
                        String sl_mon = MangLichSuDon.get(i).getSoluong();
                        String tong_tien = MangLichSuDon.get(i).getTongTien();
                        String dia_chi = MangLichSuDon.get(i).getDiaChi();
                        mang_getmore.add(new LichSuDon(ma_don,tinh_trang,ngay_dat,sl_mon,tong_tien,dia_chi));
                    }
                }

                if (item_thucte != item_show)
                {
                    item_show = item_thucte;
                }

                if (item_show == MangLichSuDon.size())
                {
                    area_loadingmore_lichsudonhang.setVisibility(View.INVISIBLE);
                    area_loading_lichsudonhang.setEnabled(false);
                }

                LichSuDonAdapter adapter =  new LichSuDonAdapter(LichSuDonHang.this, R.layout.custom_gridview_lichsudonhang, mang_getmore);
                gridview_lichsudonhang.setAdapter(adapter);

                ViewGroup.LayoutParams params_gridview = gridview_lichsudonhang.getLayoutParams();
                params_gridview.height = item_show * chieucao_gridview_macdinh;
                gridview_lichsudonhang.setLayoutParams(params_gridview);
                scrollview_lichsudon.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        txt_loadingmore_lichsudonhang.setVisibility(View.INVISIBLE);
                        scrollview_lichsudon.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                }, 1600);
            }
        });
    }


    boolean reload = false;
    void Load(String taikhoan)
    {
        MangLichSuDon = new ArrayList<LichSuDon>();
        final int check_reload = MangLichSuDon.size();
        database.collection("Users").document(taikhoan).collection("Bill")
        .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }

                for (DocumentChange document : snapshots.getDocumentChanges()) {
                    Object[] DonHang_array_key = (Object[]) document.getDocument().getData().keySet().toArray();
                    Object[] DonHang_array_value = (Object[]) document.getDocument().getData().values().toArray();

                    String madon_hang= "", tinh_trang = "", ngay_dat = "", sl_mon = "", tong_tien = "", dia_chi = "";
                    madon_hang = document.getDocument().getId();
                    for (int i = 0; i < DonHang_array_key.length; i++)
                    {
                        if (DonHang_array_key[i].toString().equals("tinh_trang")) tinh_trang = DonHang_array_value[i].toString();
                        if (DonHang_array_key[i].toString().equals("ngay_dat")) ngay_dat = DonHang_array_value[i].toString();
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
                                MangLichSuDon.set(i,new LichSuDon(document.getDocument().getId(),tinh_trang,ngay_dat,sl_mon,tong_tien,dia_chi));
                            }
                        }
                    }
                    else
                    {
                        MangLichSuDon.add(new LichSuDon(document.getDocument().getId(),tinh_trang,ngay_dat,sl_mon,tong_tien,dia_chi));
                    }
                }

                area_loading_lichsudonhang.setVisibility(View.INVISIBLE);
                gridview_lichsudonhang.setVisibility(View.VISIBLE);
                Collections.reverse(MangLichSuDon);

                int count_donhang = 0;
                ArrayList<LichSuDon> mang_getdefault = new ArrayList<>();
                for (int  i = 0; i<5; i++)
                {
                    if (i <= MangLichSuDon.size()-1)
                    {
                        count_donhang++;
                        String ma_don = MangLichSuDon.get(i).getMadon();
                        String tinh_trang = MangLichSuDon.get(i).getTinhtrang();
                        String ngay_dat = MangLichSuDon.get(i).getNgaydat();
                        String sl_mon = MangLichSuDon.get(i).getSoluong();
                        String tong_tien = MangLichSuDon.get(i).getTongTien();
                        String dia_chi = MangLichSuDon.get(i).getDiaChi();
                        mang_getdefault.add(new LichSuDon(ma_don,tinh_trang,ngay_dat,sl_mon,tong_tien,dia_chi));
                    }
                }
                LichSuDonAdapter adapter =  new LichSuDonAdapter(LichSuDonHang.this, R.layout.custom_gridview_lichsudonhang, mang_getdefault);
                gridview_lichsudonhang.setAdapter(adapter);

                if (MangLichSuDon.size()>5)
                {
                    area_loadingmore_lichsudonhang.setVisibility(View.VISIBLE);
                    area_loading_lichsudonhang.setEnabled(false);
                }

                ViewGroup.LayoutParams params_gridview = gridview_lichsudonhang.getLayoutParams();
                chieucao_gridview_macdinh = params_gridview.height;
                params_gridview.height = params_gridview.height * count_donhang;
                gridview_lichsudonhang.setLayoutParams(params_gridview);
                reload = true;
            }
        });
    }
}