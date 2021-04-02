package com.example.goodfood;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class VoucherAdapter extends BaseAdapter {

    Context myContext;
    int myLayout;
    List<Voucher> arrayVoucher;

    public VoucherAdapter(Context context, int layout, List<Voucher> voucherList)
    {
        myContext = context;
        myLayout = layout;
        arrayVoucher = voucherList;
    }

    @Override
    public int getCount() {
        return arrayVoucher.size();
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

        final TextView txt_code_voucher = (TextView) view.findViewById(R.id.txt_code_voucher);
        txt_code_voucher.setText(arrayVoucher.get(position).getMa_voucher());

        TextView txt_tieude_voucher = (TextView) view.findViewById(R.id.txt_tieude_voucher);
        txt_tieude_voucher.setText(arrayVoucher.get(position).getMo_ta());

        TextView txt_hsd_voucher = (TextView) view.findViewById(R.id.txt_hsd_voucher);
        txt_hsd_voucher.setText(arrayVoucher.get(position).getHsd());

        ImageView img_ic_voucher = (ImageView) view.findViewById(R.id.img_ic_voucher);
        img_ic_voucher.setImageResource(R.drawable.logo_app);
        return view;
    }
}
