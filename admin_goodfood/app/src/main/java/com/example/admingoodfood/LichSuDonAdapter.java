package com.example.admingoodfood;

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

public class LichSuDonAdapter extends BaseAdapter {

    Context myContext;
    int myLayout;
    List<LichSuDon> arrayLichSuDon;

    public LichSuDonAdapter(Context context, int layout, List<LichSuDon> LichSuDonList)
    {
        myContext = context;
        myLayout = layout;
        arrayLichSuDon = LichSuDonList;
    }

    @Override
    public int getCount() {
        return arrayLichSuDon.size();
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

        TextView txt_madon = (TextView) view.findViewById(R.id.txt_madon);
        txt_madon.setText("#" + arrayLichSuDon.get(position).getMadon());

        TextView txt_tongdon = (TextView) view.findViewById(R.id.txt_tongdon);
        txt_tongdon.setText("Tổng tiền: " + Format_Money(arrayLichSuDon.get(position).getTongTien()) + " đ");

        TextView txt_tinhtrang = (TextView) view.findViewById(R.id.txt_tinhtrang);
        txt_tinhtrang.setText("Tình trạng: " + arrayLichSuDon.get(position).getTinhtrang());

        TextView txt_thoigian = (TextView) view.findViewById(R.id.txt_thoigian);
        txt_thoigian.setText(arrayLichSuDon.get(position).getThoi_gian_dat());

        Button btn_xem = (Button) view.findViewById(R.id.btn_xem);
        btn_xem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(myContext, DonHang.class);
                intent.putExtra("madon", arrayLichSuDon.get(position).getMadon());
                myContext.startActivity(intent);
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
