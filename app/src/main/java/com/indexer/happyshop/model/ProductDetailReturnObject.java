package com.indexer.happyshop.model;

import com.google.gson.annotations.SerializedName;
import com.indexer.happyshop.database.entity.ProductEntity;

/**
 * Created by indexer on 29/7/17.
 */

public class ProductDetailReturnObject {
  public ProductEntity getProductEntity() {
    return productEntity;
  }

  public void setProductEntity(ProductEntity productEntity) {
    this.productEntity = productEntity;
  }

  @SerializedName("product")
  private ProductEntity productEntity;
}
