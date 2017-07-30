package com.indexer.happyshop.database;

/**
 * Created by indexer on 26/7/17.
 */

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import com.indexer.happyshop.database.dao.ProductDao;
import com.indexer.happyshop.database.entity.ProductEntity;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {ProductEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

  private static AppDatabase INSTANCE;
  private static final MutableLiveData<Boolean> mIsDatabaseCreated = new MutableLiveData<>();

  public LiveData<Boolean> isDatabaseCreated() {
    return mIsDatabaseCreated;
  }

  public static AppDatabase getDatabase(Context context) {
    mIsDatabaseCreated.setValue(false);// Trigger an update to show a loading screen.
    if (INSTANCE == null) {
      INSTANCE =
          Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "product_db")
              .build();
      mIsDatabaseCreated.setValue(true);
    }
    return INSTANCE;
  }

  public static void destroyInstance() {
    INSTANCE = null;
  }

  public abstract ProductDao getProductDao();
}