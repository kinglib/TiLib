package com.endlesscreator.tiviewlib.view.model.tidelegateadapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

/**
 * 建议直接继承 {@link ItemRecyclerViewDelegateAdapterDefault} 而不是实现此接口
 */
public interface IItemRecyclerViewDelegateAdapter<VH extends RecyclerView.ViewHolder> {

    void setDelegateAdapter(ITiRecyclerViewDelegateAdapter aDelegateAdapter);

    ITiRecyclerViewDelegateAdapter delegateAdapter();

    int getItemCount();

    VH onCreateViewHolder(ViewGroup parent, int viewType);

    void onBindViewHolder(VH holder, int position, List<Object> payloads);

    void onBindViewHolder(VH holder, int position);

    void notifyDataSetChanged();

    void notifyItemChanged(int position);

    void notifyItemChanged(int position, @Nullable Object payload);

    void notifyItemRangeChanged(int positionStart, int itemCount);

    void notifyItemRangeChanged(int positionStart, int itemCount, @Nullable Object payload);

    void notifyItemInserted(int position);

    void notifyItemRangeInserted(int positionStart, int itemCount);

    void notifyItemRemoved(int position);

    void notifyItemRangeRemoved(int positionStart, int itemCount);

    void notifyItemMoved(int fromPosition, int toPosition);

}
