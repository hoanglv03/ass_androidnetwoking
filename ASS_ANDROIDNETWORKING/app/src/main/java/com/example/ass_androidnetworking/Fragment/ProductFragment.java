package com.example.ass_androidnetworking.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.ass_androidnetworking.Adapter.ProductAdapter;
import com.example.ass_androidnetworking.DTO.Product;
import com.example.ass_androidnetworking.DTO.ProductRequest;
import com.example.ass_androidnetworking.DTO.ProductResponse;
import com.example.ass_androidnetworking.Interface.ApiService;

import com.example.ass_androidnetworking.R;
import com.example.ass_androidnetworking.form.Constants;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductFragment extends Fragment {
    private String APv4 = "10.0.2.2";
    private String listProductApi = "http://" + APv4 + ":3000/api/listProduct";
    private static String TAG = ProductFragment.class.getSimpleName();
    private ProgressDialog progressDialog;
    private ListView listView;
    Product productReturn = null;
    private FloatingActionButton flBottom;

    private ProductAdapter productAdapter;
    private Button btnSearch;
    private EditText edSearch;
    List<Product> list;
    Uri mUri;
    ImageView image;
    String path;
    public ProductFragment() {
        // Required empty public constructor
    }


    public static ProductFragment newInstance(String param1, String param2) {
        ProductFragment fragment = new ProductFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = view.findViewById(R.id.lvProduct);
        btnSearch = view.findViewById(R.id.btnSearh);
        edSearch = view.findViewById(R.id.edSearch);
        flBottom = view.findViewById(R.id.flBottom);
        list = new ArrayList<>();
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Wait.....");
        progressDialog.setCancelable(false);
        showDialog();
        getListProductFromApi();
        flBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogAdd(getActivity(), Gravity.CENTER);
            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String value = edSearch.getText().toString().trim();
                if(value.isEmpty()){
                    Snackbar.make(getView(),  "Không được bỏ trống", Snackbar.LENGTH_LONG).show();
                    return;
                }
                showDialog();
                searchProduct(value);
            }
        });
    }

    private void searchProduct(String value) {
        Retrofit retrofit = createRetrofitInstance();
        ApiService productService = retrofit.create(ApiService.class);
        Product productModal = new Product();
        productModal.setTenSanPham(value);
        ProductRequest productRequest = new ProductRequest();
        productRequest.setOperation(Constants.UPDATE_PRODUCT);
        productRequest.setProduct(productModal);
        Call<List<Product>> call
                = productService.searchProduct(productRequest);
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if(response.isSuccessful()){
                    list = response.body();

                    Log.d(TAG, "onResponse: " + list.size());
                    if(list != null){
                        productAdapter = new ProductAdapter((ArrayList<Product>) list, getContext(), ProductFragment.this);
                        listView.setAdapter(productAdapter);

                    }
                }
                hideDialog();
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                hideDialog();
            }
        });
    }


    public void deleteProduct(Context context, Product product, int position) {
        showDialog();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Xóa sản phẩm");
        builder.setMessage("Bạn có chắc chắn muốn xóa sản phẩm này?");
        builder.setPositiveButton("Xóa", (dialog, which) -> {
            Retrofit retrofit = createRetrofitInstance();
            ApiService productService = retrofit.create(ApiService.class);
            Call<Void> call
                    = productService.deleteProduct(product.get_id());
            list.remove(position);
            productAdapter.notifyDataSetChanged();
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
                    if (response.isSuccessful()) {
                        Snackbar.make(getView(),  "Xóa thành công", Snackbar.LENGTH_LONG).show();
                        hideDialog();
                    }
                }
                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Snackbar.make(getView(),  "Error" + t, Snackbar.LENGTH_LONG).show();
                    hideDialog();
                }
            });
        });
        builder.setNegativeButton("Hủy", (dialogInterface, i) -> hideDialog());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void showDialogEdit(Context context, int gravity, Product product, int position) {
        showDialog();
        Dialog dialog = createDialog(context, R.layout.modal_add, gravity);
        if (dialog == null) {
            return;
        }
        EditText edPrice, edName, edSoLuong;
        Button btnSua, btnClose;
        edPrice = dialog.findViewById(R.id.edPrice);
        edName = dialog.findViewById(R.id.edName);
        edSoLuong = dialog.findViewById(R.id.edSoLuong);
        btnSua = dialog.findViewById(R.id.btnAdd);
        btnClose = dialog.findViewById(R.id.btnClose);
        edName.setText(product.getTenSanPham());
        edSoLuong.setText(product.getSoLuong());
        edPrice.setText(product.getGiaTien());
        btnSua.setText("Sửa");
        btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edName.getText().toString();
                String price = edPrice.getText().toString();
                String soLuong = edSoLuong.getText().toString();
                if(name.trim().isEmpty() || price.trim().isEmpty() || soLuong.trim().isEmpty() ){
                    Snackbar.make(getView(),  "Không được bỏ trống", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if(Integer.parseInt(price) <0){
                    Snackbar.make(getView(),  "Giá thì không được nhỏ hơn 0", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if( Integer.parseInt(soLuong) <=0 ){
                    Snackbar.make(getView(),  "Số lượng thì không được nhỏ hơn 0 hoặc bằng 0", Snackbar.LENGTH_LONG).show();
                    return;
                }
                Retrofit retrofit = createRetrofitInstance();
                ApiService productService = retrofit.create(ApiService.class);
                Product productModal = new Product();
                productModal.setTenSanPham(name);
                productModal.setGiaTien(price);
                productModal.setSoLuong(soLuong);
                ProductRequest productRequest = new ProductRequest();
                productRequest.setOperation(Constants.UPDATE_PRODUCT);
                productRequest.setProduct(productModal);
                Call<ProductResponse> call
                        = productService.updateProduct(product.get_id(), productRequest);
                call.enqueue(new Callback<ProductResponse>() {
                    @Override
                    public void onResponse(Call<ProductResponse> call, retrofit2.Response<ProductResponse> response) {
                        if (response.isSuccessful()) {
                            ProductResponse res =  response.body();
                            if (res.getResult().equals(Constants.SUCCESS)) {
                                list.set(position, res.getProduct());
                                productAdapter.notifyDataSetChanged();
                            }
                            Snackbar.make(getView(),  res.getMessage(), Snackbar.LENGTH_LONG).show();
                            hideDialog();
                        }
                    }

                    @Override
                    public void onFailure(Call<ProductResponse> call, Throwable t) {
                        Snackbar.make(getView(),  "Error" + t, Snackbar.LENGTH_LONG).show();
                        hideDialog();

                    }
                });
                dialog.dismiss();
            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                hideDialog();
            }
        });
        dialog.show();

    }

    private void showDialogAdd(Context context, int gravity) {
        showDialog();
        Dialog dialog = createDialog(context, R.layout.modal_add, gravity);
        if (dialog == null) {
            return;
        }
        EditText edPrice, edName, edSoLuong;
        Button btnAdd, btnClose,btnAddImage;

        edPrice = dialog.findViewById(R.id.edPrice);
        edName = dialog.findViewById(R.id.edName);
        edSoLuong = dialog.findViewById(R.id.edSoLuong);
        btnAdd = dialog.findViewById(R.id.btnAdd);
        btnClose = dialog.findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                hideDialog();
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edName.getText().toString();
                String price = edPrice.getText().toString();
                String soLuong = edSoLuong.getText().toString();
                if(name.trim().isEmpty() || price.trim().isEmpty() || soLuong.trim().isEmpty() ){
                    Snackbar.make(getView(),  "Không được bỏ trống", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if(Integer.parseInt(price) <0){
                    Snackbar.make(getView(),  "Giá thì không được nhỏ hơn 0", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if( Integer.parseInt(soLuong) <=0 ){
                    Snackbar.make(getView(),  "Số lượng thì không được nhỏ hơn 0 hoặc bằng 0", Snackbar.LENGTH_LONG).show();
                    return;
                }
                Retrofit retrofit = createRetrofitInstance();
                ApiService productService = retrofit.create(ApiService.class);
                Product product = new Product();
                product.setTenSanPham(name);
                product.setGiaTien(price);
                product.setSoLuong(soLuong);
                ProductRequest productRequest = new ProductRequest();
                productRequest.setOperation(Constants.ADD_PRODUCT);
                productRequest.setProduct(product);
                Call<ProductResponse> call
                        = productService.createProduct(productRequest);
                call.enqueue(new Callback<ProductResponse>() {
                    @Override
                    public void onResponse(Call<ProductResponse> call, retrofit2.Response<ProductResponse> response) {
                        ProductResponse res =  response.body();
                        if (res.getResult().equals(Constants.SUCCESS)) {
                            list.add(res.getProduct());
                            productAdapter.notifyDataSetChanged();
                        }
                        Snackbar.make(getView(),  res.getMessage(), Snackbar.LENGTH_LONG).show();
                        dialog.dismiss();
                        hideDialog();
                    }
                    @Override
                    public void onFailure(Call<ProductResponse> call, Throwable t) {
                        Snackbar.make(getView(),  "Error" + t, Snackbar.LENGTH_LONG).show();
                        hideDialog();
                    }
                });
                dialog.dismiss();
            }
        });
        dialog.show();
    }




    private void getListProductFromApi() {
        Retrofit retrofit = createRetrofitInstance();
        ApiService productService = retrofit.create(ApiService.class);
        productService.getProduct().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if(response.isSuccessful()){
                    list = response.body();
                    if(list != null){
                        productAdapter = new ProductAdapter((ArrayList<Product>) list, getContext(), ProductFragment.this);
                        listView.setAdapter(productAdapter);
                        hideDialog();
                    }
                }
            }
            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Log.d(TAG, "listProduct: " + t);
            }
        });
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
    private Retrofit createRetrofitInstance() {
        return new Retrofit.Builder()
                .baseUrl("http://" + APv4 + ":3000/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private Dialog createDialog(Context context, int layoutResId, int gravity) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(layoutResId);
        Window window = dialog.getWindow();

        if (window == null) {
            return null;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);

        dialog.setCancelable(Gravity.BOTTOM == gravity);

        return dialog;
    }
}