package com.example.ass_androidnetworking;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private String listProductApi = "http://192.168.0.104:3000/api/listProduct";
    private static String TAG = MainActivity.class.getSimpleName();
    private ProgressDialog progressDialog;
    private ListView listView;
    private ProductAdapter productAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.lvProduct);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Wait.....");
        progressDialog.setCancelable(false);
        getListProductFromApi();

    }
    private void getListProductFromApi(){
        ArrayList<Product> list = new ArrayList<>();
        JsonObjectRequest objectRequest = new JsonObjectRequest(listProductApi, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    JSONArray listProduct = response.getJSONArray("data");
                    for(int i = 0;i < listProduct.length(); i++){
                        JSONObject objProduct = listProduct.getJSONObject(i);
                        String tenSanPham = objProduct.getString("tenSanPham");
                        String giaTien = objProduct.getString("giaTien");
                        String soLuong = objProduct.getString("soLuong");
                        Product productObj = new Product();
                        productObj.setTenSanPham(tenSanPham);
                        productObj.setGiaTien(giaTien);
                        productObj.setSoLUong(soLuong);
                        list.add(productObj);

                    }
                }catch (Exception e){
                    e.printStackTrace();
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
        productAdapter = new ProductAdapter(list,MainActivity.this);

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