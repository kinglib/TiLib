package com.endlesscreator.tiviewlib.view.model.tidelegateadapter;

import android.support.v7.widget.RecyclerView;

public abstract class ItemRecyclerViewDelegateAdapterDefault<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> implements IItemRecyclerViewDelegateAdapter<VH> {

    protected ITiRecyclerViewDelegateAdapter mDelegateAdapter;

    @Override
    public void setDelegateAdapter(ITiRecyclerViewDelegateAdapter aDelegateAdapter) {
        mDelegateAdapter = aDelegateAdapter;
    }

    @Override
    public RecyclerView.Adapter<VH> getAdapter() {
        return this;
    }
}
