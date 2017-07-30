package com.indexer.happyshop;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.indexer.happyshop.adapters.CartAdapter;
import com.indexer.happyshop.adapters.CategoryAdapter;
import com.indexer.happyshop.adapters.SpacesItemDecoration;
import com.indexer.happyshop.database.entity.ProductEntity;
import com.indexer.happyshop.model.Product;
import com.indexer.happyshop.viewmodel.ProductListViewModel;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements LifecycleRegistryOwner {
  @BindView(R.id.category) RecyclerView mCategory;
  @BindView(R.id.cart) RecyclerView mCarts;
  @BindView(R.id.mToolbar) Toolbar mToolbar;
  @BindView(R.id.cart_info) TextView mCartInfo;
  @BindView(R.id.main_content) CoordinatorLayout coordinatorLayout;
  @BindView(R.id.cart_total) TextView getmCartInfoTotal;
  private ProductListViewModel viewModel;
  private final LifecycleRegistry mRegistry = new LifecycleRegistry(this);

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);
    ButterKnife.bind(this);
    setSupportActionBar(mToolbar);
    getSupportActionBar().setDisplayShowTitleEnabled(true);
    getSupportActionBar().setTitle(R.string.app_name);
    final ArrayList<ProductEntity> productEntities = loadProduct(this);
    CategoryAdapter categoryAdapter = new CategoryAdapter();
    categoryAdapter.setItems(productEntities);
    mCategory.setHasFixedSize(true);
    mCategory.setLayoutManager(new LinearLayoutManager(this,
        LinearLayoutManager.HORIZONTAL, true));
    SpacesItemDecoration dividerItemDecoration =
        new SpacesItemDecoration(16);
    mCategory.addItemDecoration(dividerItemDecoration);
    mCategory.setAdapter(categoryAdapter);
    viewModel = ViewModelProviders.of(this).get(ProductListViewModel.class);
    if (!isNetworkAvaliable(this)) {
      noInternetAction();
      mCartInfo.setVisibility(View.VISIBLE);
    }
    final CartAdapter cartAdapter = new CartAdapter();
    mCarts.setAdapter(cartAdapter);
    mCarts.setLayoutManager(new GridLayoutManager(this, 3));

    mCartInfo.setVisibility(View.VISIBLE);

    viewModel.getAllCarts().observe(this, new Observer<List<ProductEntity>>() {
      @Override public void onChanged(@Nullable List<ProductEntity> productEntityList) {
        if (productEntities.size() > 0) {
          mCarts.setVisibility(View.VISIBLE);
          mCartInfo.setVisibility(View.GONE);
        }
        getmCartInfoTotal.setText("" + productEntityList.size());
        cartAdapter.setItems(productEntityList);
      }
    });
  }

  private void noInternetAction() {

    Snackbar snackbar = Snackbar
        .make(coordinatorLayout, "There is no Network Connection", Snackbar.LENGTH_LONG)
        .setAction("Open", new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
          }
        });

    snackbar.show();
  }

  public static boolean isNetworkAvaliable(Context ctx) {
    ConnectivityManager connectivityManager = (ConnectivityManager) ctx
        .getSystemService(Context.CONNECTIVITY_SERVICE);
    if ((connectivityManager
        .getNetworkInfo(ConnectivityManager.TYPE_MOBILE) != null && connectivityManager
        .getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED)
        || (connectivityManager
        .getNetworkInfo(ConnectivityManager.TYPE_WIFI) != null && connectivityManager
        .getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        .getState() == NetworkInfo.State.CONNECTED)) {
      return true;
    } else {
      return false;
    }
  }

  public ArrayList<ProductEntity> loadProduct(Context context) {
    ArrayList<ProductEntity> productArrayList = new ArrayList<>();
    Gson gson = new Gson();
    try {
      InputStream json = context.getAssets().open("product.json");
      JsonParser parser = new JsonParser();
      JsonReader reader = new JsonReader(new InputStreamReader(json));
      reader.setLenient(true);

      JsonArray data = parser.parse(reader).getAsJsonArray();

      for (JsonElement element : data) {
        ProductEntity productEntity = gson.fromJson(element, ProductEntity.class);
        productArrayList.add(productEntity);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    return productArrayList;
  }

  @Override public LifecycleRegistry getLifecycle() {
    return mRegistry;
  }
}
