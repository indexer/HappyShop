package com.indexer.happyshop.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import com.indexer.happyshop.database.AppDatabase;
import com.indexer.happyshop.database.entity.ProductEntity;
import com.indexer.happyshop.model.Product;
import com.indexer.happyshop.model.ProductDetailReturnObject;
import com.indexer.happyshop.model.ProductReturnObject;
import com.indexer.happyshop.rest.RestClient;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductListViewModel extends AndroidViewModel {

  private static final MutableLiveData ABSENT = new MutableLiveData();
  AppDatabase mAppDatabase;

  static {
    //noinspection unchecked
    ABSENT.setValue(null);
  }

  private LiveData<List<ProductEntity>> mObservableProducts;
  private LiveData<ProductEntity> mObservableProduct;
  private LiveData<List<ProductEntity>> getmObservableCarts;
  private LiveData<List<ProductEntity>> getProductsByCategory;

  public ProductListViewModel(Application application) {
    super(application);
    mAppDatabase = AppDatabase.getDatabase(this.getApplication());
    mObservableProducts = mAppDatabase.getProductDao().loadAllProducts();
    getmObservableCarts = mAppDatabase.getProductDao().loadAllCarts(true);
  }

  private static class insertAsyncTask extends AsyncTask<List<ProductEntity>, Void, Void> {

    private AppDatabase db;

    insertAsyncTask(AppDatabase appDatabase) {
      db = appDatabase;
    }

    @SafeVarargs @Override
    protected final Void doInBackground(final List<ProductEntity>... params) {
      db.getProductDao().insertAll(params[0]);
      return null;
    }
  }

  private static class updateAsyncTask extends AsyncTask<ProductEntity, Void, Void> {

    private AppDatabase db;

    updateAsyncTask(AppDatabase appDatabase) {
      db = appDatabase;
    }

    @SafeVarargs @Override
    protected final Void doInBackground(final ProductEntity... params) {
      db.getProductDao().updateProduct(params[0]);
      return null;
    }
  }

  public void fetchData(String category, int page) {
    Call<ProductReturnObject> productReturnObjectCall =
        RestClient.getService(this.getApplication().getApplicationContext())
            .getProducts(category, page);
    productReturnObjectCall.enqueue(
        new Callback<ProductReturnObject>() {
          @Override
          public void onResponse(@NonNull Call<ProductReturnObject> call,
              @NonNull Response<ProductReturnObject> response) {
            insertData(response.body().getProductEntityArrayList());
          }

          @Override public void onFailure(@NonNull Call<ProductReturnObject> call, Throwable t) {

          }
        });
  }

  public void insertData(List<ProductEntity> productEntityList) {
    new insertAsyncTask(mAppDatabase).execute(productEntityList);
  }

  public void updateProduct(ProductEntity productEntity) {
    new updateAsyncTask(mAppDatabase).execute(productEntity);
  }

  public void fetchDataForDetail(String product) {
    Call<ProductDetailReturnObject> mRProducat = RestClient.getService(this.getApplication().
        getApplicationContext()).getProduct(product);
    mRProducat.enqueue(new Callback<ProductDetailReturnObject>() {
      @Override public void onResponse(Call<ProductDetailReturnObject> call,
          Response<ProductDetailReturnObject> response) {
        updateProduct(response.body().getProductEntity());
      }

      @Override public void onFailure(Call<ProductDetailReturnObject> call, Throwable t) {

      }
    });
  }

  /**
   * Expose the LiveData Products query so the UI can observe it.
   */
  public LiveData<List<ProductEntity>> getProducts() {
    return mObservableProducts;
  }

  public LiveData<List<ProductEntity>> getAllCarts() {
    return getmObservableCarts;
  }

  public LiveData<ProductEntity> getProductById(int id) {
    mObservableProduct = mAppDatabase.getProductDao().loadProduct(id);
    return mObservableProduct;
  }

  public LiveData<List<ProductEntity>> getProductsByCategory(String category) {
    getProductsByCategory = mAppDatabase.getProductDao().loadProductCategory(category);
    return getProductsByCategory;
  }
}
