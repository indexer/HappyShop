package com.indexer.happyshop.adapters;

import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.view.View;
import com.indexer.happyshop.R;
import com.indexer.happyshop.base.BaseAdapter;
import com.indexer.happyshop.database.entity.ProductEntity;

public class ProductAdapter extends BaseAdapter<ProductItemView, ProductEntity> {
  @Override public ProductItemView onCreateViewHolder(ViewGroup parent, int
      viewType) {
    View view =
        LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
    return new ProductItemView(view, null);
  }

  @Override public int getItemCount() {
    return super.getItemCount();
  }

  @Override public void onBindViewHolder(ProductItemView holder, int position) {
    holder.onBind(mItems.get(position), position);
  }
}
