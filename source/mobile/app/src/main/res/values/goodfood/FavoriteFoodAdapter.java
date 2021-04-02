package com.example.goodfood;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

public class FavoriteFoodAdapter extends BaseAdapter {

    Context myContext;
    int myLayout;
    List<Kitchen_Food> arrayFV_Food;

    public FavoriteFoodAdapter(Context context, int layout, List<Kitchen_Food> FV_FoodList)
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

        TextView txt_danhmuc = (TextView) view.findViewById(R.id.txt_danhmuc);
        txt_danhmuc.setText("Danh mục: "+ arrayFV_Food.get(position).getDanh_muc());

        TextView txt_giatien = (TextView) view.findViewById(R.id.txt_giatien);
        txt_giatien.setText(Format_Money(arrayFV_Food.get(position).getGia_tien()) + " đ");

        ImageView img_fvfood = (ImageView) view.findViewById(R.id.img_fvfood);
        img_fvfood.setImageResource(R.drawable.food_comchien);

        return view;
    }

    private  static String Format_Money(String num)
    {
        DecimalFormat format = new DecimalFormat("###,###,###,###,###");
        return format.format(Integer.parseInt(num));
    }
}
