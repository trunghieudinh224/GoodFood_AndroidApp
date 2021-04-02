package com.example.goodfood;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ScrollView;
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
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.util.ArrayList;

import pl.droidsonroids.gif.GifImageView;

public class TheoDoiDonHang extends AppCompatActivity {

    ScrollView srollview_theodoidonhang;
    GifImageView img_goodfood_doing, ic_step_doing_tiepnhan, ic_step_doing_xuly, ic_step_doing_giaohang, ic_step_doing_hoanthanh;
    private ImageView btn_back, ic_step_finish_notfinish_tiepnhan, ic_step_finish_notfinish_xuly, ic_step_finish_notfinish_giaohang, ic_step_finish_notfinish_hoanthanh, ic_step_array1, ic_step_array2, ic_step_array3;
    private TextView txt_tieude_acti, txt_madon_tddh, txt_tennguoinhan_tddh, txt_SDT_tddh, txt_diachi_tddh, txt_tamtinh_tddh, txt_phiship_tddh, txt_giamgia_tddh, txt_tongtien_tddh, txt_dando_theodoidonhang;
    private GridView gridview_danhsachmonan_tddh;
    private Button btn_danhgia_sanpham;
    String madon = "";
    String tinh_trang = "";
    String danhgia = "";
    ArrayList<DanhSachMonAnKhachChon> Mang_DanhSachMonAnKhachChon;
    ArrayList<DanhSachMonAnRating> ds_rating = new ArrayList<>();

    SharedPreferences sharedPreferences;
    private FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theo_doi_don_hang);

        database = FirebaseFirestore.getInstance();
        sharedPreferences = getSharedPreferences("datalogin", MODE_PRIVATE);
        final String taikhoan = sharedPreferences.getString("Phone", "");

        srollview_theodoidonhang = findViewById(R.id.srollview_theodoidonhang);
        btn_back = findViewById(R.id.btn_back);
        txt_tieude_acti = findViewById(R.id.txt_tieude_acti);
        txt_madon_tddh = findViewById(R.id.txt_madon_tddh);
        txt_tennguoinhan_tddh = findViewById(R.id.txt_tennguoinhan_tddh);
        txt_SDT_tddh = findViewById(R.id.txt_SDT_tddh);
        txt_diachi_tddh = findViewById(R.id.txt_diachi_tddh);

        gridview_danhsachmonan_tddh = findViewById(R.id.gridview_danhsachmonan_tddh);
        txt_tamtinh_tddh = findViewById(R.id.txt_tamtinh_tddh);
        txt_phiship_tddh = findViewById(R.id.txt_phiship_tddh);
        txt_giamgia_tddh = findViewById(R.id.txt_giamgia_tddh);
        txt_tongtien_tddh = findViewById(R.id.txt_tongtien_tddh);
        txt_dando_theodoidonhang = findViewById(R.id.txt_dando_theodoidonhang);

        img_goodfood_doing = findViewById(R.id.img_goodfood_doing);
        ic_step_doing_tiepnhan = findViewById(R.id.ic_step_doing_tiepnhan);
        ic_step_finish_notfinish_tiepnhan = findViewById(R.id.ic_step_finish_notfinish_tiepnhan);
        ic_step_doing_xuly = findViewById(R.id.ic_step_doing_xuly);
        ic_step_finish_notfinish_xuly = findViewById(R.id.ic_step_finish_notfinish_xuly);
        ic_step_doing_giaohang = findViewById(R.id.ic_step_doing_giaohang);
        ic_step_finish_notfinish_giaohang = findViewById(R.id.ic_step_finish_notfinish_giaohang);
        ic_step_doing_hoanthanh = findViewById(R.id.ic_step_doing_hoanthanh);
        ic_step_finish_notfinish_hoanthanh = findViewById(R.id.ic_step_finish_notfinish_hoanthanh);

        ic_step_array1 = findViewById(R.id.ic_step_array1);
        ic_step_array2 = findViewById(R.id.ic_step_array2);
        ic_step_array3 = findViewById(R.id.ic_step_array3);

        btn_danhgia_sanpham = findViewById(R.id.btn_danhgia_sanpham);

        txt_tieude_acti.setText("Chi tiết đơn hàng");
        btn_back.setImageResource(R.drawable.ic_close_white);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            madon = extras.getString("MaDon");
        }

        final DocumentReference docRef = database.collection("Users").document(taikhoan).collection("Bill").document(madon);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Toast.makeText(TheoDoiDonHang.this, "Lỗi kết nối !!!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    int giamgia = 0, tongtien = 0;
                    txt_madon_tddh.setText(madon);


                    Object[] bill_key = (Object[]) snapshot.getData().keySet().toArray();
                    Object[] bill_value = (Object[]) snapshot.getData().values().toArray();
                    for (int i = 0; i < bill_key.length; i++)
                    {
                        if (bill_key[i].toString().equals("check_danh_gia")) danhgia = bill_value[i].toString();
                        if (bill_key[i].toString().equals("nguoi_nhan_hang")) txt_tennguoinhan_tddh.setText(bill_value[i].toString());
                        if (bill_key[i].toString().equals("sdt_nhan_hang"))
                            txt_SDT_tddh.setText(bill_value[i].toString());
                        if (bill_key[i].toString().equals("dia_chi"))
                            txt_diachi_tddh.setText(bill_value[i].toString());
                        if (bill_key[i].toString().equals("ghi_chu")) {
                            if (!bill_value[i].toString().equals(""))
                            {
                                txt_dando_theodoidonhang.setText(bill_value[i].toString());
                            }
                            else txt_dando_theodoidonhang.setText("Không có");
                        }
                        if (bill_key[i].toString().equals("tong_tien")){
                            tongtien = Integer.parseInt(bill_value[i].toString());
                            txt_tongtien_tddh.setText(Format_Money(bill_value[i].toString())+ " đ");
                        }
                        txt_phiship_tddh.setText("30,000 đ");
                        if (bill_key[i].toString().equals("giam_gia")){
                            txt_giamgia_tddh.setText("-" + Format_Money(bill_value[i].toString())+ " đ");
                            giamgia = Integer.parseInt(bill_value[i].toString());
                        }

                        if (bill_key[i].toString().equals("tinh_trang"))
                        {
                            tinh_trang = bill_value[i].toString();

                            if (tinh_trang.equals("Tiếp nhận đơn"))
                            {
                                img_goodfood_doing.setImageResource(R.drawable.ic_show_step_tiepnhan);
                                show_step_doing(ic_step_doing_tiepnhan,ic_step_doing_xuly,ic_step_doing_giaohang,ic_step_doing_hoanthanh,ic_step_finish_notfinish_xuly,ic_step_finish_notfinish_xuly,ic_step_finish_notfinish_giaohang,ic_step_finish_notfinish_hoanthanh,null,null,null,"noarray");
                                show_step_notfinish(ic_step_finish_notfinish_xuly,ic_step_finish_notfinish_giaohang,ic_step_finish_notfinish_hoanthanh,ic_step_finish_notfinish_hoanthanh, "");
                            }
                            else if (tinh_trang.equals("Xử lý đơn"))
                            {
                                img_goodfood_doing.setImageResource(R.drawable.ic_show_step_nauan);
                                show_step_doing(ic_step_doing_xuly,ic_step_doing_tiepnhan,ic_step_doing_giaohang,ic_step_doing_hoanthanh,ic_step_finish_notfinish_xuly,ic_step_finish_notfinish_tiepnhan,ic_step_finish_notfinish_hoanthanh,ic_step_finish_notfinish_hoanthanh,ic_step_array1,ic_step_array1,ic_step_array1,"");
                                show_step_notfinish(ic_step_finish_notfinish_giaohang,ic_step_finish_notfinish_hoanthanh,ic_step_finish_notfinish_hoanthanh,ic_step_finish_notfinish_hoanthanh,"");
                            }
                            else if (tinh_trang.equals("Giao hàng"))
                            {
                                img_goodfood_doing.setImageResource(R.drawable.ic_show_step_giaohang);
                                show_step_doing(ic_step_doing_giaohang,ic_step_doing_tiepnhan,ic_step_doing_xuly,ic_step_doing_hoanthanh,ic_step_finish_notfinish_xuly,ic_step_finish_notfinish_tiepnhan,ic_step_finish_notfinish_hoanthanh,ic_step_finish_notfinish_hoanthanh,ic_step_array1,ic_step_array2,ic_step_array2,"");
                                show_step_notfinish(ic_step_finish_notfinish_hoanthanh,ic_step_finish_notfinish_hoanthanh,ic_step_finish_notfinish_hoanthanh,ic_step_finish_notfinish_hoanthanh, "");
                            }
                            else
                            {
                                btn_danhgia_sanpham.setVisibility(View.VISIBLE);
                                img_goodfood_doing.setImageResource(R.drawable.ic_show_step_hoanthanh);
                                show_step_doing(ic_step_doing_hoanthanh,ic_step_doing_tiepnhan,ic_step_doing_xuly,ic_step_doing_giaohang,ic_step_finish_notfinish_xuly,ic_step_finish_notfinish_xuly,ic_step_finish_notfinish_giaohang,ic_step_finish_notfinish_hoanthanh,ic_step_array1,ic_step_array2,ic_step_array3,"");
                                show_step_notfinish(null,null,null,null, "noimg");
                            }
                        }
                        txt_tamtinh_tddh.setText(Format_Money(String.valueOf(giamgia + tongtien -30000)) + " đ");

                        gridview_danhsachmonan_tddh.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                srollview_theodoidonhang.fullScroll(ScrollView.FOCUS_UP);
                            }
                        }, 700);
                    }
                }
            }
        });

//        database.collection("Users").document(taikhoan).collection("Bill").document(madon).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful())
//                {
//                    DocumentSnapshot document = task.getResult();
//                    Object[] bill_key = (Object[]) document.getData().keySet().toArray();
//                    Object[] bill_value = (Object[]) document.getData().values().toArray();
//                    int giamgia = 0, tongtien = 0;
//
//                    txt_madon_tddh.setText(madon);
//                    for (int i = 0; i < bill_key.length; i++)
//                    {
//                        if (bill_key[i].toString().equals("tinh_trang")) tinh_trang = bill_value[i].toString();
//                        if (bill_key[i].toString().equals("nguoi_nhan_hang")) txt_tennguoinhan_tddh.setText(bill_value[i].toString());
//                        if (bill_key[i].toString().equals("sdt_nhan_hang")) txt_SDT_tddh.setText(bill_value[i].toString());
//                        if (bill_key[i].toString().equals("dia_chi")) txt_diachi_tddh.setText(bill_value[i].toString());
//
//                        if (bill_key[i].toString().equals("ghi_chu")) {
//                            if (!bill_value[i].toString().equals(""))
//                            {
//                                txt_dando_theodoidonhang.setText(bill_value[i].toString());
//                            }
//                            else txt_dando_theodoidonhang.setText("Không có");
//                        }
//                        if (bill_key[i].toString().equals("tong_tien")){
//                            tongtien = Integer.parseInt(bill_value[i].toString());
//                            txt_tongtien_tddh.setText(Format_Money(bill_value[i].toString())+ " đ");
//                        }
//                        txt_phiship_tddh.setText("30,000 đ");
//                        if (bill_key[i].toString().equals("giam_gia")){
//                            txt_giamgia_tddh.setText("-" + Format_Money(bill_value[i].toString())+ " đ");
//                            giamgia = Integer.parseInt(bill_value[i].toString());
//                        }
//                    }
//                    txt_tamtinh_tddh.setText(Format_Money(String.valueOf(giamgia + tongtien)) + " đ");
//                }
//                else
//                {
//                    Toast.makeText(TheoDoiDonHang.this, "Lỗi kết nối !!!", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });


        Mang_DanhSachMonAnKhachChon = new ArrayList<DanhSachMonAnKhachChon>();
        database.collection("Users").document(taikhoan).collection("Bill").document(madon).collection("Detail_Bill").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful())
                {
                    for (QueryDocumentSnapshot document : task.getResult())
                    {
                        String sl = "", thanhtien = "", danhgia = "";
                        Object[] chitiet_bill_key = (Object[]) document.getData().keySet().toArray();
                        Object[] chitiet_bill_value = (Object[]) document.getData().values().toArray();
                        String tenmon = document.getId();
                        for (int i = 0; i < chitiet_bill_key.length; i++)
                        {
                            if (chitiet_bill_key[i].toString().equals("so_luong")) sl = chitiet_bill_value[i].toString();
                            if (chitiet_bill_key[i].toString().equals("thanh_tien")) thanhtien = chitiet_bill_value[i].toString();
                            if (chitiet_bill_key[i].toString().equals("danh_gia")) danhgia = chitiet_bill_value[i].toString();
                        }
                        ds_rating.add(new DanhSachMonAnRating(tenmon, danhgia));
                        Mang_DanhSachMonAnKhachChon.add(new DanhSachMonAnKhachChon(tenmon, sl,"", Format_Money(thanhtien)));
                    }

                    DanhSachMonAnTheoDoiDonHangAdapter adapter_monan_detail  =  new DanhSachMonAnTheoDoiDonHangAdapter(TheoDoiDonHang.this, R.layout.custom_danhsachmonan_theodoidonhang, Mang_DanhSachMonAnKhachChon);
                    gridview_danhsachmonan_tddh.setAdapter(adapter_monan_detail);

                    ViewGroup.LayoutParams params_gridview_danhsachmonan_tddh = gridview_danhsachmonan_tddh.getLayoutParams();
                    params_gridview_danhsachmonan_tddh.height = params_gridview_danhsachmonan_tddh.height * Mang_DanhSachMonAnKhachChon.size();
                    gridview_danhsachmonan_tddh.setLayoutParams(params_gridview_danhsachmonan_tddh);
                }
                else
                {
                    Toast.makeText(TheoDoiDonHang.this, "Lỗi kết nối !!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_danhgia_sanpham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                if (ds_rating.size() == 0)
                {
                    for (int i = 0; i < Mang_DanhSachMonAnKhachChon.size(); i++)
                    {
                        ds_rating.add(new DanhSachMonAnRating(Mang_DanhSachMonAnKhachChon.get(i).getTenmon(),""));
                    }
                }

                LayoutInflater inflater = LayoutInflater.from(TheoDoiDonHang.this);
                View viewalert = inflater.inflate(R.layout.alert_voting_food, null);
                final AlertDialog alertDialog = new AlertDialog.Builder(TheoDoiDonHang.this).setView(viewalert).create();
                TextView txt_sl_monan = viewalert.findViewById(R.id.txt_sl_monan);
                txt_sl_monan.setText("#" + String.valueOf(ds_rating.size()) + " sản phẩm");
                GridView gridview_rating_food = viewalert.findViewById(R.id.gridview_rating_food);
                ImageView btn_back = viewalert.findViewById(R.id.btn_back);
                final Button btn_save_danhgia = viewalert.findViewById(R.id.btn_save_danhgia);
                if (danhgia.equals("checked"))
                {
                    btn_save_danhgia.setVisibility(View.INVISIBLE);
                    btn_save_danhgia.setEnabled(false);
                }

                btn_back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                btn_save_danhgia.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean add = true;
                        for (int i = 0; i < ds_rating.size(); i++)
                        {
                           if (ds_rating.get(i).getDanhgia().equals(""))
                           {
                               add = false;
                               Toast.makeText(TheoDoiDonHang.this, "Vui lòng đánh giá toàn bộ sản phẩm !", Toast.LENGTH_SHORT).show();
                               return;
                           }
                        }

                        if (add)
                        {
                            btn_danhgia_sanpham.setEnabled(false);
                            Toast.makeText(TheoDoiDonHang.this, "Cảm ơn bạn đã ủng hộ GoodFood", Toast.LENGTH_SHORT).show();
                            for (int i = 0; i < ds_rating.size(); i++)
                            {
                                DocumentReference doc =  database.collection("Users").document(taikhoan).collection("Bill").document(madon).collection("Detail_Bill").document(ds_rating.get(i).tenmon);
                                doc.update("danh_gia", ds_rating.get(i).danhgia).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        DocumentReference doc =  database.collection("Users").document(taikhoan).collection("Bill").document(madon);
                                        doc.update("check_danh_gia", "checked").addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                btn_danhgia_sanpham.setEnabled(true);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(TheoDoiDonHang.this, "Lỗi kết nối !!!", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(TheoDoiDonHang.this, "Lỗi kết nối !!!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                        alertDialog.dismiss();
                    }
                });

                DanhSachMonAnRatingAdapter adapter_monan_detail_alert  =  new DanhSachMonAnRatingAdapter(TheoDoiDonHang.this, R.layout.custom_danhsachmonan_rating, ds_rating);
                gridview_rating_food.setAdapter(adapter_monan_detail_alert);
                alertDialog.show();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
        });
    }


    void show_step_doing(GifImageView gif1, GifImageView gif2, GifImageView gif3, GifImageView gif4,ImageView img1, ImageView img2, ImageView img3, ImageView img4, ImageView array1, ImageView array2, ImageView array3, String note)
    {
        gif1.setVisibility(View.VISIBLE);
        gif2.setVisibility(View.INVISIBLE);
        gif3.setVisibility(View.INVISIBLE);
        gif4.setVisibility(View.INVISIBLE);

        img1.setVisibility(View.VISIBLE);img1.setImageResource(R.drawable.ic_step_finish);
        img2.setVisibility(View.VISIBLE);img2.setImageResource(R.drawable.ic_step_finish);
        img3.setVisibility(View.VISIBLE);img3.setImageResource(R.drawable.ic_step_finish);
        img4.setVisibility(View.VISIBLE);img4.setImageResource(R.drawable.ic_step_finish);

        if (!note.equals("noarray"))
        {
            array1.setImageResource(R.drawable.ic_step_array_finish);
            array2.setImageResource(R.drawable.ic_step_array_finish);
            array3.setImageResource(R.drawable.ic_step_array_finish);
        }
    }

    void show_step_notfinish( ImageView img1, ImageView img2, ImageView img3, ImageView img4, String noteimg)
    {
        if (!noteimg.equals("noimg"))
        {
            img1.setVisibility(View.VISIBLE);img1.setImageResource(R.drawable.ic_step_notfinish);
            img2.setVisibility(View.VISIBLE);img2.setImageResource(R.drawable.ic_step_notfinish);
            img3.setVisibility(View.VISIBLE);img3.setImageResource(R.drawable.ic_step_notfinish);
            img4.setVisibility(View.VISIBLE);img4.setImageResource(R.drawable.ic_step_notfinish);
        }
    }

    private  static String Format_Money(String num)
    {
        DecimalFormat format = new DecimalFormat("###,###,###,###,###");
        return format.format(Integer.parseInt(num));
    }
}