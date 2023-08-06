package com.example.ass_androidnetworking.DTO;

import com.google.gson.annotations.SerializedName;

public class Product {
    String _id;
    String tenSanPham;
    String giaTien;
    String soLuong;
    @SerializedName("avt")
    String avatar;
    public Product() {
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Product(String _id, String tenSanPham, String giaTien, String soLuong) {
        this._id = _id;
        this.tenSanPham = tenSanPham;
        this.giaTien = giaTien;
        this.soLuong = soLuong;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getTenSanPham() {
        return tenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }

    public String getGiaTien() {
        return giaTien;
    }

    public void setGiaTien(String giaTien) {
        this.giaTien = giaTien;
    }

    public String getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(String soLuong) {
        this.soLuong = soLuong;
    }
}
