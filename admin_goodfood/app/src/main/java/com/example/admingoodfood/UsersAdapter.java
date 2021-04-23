package com.example.admingoodfood;

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
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.File;
import java.text.DecimalFormat;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class UsersAdapter extends BaseAdapter {

    Context myContext;
    int myLayout;
    List<Users> arrayUsers;

    private StorageReference mStorageRef;

    public UsersAdapter(Context context, int layout, List<Users> UsersList)
    {
        myContext = context;
        myLayout = layout;
        arrayUsers = UsersList;
    }

    @Override
    public int getCount() {
        return arrayUsers.size();
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

        TextView txt_ten = (TextView) view.findViewById(R.id.txt_ten);
        txt_ten.setText(arrayUsers.get(position).getName());

        TextView txt_sdt = (TextView) view.findViewById(R.id.txt_sdt);
        txt_sdt.setText(arrayUsers.get(position).getSdt());

        TextView txt_diachi = (TextView) view.findViewById(R.id.txt_diachi);
        txt_diachi.setText("Địa chỉ: " + arrayUsers.get(position).getAddress());

        ImageView img_loaithanhvien = (ImageView) view.findViewById(R.id.img_loaithanhvien);
        if (!arrayUsers.get(position).getType_member().equals("Member"))
        {
            img_loaithanhvien.setImageResource(R.drawable.ic_vip_member);
            img_loaithanhvien.setVisibility(View.VISIBLE);
        }

        final CircularImageView img_avatar = (CircularImageView) view.findViewById(R.id.img_avatar);


        final GifImageView img_loading_avatar = (GifImageView) view.findViewById(R.id.img_loading_avatar);
        if (!arrayUsers.get(position).getLink_avatar().equals(""))
        {
            mStorageRef = FirebaseStorage.getInstance().getReference().child("users_pic/" + arrayUsers.get(position).getLink_avatar());
            try
            {
                final File file_local = File.createTempFile("users_pic", arrayUsers.get(position).getLink_avatar().substring(arrayUsers.get(position).getLink_avatar().length()-3));
                mStorageRef.getFile(file_local).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Bitmap bitmap = BitmapFactory.decodeFile(file_local.getAbsolutePath());
                        img_avatar.setImageBitmap(bitmap);
                        img_loading_avatar.setVisibility(View.INVISIBLE);
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
        }
        else
        {
            img_avatar.setImageResource(R.drawable.ic_avatar_default);
            img_loading_avatar.setVisibility(View.INVISIBLE);
        }


        return view;
    }

}
