package com.example.admingoodfood;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.firestore.Transaction;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import pl.droidsonroids.gif.GifImageView;

public class DonHang extends AppCompatActivity {

    private ImageView btn_back;
    private TextView txt_tieude_acti, txt_madon, txt_thoigiandat, txt_thoigiannhan, txt_tennguoinhan, txt_SDT, txt_diachi, txt_dando, txt_tamtinh, txt_phiship, txt_giamgia, txt_tongtien;
    private Button btn_save;
    private RadioGroup radioGroup;
    private RadioButton radioButton, radio_tiepnhan, radio_xulydon, radio_giaohang, radio_hoanthanhdon;
    private GifImageView img_tinhtrang;
    private GridView gridview_danhsachmonan;
    String madon ="";
    String taikhoan = "";
    String tinhtrang = "";
    String tinhtrang_update = "";

    private FirebaseFirestore database;
    SharedPreferences sharedPreferences;
    ArrayList<DanhSachMonAnKhachChon> Mang_DanhSachMonAnKhachChon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_don_hang);

        database = FirebaseFirestore.getInstance();

        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        txt_tieude_acti = findViewById(R.id.txt_tieude_acti);
        txt_tieude_acti.setText("Chi tiết đơn hàng");
        radioGroup = findViewById(R.id.radioGroup);

        btn_save = findViewById(R.id.btn_save);
        btn_save.setVisibility(View.INVISIBLE);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (!tinhtrang.equals(tinhtrang_update))
               {
                   final DocumentReference DocRef = database.collection("Users").document(taikhoan).collection("Bill").document(madon);
                   database.runTransaction(new Transaction.Function<Void>() {
                       @Override
                       public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                           DocumentSnapshot snapshot = transaction.get(DocRef);
                           transaction.update(DocRef, "tinh_trang", tinhtrang_update);
                           if (tinhtrang_update.equals("Hoàn thành"))
                           {
                               Date currentTime = Calendar.getInstance().getTime();
                               final SimpleDateFormat dateFormat_txt_var = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                               final String dateFormat_txt = dateFormat_txt_var.format(currentTime);
                               transaction.update(DocRef, "thoi_gian_nhan", dateFormat_txt);
                               update_user_info_bill();
                           }
                           btn_save.setVisibility(View.INVISIBLE);
                           return null;
                       }
                   }).addOnSuccessListener(new OnSuccessListener<Void>() {
                       @Override
                       public void onSuccess(Void aVoid) {

                       }
                   })
                   .addOnFailureListener(new OnFailureListener() {
                       @Override
                       public void onFailure(@NonNull Exception e) {
                           Toast.makeText(DonHang.this, "Lỗi kết nối !!!", Toast.LENGTH_SHORT).show();
                       }
                   });
               }
            }
        });


        sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        taikhoan = sharedPreferences.getString("sdt", "");
        Intent intent = getIntent();
        madon = intent.getStringExtra("madon");
        gridview_danhsachmonan = findViewById(R.id.gridview_danhsachmonan);

        txt_madon = findViewById(R.id.txt_madon);
        txt_thoigiandat = findViewById(R.id.txt_thoigiandat);
        txt_thoigiannhan = findViewById(R.id.txt_thoigiannhan);
        txt_tennguoinhan = findViewById(R.id.txt_tennguoinhan);
        txt_SDT = findViewById(R.id.txt_SDT);
        txt_diachi = findViewById(R.id.txt_diachi);
        txt_dando = findViewById(R.id.txt_dando);
        txt_tamtinh = findViewById(R.id.txt_tamtinh);
        txt_phiship = findViewById(R.id.txt_phiship);
        txt_giamgia = findViewById(R.id.txt_giamgia);
        txt_tongtien = findViewById(R.id.txt_tongtien);
        img_tinhtrang = findViewById(R.id.img_tinhtrang);
        radio_tiepnhan = findViewById(R.id.radio_tiepnhan);
        radio_xulydon = findViewById(R.id.radio_xulydon);
        radio_giaohang = findViewById(R.id.radio_giaohang);
        radio_hoanthanhdon = findViewById(R.id.radio_hoanthanhdon);


        final DocumentReference docRef = database.collection("Users").document(taikhoan).collection("Bill").document(madon);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Toast.makeText(DonHang.this, "Lỗi kết nối !!!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    int giamgia = 0, tongtien = 0;

                    Object[] bill_key = (Object[]) snapshot.getData().keySet().toArray();
                    Object[] bill_value = (Object[]) snapshot.getData().values().toArray();

                    txt_madon.setText(madon);
                    txt_phiship.setText("30,000 đ");
                    for (int i = 0; i < bill_key.length; i++)
                    {
                        if (bill_key[i].toString().equals("thoi_gian_dat")) txt_thoigiandat.setText(bill_value[i].toString());
                        if (bill_key[i].toString().equals("thoi_gian_nhan")) txt_thoigiannhan.setText(bill_value[i].toString());
                        if (bill_key[i].toString().equals("nguoi_nhan_hang"))txt_tennguoinhan.setText(bill_value[i].toString());
                        if (bill_key[i].toString().equals("sdt_nhan_hang")) txt_SDT.setText(bill_value[i].toString());
                        if (bill_key[i].toString().equals("dia_chi")) txt_diachi.setText(bill_value[i].toString());
                        if (bill_key[i].toString().equals("ghi_chu"))
                        {
                            if (!bill_value[i].toString().equals(""))
                            {
                                txt_dando.setText(bill_value[i].toString());
                            }
                            else txt_dando.setText("Không có");
                        }
                        if (bill_key[i].toString().equals("tong_tien")) tongtien = Integer.parseInt(bill_value[i].toString());
                        if (bill_key[i].toString().equals("giam_gia")) giamgia = Integer.parseInt(bill_value[i].toString());
                        if (bill_key[i].toString().equals("tinh_trang")) tinhtrang = bill_value[i].toString();
                    }
                    txt_tamtinh.setText(Format_Money(String.valueOf(tongtien+giamgia)) + " đ");
                    txt_giamgia.setText("-" + Format_Money(String.valueOf(giamgia)) + " đ");
                    txt_tongtien.setText(Format_Money(String.valueOf(tongtien)) + " đ");

                    if (tinhtrang.equals("Tiếp nhận đơn"))
                    {
                        img_tinhtrang.setImageResource(R.drawable.ic_show_step_tiepnhan);
                        radio_tiepnhan.setChecked(true);
                        show_enable_radio(radio_xulydon, radio_giaohang, radio_hoanthanhdon);
                    }
                    else if (tinhtrang.equals("Xử lý đơn"))
                    {
                        img_tinhtrang.setImageResource(R.drawable.ic_show_step_nauan);
                        radio_xulydon.setChecked(true);
                        show_unenable_radio(radio_tiepnhan,radio_tiepnhan,radio_tiepnhan);
                        show_enable_radio(radio_giaohang, radio_giaohang, radio_hoanthanhdon);
                    }
                    else if (tinhtrang.equals("Giao hàng"))
                    {
                        img_tinhtrang.setImageResource(R.drawable.ic_show_step_giaohang);
                        radio_giaohang.setChecked(true);
                        show_unenable_radio(radio_tiepnhan,radio_xulydon,radio_xulydon);
                        show_enable_radio(radio_hoanthanhdon, radio_hoanthanhdon, radio_hoanthanhdon);
                    }
                    else
                    {
                        img_tinhtrang.setImageResource(R.drawable.ic_show_step_hoanthanh);
                        radio_hoanthanhdon.setChecked(true);
                        show_unenable_radio(radio_tiepnhan,radio_xulydon,radio_giaohang);
                    }
                }
            }
        });

        Mang_DanhSachMonAnKhachChon = new ArrayList<DanhSachMonAnKhachChon>();
        database.collection("Users").document(taikhoan).collection("Bill").document(madon).collection("Detail_Bill").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful())
                {
                    for (QueryDocumentSnapshot document : task.getResult())
                    {
                        String sl = "", thanhtien = "";
                        Object[] chitiet_bill_key = (Object[]) document.getData().keySet().toArray();
                        Object[] chitiet_bill_value = (Object[]) document.getData().values().toArray();
                        String tenmon = document.getId();
                        for (int i = 0; i < chitiet_bill_key.length; i++)
                        {
                            if (chitiet_bill_key[i].toString().equals("so_luong")) sl = chitiet_bill_value[i].toString();
                            if (chitiet_bill_key[i].toString().equals("thanh_tien")) thanhtien = chitiet_bill_value[i].toString();
                        }
                        Mang_DanhSachMonAnKhachChon.add(new DanhSachMonAnKhachChon(tenmon, sl,"", Format_Money(thanhtien)));
                    }

                    DanhSachMonAnAdapter adapter_monan_detail  =  new DanhSachMonAnAdapter(DonHang.this, R.layout.custom_danhsachmonan_donhang, Mang_DanhSachMonAnKhachChon);
                    gridview_danhsachmonan.setAdapter(adapter_monan_detail);

                    ViewGroup.LayoutParams params_gridview_danhsachmonan = gridview_danhsachmonan.getLayoutParams();
                    params_gridview_danhsachmonan.height = params_gridview_danhsachmonan.height * Mang_DanhSachMonAnKhachChon.size();
                    gridview_danhsachmonan.setLayoutParams(params_gridview_danhsachmonan);
                }
                else
                {
                    Toast.makeText(DonHang.this, "Lỗi kết nối !!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    void update_user_info_bill()
    {
        final DocumentReference DocRef = database.collection("Users").document(taikhoan);
        database.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                DocumentSnapshot snapshot = transaction.get(DocRef);

                int new_so_don_dat_hang = Integer.parseInt(snapshot.getString("so_don_dat_hang")) + 1;
                transaction.update(DocRef, "so_don_dat_hang", String.valueOf(new_so_don_dat_hang));

                if (new_so_don_dat_hang > 50)
                {
                    transaction.update(DocRef, "type_member", "VIP member");
                }

                int new_so_don_tich_luy = Integer.parseInt(snapshot.getString("so_don_tich_luy")) + 1;
                transaction.update(DocRef, "so_don_tich_luy", String.valueOf(new_so_don_tich_luy));
                return null;
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                update_soluotmua();
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(DonHang.this, "Lỗi kết nối !!!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    void update_soluotmua()
    {
        for (int i = 0; i < Mang_DanhSachMonAnKhachChon.size(); i++)
        {
            final DocumentReference DocRef = database.collection("Food").document(Mang_DanhSachMonAnKhachChon.get(i).getTenmon());
            database.runTransaction(new Transaction.Function<Void>() {
                @Override
                public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                    DocumentSnapshot snapshot = transaction.get(DocRef);

                    int new_so_luot_mua = Integer.parseInt(snapshot.getString("so_luot_mua")) + 1;
                    transaction.update(DocRef, "so_luot_mua", String.valueOf(new_so_luot_mua));
                    return null;
                }
            }).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(DonHang.this, "Đơn hàng đã hoàn thành", Toast.LENGTH_SHORT).show();
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(DonHang.this, "Lỗi kết nối !!!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    public void checkButton(View v)
    {
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);

        btn_save.setVisibility(View.VISIBLE);
        if (radioButton.getText().equals(tinhtrang))
        {
            btn_save.setVisibility(View.INVISIBLE);
        }

        if (radioButton.getText().equals("Tiếp nhận đơn"))
        {
            img_tinhtrang.setImageResource(R.drawable.ic_show_step_tiepnhan);
            radio_tiepnhan.setChecked(true);
            tinhtrang_update = "Tiếp nhận đơn";
        }
        else if (radioButton.getText().equals("Xử lý đơn"))
        {
            img_tinhtrang.setImageResource(R.drawable.ic_show_step_nauan);
            radio_xulydon.setChecked(true);
            tinhtrang_update = "Xử lý đơn";
        }
        else if (radioButton.getText().equals("Giao hàng"))
        {
            img_tinhtrang.setImageResource(R.drawable.ic_show_step_giaohang);
            radio_giaohang.setChecked(true);
            tinhtrang_update = "Giao hàng";
        }
        else if (radioButton.getText().equals("Hoàn thành"))
        {
            img_tinhtrang.setImageResource(R.drawable.ic_show_step_hoanthanh);
            radio_hoanthanhdon.setChecked(true);
            tinhtrang_update = "Hoàn thành";
        }
    }

    private void show_enable_radio(RadioButton rd1, RadioButton rd2, RadioButton rd3)
    {
        rd1.setEnabled(true);
        rd2.setEnabled(true);
        rd3.setEnabled(true);
    }

    private void show_unenable_radio(RadioButton rd1, RadioButton rd2, RadioButton rd3)
    {
        rd1.setEnabled(false);
        rd2.setEnabled(false);
        rd3.setEnabled(false);
    }

    private  static String Format_Money(String num)
    {
        DecimalFormat format = new DecimalFormat("###,###,###,###,###");
        return format.format(Integer.parseInt(num));
    }
}