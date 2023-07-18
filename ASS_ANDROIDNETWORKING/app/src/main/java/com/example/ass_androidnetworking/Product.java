package com.example.ass_androidnetworking;

public class Product {
    int _id;
    String tenSanPham;
    String giaTien;
    String soLUong;

    public Product() {
    }

    public Product(int _id, String tenSanPham, String giaTien, String soLUong) {
        this._id = _id;
        this.tenSanPham = tenSanPham;
        this.giaTien = giaTien;
        this.soLUong = soLUong;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
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

    public String getSoLUong() {
        return soLUong;
    }

    public void setSoLUong(String soLUong) {
        this.soLUong = soLUong;
    }
}
