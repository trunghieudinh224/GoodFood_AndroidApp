package com.example.goodfood;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class DanhMucAdapter extends BaseAdapter {

    Context myContext;
    int myLayout;
    List<DanhMuc> arrayDanhMuc;

    public DanhMucAdapter(Context context, int layout, List<DanhMuc> danhmucList)
    {
        myContext = context;
        myLayout = layout;
        arrayDanhMuc = danhmucList;
    }

    @Override
    public int getCount() {
        return arrayDanhMuc.size();
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

        TextView txt_tendanhmuc = (TextView) view.findViewById(R.id.txt_tendanhmuc);
        txt_tendanhmuc.setText(arrayDanhMuc.get(position).getTenDanhMuc());

        ImageView img_danhmuc_gridview = (ImageView) view.findViewById(R.id.img_danhmuc_gridview);
        img_danhmuc_gridview.setImageResource(arrayDanhMuc.get(position).getImg());

        return view;
    }
}
