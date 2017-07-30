package com.indexer.happyshop.rest;

/**
 * Created by indexer on 26/7/17.
 */

import com.indexer.happyshop.model.ProductDetailReturnObject;
import com.indexer.happyshop.model.ProductReturnObject;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RestService {
  @GET("api/v1/products.json")
  Call<ProductReturnObject> getProducts(@Query("category") String category,
      @Query("page") int page);

  @GET("api/v1/products/{product}")
  Call<ProductDetailReturnObject> getProduct(@Path("product") String category);
}
