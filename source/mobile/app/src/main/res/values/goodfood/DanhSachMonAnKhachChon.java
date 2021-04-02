package com.example.goodfood;

import java.io.Serializable;

public class DanhSachMonAnKhachChon implements Serializable {
    private String tenmon;
    private String sl;
    private String giatien;
    private String thanhtien;

    public DanhSachMonAnKhachChon(String tenmon, String sl, String giatien, String thanhtien) {
        this.tenmon = tenmon;
        this.sl = sl;
        this.giatien = giatien;
        this.thanhtien = thanhtien;
    }

    public String getTenmon() {
        return tenmon;
    }

    public void setTenmon(String tenmon) {
        this.tenmon = tenmon;
    }

    public String getSl() {
        return sl;
    }

    public void setSl(String sl) {
        this.sl = sl;
    }

    public String getGiatien() {
        return giatien;
    }

    public void setGiatien(String giatien) {
        this.giatien = giatien;
    }

    public String getThanhtien() {
        return thanhtien;
    }

    public void setThanhtien(String thanhtien) {
        this.thanhtien = thanhtien;
    }
}

