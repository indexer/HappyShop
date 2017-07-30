package com.indexer.happyshop;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.indexer.happyshop.database.entity.ProductEntity;
import com.indexer.happyshop.model.Product;
import com.indexer.happyshop.rest.RestClient;
import com.indexer.happyshop.viewmodel.ProductListViewModel;
import com.squareup.picasso.Picasso;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity implements LifecycleRegistryOwner {
  @BindView(R.id.mToolbar) Toolbar mToolbar;
  private final LifecycleRegistry mRegistry = new LifecycleRegistry(this);
  ProductListViewModel viewModel;
  @BindView(R.id.detailImage) ImageView detailImage;
  @BindView(R.id.product_name) TextView mProductName;
  @BindView(R.id.product_price) TextView getmProductPrice;
  @BindView(R.id.product_description) TextView mProductDescription;
  @BindView(R.id.addCart) Button addToCart;
  ProductEntity mProduct;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_detail);
    ButterKnife.bind(this);
    setSupportActionBar(mToolbar);
    if (mToolbar != null) {
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    mProduct = (ProductEntity) getIntent().getSerializableExtra("product");
    final String browseId = mProduct.getId() + ".json";
    //for testusage
    /*String browseId ="10.json";
    int productId =10;*/


    viewModel = ViewModelProviders.of(this).get(ProductListViewModel.class);
    viewModel.getProductById(mProduct.getId()).observe(this, new Observer<ProductEntity>() {
      @Override
      public void onChanged(@Nullable ProductEntity productEntity) {
        mProduct = productEntity;
        if (mProduct.getDescription() == null) {
          viewModel.fetchDataForDetail(browseId);
        } else {
          addToCart.setEnabled(true);
        }
        setUpUI(productEntity);
      }
    });

    addToCart.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        if (mProduct.isInCash()) {
          mProduct.setInCash(false);
        } else {
          mProduct.setInCash(true);
        }
        viewModel.updateProduct(mProduct);
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

  void setUpUI(ProductEntity productEntity) {
    Picasso.with(this)
        .load(productEntity.getImg_url())
        .into(detailImage);
    mProductName.setText(productEntity.getName());
    NumberFormat format = NumberFormat.getCurrencyInstance();
    String formatValue = format.format(productEntity.getPrice());
    getmProductPrice.setText(formatValue);
    mProductDescription.setText(productEntity.getDescription());
    if (productEntity.isInCash()) {
      addToCart.setText("Remove");
    } else {
      addToCart.setText("Add to Cart");
    }
  }

  @Override public LifecycleRegistry getLifecycle() {
    return mRegistry;
  }
}
