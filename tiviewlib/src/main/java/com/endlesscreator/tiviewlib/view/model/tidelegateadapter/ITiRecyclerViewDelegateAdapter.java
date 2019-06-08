package com.endlesscreator.tiviewlib.view.model.tidelegateadapter;

public interface ITiRecyclerViewDelegateAdapter {

    void notifyDataSetChanged(IItemRecyclerViewDelegateAdapter aAdapter);

    void notifyItemChanged(IItemRecyclerViewDelegateAdapter aAdapter, int aPosition);

    void notifyItemChanged(IItemRecyclerViewDelegateAdapter aAdapter, int aPosition, Object aPayload);

    void notifyItemRangeChanged(IItemRecyclerViewDelegateAdapter aAdapter, int aPositionStart, int aItemCount);

    void notifyItemRangeChanged(IItemRecyclerViewDelegateAdapter aAdapter, int aPositionStart, int aItemCount, Object aPayload);

    void notifyItemInserted(IItemRecyclerViewDelegateAdapter aAdapter, int aPosition);

    void notifyItemRangeInserted(IItemRecyclerViewDelegateAdapter aAdapter, int aPreviousSize, int aSize);

    void notifyItemRemoved(IItemRecyclerViewDelegateAdapter aAdapter, int aPosition);

    void notifyItemRangeRemoved(IItemRecyclerViewDelegateAdapter aAdapter, int aPosition, int aSize);

    void notifyItemMoved(IItemRecyclerViewDelegateAdapter aAdapter, int aFromPosition, int aToPosition);

    int getInnerPosition(IItemRecyclerViewDelegateAdapter aAdapter, int aRVPosition);

    int findAdapterIndex(IItemRecyclerViewDelegateAdapter aAdapter);

    int findAdapterFirstItemPosition(IItemRecyclerViewDelegateAdapter aAdapter);

    ItemDelegateAdapterInfo findAdapterInfo(int aRVPosition);

}
