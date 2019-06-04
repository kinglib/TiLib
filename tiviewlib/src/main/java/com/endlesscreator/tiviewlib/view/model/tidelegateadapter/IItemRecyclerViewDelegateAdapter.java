package com.endlesscreator.tiviewlib.view.model.tidelegateadapter;

import android.support.v7.widget.RecyclerView;

public interface IItemRecyclerViewDelegateAdapter<VH extends RecyclerView.ViewHolder> {

     void setDelegateAdapter(ITiRecyclerViewDelegateAdapter aDelegateAdapter);

     RecyclerView.Adapter<VH> getAdapter();

}
