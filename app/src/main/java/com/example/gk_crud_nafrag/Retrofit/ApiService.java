package com.example.gk_crud_nafrag.Retrofit;

import com.example.gk_crud_nafrag.model.Feature;
import com.example.gk_crud_nafrag.model.Image;
import com.example.gk_crud_nafrag.model.Product;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;


public interface ApiService {
    //https://d19cqcnpm01-api.azurewebsites.net/api/users
    //https://jsonplaceholder.typicode.com/posts
    //https://d19cqcnpm01-api.azurewebsites.net/api/products
    //https://d19cqcnpm01-api.azurewebsites.net/api/images?productId=3
    //https://d19cqcnpm01-api.azurewebsites.net/api/products/20141
    //http://localhost:8080/api/products
    //http://192.168.10.238:8080/api/products
    //http://192.168.10.238:8080/swagger-ui/index.html#/
    //
    String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbjAwNyIsImlhdCI6MTY4MDk1MTg2OCwiZXhwIjoxNjgxMDM4MjY4fQ.ib_io4c-tz7jsdp3m0lASqtt98W9ADDzIVGsDQfu3qkp4ugnF6TSiBxE9_nJ0GfvbGwnrACw4ugfqVlmiJvd5g";

    OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(100, TimeUnit.SECONDS)
            .readTimeout(100,TimeUnit.SECONDS)
            .addInterceptor(new TokenInterceptor(token))
            .build();
    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();

    // client -> setTime gọi API,

    ApiService apiService = new Retrofit.Builder()
            .baseUrl("http://192.168.1.7:8080/")
            .client(client) // bỏ cũng đc nếu API chạy nhanh
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService.class);
    @GET("api/products")
    Call<List<Product>> productListData();
    @DELETE("api/products/{productId}")
    Call<Void> DeleteProductID(@Path("productId") int productId);
    @GET("api/features")
    Call<List<Feature>> featureListData();

    @POST("api/products")
    Call<Product> PostProduct(@Body Product products);

    @PUT("api/products/{productId}")
    Call<Product> UpdateProduct(@Path("productId") int productId, @Body Product product);

}
