package com.example.ass_androidnetworking;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ProductAdapter extends ArrayAdapter<Product> {
    private ArrayList<Product> listProduct;
    Context mContext;
    private static class ViewHolder{
        TextView tvTenSanPham,tvSoLuong,tvGia;
    }
    public ProductAdapter(ArrayList<Product> listProduct, Context context) {
        super(context, R.layout.item_product,listProduct);
        this.listProduct = listProduct;
        this.mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Product product = getItem(position);
        ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_product,parent,false);
            viewHolder.tvTenSanPham = convertView.findViewById(R.id.tvTenSanPham);
            viewHolder.tvGia = convertView.findViewById(R.id.tvGiaSanPham);
            viewHolder.tvSoLuong = convertView.findViewById(R.id.tvSoLuong);
            convertView.setTag(viewHolder);
        }else{
            viewHolder =(ViewHolder) convertView.getTag();
        }
        viewHolder.tvSoLuong.setText("Số lượng " + product.soLUong);
        viewHolder.tvGia.setText("Giá " + product.giaTien);
        viewHolder.tvTenSanPham.setText("Tên sản phẩm " + product.tenSanPham);
        return convertView;
    }
}
