package com.example.goodfood;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mikhaellopez.circularimageview.CircularImageView;


import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import pl.droidsonroids.gif.GifImageView;


public class Home extends AppCompatActivity{

    private ImageView btn_UserProfile, btn_GioHang;
    private GridView gridview_danhmuc, gridview_fv_food, gridview_CacMonDaDat;
    private ListView listview_goodfood_kitchen;
    private LinearLayout linearlayout_timkiem, area_loading_wait, area_kitchen;
    private RelativeLayout area_loadingmore_home;
    private TextView txt_loadingmore_home, txt_diachi_home;
    private ScrollView scrollview_acti;
    private ViewPager2 viewPagerImageSlider;
    private Handler sliderHander = new Handler();

    ArrayList<DanhMuc> MangDanhMuc;
    ArrayList<Kitchen_Food> MangFVFood;
    ArrayList<Kitchen_Food> MangKitchen;
    Kitchen_FoodAdapter adapter_KT;

    private FirebaseFirestore database;
    SharedPreferences sharedPreferences;
    Timer timer;

    private int slmon = 0;
    private String taikhoan;

    private ArrayList<DanhSachMonAnKhachChon> listMonAnKhachChon = new ArrayList<DanhSachMonAnKhachChon>();;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        database = FirebaseFirestore.getInstance();
        sharedPreferences = getSharedPreferences("datalogin", MODE_PRIVATE);
        taikhoan = sharedPreferences.getString("Phone", "");

        load_slide();



        scrollview_acti = findViewById(R.id.scrollview_acti);
        scrollview_acti.setVisibility(View.INVISIBLE);
        area_loading_wait = findViewById(R.id.area_loading_wait);
        area_loading_wait.setVisibility(View.VISIBLE);



        btn_UserProfile = findViewById(R.id.btn_UserProfile);
        btn_UserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, TaiKhoan.class);
                startActivity(intent);
            }
        });

        linearlayout_timkiem = findViewById(R.id.linearlayout_timkiem);
        linearlayout_timkiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, TimKiemMonAn.class);
                intent.putExtra("LoaiTimKiem","sanpham");
                startActivityForResult(intent, 2);
            }
        });

        //Hiển thị gridview danh mục món ăn
        gridview_danhmuc = findViewById(R.id.gridview_danhmuc);
        MangDanhMuc = new ArrayList<DanhMuc>();
        MangDanhMuc.add(new DanhMuc("Cà phê", R.drawable.danhmuc_caphe));
        MangDanhMuc.add(new DanhMuc("Cơm phần", R.drawable.danhmuc_com));
        MangDanhMuc.add(new DanhMuc("Fastfood", R.drawable.danhmuc_fastfood));
        MangDanhMuc.add(new DanhMuc("Kem", R.drawable.danhmuc_kem));
        MangDanhMuc.add(new DanhMuc("Hotpot", R.drawable.danhmuc_lau));
        MangDanhMuc.add(new DanhMuc("Mì", R.drawable.danhmuc_mi));
        MangDanhMuc.add(new DanhMuc("Tạp hóa", R.drawable.danhmuc_taphoa));
        MangDanhMuc.add(new DanhMuc("Trà sữa", R.drawable.danhmuc_trasua));
        DanhMucAdapter adapter  =  new DanhMucAdapter(this, R.layout.custom_gridview_danhmuc, MangDanhMuc);
        gridview_danhmuc.setAdapter(adapter);


        //Sự kiện khi chọn danh mục món ăn
        gridview_danhmuc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView txt_tendanhmuc= view.findViewById(R.id.txt_tendanhmuc);
                String danhmuc_value = txt_tendanhmuc.getText().toString().trim();

                Intent intent = new Intent(Home.this, TimKiemMonAn.class);
                intent.putExtra("LoaiTimKiem","danhmuc");
                intent.putExtra("DanhMuc",danhmuc_value);
                startActivityForResult(intent, 2);
            }
        });

        //Hiển thị thông báo khi user tạo đơn hàng thành công
        final GifImageView img_thongbao_donhang = findViewById(R.id.img_thongbao_donhang);
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

                    for (int i = 0; i < obj_key.length; i++)
                    {
                        if (obj_key[i].toString().equals("tinh_trang"))
                        {
                            if (!obj_value[i].toString().equals("Hoàn thành"))
                            {
                                img_thongbao_donhang.setVisibility(View.VISIBLE);
                                return;
                            }
                            else
                            {
                                img_thongbao_donhang.setVisibility(View.INVISIBLE);
                            }
                        }
                    }
                }
            }
        });


        get_list_fv_food();
        get_fvfood_user();
        goodfood_kitchen();

        txt_diachi_home = findViewById(R.id.txt_diachi_home);
        final DocumentReference docRef = database.collection("Users").document(taikhoan);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {

                Object[] obj_key = (Object[]) snapshot.getData().keySet().toArray();
                Object[] obj_value = (Object[]) snapshot.getData().values().toArray();

                for (int i = 0; i < obj_key.length; i++)
                {
                    if (obj_key[i].toString().equals("address"))
                    {
                        txt_diachi_home.setText(obj_value[i].toString());
                    }
                }
            }
        });



        txt_diachi_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, ThongTinTaiKhoan.class);
                intent.putExtra("focus_address", true);
                startActivity(intent);
            }
        });


        btn_GioHang = findViewById(R.id.btn_GioHang);
        btn_GioHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (listMonAnKhachChon.size() != 0)
                {
                    Intent intent = new Intent(Home.this, ChiTietHoaDon.class);
                    Bundle args = new Bundle();
                    args.putSerializable("ARRAYLIST",(Serializable)listMonAnKhachChon);
                    intent.putExtra("list",args);
                    startActivityForResult(intent, 1);
                }
            }
        });


        area_loadingmore_home = findViewById(R.id.area_loadingmore_home);
        txt_loadingmore_home = findViewById(R.id.txt_loadingmore_home);
        txt_loadingmore_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int slsp_cu = mang_getdefault.size();
                for (int  i = slsp_cu; i< slsp_cu+5; i++)
                {
                    if (i < MangKitchen.size())
                    {
                        String ten_mon = MangKitchen.get(i).getTen_mon();
                        String gia_tien = MangKitchen.get(i).getGia_tien();
                        String so_luot_mua = MangKitchen.get(i).getSo_luot_mua();
                        String so_luot_danh_gia_tot = MangKitchen.get(i).getSo_luot_danh_gia_tot();
                        String so_luot_danh_gia_khong_tot = MangKitchen.get(i).getSo_luot_danh_gia_khong_tot();
                        String link_hinh = MangKitchen.get(i).getLink_hinh();
                        String danh_muc = MangKitchen.get(i).getDanh_muc();
                        mang_getdefault.add(new Kitchen_Food(ten_mon,gia_tien,so_luot_mua,so_luot_danh_gia_tot,so_luot_danh_gia_khong_tot,link_hinh,danh_muc));
                        adapter_KT.notifyDataSetChanged();
                    }
                }

                if (mang_getdefault.size() == MangKitchen.size())
                {
                    area_loadingmore_home.setEnabled(false);
                    ViewGroup.LayoutParams params_area_loadingmore_home = area_loadingmore_home.getLayoutParams();
                    params_area_loadingmore_home.height = 0;
                    area_loadingmore_home.setLayoutParams(params_area_loadingmore_home);
                }

                ViewGroup.LayoutParams params_listview_goodfood_kitchen = listview_goodfood_kitchen.getLayoutParams();
                params_listview_goodfood_kitchen.height = (params_listview_goodfood_kitchen.height/slsp_cu) * mang_getdefault.size();
                listview_goodfood_kitchen.setLayoutParams(params_listview_goodfood_kitchen);
                scrollview_acti.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        scrollview_acti.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                }, 1600);
            }
        });
    }

    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            viewPagerImageSlider.setCurrentItem(viewPagerImageSlider.getCurrentItem() + 1);
        }
    };

    void load_slide()
    {
        viewPagerImageSlider = findViewById(R.id.viewPagerImageSlider);

        List<SliderItem> sliderItems = new ArrayList<>();

        sliderItems.add(new SliderItem(R.drawable.slide_pic1));
        sliderItems.add(new SliderItem(R.drawable.slide_pic2));
        sliderItems.add(new SliderItem(R.drawable.slide_pic3));
        sliderItems.add(new SliderItem(R.drawable.slide_pic4));

        viewPagerImageSlider.setAdapter(new SliderAdapter(sliderItems, viewPagerImageSlider));

        viewPagerImageSlider.setClipToPadding(false);
        viewPagerImageSlider.setClipChildren(false);
        viewPagerImageSlider.setOffscreenPageLimit(3);
        viewPagerImageSlider.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r * 0.15f);
            }
        });

        viewPagerImageSlider.setPageTransformer(compositePageTransformer);

        viewPagerImageSlider.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                sliderHander.removeCallbacks(sliderRunnable);
                sliderHander.postDelayed(sliderRunnable, 3000);
            }
        });
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LayoutInflater inflater = LayoutInflater.from(Home.this);
        View viewalert = inflater.inflate(R.layout.alert_thongbaochung, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(Home.this).setView(viewalert).create();
        alertDialog.setCanceledOnTouchOutside(false);
        final TextView txt_loading_noti = viewalert.findViewById(R.id.txt_loading_noti);
        final TextView txt_noidung_noti = viewalert.findViewById(R.id.txt_noidung_noti);
        final GifImageView gif_loading_chung = viewalert.findViewById(R.id.gif_loading_chung);
        final Button btn_accept = viewalert.findViewById(R.id.btn_accept);
        final Button btn_not_accept = viewalert.findViewById(R.id.btn_not_accept);

        GifImageView img_thongbao_giohang = findViewById(R.id.img_thongbao_giohang);

        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                boolean ThanhToan = data.getBooleanExtra("ThanhToan", false);

                if (ThanhToan == true)
                {
                    final String MaDon = data.getStringExtra("MaDon");
                    btn_GioHang.setVisibility(View.INVISIBLE);
                    listMonAnKhachChon.clear();
                    slmon = 0;
                    TextView txt_sl_monan = (TextView)findViewById(R.id.txt_sl_monan);
                    txt_sl_monan.setText(null);
                    txt_sl_monan.setVisibility(ImageView.INVISIBLE);
                    img_thongbao_giohang.setVisibility(ImageView.INVISIBLE);

                    txt_loading_noti.setText("THANH TOÁN");
                    txt_noidung_noti.setText("Đã nhận đơn, chúc bạn ngon miệng");
                    btn_accept.setText("Chi tiết đơn");
                    btn_accept.setVisibility(View.VISIBLE);
                    btn_not_accept.setText("Mua sắm tiếp");
                    btn_not_accept.setVisibility(View.VISIBLE);
                    gif_loading_chung.setImageResource(R.drawable.ic_success_loading);
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    alertDialog.show();

                    btn_accept.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(Home.this, TheoDoiDonHang.class);
                            intent.putExtra("MaDon",MaDon);
                            startActivity(intent);
                            alertDialog.dismiss();
                        }
                    });

                    btn_not_accept.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                        }
                    });
                    return;
                }

                Bundle args = data.getBundleExtra("list");
                ArrayList<DanhSachMonAnKhachChon> result = (ArrayList<DanhSachMonAnKhachChon>) args.getSerializable("ARRAYLIST");

                if (result.size() == 0)
                {
                    btn_GioHang.setVisibility(View.INVISIBLE);
                    listMonAnKhachChon.clear();
                    slmon = 0;
                    TextView txt_sl_monan = (TextView)findViewById(R.id.txt_sl_monan);
                    txt_sl_monan.setText(null);
                    txt_sl_monan.setVisibility(ImageView.INVISIBLE);
                    img_thongbao_giohang.setVisibility(ImageView.INVISIBLE);
                }
                else if (result.size() != listMonAnKhachChon.size())
                {
                    listMonAnKhachChon.clear();
                    listMonAnKhachChon = new ArrayList<>(result);
//                    for (int i = 0; i < result.size(); i++)
//                    {
//                        for (int j = 0; j < listMonAnKhachChon.size(); j++)
//                        {
//                            DanhSachMonAnKhachChon obj1 = result.get(i);
//                            DanhSachMonAnKhachChon obj2 = listMonAnKhachChon.get(j);
//                            if (obj1 instanceof DanhSachMonAnKhachChon && obj2 instanceof DanhSachMonAnKhachChon) {
//                                DanhSachMonAnKhachChon cls1 = (DanhSachMonAnKhachChon) obj1;
//                                DanhSachMonAnKhachChon cls2 = (DanhSachMonAnKhachChon) obj2;
//                                if (!cls2.getTenmon().equals(cls1.getTenmon()))
//                                {
//                                    listMonAnKhachChon.remove(j);
//                                }
//                            }
//                        }
//                    }
                    slmon = result.size() ;
                    TextView txt_sl_monan = (TextView)findViewById(R.id.txt_sl_monan);
                    txt_sl_monan.setText(String.valueOf(slmon));
                }
            }
        }
        else if (requestCode == 2) {
            if (data !=  null)
            {
                Bundle args = data.getBundleExtra("list");
                ArrayList<DanhSachMonAnKhachChon> result = (ArrayList<DanhSachMonAnKhachChon>) args.getSerializable("ARRAYLIST");
                boolean monmoi = true;

                if (listMonAnKhachChon.size() > 0) {
                    for (int j = 0; j < listMonAnKhachChon.size(); j++) {
                        DanhSachMonAnKhachChon obj1 = result.get(0);
                        DanhSachMonAnKhachChon obj2 = listMonAnKhachChon.get(j);
                        if (obj1 instanceof DanhSachMonAnKhachChon && obj2 instanceof DanhSachMonAnKhachChon) {
                            DanhSachMonAnKhachChon cls1 = (DanhSachMonAnKhachChon) obj1;
                            DanhSachMonAnKhachChon cls2 = (DanhSachMonAnKhachChon) obj2;
                            if (cls2.getTenmon().equals(cls1.getTenmon())) {
                                monmoi = false;
                                listMonAnKhachChon.set(j, new DanhSachMonAnKhachChon(cls1.getTenmon(), String.valueOf(Integer.parseInt(cls2.getSl()) + Integer.parseInt(cls1.getSl())), cls1.getGiatien(), String.valueOf(Integer.parseInt(cls2.getThanhtien()) + Integer.parseInt(cls1.getThanhtien()))));
                            }
                        }
                    }
                }

                if (monmoi == true) {
                    slmon++;
                    btn_GioHang.setVisibility(View.VISIBLE);
                    img_thongbao_giohang.setVisibility(View.VISIBLE);
                    TextView txt_sl_monan = (TextView) findViewById(R.id.txt_sl_monan);
                    txt_sl_monan.setText(String.valueOf(slmon));
                    txt_sl_monan.setVisibility(ImageView.VISIBLE);
                    DanhSachMonAnKhachChon obj1 = result.get(0);
                    if (obj1 instanceof DanhSachMonAnKhachChon) {
                        DanhSachMonAnKhachChon cls1 = (DanhSachMonAnKhachChon) obj1;
                        listMonAnKhachChon.add(new DanhSachMonAnKhachChon(cls1.getTenmon(), String.valueOf(Integer.parseInt(cls1.getSl())), cls1.getGiatien(), String.valueOf(Integer.parseInt(cls1.getThanhtien()))));
                    }
                }
            }
        }
    }

    private void get_fvfood_user()
    {
        final ArrayList<String> list_tenmon = new ArrayList<>();

        database.collection("Users").document(taikhoan).collection("fv_food").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult())
                    {
                        list_tenmon.add(document.getId().toString());
                    }
                    if (list_tenmon.size() > 0)
                    {
                        get_fvfood(list_tenmon);
                    }
                } else {
                    Toast.makeText(Home.this, "Lỗi kết nối !!!", Toast.LENGTH_SHORT).show();
                }
            }
                });


    }

    private void get_fvfood(final ArrayList<String> list_tenmon)
    {
        gridview_fv_food = findViewById(R.id.gridview_fv_food);
        MangFVFood = new ArrayList<Kitchen_Food>();

        database.collection("Food").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
            if (task.isSuccessful())
            {
                for (DocumentSnapshot document : task.getResult())
                {
                    boolean get = false;
                    String ten_mon = document.getId();
                    for (int i = 0; i<list_tenmon.size(); i++)
                    {
                        if (ten_mon.equals(list_tenmon.get(i)))
                        {
                            get = true;
                            list_tenmon.remove(i);
                        }
                    }
                    Object[] Monan_array_key = (Object[]) document.getData().keySet().toArray();
                    Object[] Monan_array_value = (Object[]) document.getData().values().toArray();

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
                        MangFVFood.add(new Kitchen_Food(ten_mon, gia_tien, so_luot_mua, so_luot_danh_gia_tot, so_luot_danh_gia_khong_tot, link_hinh, danh_muc));
                    }
                }
                set_gridview_fv_food(MangFVFood);

            }
            else
            {
                Toast.makeText(Home.this, "Lỗi kết nối !!!", Toast.LENGTH_SHORT).show();
            }
            }
        });
    }

    void set_gridview_fv_food(final ArrayList<Kitchen_Food> FV_Food_array)
    {
        FavoriteFoodAdapter adapter_FV  =  new FavoriteFoodAdapter(this, R.layout.custom_gridview_fvfood, FV_Food_array);
        gridview_fv_food.setAdapter(adapter_FV);
        ViewGroup.LayoutParams params_gridview_fv_food = gridview_fv_food.getLayoutParams();
        params_gridview_fv_food.width = params_gridview_fv_food.width * FV_Food_array.size();
        gridview_fv_food.setLayoutParams(params_gridview_fv_food);

        final RelativeLayout area_loadingmore_home = findViewById(R.id.area_loadingmore_home);
        area_loadingmore_home.setVisibility(View.VISIBLE);

        scrollview_acti = findViewById(R.id.scrollview_acti);
        scrollview_acti.setVisibility(View.VISIBLE);
        area_loading_wait = findViewById(R.id.area_loading_wait);
        area_loading_wait.setVisibility(View.INVISIBLE);

        gridview_fv_food.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                event_click_item(view, position, FV_Food_array);
            }
        });
    }


    ArrayList<String> list_fv_food = new ArrayList<>();
    void get_list_fv_food()
    {
        database.collection("Users").document(taikhoan).collection("fv_food")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                list_fv_food.add(document.getId());
                            }
                        } else {
                            Toast.makeText(Home.this, "Lỗi kết nối !!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    final boolean[] yeuthich = {false};
    void event_click_item(View view, int vitri, ArrayList<Kitchen_Food> array) {
        final TextView ten = view.findViewById(R.id.txt_ten_fvfood);
        final ImageView img_fvfood = view.findViewById(R.id.img_fvfood);
        Bitmap bm = ((BitmapDrawable)img_fvfood.getDrawable()).getBitmap();
        String giatien = array.get(vitri).getGia_tien();
        final String tenmon = ten.getText().toString().trim();

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(Home.this, R.style.BottomSheetDialogTheme);
        bottomSheetDialog.setCanceledOnTouchOutside(false);
        final View bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.dialog_chitietmonan, (LinearLayout)findViewById(R.id.acti_chitietmonan));
        final TextView txt_soluong = bottomSheetView.findViewById(R.id.txt_soluong);
        final CircularImageView img_monan = bottomSheetView.findViewById(R.id.img_monan);
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

        for (int i = 0; i < list_fv_food.size(); i++)
        {
            if (tenmon.equals(list_fv_food.get(i)))
            {
                btn_yeuthich.setImageResource(R.drawable.ic_favorite);break;
            }
            else
            {
                btn_yeuthich.setImageResource(R.drawable.ic_not_favorite);
            }
        }

        btn_yeuthich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_close.setEnabled(false);
                int count = 0, position = 0;
                for (int i  = 0; i < list_fv_food.size(); i++)
                {
                    if (list_fv_food.get(i).equals(tenmon))
                    {
                        count++;
                        position = i;
                    }
                }

                if (count == 1)
                {
                    delete_fav_food(taikhoan, tenmon, btn_yeuthich, txt_noti_fv_food, btn_close);
                    list_fv_food.remove(position);
                }
                else {
                    add_fav_food(taikhoan, tenmon, btn_yeuthich, txt_noti_fv_food, btn_close);
                    list_fv_food.add(tenmon);
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


                TextView txt_sl_monan = findViewById(R.id.txt_sl_monan);
                txt_sl_monan.setVisibility(ImageView.VISIBLE);
                GifImageView img_thongbao_giohang = findViewById(R.id.img_thongbao_giohang);
                img_thongbao_giohang.setVisibility(ImageView.VISIBLE);
                btn_GioHang.setVisibility(View.VISIBLE);

                    int sl = Integer.parseInt(txt_soluong.getText().toString().trim());
                    int giatien = Integer.parseInt(txt_giatien.getText().toString().trim().replace(",","").replace(" đ", ""));
                    int thanhtien = sl*giatien;
                boolean them = true;


                for (int i = 0; i<listMonAnKhachChon.size(); i++)
                {
                    DanhSachMonAnKhachChon obj = listMonAnKhachChon.get(i);
                    if (obj instanceof DanhSachMonAnKhachChon) {
                        DanhSachMonAnKhachChon cls = (DanhSachMonAnKhachChon) obj;
                        if (cls.getTenmon().equals(tenmon))
                        {
                            them = false;
                            listMonAnKhachChon.set(i, new DanhSachMonAnKhachChon(tenmon, String.valueOf(sl + Integer.parseInt(cls.getSl())), String.valueOf(giatien), String.valueOf(thanhtien + Integer.parseInt(cls.getGiatien()))));
                        }
                    }
                }

                if (them == true)
                {
                    slmon++;
                    txt_sl_monan.setText(String.valueOf(slmon));
                    listMonAnKhachChon.add(new DanhSachMonAnKhachChon(tenmon, String.valueOf(sl), String.valueOf(giatien), String.valueOf(thanhtien)));
                }

                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }

    void add_fav_food(String phone, final String monan, final ImageView btn_yeuthich, final TextView txt_noti_fv_food, final ImageView btn_close)
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
                btn_close.setEnabled(true);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Home.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void delete_fav_food(String phone, final String monan, final ImageView btn_yeuthich, final TextView txt_noti_fv_food, final ImageView btn_close)
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
                btn_close.setEnabled(true);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Home.this, "Lỗi kết nối !!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void khachhanguachuong()
    {
        gridview_CacMonDaDat = findViewById(R.id.gridview_CacMonDaDat);
        final ArrayList<Kitchen_Food> array_khachhanguachuong = new ArrayList<>(MangKitchen);

        Collections.sort(array_khachhanguachuong, new Comparator<Kitchen_Food>() {
            @Override
            public int compare(Kitchen_Food o1, Kitchen_Food o2) {
                return o1.getSo_luot_mua().compareTo(o2.getSo_luot_mua());
            };

        });
        Collections.reverse(array_khachhanguachuong);
        array_khachhanguachuong.subList(5 ,array_khachhanguachuong.size()).clear();

        KhachHangUaChuongAdapter adapter_HF  =  new KhachHangUaChuongAdapter(this, R.layout.custom_gridview_khachhanguachuong, array_khachhanguachuong);
        gridview_CacMonDaDat.setAdapter(adapter_HF);

        ViewGroup.LayoutParams params_gridview_CacMonDaDat = gridview_CacMonDaDat.getLayoutParams();
        params_gridview_CacMonDaDat.width = params_gridview_CacMonDaDat.width * 5;
        gridview_CacMonDaDat.setLayoutParams(params_gridview_CacMonDaDat);

        gridview_CacMonDaDat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                event_click_item(view, position, array_khachhanguachuong);
            }
        });
    }

    ArrayList<Kitchen_Food> mang_getdefault;
    boolean reload = false;
    private void goodfood_kitchen()
    {
        listview_goodfood_kitchen = findViewById(R.id.listview_goodfood_kitchen);
        MangKitchen = new ArrayList<Kitchen_Food>();

        database.collection("Food")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            return;
                        }

                        for (DocumentChange document : snapshots.getDocumentChanges()) {
                            Object[] obj_key = (Object[]) document.getDocument().getData().keySet().toArray();
                            Object[] obj_value = (Object[]) document.getDocument().getData().values().toArray();
                            String gia_tien = "", so_luot_mua = "", so_luot_danh_gia_tot = "", so_luot_danh_gia_khong_tot = "", link_hinh = "", danh_muc = "";
                            String ten_mon = document.getDocument().getId();
                            for (int i = 0; i < obj_key.length; i++)
                            {
                                if (obj_key[i].toString().equals("gia_tien")) gia_tien = obj_value[i].toString();
                                if (obj_key[i].toString().equals("so_luot_mua")) so_luot_mua = obj_value[i].toString();
                                if (obj_key[i].toString().equals("so_luot_danh_gia_tot")) so_luot_danh_gia_tot = obj_value[i].toString();
                                if (obj_key[i].toString().equals("so_luot_danh_gia_khong_tot")) so_luot_danh_gia_khong_tot = obj_value[i].toString();
                                if (obj_key[i].toString().equals("link_hinh")) link_hinh = obj_value[i].toString();
                                if (obj_key[i].toString().equals("danh_muc")) danh_muc = obj_value[i].toString();
                            }
                            if (reload)
                            {
                                for (int i = MangKitchen.size()-1; i >= 0; i--)
                                {
                                    if (MangKitchen.get(i).getTen_mon().equals(ten_mon))
                                    {
                                        MangKitchen.set(i,new Kitchen_Food(ten_mon, gia_tien, so_luot_mua, so_luot_danh_gia_tot, so_luot_danh_gia_khong_tot, link_hinh, danh_muc));
                                        mang_getdefault.set(i,new Kitchen_Food(ten_mon, gia_tien, so_luot_mua, so_luot_danh_gia_tot, so_luot_danh_gia_khong_tot, link_hinh, danh_muc));
                                        adapter_KT.notifyDataSetChanged();
                                        return;
                                    }
                                }
                            }
                            else
                            {
                                MangKitchen.add(new Kitchen_Food(ten_mon, gia_tien, so_luot_mua, so_luot_danh_gia_tot, so_luot_danh_gia_khong_tot, link_hinh, danh_muc));
                            }
                        }

                        mang_getdefault = new ArrayList<>(MangKitchen);
                        mang_getdefault.subList(5,mang_getdefault.size()).clear();
                        adapter_KT  =  new Kitchen_FoodAdapter(Home.this, R.layout.custom_listview_kitchen, mang_getdefault);
                        listview_goodfood_kitchen.setAdapter(adapter_KT);

                        ViewGroup.LayoutParams params_listview_goodfood_kitchen = listview_goodfood_kitchen.getLayoutParams();
                        params_listview_goodfood_kitchen.height = params_listview_goodfood_kitchen.height * 5;
                        listview_goodfood_kitchen.setLayoutParams(params_listview_goodfood_kitchen);

                        khachhanguachuong();
                        reload = true;
                    }
                });


//        database.collection("Food").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful())
//                {
//                    for (DocumentSnapshot document : task.getResult())
//                    {
//                        Object[] obj_key = (Object[]) document.getData().keySet().toArray();
//                        Object[] obj_value = (Object[]) document.getData().values().toArray();
//                        String gia_tien = "", so_luot_mua = "", so_luot_danh_gia_tot = "", so_luot_danh_gia_khong_tot = "", link_hinh = "", danh_muc = "";
//                        String ten_mon = document.getId();
//                        for (int i = 0; i < obj_key.length; i++)
//                        {
//                            if (obj_key[i].toString().equals("gia_tien")) gia_tien = obj_value[i].toString();
//                            if (obj_key[i].toString().equals("so_luot_mua")) so_luot_mua = obj_value[i].toString();
//                            if (obj_key[i].toString().equals("so_luot_danh_gia_tot")) so_luot_danh_gia_tot = obj_value[i].toString();
//                            if (obj_key[i].toString().equals("so_luot_danh_gia_khong_tot")) so_luot_danh_gia_khong_tot = obj_value[i].toString();
//                            if (obj_key[i].toString().equals("link_hinh")) link_hinh = obj_value[i].toString();
//                            if (obj_key[i].toString().equals("danh_muc")) danh_muc = obj_value[i].toString();
//                        }
//                        MangKitchen.add(new Kitchen_Food(ten_mon, gia_tien, so_luot_mua, so_luot_danh_gia_tot, so_luot_danh_gia_khong_tot, link_hinh, danh_muc));
//                    }
//
//                    mang_getdefault = new ArrayList<>(MangKitchen);
//                    mang_getdefault.subList(5,mang_getdefault.size()).clear();
//                    adapter_KT  =  new Kitchen_FoodAdapter(Home.this, R.layout.custom_listview_kitchen, mang_getdefault);
//                    listview_goodfood_kitchen.setAdapter(adapter_KT);
//
//                    ViewGroup.LayoutParams params_listview_goodfood_kitchen = listview_goodfood_kitchen.getLayoutParams();
//                    params_listview_goodfood_kitchen.height = params_listview_goodfood_kitchen.height * 5;
//                    listview_goodfood_kitchen.setLayoutParams(params_listview_goodfood_kitchen);
//
//                    khachhanguachuong();
//                }
//                else
//                {
//                    Toast.makeText(Home.this, "Lỗi kết nối !!!", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });



        listview_goodfood_kitchen.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                event_click_item(view, position, MangKitchen);
            }
        });
    }

    private  static String Format_Money(String num)
    {
        DecimalFormat format = new DecimalFormat("###,###,###,###,###");
        return format.format(Integer.parseInt(num));
    }

    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Chạm lần nữa để thoát", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}