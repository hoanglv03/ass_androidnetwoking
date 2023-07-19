package com.example.ass_androidnetworking;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ProductAdapter extends ArrayAdapter<Product> {
    private ArrayList<Product> listProduct;
    Context mContext;
    MainActivity mainActivity;
    private static class ViewHolder{
        TextView tvTenSanPham,tvSoLuong,tvGia;
        ImageView edit,delete;
    }
    public ProductAdapter(ArrayList<Product> listProduct, Context context,MainActivity mainActivity) {
        super(context, R.layout.item_product,listProduct);
        this.listProduct = listProduct;
        this.mContext = context;
        this.mainActivity = mainActivity;
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
            viewHolder.edit = convertView.findViewById(R.id.imgEdit);
            viewHolder.delete = convertView.findViewById(R.id.imgDelete);
            convertView.setTag(viewHolder);
        }else{
            viewHolder =(ViewHolder) convertView.getTag();
        }
        viewHolder.tvSoLuong.setText("Số lượng " + product.soLuong);
        viewHolder.tvGia.setText("Giá " + product.giaTien);
        viewHolder.tvTenSanPham.setText("Tên sản phẩm " + product.tenSanPham);
        viewHolder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.showDialogEdit(mContext, Gravity.CENTER,product,position);
            }
        });
        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.deleteProduct(mContext,product,position);
            }
        });
        return convertView;
    }
}
