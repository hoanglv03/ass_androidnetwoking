package com.example.ass_androidnetworking;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private String APv4 = "192.168.0.104";
    private String listProductApi = "http://" + APv4 + ":3000/api/listProduct";
    private static String TAG = MainActivity.class.getSimpleName();
    private ProgressDialog progressDialog;
    private ListView listView;
    Product productReturn = null;
    private FloatingActionButton flBottom;

    private ProductAdapter productAdapter;
    ArrayList<Product> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.lvProduct);
        flBottom = findViewById(R.id.flBottom);
        list = new ArrayList<>();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Wait.....");
        progressDialog.setCancelable(false);
        getListProductFromApi();
        flBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogAdd(MainActivity.this, Gravity.CENTER);
            }
        });

    }
    public void deleteProduct(Context context,Product product,int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Xóa sản phẩm");
        builder.setMessage("Bạn có chắc chắn muốn xóa sản phẩm này?");
        builder.setPositiveButton("Xóa", (dialog, which) -> {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://" + APv4 + ":3000/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            ApiService productService = retrofit.create(ApiService.class);
            Call<Void> call
                    = productService.deleteProduct(product.get_id());
            list.remove(position);
            productAdapter.notifyDataSetChanged();
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
                    if(response.isSuccessful()){
                        Toast.makeText(MainActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {

                }
            });
        });
        builder.setNegativeButton("Hủy", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public void showDialogEdit(Context context, int gravity,Product product,int position) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.modal_add);
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);

        if (Gravity.BOTTOM == gravity) {
            dialog.setCancelable(true);
        } else {
            dialog.setCancelable(false);
        }
        EditText edPrice, edName, edSoLuong;
        Button btnSua, btnClose;
        edPrice = dialog.findViewById(R.id.edPrice);
        edName = dialog.findViewById(R.id.edName);
        edSoLuong = dialog.findViewById(R.id.edSoLuong);
        btnSua = dialog.findViewById(R.id.btnAdd);
        btnClose = dialog.findViewById(R.id.btnClose);


        edName.setText(product.tenSanPham);
        edSoLuong.setText(product.soLuong);
        edPrice.setText(product.giaTien);

        btnSua.setText("Sửa");
        btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edName.getText().toString();
                String price = edPrice.getText().toString();
                String soLuong = edSoLuong.getText().toString();
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://" + APv4 + ":3000/api/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                ApiService productService = retrofit.create(ApiService.class);
                Product productModal = new Product();
                productModal.setTenSanPham(name);
                productModal.setGiaTien(price);
                productModal.setSoLuong(soLuong);
                Call<Product> call
                        = productService.updateProduct(product._id,productModal);
                call.enqueue(new Callback<Product>() {
                    @Override
                    public void onResponse(Call<Product> call, retrofit2.Response<Product> response) {
                        if(response.isSuccessful()){

                            Toast.makeText(MainActivity.this, "Sửa thành công", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onResponse222: " );
                            list.set(position,response.body());
                            productAdapter.notifyDataSetChanged();

                        }
                    }

                    @Override
                    public void onFailure(Call<Product> call, Throwable t) {

                    }
                });
                dialog.dismiss();
            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }
    private void showDialogAdd(Context context, int gravity) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.modal_add);
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);

        if (Gravity.BOTTOM == gravity) {
            dialog.setCancelable(true);
        } else {
            dialog.setCancelable(false);
        }
        EditText edPrice, edName, edSoLuong;
        Button btnAdd, btnClose;
        edPrice = dialog.findViewById(R.id.edPrice);
        edName = dialog.findViewById(R.id.edName);
        edSoLuong = dialog.findViewById(R.id.edSoLuong);
        btnAdd = dialog.findViewById(R.id.btnAdd);
        btnClose = dialog.findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edName.getText().toString();
                String price = edPrice.getText().toString();
                String soLuong = edSoLuong.getText().toString();
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://" + APv4 + ":3000/api/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                ApiService productService = retrofit.create(ApiService.class);
                Product product = new Product();
                product.setTenSanPham(name);
                product.setGiaTien(price);
                product.setSoLuong(soLuong);
                list.add(product);
                productAdapter.notifyDataSetChanged();
                Call<Product> call
                        = productService.createProduct(product);
                call.enqueue(new Callback<Product>() {
                    @Override
                    public void onResponse(Call<Product> call, retrofit2.Response<Product> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();

                        }
                    }
                    @Override
                    public void onFailure(Call<Product> call, Throwable t) {

                    }
                });
                dialog.dismiss();

            }

        });

        dialog.show();
    }

    private void getListProductFromApi() {
        JsonObjectRequest objectRequest = new JsonObjectRequest(listProductApi, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray listProduct = response.getJSONArray("data");
                    for (int i = 0; i < listProduct.length(); i++) {
                        JSONObject objProduct = listProduct.getJSONObject(i);
                        String id = objProduct.getString("_id");
                        String tenSanPham = objProduct.getString("tenSanPham");
                        String giaTien = objProduct.getString("giaTien");
                        String soLuong = objProduct.getString("soLuong");
                        Product productObj = new Product();
                        productObj.set_id(id);
                        productObj.setTenSanPham(tenSanPham);
                        productObj.setGiaTien(giaTien);
                        productObj.setSoLuong(soLuong);
                        list.add(productObj);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(TAG, "Error: " + e.getMessage());
                }

                hideDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        });

        AppController.getInstance().addToRequestQueue(objectRequest);
        productAdapter = new ProductAdapter(list, MainActivity.this,this);
        Log.d(TAG, "ListSize 1: " + list.size());
        listView.setAdapter(productAdapter);
    }

    private void showDialog() {
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    private void hideDialog() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}