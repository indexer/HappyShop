package com.indexer.happyshop.base;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseAdapter<VH extends BaseViewHolder, T> extends RecyclerView.Adapter<VH> {

  protected List<T> mItems;

  public BaseAdapter() {
    mItems = new ArrayList<>();
  }

  public BaseAdapter(List<T> items) {
    this.mItems = items;
  }

  @Override public abstract VH onCreateViewHolder(ViewGroup parent, int viewType);

  @Override public abstract void onBindViewHolder(VH holder, int position);

  @Override public int getItemCount() {
    return mItems.size();
  }

  public List<T> getItems() {
    return mItems;
  }

  /**
   * @param items new array list for adapter
   */
  public void setItems(List<T> items) {
    this.mItems = (List<T>) items;
    notifyDataSetChanged();
  }

  /**
   * @param position position to return Item
   */
  public T getItem(int position) {
    return mItems.get(position);
  }

  /**
   * @param position position the lyric_item to be added in the adapter
   * @param item the lyric_item to be added to the list
   */
  public void addItem(int position, T item) {

    if (position < 0) {
      return;
    }

    if (position < mItems.size()) {
      mItems.add(position, item);
    } else {
      mItems.add(item);
      position = mItems.size();
    }

    notifyItemInserted(position);
  }

  /**
   * @param position position to remove the lyric_item from the adapter
   */
  public void removeItem(int position) {
    this.mItems.remove(position);
    notifyItemRemoved(position);
  }

  /**
   * remove all items from adapter
   */
  public void removeItems() {
    this.mItems.clear();
    notifyItemRangeRemoved(0, mItems.size());
  }

  /**
   * @param items the list to be added to existing list in the adapter
   */
  public void addItems(List<T> items) {
    int startIndex = this.mItems.size();
    this.mItems.addAll(startIndex, items);
    notifyItemRangeInserted(startIndex, items.size());
  }
}
