package com.indexer.happyshop.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.indexer.happyshop.R;
import com.indexer.happyshop.base.BaseAdapter;
import com.indexer.happyshop.database.entity.ProductEntity;

public class CartAdapter extends BaseAdapter<CartItemView, ProductEntity> {
  @Override public CartItemView onCreateViewHolder(ViewGroup parent, int
      viewType) {
    View view =
        LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
    return new CartItemView(view, null);
  }

  @Override public int getItemCount() {
    return super.getItemCount();
  }

  @Override public void onBindViewHolder(CartItemView holder, int position) {
    holder.onBind(mItems.get(position), position);
  }
}
