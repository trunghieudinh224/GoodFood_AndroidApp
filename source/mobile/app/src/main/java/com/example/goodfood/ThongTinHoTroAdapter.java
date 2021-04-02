package com.example.goodfood;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

public class ThongTinHoTroAdapter extends BaseAdapter {

    Context myContext;
    int myLayout;
    List<String> list;

    public ThongTinHoTroAdapter(Context context, int layout, List<String> list_array)
    {
        myContext = context;
        myLayout = layout;
        list = list_array;
    }

    @Override
    public int getCount() {
        return list.size();
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


        TextView txt_noidung_hotro = (TextView) view.findViewById(R.id.txt_noidung_hotro);
        txt_noidung_hotro.setText(list.get(position));

        return view;
    }

}
