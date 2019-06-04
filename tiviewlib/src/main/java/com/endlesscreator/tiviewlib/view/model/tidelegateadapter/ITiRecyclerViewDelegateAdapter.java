package com.endlesscreator.tiviewlib.view.model.tidelegateadapter;

import android.support.v7.widget.RecyclerView;


public interface ITiRecyclerViewDelegateAdapter {

    void notifyDataSetChanged(RecyclerView.Adapter aAdapter);

    void notifyItemChanged(RecyclerView.Adapter aAdapter, int aPosition);

    void notifyItemChanged(RecyclerView.Adapter aAdapter, int aPosition, Object aPayload);

    void notifyItemInserted(RecyclerView.Adapter aAdapter, int aPosition);

    void notifyItemRangeInserted(RecyclerView.Adapter aAdapter, int aPreviousSize, int aSize);

    void notifyItemRemoved(RecyclerView.Adapter aAdapter, int aPosition);

    void notifyItemRangeRemoved(RecyclerView.Adapter aAdapter, int aPosition, int aSize);


}
