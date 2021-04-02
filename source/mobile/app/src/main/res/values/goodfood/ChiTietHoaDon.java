package com.example.goodfood;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
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

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import pl.droidsonroids.gif.GifImageView;

public class ChiTietHoaDon extends AppCompatActivity {

    private GridView gridview_danhsachmonan;
    private TextView txt_tongtien_chitiethoadon, txt_phiship, txt_diachi_chitiethoadon, txt_thoigiandathang, txt_tonggiamgia;
    private ImageView btn_back_chitietdonhang, btn_danhsach_voucher, btn_xacnhanvoucher;
    private EditText edittext_voucher_code, txt_SDT_chitiethoadon, txt_nguoinhan_chitiethoadon, txt_dando_chitiethoadon;
    private Button btn_thanhtoan;

    ArrayList<DanhSachMonAnKhachChon> Mang_DanhSachMonAnKhachChon;
    ArrayList<Voucher> MangVoucher;
    SharedPreferences sharedPreferences;
    private FirebaseFirestore database;
    Timer timer;
    private int TongTien = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_hoa_don);

        database = FirebaseFirestore.getInstance();
        sharedPreferences = getSharedPreferences("datalogin", MODE_PRIVATE);
        final String taikhoan = sharedPreferences.getString("Phone", "");
        final int SoBillDangThucHien = sharedPreferences.getInt("SoBillDangThucHien", 0);

        txt_diachi_chitiethoadon = findViewById(R.id.txt_diachi_chitiethoadon);
        txt_SDT_chitiethoadon = findViewById(R.id.txt_SDT_chitiethoadon);
        txt_nguoinhan_chitiethoadon = findViewById(R.id.txt_nguoinhan_chitiethoadon);
        txt_dando_chitiethoadon = findViewById(R.id.txt_dando_chitiethoadon);
        txt_tonggiamgia = findViewById(R.id.txt_tonggiamgia);

        LayoutInflater inflater = LayoutInflater.from(ChiTietHoaDon.this);
        View viewalert = inflater.inflate(R.layout.alert_thongbaochung, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(ChiTietHoaDon.this).setView(viewalert).create();
        alertDialog.setCanceledOnTouchOutside(false);
        final TextView txt_loading_noti = viewalert.findViewById(R.id.txt_loading_noti);
        final TextView txt_noidung_noti = viewalert.findViewById(R.id.txt_noidung_noti);
        final GifImageView gif_loading_chung = viewalert.findViewById(R.id.gif_loading_chung);
        final Button btn_accept = viewalert.findViewById(R.id.btn_accept);
        final Button btn_not_accept = viewalert.findViewById(R.id.btn_not_accept);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        database.collection("Users").document(taikhoan).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful())
                {
                    DocumentSnapshot document = task.getResult();
                    Object[] Users_key = (Object[]) document.getData().keySet().toArray();
                    Object[] Users_value = (Object[]) document.getData().values().toArray();

                    txt_SDT_chitiethoadon.setText(taikhoan);
                    for (int i = 0; i < Users_key.length; i++)
                    {
                        if (Users_key[i].toString().equals("address")) txt_diachi_chitiethoadon.setText(Users_value[i].toString());
                        if (Users_key[i].toString().equals("name")) txt_nguoinhan_chitiethoadon.setText(Users_value[i].toString());
                    }
                }
                else
                {
                    Toast.makeText(ChiTietHoaDon.this, "Lỗi kết nối !!!", Toast.LENGTH_SHORT).show();
                }
            }
        });


        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("list");
        ArrayList<DanhSachMonAnKhachChon> object = (ArrayList<DanhSachMonAnKhachChon>) args.getSerializable("ARRAYLIST");

        txt_thoigiandathang = findViewById(R.id.txt_thoigiandathang);
        Date currentTime = Calendar.getInstance().getTime();
        final SimpleDateFormat dateFormat_full_var = new SimpleDateFormat("'D'ddMMyyyy'T'HHmmss", Locale.getDefault());
        final SimpleDateFormat dateFormat_txt_var = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        final String dateFormat_full = dateFormat_full_var.format(currentTime);
        final String dateFormat_txt = dateFormat_txt_var.format(currentTime);
        txt_thoigiandathang.setText(dateFormat_txt);

        btn_back_chitietdonhang = findViewById(R.id.btn_back_chitietdonhang);
        btn_back_chitietdonhang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resultIntent = new Intent();
                Bundle args = new Bundle();
                args.putSerializable("ARRAYLIST",(Serializable)Mang_DanhSachMonAnKhachChon);
                resultIntent.putExtra("list",args);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });



        gridview_danhsachmonan = findViewById(R.id.gridview_danhsachmonan);
        Mang_DanhSachMonAnKhachChon = new ArrayList<DanhSachMonAnKhachChon>();

        for (int i = 0 ; i < object.size() ; i++)
        {
            DanhSachMonAnKhachChon obj = object.get(i);
            if (obj instanceof DanhSachMonAnKhachChon) {
                DanhSachMonAnKhachChon cls1 = (DanhSachMonAnKhachChon)obj;
                String tenmon = cls1.getTenmon();
                String sl  = cls1.getSl();
                String thanhtien = cls1.getThanhtien();

                TongTien = TongTien + Integer.parseInt(thanhtien);

                Mang_DanhSachMonAnKhachChon.add(new DanhSachMonAnKhachChon(tenmon, sl,"", Format_Money(thanhtien)));
            }
        }
        DanhSachMonAnKhachChonAdapter adapter  =  new DanhSachMonAnKhachChonAdapter(this, R.layout.custom_danhsachmonan_chitiethoadon, Mang_DanhSachMonAnKhachChon);
        gridview_danhsachmonan.setAdapter(adapter);
        ViewGroup.LayoutParams params_gridview_danhsachmonan = gridview_danhsachmonan.getLayoutParams();
        params_gridview_danhsachmonan.height = params_gridview_danhsachmonan.height * Mang_DanhSachMonAnKhachChon.size();
        gridview_danhsachmonan.setLayoutParams(params_gridview_danhsachmonan);


        edittext_voucher_code = findViewById(R.id.edittext_voucher_code);
        edittext_voucher_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                edittext_voucher_code.setBackground(getDrawable(R.drawable.custom_edittext_voucher_chitiethoadon_editting));
                txt_tongtien_chitiethoadon.setText(Format_Money(String.valueOf(TongTien + 30000)) + " đ");
                txt_tonggiamgia.setText("0 đ");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btn_xacnhanvoucher = findViewById(R.id.btn_xacnhanvoucher);
        btn_xacnhanvoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = edittext_voucher_code.getText().toString().trim();
                if (code.length() > 0)
                {
                    MangVoucher = new ArrayList<Voucher>();
                    database.collection("Voucher").document(code).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful())
                            {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
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

                                    Check_Voucher(ma_voucher,Integer.parseInt(gia_tri_don_nho_nhat),Integer.parseInt(giam_phan_tram),Integer.parseInt(giam_tien),Integer.parseInt(giam_toi_da),hsd,loai_giam_gia,loai_voucher,TongTien);
                                }
                                else
                                {
                                    txt_loading_noti.setText("VOUCHER");
                                    txt_noidung_noti.setText("Mã voucher không tồn tại");
                                    gif_loading_chung.setImageResource(R.drawable.ic_error_loading);
                                    alertDialog.show();
                                    timer = new Timer();
                                    timer.schedule(new TimerTask() {
                                        @Override
                                        public void run() {
                                            alertDialog.dismiss();
                                        }
                                    }, 1600);
                                }
                            }
                            else
                            {
                                Toast.makeText(ChiTietHoaDon.this, "Lỗi kết nối !!!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else
                {
                    Toast.makeText(ChiTietHoaDon.this, "Vui lòng nhập mã voucher", Toast.LENGTH_SHORT).show();
                }
            }
        });

        txt_phiship = findViewById(R.id.txt_phiship);
        txt_phiship.setText(Format_Money("30000") + " đ");

        txt_tongtien_chitiethoadon = findViewById(R.id.txt_tongtien_chitiethoadon);
        txt_tongtien_chitiethoadon.setText(Format_Money(String.valueOf(TongTien + 30000)) + " đ");

        btn_danhsach_voucher = findViewById(R.id.btn_danhsach_voucher);
        btn_danhsach_voucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_voucher = new Intent(ChiTietHoaDon.this, ViVoucher.class);
                intent_voucher.putExtra("activity", "chitiethoadon");
                intent_voucher.putExtra("tongtien", TongTien);
                startActivityForResult(intent_voucher, 1);
            }
        });

        btn_thanhtoan = findViewById(R.id.btn_thanhtoan);
        btn_thanhtoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                txt_loading_noti.setText("THANH TOÁN");
                txt_noidung_noti.setText("Bạn muốn thực hiện thanh toán ?");
                gif_loading_chung.setImageResource(R.drawable.ic_error_loading);
                btn_not_accept.setVisibility(View.VISIBLE);
                btn_accept.setVisibility(View.VISIBLE);
                alertDialog.show();

                btn_accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Map<String, Object> hoadon = new HashMap<>();
                        hoadon.put("ngay_dat", dateFormat_txt);
                        hoadon.put("tinh_trang", "Tiếp nhận đơn");
                        hoadon.put("tong_tien", txt_tongtien_chitiethoadon.getText().toString().replace(",", "").replace(" đ", ""));
                        hoadon.put("sl_mon", String.valueOf(Mang_DanhSachMonAnKhachChon.size()));
                        hoadon.put("dia_chi", txt_diachi_chitiethoadon.getText().toString().trim());
                        hoadon.put("sdt_nhan_hang", txt_SDT_chitiethoadon.getText().toString().trim());
                        hoadon.put("nguoi_nhan_hang", txt_nguoinhan_chitiethoadon.getText().toString().trim());
                        hoadon.put("ghi_chu", txt_dando_chitiethoadon.getText().toString().trim());
                        hoadon.put("giam_gia", txt_tonggiamgia.getText().toString().trim().replace("-", "").replace(",", "").replace(" đ", ""));
                        hoadon.put("check_danh_gia", "unchecked");
                        if (txt_tonggiamgia.getText().toString().equals("0 đ"))
                        {
                            hoadon.put("ma_voucher", edittext_voucher_code.getText().toString().trim());
                        }
                        else
                        {
                            hoadon.put("ma_voucher", "");
                        }

                        database.collection("Users").document(taikhoan).collection("Bill").document(dateFormat_full).set(hoadon).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                get_soluotmua();
                                for (int i = 0; i < Mang_DanhSachMonAnKhachChon.size(); i++)
                                {
                                    create_chitietdonhang(taikhoan,dateFormat_full, Mang_DanhSachMonAnKhachChon.get(i).getTenmon(), Mang_DanhSachMonAnKhachChon.get(i).getSl(),  Mang_DanhSachMonAnKhachChon.get(i).getThanhtien());
                                }

                                if (SoBillDangThucHien <= 2)
                                {
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putInt("SoBillDangThucHien", SoBillDangThucHien+1);
                                    editor.commit();
                                }


                                Intent resultIntent = new Intent();
                                resultIntent.putExtra("ThanhToan",true);
                                resultIntent.putExtra("MaDon",dateFormat_full);
                                setResult(Activity.RESULT_OK, resultIntent);
                                alertDialog.dismiss();
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ChiTietHoaDon.this, "Lỗi kết nối !!!", Toast.LENGTH_SHORT).show();
                            }
                        });
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
    }

    void create_chitietdonhang(String taikhoan, String madon, String tenmon, String sl, String thanhtien)
    {
        Map<String, Object> chitiet_hoadon = new HashMap<>();
        chitiet_hoadon.put("so_luong", sl);
        chitiet_hoadon.put("thanh_tien", thanhtien.replace(",",""));

        database.collection("Users").document(taikhoan).collection("Bill").document(madon).collection("Detail_Bill").document(tenmon).set(chitiet_hoadon).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ChiTietHoaDon.this, "Lỗi kết nối !!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void get_soluotmua()
    {
        sharedPreferences = getSharedPreferences("datalogin", MODE_PRIVATE);
        final String taikhoan = sharedPreferences.getString("Phone", "");

        for (int i = 0; i<Mang_DanhSachMonAnKhachChon.size(); i++)
        {
            final String tenmon = Mang_DanhSachMonAnKhachChon.get(i).getTenmon();
            database.collection("Food").document(tenmon).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful())
                    {
                        String so_luot_mua = "";
                        DocumentSnapshot document = task.getResult();
                        Object[] Food_key = (Object[]) document.getData().keySet().toArray();
                        Object[] Food_value = (Object[]) document.getData().values().toArray();
                        for (int i = 0; i < Food_key.length; i++)
                        {
                            if (Food_key[i].toString().equals("so_luot_mua")) so_luot_mua = String.valueOf(Integer.parseInt(Food_value[i].toString()) + 1);
                            update_soluotmua(tenmon, so_luot_mua);
                        }
                    }
                    else
                    {
                        Toast.makeText(ChiTietHoaDon.this, "Lỗi kết nối !!!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    void update_soluotmua(String tenmon, String so_luot_mua)
    {
        DocumentReference washingtonRef = database.collection("Food").document(tenmon);
        washingtonRef.update("so_luot_mua", so_luot_mua).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ChiTietHoaDon.this, "Lỗi kết nối !!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private  static String Format_Money(String num)
    {
        DecimalFormat format = new DecimalFormat("###,###,###,###,###");
        return format.format(Integer.parseInt(num));
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                String code = data.getStringExtra("code");
                EditText edittext_voucher_code = findViewById(R.id.edittext_voucher_code);
                edittext_voucher_code.setText(code);

                int tiensaukhigiam = data.getIntExtra("tiensaukhigiam", 0);
                TextView txt_tongtien_chitiethoadon = findViewById(R.id.txt_tongtien_chitiethoadon);
                txt_tongtien_chitiethoadon.setText(Format_Money(String.valueOf(tiensaukhigiam)) + " đ");

                int tiengiam = data.getIntExtra("tiengiam", 0);
                TextView txt_tonggiamgia = findViewById(R.id.txt_tonggiamgia);
                txt_tonggiamgia.setText(Format_Money("-" + String.valueOf(tiengiam)) + " đ");

                edittext_voucher_code.setBackground(getDrawable(R.drawable.custom_edittext_voucher_chitiethoadon));
            }
        }
    }

    private void Check_Voucher(String ma_voucher, int gia_tri_don_nho_nhat, int giam_phan_tram, int giam_tien, int giam_toi_da, String hsd , String loai_giam_gia, String loai_voucher, final int TongTien)
    {
        LayoutInflater inflater = LayoutInflater.from(ChiTietHoaDon.this);
        View viewalert = inflater.inflate(R.layout.alert_thongbaochung, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(ChiTietHoaDon.this).setView(viewalert).create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final TextView txt_loading_noti = viewalert.findViewById(R.id.txt_loading_noti);
        final TextView txt_noidung_noti = viewalert.findViewById(R.id.txt_noidung_noti);
        final GifImageView gif_loading_chung = viewalert.findViewById(R.id.gif_loading_chung);

        Date ngay_hien_tai = null;
        Date ngay_hsd = null;
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
                            edittext_voucher_code.setBackground(getDrawable(R.drawable.custom_edittext_voucher_chitiethoadon_editting));
                            GiamGia_Edittext(TongTien, giam_toi_da);
                        }
                        else
                        {
                            edittext_voucher_code.setBackground(getDrawable(R.drawable.custom_edittext_voucher_chitiethoadon_editting));
                            GiamGia_Edittext(TongTien, tien_giam);
                        }
                    }
                    else
                    {
                        edittext_voucher_code.setBackground(getDrawable(R.drawable.custom_edittext_voucher_chitiethoadon_editting));
                        GiamGia_Edittext(TongTien, giam_tien);
                    }
                }
                else
                {
                    txt_loading_noti.setText("VOUCHER");
                    txt_noidung_noti.setText("Giá trị đơn hàng chưa đạt yêu cầu voucher");
                    gif_loading_chung.setImageResource(R.drawable.ic_error_loading);
                    alertDialog.show();
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            alertDialog.dismiss();
                        }
                    }, 1600);
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
                            edittext_voucher_code.setBackground(getDrawable(R.drawable.custom_edittext_voucher_chitiethoadon_editting));
                            GiamGia_Edittext(TongTien, giam_toi_da);
                        }
                        else
                        {
                            edittext_voucher_code.setBackground(getDrawable(R.drawable.custom_edittext_voucher_chitiethoadon_editting));
                            GiamGia_Edittext(TongTien, tien_giam);
                        }
                    }
                    else
                    {
                        edittext_voucher_code.setBackground(getDrawable(R.drawable.custom_edittext_voucher_chitiethoadon_editting));
                        GiamGia_Edittext(TongTien, giam_tien);
                    }
                }
                else
                {
                    txt_loading_noti.setText("VOUCHER");
                    txt_noidung_noti.setText("Giá trị đơn hàng chưa đạt yêu cầu voucher");
                    gif_loading_chung.setImageResource(R.drawable.ic_error_loading);
                    alertDialog.show();
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            alertDialog.dismiss();
                        }
                    }, 1600);
                }
            }
        }
        else
        {
            txt_loading_noti.setText("VOUCHER");
            txt_noidung_noti.setText("Voucher đã hết hạn sử dụng");
            gif_loading_chung.setImageResource(R.drawable.ic_error_loading);
            alertDialog.show();
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    alertDialog.dismiss();
                }
            }, 1600);
        }
    }

    public void GiamGia_Edittext (int TongTien, int money)
    {
        txt_tongtien_chitiethoadon.setText(Format_Money(String.valueOf(TongTien - money + 30000)) + " đ");
        txt_tonggiamgia.setText("-" + Format_Money(String.valueOf(money)) + " đ");
    }
}