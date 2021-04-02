package com.example.goodfood;

public class DanhSachMonAnRating {
    String tenmon;
    String danhgia;

    public DanhSachMonAnRating(String tenmon, String danhgia) {
        this.tenmon = tenmon;
        this.danhgia = danhgia;
    }

    public String getTenmon() {
        return tenmon;
    }

    public void setTenmon(String tenmon) {
        this.tenmon = tenmon;
    }

    public String getDanhgia() {
        return danhgia;
    }

    public void setDanhgia(String danhgia) {
        this.danhgia = danhgia;
    }
}
