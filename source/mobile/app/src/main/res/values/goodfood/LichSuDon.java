package com.example.goodfood;

public class LichSuDon {
    private String Madon;
    private String Tinhtrang;
    private String Ngaydat;
    private String Soluong;
    private String TongTien;
    private String DiaChi;

    public LichSuDon(String madon, String tinhtrang, String ngaydat, String soluong, String tongTien, String diaChi) {
        Madon = madon;
        Tinhtrang = tinhtrang;
        Ngaydat = ngaydat;
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

    public String getNgaydat() {
        return Ngaydat;
    }

    public void setNgaydat(String ngaydat) {
        Ngaydat = ngaydat;
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
