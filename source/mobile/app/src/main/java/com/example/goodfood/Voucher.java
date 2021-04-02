package com.example.goodfood;

public class Voucher {
    private String ma_voucher;
    private String gia_tri_don_nho_nhat;
    private String giam_phan_tram;
    private String giam_tien;
    private String giam_toi_da;
    private String hsd;
    private String loai_giamgia;
    private String loai_voucher;
    private String mo_ta;

    public Voucher(String ma_voucher, String gia_tri_don_nho_nhat, String giam_phan_tram, String giam_tien, String giam_toi_da, String hsd, String loai_giamgia, String loai_voucher, String mo_ta) {
        this.ma_voucher = ma_voucher;
        this.gia_tri_don_nho_nhat = gia_tri_don_nho_nhat;
        this.giam_phan_tram = giam_phan_tram;
        this.giam_tien = giam_tien;
        this.giam_toi_da = giam_toi_da;
        this.hsd = hsd;
        this.loai_giamgia = loai_giamgia;
        this.loai_voucher = loai_voucher;
        this.mo_ta = mo_ta;
    }

    public String getMa_voucher() {
        return ma_voucher;
    }

    public void setMa_voucher(String ma_voucher) {
        this.ma_voucher = ma_voucher;
    }

    public String getGia_tri_don_nho_nhat() {
        return gia_tri_don_nho_nhat;
    }

    public void setGia_tri_don_nho_nhat(String gia_tri_don_nho_nhat) {
        this.gia_tri_don_nho_nhat = gia_tri_don_nho_nhat;
    }

    public String getGiam_phan_tram() {
        return giam_phan_tram;
    }

    public void setGiam_phan_tram(String giam_phan_tram) {
        this.giam_phan_tram = giam_phan_tram;
    }

    public String getGiam_tien() {
        return giam_tien;
    }

    public void setGiam_tien(String giam_tien) {
        this.giam_tien = giam_tien;
    }

    public String getGiam_toi_da() {
        return giam_toi_da;
    }

    public void setGiam_toi_da(String giam_toi_da) {
        this.giam_toi_da = giam_toi_da;
    }

    public String getHsd() {
        return hsd;
    }

    public void setHsd(String hsd) {
        this.hsd = hsd;
    }

    public String getLoai_giamgia() {
        return loai_giamgia;
    }

    public void setLoai_giamgia(String loai_giamgia) {
        this.loai_giamgia = loai_giamgia;
    }

    public String getLoai_voucher() {
        return loai_voucher;
    }

    public void setLoai_voucher(String loai_voucher) {
        this.loai_voucher = loai_voucher;
    }

    public String getMo_ta() {
        return mo_ta;
    }

    public void setMo_ta(String mo_ta) {
        this.mo_ta = mo_ta;
    }
}
