package com.example.goodfood;

public class Kitchen_Food {
    private String ten_mon;
    private String gia_tien;
    private String so_luot_mua;
    private String so_luot_danh_gia_tot;
    private String so_luot_danh_gia_khong_tot;
    private String link_hinh;
    private String danh_muc;

    public Kitchen_Food(String ten_mon, String gia_tien, String so_luot_mua, String so_luot_danh_gia_tot, String so_luot_danh_gia_khong_tot, String link_hinh, String danh_muc) {
        this.ten_mon = ten_mon;
        this.gia_tien = gia_tien;
        this.so_luot_mua = so_luot_mua;
        this.so_luot_danh_gia_tot = so_luot_danh_gia_tot;
        this.so_luot_danh_gia_khong_tot = so_luot_danh_gia_khong_tot;
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

    public String getSo_luot_danh_gia_tot() {
        return so_luot_danh_gia_tot;
    }

    public void setSo_luot_danh_gia_tot(String so_luot_danh_gia_tot) {
        this.so_luot_danh_gia_tot = so_luot_danh_gia_tot;
    }

    public String getSo_luot_danh_gia_khong_tot() {
        return so_luot_danh_gia_khong_tot;
    }

    public void setSo_luot_danh_gia_khong_tot(String so_luot_danh_gia_khong_tot) {
        this.so_luot_danh_gia_khong_tot = so_luot_danh_gia_khong_tot;
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
