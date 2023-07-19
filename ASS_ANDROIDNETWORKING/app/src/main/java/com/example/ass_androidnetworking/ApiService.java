package com.example.ass_androidnetworking;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {
    @POST("addProduct")
    Call<Product> createProduct(@Body Product product);
    @PUT("updateProduct/{id}")
    Call<Product> updateProduct(@Path("id") String id, @Body Product product);
    @DELETE("deleteProduct/{id}")
    Call<Void> deleteProduct(@Path("id") String id);
}
