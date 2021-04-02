package com.example.goodfood;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import pl.droidsonroids.gif.GifImageView;

public class ViVoucher extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private ImageView btn_back_TTTK;
    private GridView listview_vi_voucher;
    private FirebaseFirestore database;
    ArrayList<Voucher> MangVoucher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vi_voucher);

        database = FirebaseFirestore.getInstance();


        btn_back_TTTK = findViewById(R.id.btn_back_TTTK);
        btn_back_TTTK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        listview_vi_voucher = findViewById(R.id.listview_vi_voucher);
        MangVoucher = new ArrayList<Voucher>();
        database.collection("Voucher").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful())
                {
                    for (DocumentSnapshot document : task.getResult())
                    {
                        Object[] obj_key = (Object[]) document.getData().keySet().toArray();
                        Object[] obj_value = (Object[]) document.getData().values().toArray();
                        String ma_voucher = "", gia_tri_don_nho_nhat = "", giam_phan_tram = "", giam_tien = "", giam_toi_da = "", hsd = "", loai_giam_gia ="", loai_voucher = "", mo_ta = "";
                        ma_voucher = document.getId();
                        for (int i = 0; i < obj_key.length; i++)
                        {
                            if (obj_key[i].toString().equals("gia_tri_don_nho_nhat")) gia_tri_don_nho_nhat = obj_value[i].toString();
                            if (obj_key[i].toString().equals("giam_phan_tram")) giam_phan_tram = obj_value[i].toString();
                            if (obj_key[i].toString().equals("giam_tien")) giam_tien = obj_value[i].toString();
                            if (obj_key[i].toString().equals("giam_toi_da")) giam_toi_da = obj_value[i].toString();
                            if (obj_key[i].toString().equals("hsd")) hsd = obj_value[i].toString();
                            if (obj_key[i].toString().equals("loai_giam_gia")) loai_giam_gia = obj_value[i].toString();
                            if (obj_key[i].toString().equals("loai_voucher")) loai_voucher = obj_value[i].toString();
                            if (obj_key[i].toString().equals("mo_ta")) mo_ta = obj_value[i].toString();
                        }
                        MangVoucher.add(new Voucher(ma_voucher,gia_tri_don_nho_nhat,giam_phan_tram,giam_tien,giam_toi_da,hsd,loai_giam_gia,loai_voucher,mo_ta));
                        VoucherAdapter adapter  =  new VoucherAdapter(ViVoucher.this, R.layout.custom_row_vivoucher, MangVoucher);
                        listview_vi_voucher.setAdapter(adapter);
                    }
                }
                else
                {
                    Toast.makeText(ViVoucher.this, "Lỗi kết nối !!!", Toast.LENGTH_SHORT).show();
                }
            }
        });



        listview_vi_voucher.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        String code = MangVoucher.get(position).getMa_voucher();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("activity");
            int tongtien = extras.getInt("tongtien");

            if (value.equals("chitiethoadon"))
            {
                Check_Voucher(position, tongtien);
            }
        }
        else
        {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("String", code);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(this, "Đã sao chép mã voucher", Toast.LENGTH_SHORT).show();
        }
    }


    Date ngay_hien_tai = null, ngay_hsd = null;
    private void Check_Voucher(int position, final int TongTien)
    {
        LayoutInflater inflater = LayoutInflater.from(ViVoucher.this);
        View viewalert = inflater.inflate(R.layout.alert_thongbaochung, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(ViVoucher.this).setView(viewalert).create();
        alertDialog.setCanceledOnTouchOutside(false);
        final TextView txt_loading_noti = viewalert.findViewById(R.id.txt_loading_noti);
        final TextView txt_noidung_noti = viewalert.findViewById(R.id.txt_noidung_noti);
        final GifImageView gif_loading_chung = viewalert.findViewById(R.id.gif_loading_chung);
        final Button btn_accept = viewalert.findViewById(R.id.btn_accept);
        final Button btn_not_accept = viewalert.findViewById(R.id.btn_not_accept);
        btn_not_accept.setVisibility(View.VISIBLE);
        btn_accept.setVisibility(View.VISIBLE);
        btn_accept.setText("Chọn lại");
        btn_not_accept.setText("Không xài nữa");

        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        btn_not_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                TinhTien_GiamGia ("", TongTien, 0);
            }
        });




        String ma_voucher = MangVoucher.get(position).getMa_voucher();
        int gia_tri_don_nho_nhat = Integer.parseInt(MangVoucher.get(position).getGia_tri_don_nho_nhat());
        int giam_phan_tram = Integer.parseInt(MangVoucher.get(position).getGiam_phan_tram());
        int giam_tien = Integer.parseInt(MangVoucher.get(position).getGiam_tien());
        int giam_toi_da = Integer.parseInt(MangVoucher.get(position).getGiam_toi_da());
        String hsd = MangVoucher.get(position).getHsd();
        String loai_giam_gia = MangVoucher.get(position).getLoai_giamgia();
        String loai_voucher = MangVoucher.get(position).getLoai_voucher();

        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat getdate_format = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        String date = getdate_format.format(currentTime);
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        try {
            ngay_hien_tai = sdf.parse(date);
            ngay_hsd = sdf.parse(hsd);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        if (ngay_hsd.after(ngay_hien_tai))
        {
            if (loai_voucher.equals("normal"))
            {
                if (TongTien >= gia_tri_don_nho_nhat)
                {
                    if (loai_giam_gia.equals("phần trăm"))
                    {
                        int tien_giam = (TongTien*giam_phan_tram)/100;
                        if (tien_giam > giam_toi_da)
                        {
                            //return giam_toi_da;
                            TinhTien_GiamGia(ma_voucher,TongTien,giam_toi_da);
                        }
                        else
                        {
                            //return tien_giam;
                            TinhTien_GiamGia(ma_voucher,TongTien,tien_giam);
                        }
                    }
                    else
                    {
                        //return giam_tien;
                        TinhTien_GiamGia(ma_voucher,TongTien,giam_tien);
                    }
                }
                else
                {
                    txt_loading_noti.setText("VOUCHER");
                    txt_noidung_noti.setText("Giá trị đơn hàng chưa đạt yêu cầu voucher");
                    gif_loading_chung.setImageResource(R.drawable.ic_error_loading);
                    alertDialog.show();
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                }
            }


            else
            {
                if (TongTien >= gia_tri_don_nho_nhat)
                {
                    if (loai_giam_gia.equals("phần trăm"))
                    {
                        int tien_giam = (TongTien*giam_phan_tram)/100;
                        if (tien_giam > giam_toi_da)
                        {
                            //return giam_toi_da;
                            TinhTien_GiamGia(ma_voucher,TongTien,giam_toi_da);
                        }
                        else
                        {
                            //return tien_giam;
                            TinhTien_GiamGia(ma_voucher,TongTien,tien_giam);
                        }
                    }
                    else
                    {
                        //return giam_tien;
                        TinhTien_GiamGia(ma_voucher,TongTien,giam_tien);
                    }
                }
                else
                {
                    txt_loading_noti.setText("VOUCHER");
                    txt_noidung_noti.setText("Giá trị đơn hàng chưa đạt yêu cầu voucher");
                    gif_loading_chung.setImageResource(R.drawable.ic_error_loading);
                    alertDialog.show();
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                }
            }
        }
        else
        {
            txt_loading_noti.setText("VOUCHER");
            txt_noidung_noti.setText("Voucher đã hết hạn sử dụng");
            gif_loading_chung.setImageResource(R.drawable.ic_error_loading);
            alertDialog.show();
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    public void TinhTien_GiamGia (String ma_voucher, int TongTien, int money)
    {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("code",ma_voucher);
        resultIntent.putExtra("tiensaukhigiam",TongTien - money + 30000);
        resultIntent.putExtra("tiengiam",money);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}