package com.example.goodfood;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

        TextView txt_tinhtrang_lichsudon = (TextView) view.findViewById(R.id.txt_tinhtrang_lichsudon);
        if (!arrayLichSuDon.get(position).getTinhtrang().equals("Hoàn thành"))
        {
            txt_tinhtrang_lichsudon.setTextColor(Color.parseColor("#e85151"));
        }
        txt_tinhtrang_lichsudon.setText("#" + arrayLichSuDon.get(position).getTinhtrang());

        TextView txt_ngaydat_lichsudon = (TextView) view.findViewById(R.id.txt_ngaydat_lichsudon);
//        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
//        try {
//            Date d = sdf.parse(arrayLichSuDon.get(position).getNgaydat());
//        } catch (ParseException ex) {
//
//        }
        txt_ngaydat_lichsudon.setText(arrayLichSuDon.get(position).getThoi_gian_nhan());

        TextView txt_slmon_lichsudon = (TextView) view.findViewById(R.id.txt_slmon_lichsudon);
        txt_slmon_lichsudon.setText(arrayLichSuDon.get(position).getSoluong() + " món");

        TextView txt_tongtien_lichsudon = (TextView) view.findViewById(R.id.txt_tongtien_lichsudon);
        txt_tongtien_lichsudon.setText("Tổng tiền: " + Format_Money(arrayLichSuDon.get(position).getTongTien()) + " đ");

        TextView txt_diachi_lichsudon = (TextView) view.findViewById(R.id.txt_diachi_lichsudon);
        txt_diachi_lichsudon.setText("Nơi nhận: " + arrayLichSuDon.get(position).getDiaChi());

        Button btn_xemchitiet_lichsudon = (Button) view.findViewById(R.id.btn_xemchitiet_lichsudon);
        btn_xemchitiet_lichsudon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(myContext, TheoDoiDonHang.class);
                intent.putExtra("MaDon",arrayLichSuDon.get(position).getMadon());
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
