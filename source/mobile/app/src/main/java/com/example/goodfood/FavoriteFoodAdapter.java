package com.example.goodfood;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.text.DecimalFormat;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class FavoriteFoodAdapter extends BaseAdapter {

    Context myContext;
    int myLayout;
    List<Kitchen_Food> arrayFV_Food;

    private StorageReference mStorageRef;

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

        final GifImageView img_waiting_fvfood = (GifImageView) view.findViewById(R.id.img_waiting_fvfood);

        final ImageView img_fvfood = (ImageView) view.findViewById(R.id.img_fvfood);
        mStorageRef = FirebaseStorage.getInstance().getReference().child("food_pic/" + arrayFV_Food.get(position).getLink_hinh());
        try
        {
            final File file_local = File.createTempFile("food_pic", arrayFV_Food.get(position).getLink_hinh().substring(arrayFV_Food.get(position).getLink_hinh().length()-3));
            mStorageRef.getFile(file_local).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(file_local.getAbsolutePath());
                    img_fvfood.setImageBitmap(bitmap);
                    img_fvfood.setVisibility(View.VISIBLE);
                    img_waiting_fvfood.setVisibility(View.INVISIBLE);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(((Activity) myContext), "cc", Toast.LENGTH_SHORT).show();
                }
            });
        }
        catch (Exception ex)
        {
            Toast.makeText(((Activity) myContext), ex.toString(), Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    private  static String Format_Money(String num)
    {
        DecimalFormat format = new DecimalFormat("###,###,###,###,###");
        return format.format(Integer.parseInt(num));
    }
}
