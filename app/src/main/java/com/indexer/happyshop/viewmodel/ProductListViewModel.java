package com.indexer.happyshop.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.indexer.happyshop.database.AppDatabase;
import com.indexer.happyshop.database.entity.ProductEntity;
import com.indexer.happyshop.model.ProductDetailReturnObject;
import com.indexer.happyshop.model.ProductReturnObject;
import com.indexer.happyshop.rest.RestClient;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.android.schedulers.AndroidSchedulers;
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
    private LiveData<List<ProductEntity>> getmObservableCarts;

    public ProductListViewModel(Application application) {
        super(application);
        mAppDatabase = AppDatabase.getDatabase(this.getApplication());
        mObservableProducts = mAppDatabase.getProductDao().loadAllProducts();
        getmObservableCarts = mAppDatabase.getProductDao().loadAllCarts(true);
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

                    @Override
                    public void onFailure(@NonNull Call<ProductReturnObject> call, @NonNull Throwable t) {

                    }
                });
    }

    private void insertData(final List<ProductEntity> productEntityList) {
        io.reactivex.Observable.fromCallable(new Callable<List<ProductEntity>>() {
            @Override
            public List<ProductEntity> call() throws Exception {
                mAppDatabase.getProductDao().insertAll(productEntityList);
                return null;
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe();
    }

    public void updateProduct(final ProductEntity productEntity) {
        io.reactivex.Observable.fromCallable(new Callable<ProductEntity>() {
            @Override
            public ProductEntity call() throws Exception {
                mAppDatabase.getProductDao().updateProduct(productEntity);
                return null;
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe();
    }

    public void fetchDataForDetail(String product) {
        Call<ProductDetailReturnObject> mRProducat = RestClient.getService(this.getApplication().
                getApplicationContext()).getProduct(product);
        mRProducat.enqueue(new Callback<ProductDetailReturnObject>() {
            @Override
            public void onResponse(@NonNull Call<ProductDetailReturnObject> call,
                                   @NonNull Response<ProductDetailReturnObject> response) {
                updateProduct(response.body().getProductEntity());

            }

            @Override
            public void onFailure(@NonNull Call<ProductDetailReturnObject> call, Throwable t) {

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
        return mAppDatabase.getProductDao().loadProduct(id);
    }

    public LiveData<List<ProductEntity>> getProductsByCategory(String category) {
        return mAppDatabase.getProductDao().loadProductCategory(category);
    }
}
