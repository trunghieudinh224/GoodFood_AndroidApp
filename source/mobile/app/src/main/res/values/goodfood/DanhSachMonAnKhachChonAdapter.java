package com.example.goodfood;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class DanhSachMonAnKhachChonAdapter extends BaseAdapter {

    Context myContext;
    int myLayout;
    List<DanhSachMonAnKhachChon> arrayDanhSachMonAnKhachChon;
    int tongtien = 0;
    ArrayList<Integer> list_mondaxoa = new ArrayList<>();

    public DanhSachMonAnKhachChonAdapter(Context context, int layout, List<DanhSachMonAnKhachChon> DanhSachMonAnKhachChonList)
    {
        myContext = context;
        myLayout = layout;
        arrayDanhSachMonAnKhachChon = DanhSachMonAnKhachChonList;
    }

    @Override
    public int getCount() {
        return arrayDanhSachMonAnKhachChon.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(myLayout, null);

        TextView txt_SL_chitiethoadon = (TextView) view.findViewById(R.id.txt_SL_chitiethoadon);
        txt_SL_chitiethoadon.setText(arrayDanhSachMonAnKhachChon.get(position).getSl());

        TextView txt_monan_chitiethoadon = (TextView) view.findViewById(R.id.txt_monan_chitiethoadon);
        txt_monan_chitiethoadon.setText(arrayDanhSachMonAnKhachChon.get(position).getTenmon());

        final TextView txt_thanhtien_chitiethoadon = (TextView) view.findViewById(R.id.txt_thanhtien_chitiethoadon);
        txt_thanhtien_chitiethoadon.setText(arrayDanhSachMonAnKhachChon.get(position).getThanhtien());

        ImageView btn_remove_chitiethoadon = (ImageView) view.findViewById(R.id.btn_remove_chitiethoadon);
        btn_remove_chitiethoadon.setImageResource(R.drawable.ic_remove_chitiethoadon);

        final TextView txt_tongtien_chitiethoadon = ((Activity) myContext).findViewById(R.id.txt_tongtien_chitiethoadon);
        final String x  = txt_tongtien_chitiethoadon.getText().toString().trim();
//        if (myContext instanceof Activity)
//        {
//            TextView txt_tongtien_chitiethoadon = ((Activity) myContext).findViewById(R.id.txt_tongtien_chitiethoadon);
//            tongtien = Integer.parseInt(txt_tongtien_chitiethoadon.getText().toString().trim());
//        }


        btn_remove_chitiethoadon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = LayoutInflater.from(myContext);
                View viewalert = inflater.inflate(R.layout.alert_thongbaochung, null);
                final AlertDialog alertDialog = new AlertDialog.Builder(myContext).setView(viewalert).create();
                alertDialog.setCanceledOnTouchOutside(true);
                final TextView txt_loading_noti = viewalert.findViewById(R.id.txt_loading_noti);
                txt_loading_noti.setText("XÓA MÓN");
                final TextView txt_noidung_noti = viewalert.findViewById(R.id.txt_noidung_noti);
                txt_noidung_noti.setText("Bạn có chắc muốn xóa món này ?");
                final GifImageView gif_loading_chung = viewalert.findViewById(R.id.gif_loading_chung);
                gif_loading_chung.setImageResource(R.drawable.ic_error_loading);
                final Button btn_accept = viewalert.findViewById(R.id.btn_accept);
                final Button btn_not_accept = viewalert.findViewById(R.id.btn_not_accept);
                btn_not_accept.setVisibility(View.VISIBLE);
                btn_accept.setVisibility(View.VISIBLE);

                btn_accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String thanhtien_monxoa = txt_thanhtien_chitiethoadon.getText().toString().trim().replace(",", "");
                        String tongtienhientai = x.replace(",", "");
                        tongtienhientai = tongtienhientai.replace(" đ", "");
                        String tongtienmoi = String.valueOf(Integer.parseInt(tongtienhientai) - Integer.parseInt(thanhtien_monxoa));
                        txt_tongtien_chitiethoadon.setText(Format_Money(tongtienmoi) + " đ");

                        arrayDanhSachMonAnKhachChon.remove(position);
                        notifyDataSetChanged();
                        GridView gridview_danhsachmonan = ((Activity) myContext).findViewById(R.id.gridview_danhsachmonan);
                        ViewGroup.LayoutParams params_gridview_danhsachmonan = gridview_danhsachmonan.getLayoutParams();
                        params_gridview_danhsachmonan.height = (params_gridview_danhsachmonan.height/(arrayDanhSachMonAnKhachChon.size()+1)) * arrayDanhSachMonAnKhachChon.size();
                        gridview_danhsachmonan.setLayoutParams(params_gridview_danhsachmonan);
                        if (arrayDanhSachMonAnKhachChon.size() == 0)
                        {
                            Intent resultIntent = new Intent();
                            Bundle args = new Bundle();
                            args.putSerializable("ARRAYLIST",(Serializable)arrayDanhSachMonAnKhachChon);
                            resultIntent.putExtra("list",args);

                            if (myContext instanceof Activity)
                            {
                                ((Activity) myContext).setResult(Activity.RESULT_OK, resultIntent);
                            }

                            ((Activity)myContext).finish();
                        }
                        alertDialog.dismiss();
                    }
                });

                btn_not_accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });

                alertDialog.show();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
        });

        return view;
    }



    private  static String Format_Money(String num)
    {
        DecimalFormat format = new DecimalFormat("###,###,###,###,###");
        return format.format(Integer.parseInt(num));
    }
}
