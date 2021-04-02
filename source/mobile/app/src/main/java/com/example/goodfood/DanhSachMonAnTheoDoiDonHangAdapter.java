package com.example.goodfood;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class DanhSachMonAnTheoDoiDonHangAdapter extends BaseAdapter {

    Context myContext;
    int myLayout;
    List<DanhSachMonAnKhachChon> array_theodoidonhang;

    public DanhSachMonAnTheoDoiDonHangAdapter(Context context, int layout, List<DanhSachMonAnKhachChon> theodoidonhangList)
    {
        myContext = context;
        myLayout = layout;
        array_theodoidonhang = theodoidonhangList;
    }

    @Override
    public int getCount() {
        return array_theodoidonhang.size();
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
    public View getView(int position, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(myLayout, null);

        TextView txt_SL_chitiethoadon = (TextView) view.findViewById(R.id.txt_SL_chitiethoadon);
        txt_SL_chitiethoadon.setText(array_theodoidonhang.get(position).getSl() + "x");

        TextView txt_monan_chitiethoadon = (TextView) view.findViewById(R.id.txt_monan_chitiethoadon);
        txt_monan_chitiethoadon.setText(array_theodoidonhang.get(position).getTenmon());

        final TextView txt_thanhtien_chitiethoadon = (TextView) view.findViewById(R.id.txt_thanhtien_chitiethoadon);
        txt_thanhtien_chitiethoadon.setText(array_theodoidonhang.get(position).getThanhtien() + " đ");

        return view;
    }
}