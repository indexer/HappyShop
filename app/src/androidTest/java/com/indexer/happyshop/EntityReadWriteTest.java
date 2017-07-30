package com.indexer.happyshop;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import com.indexer.happyshop.database.AppDatabase;
import com.indexer.happyshop.database.dao.ProductDao;
import com.indexer.happyshop.database.entity.ProductEntity;
import com.indexer.happyshop.model.Product;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
public class EntityReadWriteTest {
  private ProductDao mUserDao;
  private AppDatabase mDb;

  @Before
  public void createDb() {
    Context context = InstrumentationRegistry.getTargetContext();
    mDb = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
    mUserDao = mDb.getProductDao();
  }

  @After
  public void closeDb() throws IOException {
    mDb.close();
  }

  @Test
  public void writeUserAndReadInList() throws Exception {
    ProductEntity productEntity = new ProductEntity();
    productEntity.setId(10);
    List<ProductEntity> mList = new ArrayList<>();
    mList.add(productEntity);
    mUserDao.insertAll(mList);
    ProductEntity mProduct = mUserDao.loadProductSync(10);
    assertThat(10, equalTo(mProduct.getId()));
  }
}