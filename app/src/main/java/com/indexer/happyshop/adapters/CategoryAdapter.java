package com.indexer.happyshop.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.indexer.happyshop.R;
import com.indexer.happyshop.base.BaseAdapter;
import com.indexer.happyshop.database.entity.ProductEntity;

public class CategoryAdapter extends BaseAdapter<CategoryItemView, ProductEntity> {
  @Override public CategoryItemView onCreateViewHolder(ViewGroup parent, int
      viewType) {
    View view =
        LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);
    return new CategoryItemView(view, null);
  }

  @Override public int getItemCount() {
    return super.getItemCount();
  }

  @Override public void onBindViewHolder(CategoryItemView holder, int position) {
    holder.onBind(mItems.get(position), position);
  }
}
