package com.indexer.happyshop;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.persistence.room.Query;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.indexer.happyshop.adapters.ProductAdapter;
import com.indexer.happyshop.adapters.SpacesItemDecoration;
import com.indexer.happyshop.database.AppDatabase;
import com.indexer.happyshop.database.entity.ProductEntity;
import com.indexer.happyshop.model.ProductReturnObject;
import com.indexer.happyshop.viewmodel.ProductListViewModel;
import com.squareup.picasso.Picasso;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LifecycleRegistryOwner {
  AppDatabase mAppDatabase;
  private ProductListViewModel viewModel;
  ProductAdapter mProductAdapter;
  @BindView(R.id.mProgress) ProgressBar mProgress;
  @BindView(R.id.product_list) RecyclerView product_list;
  @BindView(R.id.category) ImageView mImage;
  @BindView(R.id.mToolbar) android.support.v7.widget.Toolbar mToolbar;
  @BindView(R.id.textView) TextView mTextView;
  private int page = 1;
  ProductEntity mProduct;
  private final LifecycleRegistry mRegistry = new LifecycleRegistry(this);

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
    setSupportActionBar(mToolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setDisplayShowTitleEnabled(false);
    mProduct = (ProductEntity) getIntent().getSerializableExtra("product");
    Picasso.with(this)
        .load(mProduct.getImg_url())
        .into(mImage);
    mTextView.setText(mProduct.getCategory());

    mProductAdapter = new ProductAdapter();
    product_list.setAdapter(mProductAdapter);
    final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    product_list.setHasFixedSize(true);
    product_list.setItemAnimator(new DefaultItemAnimator());
    SpacesItemDecoration dividerItemDecoration =
        new SpacesItemDecoration(16);
    product_list.addItemDecoration(dividerItemDecoration);
    product_list.setLayoutManager(linearLayoutManager);

    viewModel = ViewModelProviders.of(this).get(ProductListViewModel.class);
    mAppDatabase = AppDatabase.getDatabase(this.getApplication());
    if (mAppDatabase.getProductDao() != null) {
      viewModel.fetchData(mProduct.getCategory(), page);
    }

    product_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
      @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if (dy > 0) {
          if ((linearLayoutManager.getChildCount()
              + linearLayoutManager.findFirstVisibleItemPosition())
              >= linearLayoutManager.getItemCount()) {
            page = page + 1;
            viewModel.fetchData(mProduct.getCategory(), page);
          }
        }
      }
    });

    viewModel.getProductsByCategory(mProduct.getCategory())
        .observe(this, new Observer<List<ProductEntity>>() {
          @Override public void onChanged(@Nullable List<ProductEntity> productEntityList) {
            if (productEntityList.size() == 0) {
              mProgress.setVisibility(View.VISIBLE);
              product_list.setVisibility(View.GONE);
            } else {
              mProgress.setVisibility(View.GONE);
              product_list.setVisibility(View.VISIBLE);
              mProductAdapter.addItems(productEntityList);
              mProductAdapter.notifyDataSetChanged();
            }
          }
        });
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    return super.onCreateOptionsMenu(menu);
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        finish();
        break;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override public LifecycleRegistry getLifecycle() {
    return mRegistry;
  }
}
