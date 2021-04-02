package com.example.goodfood;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class FV_FoodAdapter extends BaseAdapter {

    Context myContext;
    int myLayout;
    List<Kitchen_Food> arrayFV_Food;

    public FV_FoodAdapter(Context context, int layout, List<Kitchen_Food> FV_FoodList)
    {
        myContext = context;
        myLayout = layout;
        arrayFV_Food = FV_FoodList;
    }

    @Override
    public int getCount() {
        return arrayFV_Food.size();
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

        TextView txt_ten_fvfood = (TextView) view.findViewById(R.id.txt_ten_fvfood);
        txt_ten_fvfood.setText(arrayFV_Food.get(position).getTen_mon());

        TextView txt_SoLanMua = (TextView) view.findViewById(R.id.txt_SoLanMua);
        txt_SoLanMua.setText("Lượt đặt hàng: "+ arrayFV_Food.get(position).getSo_luot_mua());

        TextView txt_danh_gia_tot = (TextView) view.findViewById(R.id.txt_danh_gia_tot);
        txt_danh_gia_tot.setText(arrayFV_Food.get(position).getSo_luot_danh_gia_tot());

        TextView txt_danh_gia_khongtot = (TextView) view.findViewById(R.id.txt_danh_gia_khongtot);
        txt_danh_gia_khongtot.setText(arrayFV_Food.get(position).getSo_luot_danh_gia_khong_tot());

        ImageView img_fvfood = (ImageView) view.findViewById(R.id.img_fvfood);
        img_fvfood.setImageResource(R.drawable.food_comchien);

        return view;
    }
}
