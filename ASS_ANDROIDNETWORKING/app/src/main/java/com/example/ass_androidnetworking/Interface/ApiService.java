package com.example.ass_androidnetworking.Interface;

import com.example.ass_androidnetworking.DTO.Product;
import com.example.ass_androidnetworking.DTO.ProductRequest;
import com.example.ass_androidnetworking.DTO.ProductResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {
    @GET ("listProduct")
    Call<List<Product>> getProduct();
    @POST("addProduct")
    Call<ProductResponse> createProduct(@Body ProductRequest productRequest);

    @POST("searchProduct")
    Call<List<Product>> searchProduct(@Body ProductRequest productRequest);
    @PUT("updateProduct/{id}")
    Call<ProductResponse> updateProduct(@Path("id") String id, @Body ProductRequest productRequest);
    @DELETE("deleteProduct/{id}")
    Call<Void> deleteProduct(@Path("id") String id);
}
