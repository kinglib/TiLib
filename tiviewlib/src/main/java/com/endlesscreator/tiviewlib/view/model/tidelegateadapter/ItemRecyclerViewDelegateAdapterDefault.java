package com.endlesscreator.tiviewlib.view.model.tidelegateadapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import java.util.List;

public abstract class ItemRecyclerViewDelegateAdapterDefault<VH extends RecyclerView.ViewHolder> implements IItemRecyclerViewDelegateAdapter<VH> {

    protected ITiRecyclerViewDelegateAdapter mDelegateAdapter;

    @Override
    public void setDelegateAdapter(ITiRecyclerViewDelegateAdapter aDelegateAdapter) {
        mDelegateAdapter = aDelegateAdapter;
    }

    @Override
    public void onBindViewHolder(VH holder, int position, List<Object> payloads) {
        onBindViewHolder(holder, position);
    }

    @Override
    public void notifyDataSetChanged() {
        mDelegateAdapter.notifyDataSetChanged(this);
    }

    @Override
    public void notifyItemChanged(int position) {
        mDelegateAdapter.notifyItemChanged(this, position);
    }

    @Override
    public void notifyItemChanged(int position, @Nullable Object payload) {
        mDelegateAdapter.notifyItemChanged(this, position, payload);
    }

    @Override
    public void notifyItemRangeChanged(int positionStart, int itemCount) {
        mDelegateAdapter.notifyItemRangeChanged(this, positionStart, itemCount);
    }

    @Override
    public void notifyItemRangeChanged(int positionStart, int itemCount, @Nullable Object payload) {
        mDelegateAdapter.notifyItemRangeChanged(this, positionStart, itemCount, payload);
    }

    @Override
    public void notifyItemInserted(int position) {
        mDelegateAdapter.notifyItemInserted(this, position);
    }

    @Override
    public void notifyItemRangeInserted(int positionStart, int itemCount) {
        mDelegateAdapter.notifyItemRangeInserted(this, positionStart, itemCount);
    }

    @Override
    public void notifyItemRemoved(int position) {
        mDelegateAdapter.notifyItemRemoved(this, position);
    }

    @Override
    public void notifyItemRangeRemoved(int positionStart, int itemCount) {
        mDelegateAdapter.notifyItemRangeRemoved(this, positionStart, itemCount);
    }

    @Override
    public void notifyItemMoved(int fromPosition, int toPosition) {
        mDelegateAdapter.notifyItemMoved(this, fromPosition, toPosition);
    }
}
