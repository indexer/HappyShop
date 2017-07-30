package com.indexer.happyshop.database.dao;

/**
 * Created by indexer on 26/7/17.
 */

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import android.arch.persistence.room.Update;
import com.indexer.happyshop.database.entity.ProductEntity;
import java.util.List;

@Dao
public interface ProductDao {
  @Query("SELECT * FROM products")
  LiveData<List<ProductEntity>> loadAllProducts();

  @Update
  void updateProduct(ProductEntity user);

  @Query(("SELECT * from products where isInCash = :isInCash"))
  LiveData<List<ProductEntity>> loadAllCarts(boolean isInCash);

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  void insertAll(List<ProductEntity> products);

  @Query("select * from products where id = :productId")
  LiveData<ProductEntity> loadProduct(int productId);

  @Query("select * from products where id = :productId")
  ProductEntity loadProductSync(int productId);

  @Query("select * from products where category = :category")
  LiveData<List<ProductEntity>> loadProductCategory(String category);
}