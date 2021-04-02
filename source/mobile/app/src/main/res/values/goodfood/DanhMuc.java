package com.example.goodfood;

public class DanhMuc {
    private String TenDanhMuc;
    private int img;

    public DanhMuc(String tenDanhMuc, int img) {
        TenDanhMuc = tenDanhMuc;
        this.img = img;
    }

    public String getTenDanhMuc() {
        return TenDanhMuc;
    }

    public void setTenDanhMuc(String tenDanhMuc) {
        TenDanhMuc = tenDanhMuc;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }
}
