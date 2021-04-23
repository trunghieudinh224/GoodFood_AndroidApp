package com.example.admingoodfood;

public class Users {
    private String sdt;
    private String address;
    private String email;
    private String link_avatar;
    private String name;
    private String password;
    private String so_don_dat_hang;
    private String so_don_tich_luy;
    private String so_voucher_tich_luy;
    private String tich_luy_thang;
    private String type_member;

    public Users(String sdt, String address, String email, String link_avatar, String name, String password, String so_don_dat_hang, String so_don_tich_luy, String so_voucher_tich_luy, String tich_luy_thang, String type_member) {
        this.sdt = sdt;
        this.address = address;
        this.email = email;
        this.link_avatar = link_avatar;
        this.name = name;
        this.password = password;
        this.so_don_dat_hang = so_don_dat_hang;
        this.so_don_tich_luy = so_don_tich_luy;
        this.so_voucher_tich_luy = so_voucher_tich_luy;
        this.tich_luy_thang = tich_luy_thang;
        this.type_member = type_member;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLink_avatar() {
        return link_avatar;
    }

    public void setLink_avatar(String link_avatar) {
        this.link_avatar = link_avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSo_don_dat_hang() {
        return so_don_dat_hang;
    }

    public void setSo_don_dat_hang(String so_don_dat_hang) {
        this.so_don_dat_hang = so_don_dat_hang;
    }

    public String getSo_don_tich_luy() {
        return so_don_tich_luy;
    }

    public void setSo_don_tich_luy(String so_don_tich_luy) {
        this.so_don_tich_luy = so_don_tich_luy;
    }

    public String getSo_voucher_tich_luy() {
        return so_voucher_tich_luy;
    }

    public void setSo_voucher_tich_luy(String so_voucher_tich_luy) {
        this.so_voucher_tich_luy = so_voucher_tich_luy;
    }

    public String getTich_luy_thang() {
        return tich_luy_thang;
    }

    public void setTich_luy_thang(String tich_luy_thang) {
        this.tich_luy_thang = tich_luy_thang;
    }

    public String getType_member() {
        return type_member;
    }

    public void setType_member(String type_member) {
        this.type_member = type_member;
    }
}
