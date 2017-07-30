package com.indexer.happyshop.adapters;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.indexer.happyshop.DetailActivity;
import com.indexer.happyshop.MainActivity;
import com.indexer.happyshop.R;
import com.indexer.happyshop.base.BaseViewHolder;
import com.indexer.happyshop.database.entity.ProductEntity;
import com.squareup.picasso.Picasso;
import java.text.NumberFormat;

public class CartItemView extends BaseViewHolder {
  @BindView(R.id.category_image) ImageView itemImage;
  @BindView(R.id.product_name) TextView product_name;
  @BindView(R.id.product_price) TextView product_price;
  @BindView(R.id.category_title) TextView mCategory;

  ProductEntity mProduct;

  public CartItemView(View itemView, OnItemClickListener listener) {
    super(itemView, listener);
    ButterKnife.bind(this, itemView);
  }

  @Override public void onClick(View v) {
    Intent intent = new Intent(itemView.getContext(), DetailActivity.class);
    intent.putExtra("product", mProduct);
    itemView.getContext().startActivity(intent);
  }

  public void onBind(ProductEntity product, int position) {
    mProduct = product;
    Picasso.with(itemView.getContext())
        .load(product.getImg_url())
        .into(itemImage);
    product_name.setText(mProduct.getName());
    NumberFormat format = NumberFormat.getCurrencyInstance();
    String formatValue = format.format(product.getPrice());
    product_price.setText(formatValue);
    mCategory.setText(product.getCategory());
  }
}
