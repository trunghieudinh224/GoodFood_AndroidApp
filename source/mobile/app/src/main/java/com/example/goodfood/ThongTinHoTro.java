package com.example.goodfood;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ThongTinHoTro extends AppCompatActivity {

    private ImageView btn_back;
    private TextView txt_tieude_acti;
    private ListView listview_hotro;

    ThongTinHoTroAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_tin_ho_tro);

        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txt_tieude_acti = findViewById(R.id.txt_tieude_acti);
        txt_tieude_acti.setText("Thông tin hỗ trợ");

        listview_hotro = findViewById(R.id.listview_hotro);
        final ArrayList<String> list = new ArrayList<>();
        list.add("Giới thiệu GoodFood");
        list.add("Các lỗi thường gặp");
        list.add("Chính sách khách hàng");
        list.add("Thông tin phần mềm");
        adapter =  new ThongTinHoTroAdapter(ThongTinHoTro.this, R.layout.custom_listview_hotro, list);
        listview_hotro.setAdapter(adapter);
//        listview_hotro.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (list.get(position).toString().equals("Giới thiệu GoodFood"))
//                {
//                    Intent intent = new Intent(ThongTinHoTro.this, WebView.class);
//                    intent.putExtra("url", "https://www.facebook.com/dinhtrunghieu224/");
//                    startActivity(intent);
//                }
//                else if (list.get(position).toString().equals("Các lỗi thường gặp"))
//                {
//                    Intent intent = new Intent(ThongTinHoTro.this, WebView.class);
//                    intent.putExtra("url", "https://www.facebook.com/luan.lehuu.94/");
//                    startActivity(intent);
//                }
//                else if (list.get(position).toString().equals("Chính sách khách hàng"))
//                {
//                    Intent intent = new Intent(ThongTinHoTro.this, WebView.class);
//                    intent.putExtra("url", "https://www.facebook.com/dinhdat6699/");
//                    startActivity(intent);
//                }
//                else
//                {
//                    Intent intent = new Intent(ThongTinHoTro.this, WebView.class);
//                    intent.putExtra("url", "https://www.facebook.com/ln.duy99/");
//                    startActivity(intent);
//                }
//            }
//        });

    }
}