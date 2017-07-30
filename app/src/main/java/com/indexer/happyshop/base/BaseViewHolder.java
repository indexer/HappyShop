package com.indexer.happyshop.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

public abstract class BaseViewHolder extends RecyclerView.ViewHolder
    implements View.OnClickListener {

  protected final OnItemClickListener listener;

  public BaseViewHolder(View itemView, OnItemClickListener listener) {
    super(itemView);
    this.listener = listener;
    this.itemView.setOnClickListener(this);
  }

  @Override public void onClick(View v) {
    if (listener != null) listener.onItemClick(getAdapterPosition());
  }

  public interface OnItemClickListener {
    void onItemClick(int position);
  }
}
