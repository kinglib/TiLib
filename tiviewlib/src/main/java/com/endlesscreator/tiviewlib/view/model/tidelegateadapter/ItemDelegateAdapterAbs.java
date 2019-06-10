package com.endlesscreator.tiviewlib.view.model.tidelegateadapter;

import android.support.v7.widget.RecyclerView;

import com.endlesscreator.tiviewlib.view.TiRecyclerView;
import com.endlesscreator.tiviewlib.view.adapter.tool.TiAdapterListProxy;

import java.util.List;

/**
 * 提供了数据集合及基础操作，如果不需要，可仿照本class实现自己需要的ItemAdapter逻辑
 *
 * @param <VH> 可继承自 {@link FullSpanDelegateViewHolderAbs} 或仿照其实现
 * @param <T>  数据集合的Item类型
 */
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
        if (aIndex < 0) return;
        proxy().notifyItemChanged(aIndex);
    }

    public void change(int aIndex, Object aPayload) {
        if (aIndex < 0) return;
        proxy().notifyItemChanged(aIndex, aPayload);
    }

    public void change(T aData) {
        change(proxy.index(aData));
    }

    public void change(T aData, Object aPayload) {
        change(proxy.index(aData), aPayload);
    }

    public T getData(int position) {
        return proxy.getDataItem(position);
    }

    @Override
    public int getItemCount() {
        return proxy.getItemCount();
    }

}
