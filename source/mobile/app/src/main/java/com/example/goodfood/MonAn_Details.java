package com.example.goodfood;

public class MonAn_Details {
    String ten_mon;
    String gia_tien;
    String so_luot_mua;
    String diem_danh_gia;
    String so_luot_danh_gia;
    String link_hinh;
    String danh_muc;

    public MonAn_Details(String ten_mon, String gia_tien, String so_luot_mua, String diem_danh_gia, String so_luot_danh_gia, String link_hinh, String danh_muc) {
        this.ten_mon = ten_mon;
        this.gia_tien = gia_tien;
        this.so_luot_mua = so_luot_mua;
        this.diem_danh_gia = diem_danh_gia;
        this.so_luot_danh_gia = so_luot_danh_gia;
        this.link_hinh = link_hinh;
        this.danh_muc = danh_muc;
    }

    public String getTen_mon() {
        return ten_mon;
    }

    public void setTen_mon(String ten_mon) {
        this.ten_mon = ten_mon;
    }

    public String getGia_tien() {
        return gia_tien;
    }

    public void setGia_tien(String gia_tien) {
        this.gia_tien = gia_tien;
    }

    public String getSo_luot_mua() {
        return so_luot_mua;
    }

    public void setSo_luot_mua(String so_luot_mua) {
        this.so_luot_mua = so_luot_mua;
    }

    public String getDiem_danh_gia() {
        return diem_danh_gia;
    }

    public void setDiem_danh_gia(String diem_danh_gia) {
        this.diem_danh_gia = diem_danh_gia;
    }

    public String getSo_luot_danh_gia() {
        return so_luot_danh_gia;
    }

    public void setSo_luot_danh_gia(String so_luot_danh_gia) {
        this.so_luot_danh_gia = so_luot_danh_gia;
    }

    public String getLink_hinh() {
        return link_hinh;
    }

    public void setLink_hinh(String link_hinh) {
        this.link_hinh = link_hinh;
    }

    public String getDanh_muc() {
        return danh_muc;
    }

    public void setDanh_muc(String danh_muc) {
        this.danh_muc = danh_muc;
    }
}
