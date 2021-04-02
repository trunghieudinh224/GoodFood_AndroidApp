package com.example.goodfood;

public class LichSuDon {
    private String Madon;
    private String Tinhtrang;
    private String Thoi_gian_dat;
    private String Thoi_gian_nhan;
    private String Soluong;
    private String TongTien;
    private String DiaChi;

    public LichSuDon(String madon, String tinhtrang, String thoi_gian_dat, String thoi_gian_nhan, String soluong, String tongTien, String diaChi) {
        Madon = madon;
        Tinhtrang = tinhtrang;
        Thoi_gian_dat = thoi_gian_dat;
        Thoi_gian_nhan = thoi_gian_nhan;
        Soluong = soluong;
        TongTien = tongTien;
        DiaChi = diaChi;
    }

    public String getMadon() {
        return Madon;
    }

    public void setMadon(String madon) {
        Madon = madon;
    }

    public String getTinhtrang() {
        return Tinhtrang;
    }

    public void setTinhtrang(String tinhtrang) {
        Tinhtrang = tinhtrang;
    }

    public String getThoi_gian_dat() {
        return Thoi_gian_dat;
    }

    public void setThoi_gian_dat(String thoi_gian_dat) {
        Thoi_gian_dat = thoi_gian_dat;
    }

    public String getThoi_gian_nhan() {
        return Thoi_gian_nhan;
    }

    public void setThoi_gian_nhan(String thoi_gian_nhan) {
        Thoi_gian_nhan = thoi_gian_nhan;
    }

    public String getSoluong() {
        return Soluong;
    }

    public void setSoluong(String soluong) {
        Soluong = soluong;
    }

    public String getTongTien() {
        return TongTien;
    }

    public void setTongTien(String tongTien) {
        TongTien = tongTien;
    }

    public String getDiaChi() {
        return DiaChi;
    }

    public void setDiaChi(String diaChi) {
        DiaChi = diaChi;
    }
}
