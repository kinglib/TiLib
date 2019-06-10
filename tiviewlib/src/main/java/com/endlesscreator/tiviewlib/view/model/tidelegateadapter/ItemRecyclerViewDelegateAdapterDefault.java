package com.endlesscreator.tiviewlib.view.model.tidelegateadapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import java.util.List;

public abstract class ItemRecyclerViewDelegateAdapterDefault<VH extends RecyclerView.ViewHolder> implements IItemRecyclerViewDelegateAdapter<VH> {

    private ITiRecyclerViewDelegateAdapter delegateAdapter;

    @Override
    public void setDelegateAdapter(ITiRecyclerViewDelegateAdapter aDelegateAdapter) {
        delegateAdapter = aDelegateAdapter;
    }

    @Override
    public ITiRecyclerViewDelegateAdapter delegateAdapter() {
        return delegateAdapter;
    }

    @Override
    public void onBindViewHolder(VH holder, int position, List<Object> payloads) {
        onBindViewHolder(holder, position);
    }

    @Override
    public void notifyDataSetChanged() {
        delegateAdapter.notifyDataSetChanged(this);
    }

    @Override
    public void notifyItemChanged(int position) {
        delegateAdapter.notifyItemChanged(this, position);
    }

    @Override
    public void notifyItemChanged(int position, @Nullable Object payload) {
        delegateAdapter.notifyItemChanged(this, position, payload);
    }

    @Override
    public void notifyItemRangeChanged(int positionStart, int itemCount) {
        delegateAdapter.notifyItemRangeChanged(this, positionStart, itemCount);
    }

    @Override
    public void notifyItemRangeChanged(int positionStart, int itemCount, @Nullable Object payload) {
        delegateAdapter.notifyItemRangeChanged(this, positionStart, itemCount, payload);
    }

    @Override
    public void notifyItemInserted(int position) {
        delegateAdapter.notifyItemInserted(this, position);
    }

    @Override
    public void notifyItemRangeInserted(int positionStart, int itemCount) {
        delegateAdapter.notifyItemRangeInserted(this, positionStart, itemCount);
    }

    @Override
    public void notifyItemRemoved(int position) {
        delegateAdapter.notifyItemRemoved(this, position);
    }

    @Override
    public void notifyItemRangeRemoved(int positionStart, int itemCount) {
        delegateAdapter.notifyItemRangeRemoved(this, positionStart, itemCount);
    }

    @Override
    public void notifyItemMoved(int fromPosition, int toPosition) {
        delegateAdapter.notifyItemMoved(this, fromPosition, toPosition);
    }
}
