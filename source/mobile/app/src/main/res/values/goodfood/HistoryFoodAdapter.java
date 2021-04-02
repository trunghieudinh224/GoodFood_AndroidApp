package com.example.goodfood;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class HistoryFoodAdapter extends BaseAdapter {

    Context myContext;
    int myLayout;
    List<History_Food> arrayHistory_Food;

    public HistoryFoodAdapter(Context context, int layout, List<History_Food> History_FoodList)
    {
        myContext = context;
        myLayout = layout;
        arrayHistory_Food = History_FoodList;
    }

    @Override
    public int getCount() {
        return arrayHistory_Food.size();
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
        txt_ten_fvfood.setText(arrayHistory_Food.get(position).getName_Food());

        TextView txt_SoLanMua = (TextView) view.findViewById(R.id.txt_SoLanMua);
        txt_SoLanMua.setText("Đặt gân nhất: "+ arrayHistory_Food.get(position).getLast_Day());

        ImageView img_fvfood = (ImageView) view.findViewById(R.id.img_fvfood);
        img_fvfood.setImageResource(arrayHistory_Food.get(position).getImg());

        return view;
    }
}
