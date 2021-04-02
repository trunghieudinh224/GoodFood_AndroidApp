package com.example.goodfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class TimKiemMonAn extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private ImageView btn_back_timkiemmonan;
    private TextView txt_ketquatimkiem;
    private EditText edittext_timkiem;
    private ListView listview_danhsachtimkiem;
    private LinearLayout area_loading_search;
    private ImageButton btn_search;

    private String LoaiTimKiem;
    private String taikhoan;

    private FirebaseFirestore database;
    SharedPreferences sharedPreferences;
    Kitchen_FoodAdapter adapter;

    Timer timer;

    ArrayList<Kitchen_Food> MangKitchen;
    private ArrayList<DanhSachMonAnKhachChon> listMonAnKhachChon = new ArrayList<DanhSachMonAnKhachChon>();
    final boolean[] yeuthich = {false};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tim_kiem_mon_an);

        database = FirebaseFirestore.getInstance();
        sharedPreferences = getSharedPreferences("datalogin", MODE_PRIVATE);
        taikhoan = sharedPreferences.getString("Phone", "");

        btn_back_timkiemmonan = findViewById(R.id.btn_back_timkiemmonan);
        btn_back_timkiemmonan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        area_loading_search = findViewById(R.id.area_loading_search);

        btn_search = findViewById(R.id.btn_search);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showResult(edittext_timkiem.getText().toString().trim());
            }
        });


        listview_danhsachtimkiem = findViewById(R.id.listview_danhsachtimkiem);

        Intent intent = getIntent();
        LoaiTimKiem = intent.getStringExtra("LoaiTimKiem");
        if (LoaiTimKiem.equals("danhmuc"))
        {
            area_loading_search.setVisibility(View.VISIBLE);
            final String ten_danhmuc = intent.getStringExtra("DanhMuc");

            MangKitchen = new ArrayList<Kitchen_Food>();
            database.collection("Food").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful())
                    {
                        for (DocumentSnapshot document : task.getResult())
                        {
                            String ten_mon = document.getId();
                            Object[] Monan_array_key = (Object[]) document.getData().keySet().toArray();
                            Object[] Monan_array_value = (Object[]) document.getData().values().toArray();

                            boolean get = false;
                            for (int i = 0; i < Monan_array_value.length; i++)
                            {
                                if (Monan_array_value[i].toString().equals(ten_danhmuc)) {
                                    get = true;
                                }
                            }

                            String gia_tien = "", so_luot_mua = "", so_luot_danh_gia_tot = "", so_luot_danh_gia_khong_tot = "", link_hinh = "", danh_muc = "";
                            if (get)
                            {
                                for (int i = 0; i < Monan_array_key.length; i++)
                                {
                                    if (Monan_array_key[i].toString().equals("gia_tien")) gia_tien = Monan_array_value[i].toString();
                                    if (Monan_array_key[i].toString().equals("so_luot_mua")) so_luot_mua = Monan_array_value[i].toString();
                                    if (Monan_array_key[i].toString().equals("so_luot_danh_gia_tot")) so_luot_danh_gia_tot = Monan_array_value[i].toString();
                                    if (Monan_array_key[i].toString().equals("so_luot_danh_gia_khong_tot")) so_luot_danh_gia_khong_tot = Monan_array_value[i].toString();
                                    if (Monan_array_key[i].toString().equals("link_hinh")) link_hinh = Monan_array_value[i].toString();
                                    if (Monan_array_key[i].toString().equals("danh_muc")) danh_muc = Monan_array_value[i].toString();
                                }
                                MangKitchen.add(new Kitchen_Food(ten_mon, gia_tien, so_luot_mua,so_luot_danh_gia_tot,so_luot_danh_gia_khong_tot, link_hinh, danh_muc));
                            }
                        }

                        area_loading_search.setVisibility(View.INVISIBLE);
                        txt_ketquatimkiem.setText("Các sản phẩm của danh mục '" + ten_danhmuc +"'");
                        txt_ketquatimkiem.setVisibility(View.VISIBLE);

                        set_listview_danhsachtimkiem(MangKitchen);

                    } else {
                        Toast.makeText(TimKiemMonAn.this, "Lỗi kết nối !!!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }


        txt_ketquatimkiem = findViewById(R.id.txt_ketquatimkiem);

        edittext_timkiem = findViewById(R.id.edittext_timkiem);
        edittext_timkiem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                area_loading_search.setVisibility(View.VISIBLE);
                if (MangKitchen != null)
                {
                    if (MangKitchen.size() > 0)
                    {
                        MangKitchen.clear();
                        adapter.notifyDataSetChanged();
                        txt_ketquatimkiem.setVisibility(View.INVISIBLE);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    void set_listview_danhsachtimkiem(ArrayList<Kitchen_Food> Kitchen_array)
    {
        adapter  =  new Kitchen_FoodAdapter(this, R.layout.custom_listview_kitchen, Kitchen_array);
        listview_danhsachtimkiem.setAdapter(adapter);
        ViewGroup.LayoutParams params_listview_danhsachtimkiem = listview_danhsachtimkiem.getLayoutParams();
        params_listview_danhsachtimkiem.height = params_listview_danhsachtimkiem.height * Kitchen_array.size();
        listview_danhsachtimkiem.setLayoutParams(params_listview_danhsachtimkiem);


        listview_danhsachtimkiem.setOnItemClickListener(this);
    }

    private void showResult(final String tenmonan)
    {
        if (!tenmonan.equals(""))
        {
            final TextView txt_ketquatimkiem = findViewById(R.id.txt_ketquatimkiem);

            final String ten_mon_format = tenmonan.substring(0,1).toUpperCase() + tenmonan.substring(1);
            MangKitchen = new ArrayList<Kitchen_Food>();
            final ArrayList<MonAn_Details> list = new ArrayList<>();
            DocumentReference docRef = database.collection("Food").document(ten_mon_format);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String ten_mon = document.getId();
                            if (ten_mon_format.equals(ten_mon))
                            {
                                Object[] Monan_array_key = (Object[]) document.getData().keySet().toArray();
                                Object[] Monan_array_value = (Object[]) document.getData().values().toArray();

                                String gia_tien = "", so_luot_mua = "", so_luot_danh_gia_tot = "", so_luot_danh_gia_khong_tot = "", link_hinh = "", danh_muc = "";
                                for (int i = 0; i < Monan_array_key.length; i++)
                                {
                                    if (Monan_array_key[i].toString().equals("gia_tien")) gia_tien = Monan_array_value[i].toString();
                                    if (Monan_array_key[i].toString().equals("so_luot_mua")) so_luot_mua = Monan_array_value[i].toString();
                                    if (Monan_array_key[i].toString().equals("so_luot_danh_gia_tot")) so_luot_danh_gia_tot = Monan_array_value[i].toString();
                                    if (Monan_array_key[i].toString().equals("so_luot_danh_gia_khong_tot")) so_luot_danh_gia_khong_tot = Monan_array_value[i].toString();
                                    if (Monan_array_key[i].toString().equals("link_hinh")) link_hinh = Monan_array_value[i].toString();
                                    if (Monan_array_key[i].toString().equals("danh_muc")) danh_muc = Monan_array_value[i].toString();
                                }
                                MangKitchen.add(new Kitchen_Food(ten_mon, gia_tien, so_luot_mua,so_luot_danh_gia_tot,so_luot_danh_gia_khong_tot, link_hinh, danh_muc));

                                if (MangKitchen.size() > 0)
                                {
                                    area_loading_search.setVisibility(View.INVISIBLE);
                                    txt_ketquatimkiem.setVisibility(View.VISIBLE);
                                    txt_ketquatimkiem.setText(String.valueOf("#" + MangKitchen.size()) + " kết quả được tìm thấy");
                                }

                                set_listview_danhsachtimkiem(MangKitchen);
                            }
                        } else {
                            area_loading_search.setVisibility(View.INVISIBLE);
                            txt_ketquatimkiem.setVisibility(View.VISIBLE);
                            txt_ketquatimkiem.setText("0 kết quả phù hợp ");
                        }
                    } else {

                    }
                }
            });
        }
    }


    void add_fav_food(String phone, String monan, final ImageView btn_yeuthich, final TextView txt_noti_fv_food)
    {
        yeuthich[0] = true;
        Map<String, Object> fv_food = new HashMap<>();
        database.collection("Users").document(phone).collection("fv_food").document(monan).set(fv_food).addOnSuccessListener(new OnSuccessListener<Void>() {
            @SuppressLint("ResourceType")
            @Override
            public void onSuccess(Void aVoid) {
                btn_yeuthich.setImageResource(R.drawable.ic_favorite);
                txt_noti_fv_food.setText("Yêu thích");
                txt_noti_fv_food.setVisibility(View.VISIBLE);
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        txt_noti_fv_food.setVisibility(View.INVISIBLE);
                    }
                }, 1500);
                btn_yeuthich.setEnabled(true);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(TimKiemMonAn.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void delete_fav_food(String phone, String monan, final ImageView btn_yeuthich, final TextView txt_noti_fv_food)
    {
        yeuthich[0] = false;
        database.collection("Users").document(phone).collection("fv_food").document(monan).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @SuppressLint("ResourceType")
            @Override
            public void onSuccess(Void aVoid) {
                btn_yeuthich.setImageResource(R.drawable.ic_not_favorite);
                txt_noti_fv_food.setText("Bỏ yêu thích");
                txt_noti_fv_food.setVisibility(View.VISIBLE);
                timer = new Timer();

                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        txt_noti_fv_food.setVisibility(View.INVISIBLE);
                    }
                }, 1500);
                btn_yeuthich.setEnabled(true);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(TimKiemMonAn.this, "Lỗi kết nối !!!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

        yeuthich[0] = false;
        final TextView txt_ten_fvfood = view.findViewById(R.id.txt_ten_fvfood);
        final ImageView img_fvfood = view.findViewById(R.id.img_fvfood);
        Bitmap bm = ((BitmapDrawable)img_fvfood.getDrawable()).getBitmap();
        String giatien = MangKitchen.get(position).getGia_tien();

        final String tenmon = txt_ten_fvfood.getText().toString().trim();

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(TimKiemMonAn.this, R.style.BottomSheetDialogTheme);
        bottomSheetDialog.setCanceledOnTouchOutside(false);
        final View bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.dialog_chitietmonan, (LinearLayout)findViewById(R.id.acti_chitietmonan));
        final TextView txt_soluong = bottomSheetView.findViewById(R.id.txt_soluong);
        final CircularImageView img_monan = bottomSheetView.findViewById(R.id.img_monan);
        img_monan.setImageBitmap(bm);
        final ImageView btn_close = bottomSheetView.findViewById(R.id.btn_close);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });
        img_monan.setImageBitmap(bm);

        final TextView txt_tenmonan = bottomSheetView.findViewById(R.id.txt_tenmonan);
        txt_tenmonan.setText(tenmon);
        final TextView txt_giatien = bottomSheetView.findViewById(R.id.txt_giatien);
        txt_giatien.setText(Format_Money(giatien) + " đ");

        final ImageView btn_yeuthich = bottomSheetView.findViewById(R.id.btn_yeuthich);
        final TextView txt_noti_fv_food = bottomSheetView.findViewById(R.id.txt_noti_fv_food);


        database.collection("Users").document(taikhoan).collection("fv_food")
        .get()
        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (document.getId().equals(tenmon)) {
                            yeuthich[0] = true;
                            btn_yeuthich.setImageResource(R.drawable.ic_favorite);
                        }
                    }

                    if (yeuthich[0] == false)
                    {
                        btn_yeuthich.setImageResource(R.drawable.ic_not_favorite);
                    }
                } else {
                    Toast.makeText(TimKiemMonAn.this, "Lỗi kết nối !!!", Toast.LENGTH_SHORT).show();
                }
            }
        });




        btn_yeuthich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_yeuthich.setEnabled(false);
                if (yeuthich[0] == false)
                {
                    add_fav_food(taikhoan, tenmon, btn_yeuthich, txt_noti_fv_food);
                }
                else
                {
                    delete_fav_food(taikhoan, tenmon, btn_yeuthich, txt_noti_fv_food);
                }
            }
        });

        bottomSheetView.findViewById(R.id.btn_xoasl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int sl = Integer.parseInt(txt_soluong.getText().toString().trim());

                if (sl > 1)
                {
                    sl--;
                    txt_soluong.setText(String.valueOf(sl));
                }
            }
        });

        bottomSheetView.findViewById(R.id.btn_themsl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int sl = Integer.parseInt(txt_soluong.getText().toString().trim());

                if (sl >= 1 && sl < 99)
                {
                    sl++;
                    txt_soluong.setText(String.valueOf(sl));
                }
            }
        });

        bottomSheetView.findViewById(R.id.btn_ThemMon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int sl = Integer.parseInt(txt_soluong.getText().toString().trim());
                int giatien = Integer.parseInt(txt_giatien.getText().toString().trim().replace(",","").replace(" đ",""));
                int thanhtien = sl*giatien;

                listMonAnKhachChon.add(new DanhSachMonAnKhachChon(tenmon, String.valueOf(sl), String.valueOf(giatien), String.valueOf(thanhtien)));

                Intent resultIntent = new Intent();
                Bundle args = new Bundle();
                args.putSerializable("ARRAYLIST",(Serializable)listMonAnKhachChon);
                resultIntent.putExtra("list",args);
                setResult(Activity.RESULT_OK, resultIntent);

                bottomSheetDialog.dismiss();
                finish();
            }
        });

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }

    private  static String Format_Money(String num)
    {
        DecimalFormat format = new DecimalFormat("###,###,###,###,###");
        return format.format(Integer.parseInt(num));
    }
}