package com.example.goodfood;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class DanhSachMonAnRatingAdapter extends BaseAdapter {

    Context myContext;
    int myLayout;
    List<DanhSachMonAnRating> array_rating;

    public DanhSachMonAnRatingAdapter(Context context, int layout, List<DanhSachMonAnRating> ratingList)
    {
        myContext = context;
        myLayout = layout;
        array_rating = ratingList;
    }

    @Override
    public int getCount() {
        return array_rating.size();
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

        TextView txt_monan_rating = (TextView) view.findViewById(R.id.txt_monan_rating);
        txt_monan_rating.setText(array_rating.get(position).getTenmon());

        final GifImageView ic_danhgia_tot = (GifImageView) view.findViewById(R.id.ic_danhgia_tot);
        final GifImageView ic_danhgia_khongtot = (GifImageView) view.findViewById(R.id.ic_danhgia_khongtot);

        if (array_rating.get(position).getDanhgia().equals("danhgiatot"))
        {
            ic_danhgia_tot.setEnabled(false);
            ic_danhgia_tot.setImageResource(R.drawable.ic_danhgia_tot);
            ic_danhgia_khongtot.setEnabled(false);
            ic_danhgia_khongtot.setImageResource(R.drawable.ic_danhgia_khongtot_false);
        }
        else  if (array_rating.get(position).getDanhgia().equals("danhgiakhongtot"))
        {
            ic_danhgia_tot.setEnabled(false);
            ic_danhgia_tot.setImageResource(R.drawable.ic_danhgia_tot_false);
            ic_danhgia_khongtot.setEnabled(false);
            ic_danhgia_khongtot.setImageResource(R.drawable.ic_danhgia_khongtot);
        }
        else
        {
            ic_danhgia_tot.setImageResource(R.drawable.ic_danhgia_tot);
            ic_danhgia_khongtot.setImageResource(R.drawable.ic_danhgia_khongtot);
        }



        ic_danhgia_tot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                array_rating.get(position).setDanhgia("danhgiatot");
                ic_danhgia_khongtot.setImageResource(R.drawable.ic_danhgia_khongtot_false);
                ic_danhgia_tot.setEnabled(false);
                ic_danhgia_khongtot.setEnabled(false);
            }
        });


        ic_danhgia_khongtot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                array_rating.get(position).setDanhgia("danhgiakhongtot");
                ic_danhgia_tot.setImageResource(R.drawable.ic_danhgia_tot_false);
                ic_danhgia_tot.setEnabled(false);
                ic_danhgia_khongtot.setEnabled(false);
            }
        });

        return view;
    }
}