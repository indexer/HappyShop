package com.indexer.happyshop.database.entity;

/**
 * Created by indexer on 26/7/17.
 */

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.indexer.happyshop.model.Product;
import java.io.Serializable;

@Entity(tableName = "products")
public class ProductEntity implements Product, Serializable {
  @PrimaryKey
  @SerializedName("id")
  @Expose
  private int id;
  @SerializedName("name")
  @Expose
  private String name;
  @SerializedName("description")
  @Expose
  private String description;
  @SerializedName("price")
  @Expose
  private int price;

  public void setCategory(String category) {
    this.category = category;
  }

  @SerializedName("category")
  private String category;

  public void setInCash(boolean inCash) {
    isInCash = inCash;
  }

  private boolean isInCash = false;

  public boolean isUnder_sale() {
    return under_sale;
  }

  @Override public boolean isInCash() {
    return isInCash;
  }

  public void setUnder_sale(boolean under_sale) {
    this.under_sale = under_sale;
  }

  @SerializedName("under_sale")
  private boolean under_sale;

  public String getImg_url() {
    return img_url;
  }

  public void setImg_url(String img_url) {
    this.img_url = img_url;
  }

  @SerializedName("img_url")
  private String img_url;

  @Override
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  @Override
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public int getPrice() {
    return price;
  }

  @Override public String getCategory() {
    return category;
  }

  public void setPrice(int price) {
    this.price = price;
  }

  public ProductEntity() {
  }

  public ProductEntity(Product product) {
    this.id = product.getId();
    this.name = product.getName();
    this.category = product.getCategory();
    this.description = product.getDescription();
    this.price = product.getPrice();
    this.img_url = product.getImg_url();
    this.isInCash = product.isInCash();
  }
}

