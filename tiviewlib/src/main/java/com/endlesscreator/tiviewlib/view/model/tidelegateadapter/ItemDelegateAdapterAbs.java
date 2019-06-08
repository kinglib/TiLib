package com.endlesscreator.tiviewlib.view.model.tidelegateadapter;

import android.support.v7.widget.RecyclerView;

import com.endlesscreator.tiviewlib.view.TiRecyclerView;
import com.endlesscreator.tiviewlib.view.adapter.tool.TiAdapterListProxy;

import java.util.List;

public abstract class ItemDelegateAdapterAbs<VH extends RecyclerView.ViewHolder, T> extends ItemRecyclerViewDelegateAdapterDefault<VH> {

    private TiAdapterListProxy<T> proxy;

    public ItemDelegateAdapterAbs() {
        proxy = new TiAdapterListProxy<>(this);
    }

    public TiAdapterListProxy<T> proxy() {
        return proxy;
    }

    public void setDataList(List<T> aDataList) {
        setDataList(aDataList, null);
    }

    public void setDataList(List<T> aDataList, TiRecyclerView aRecyclerView) {
        proxy.removeData();
        proxy.addDataList(aDataList);
        if (aRecyclerView != null) {
            int lItemPosition = mDelegateAdapter.findAdapterFirstItemPosition(this);
            if (lItemPosition >= 0) aRecyclerView.scrollTo(lItemPosition);
        }
    }

    public void addDataList(List<T> aDataList) {
        proxy.addDataList(aDataList);
    }

    public void remove(int aIndex) {
        proxy.removeData(aIndex);
    }

    public void add(int aIndex, T aData) {
        proxy.addData(aIndex, aData);
    }

    public void change(int aIndex) {
        proxy().notifyItemChanged(aIndex);
    }

    public T getData(int position) {
        return proxy.getDataItem(position);
    }

    @Override
    public int getItemCount() {
        return proxy.getItemCount();
    }

}
