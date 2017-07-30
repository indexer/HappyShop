package com.indexer.happyshop.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.indexer.happyshop.database.entity.ProductEntity;
import java.util.ArrayList;

/**
 * Created by indexer on 26/7/17.
 */

public class ProductReturnObject {

  @Expose
  @SerializedName("products")
  private ArrayList<ProductEntity> productEntityArrayList = new ArrayList<>();

  public ArrayList<ProductEntity> getProductEntityArrayList() {
    return productEntityArrayList;
  }
}
