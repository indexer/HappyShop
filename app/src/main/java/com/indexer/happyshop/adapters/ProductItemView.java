package com.indexer.happyshop.adapters;

import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.indexer.happyshop.DetailActivity;
import com.indexer.happyshop.R;
import com.indexer.happyshop.base.BaseViewHolder;
import com.indexer.happyshop.database.entity.ProductEntity;
import com.indexer.happyshop.model.Product;
import com.squareup.picasso.Picasso;
import java.text.NumberFormat;

public class ProductItemView extends BaseViewHolder {
  @BindView(R.id.item_image) ImageView itemImage;
  @BindView(R.id.product_name) TextView mProductText;
  @BindView(R.id.product_description) TextView getmProductDescriptionText;
  @BindView(R.id.product_discount) TextView getProductDiscount;
  @BindString(R.string.discount) String discount;
  ProductEntity mProduct;

  public ProductItemView(View itemView, OnItemClickListener listener) {
    super(itemView, listener);
    ButterKnife.bind(this, itemView);
  }

  @Override public void onClick(View v) {
    Intent intent = new Intent(itemView.getContext(), DetailActivity.class);
    intent.putExtra("product", mProduct);
    itemView.getContext().startActivity(intent);
  }

  public void onBind(ProductEntity product, int position) {
    mProduct = new ProductEntity(product);
    Picasso.with(itemView.getContext())
        .load(product.getImg_url())
        .into(itemImage);
    if (mProduct.getName() != null) {
      mProductText.setText(product.getName().trim());
    }
    NumberFormat format = NumberFormat.getCurrencyInstance();
    String formatValue = format.format(product.getPrice());
    getmProductDescriptionText.setText(formatValue);
    if (product.isUnder_sale()) {
      getProductDiscount.setVisibility(View.VISIBLE);
      getProductDiscount.setText(discount);
    } else {
      getProductDiscount.setVisibility(View.GONE);
    }
  }
}
