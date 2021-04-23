package com.example.admingoodfood;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class QuanLyUsers extends AppCompatActivity {

    private FirebaseFirestore database;
    private ListView listview_users;

    ArrayList<Users> list_Users = new ArrayList<>();
    SharedPreferences sharedPreferences;
    boolean reload = false;
    UsersAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_users);

        database = FirebaseFirestore.getInstance();
        listview_users = findViewById(R.id.listview_users);

        database.collection("Users").addSnapshotListener(new EventListener<QuerySnapshot>() {
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
                    String sdt = "", address = "", email = "", link_avatar = "", name = "", password = "", so_don_dat_hang = "", so_don_tich_luy = "", so_voucher_tich_luy = "", tich_luy_thang = "", type_member = "";

                    sdt = document.getDocument().getId();
                    for (int i = 0; i < obj_key.length; i++)
                    {
                        if (obj_key[i].toString().equals("address")) address = obj_value[i].toString();
                        if (obj_key[i].toString().equals("email")) email = obj_value[i].toString();
                        if (obj_key[i].toString().equals("link_avatar")) link_avatar = obj_value[i].toString();
                        if (obj_key[i].toString().equals("name")) name = obj_value[i].toString();
                        if (obj_key[i].toString().equals("password")) password = obj_value[i].toString();
                        if (obj_key[i].toString().equals("so_don_dat_hang")) so_don_dat_hang = obj_value[i].toString();
                        if (obj_key[i].toString().equals("so_don_tich_luy")) so_don_tich_luy = obj_value[i].toString();
                        if (obj_key[i].toString().equals("so_voucher_tich_luy")) so_voucher_tich_luy = obj_value[i].toString();
                        if (obj_key[i].toString().equals("tich_luy_thang")) tich_luy_thang = obj_value[i].toString();
                        if (obj_key[i].toString().equals("type_member")) type_member = obj_value[i].toString();
                    }

                    if (reload)
                    {
                        for (int i = list_Users.size()-1; i >= 0; i--)
                        {
                            if (list_Users.get(i).getSdt().equals(sdt))
                            {
                                list_Users.set(i, new Users(sdt,address,email,link_avatar,name,password,so_don_dat_hang,so_don_tich_luy,so_voucher_tich_luy,tich_luy_thang,type_member));
                                adapter.notifyDataSetChanged();
                                return;
                            }
                        }
                    }
                    else
                    {
                        list_Users.add(new Users(sdt,address,email,link_avatar,name,password,so_don_dat_hang,so_don_tich_luy,so_voucher_tich_luy,tich_luy_thang,type_member));
                    }

                }
                ViewGroup.LayoutParams params_listview_users = listview_users.getLayoutParams();
                params_listview_users.height = params_listview_users.height * list_Users.size();
                listview_users.setLayoutParams(params_listview_users);
                adapter  =  new UsersAdapter(QuanLyUsers.this, R.layout.custom_quanlyusers_gridview, list_Users);
                listview_users.setAdapter(adapter);
                reload = true;
            }
        });

        listview_users.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("sdt", list_Users.get(position).getSdt());
                editor.commit();
                Intent intent = new Intent(QuanLyUsers.this, ThongTinCaNhan_DonHang_TichLuy.class);
                startActivity(intent);
            }
        });
    }
}