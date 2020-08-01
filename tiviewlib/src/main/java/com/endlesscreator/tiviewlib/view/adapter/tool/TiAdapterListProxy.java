package com.endlesscreator.tiviewlib.view.adapter.tool;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import com.endlesscreator.titoollib.utils.CollectionUtil;
import com.endlesscreator.tiviewlib.view.model.tidelegateadapter.IItemRecyclerViewDelegateAdapter;

import java.util.ArrayList;
import java.util.List;

public class TiAdapterListProxy<T> {

    // 支持两种类型的Adapter
    private RecyclerView.Adapter mAdapter;
    private IItemRecyclerViewDelegateAdapter mItemAdapter;

    private List<T> mDataList;

    public TiAdapterListProxy() {
        this(null, null, null);
    }

    public TiAdapterListProxy(RecyclerView.Adapter aAdapter) {
        this(aAdapter, null);
    }

    public TiAdapterListProxy(IItemRecyclerViewDelegateAdapter aItemAdapter) {
        this(aItemAdapter, null);
    }

    public TiAdapterListProxy(List<T> aDataList) {
        this(null, null, aDataList);
    }

    public TiAdapterListProxy(IItemRecyclerViewDelegateAdapter aItemAdapter, List<T> aDataList) {
        this(null, aItemAdapter, aDataList);
    }

    public TiAdapterListProxy(RecyclerView.Adapter aAdapter, List<T> aDataList) {
        this(aAdapter, null, aDataList);
    }

    public TiAdapterListProxy(RecyclerView.Adapter aAdapter, IItemRecyclerViewDelegateAdapter aItemAdapter, List<T> aDataList) {
        setAdapter(aAdapter);
        setAdapter(aItemAdapter);
        mDataList = new ArrayList<>();
        if (!CollectionUtil.empty(aDataList)) mDataList.addAll(aDataList);
    }

    public void setAdapter(RecyclerView.Adapter aAdapter) {
        mAdapter = aAdapter;
    }

    public void setAdapter(IItemRecyclerViewDelegateAdapter aItemAdapter) {
        mItemAdapter = aItemAdapter;
    }

    public void removeAdapter() {
        mAdapter = null;
        mItemAdapter = null;
    }

    public boolean setData(T aData) {
        if (aData == null) return false;
        mDataList.clear();
        mDataList.add(aData);
        if (mAdapter != null) mAdapter.notifyDataSetChanged();
        if (mItemAdapter != null) mItemAdapter.notifyDataSetChanged();
        return true;
    }

    public boolean setDataList(List<T> aDataList) {
        if (CollectionUtil.empty(aDataList)) return false;
        mDataList.clear();
        mDataList.addAll(aDataList);
        if (mAdapter != null) mAdapter.notifyDataSetChanged();
        if (mItemAdapter != null) mItemAdapter.notifyDataSetChanged();
        return true;
    }

    public boolean addDataList(List<T> aDataList) {
        return addDataList(mDataList.size(), aDataList);
    }

    public boolean addDataList(int aIndex, List<T> aDataList) {
        if (CollectionUtil.empty(aDataList)) return false;
        if (aIndex > mDataList.size()) aIndex = mDataList.size();
        mDataList.addAll(aIndex, aDataList);
        if (mAdapter != null) mAdapter.notifyItemRangeInserted(aIndex, aDataList.size());
        if (mItemAdapter != null) mItemAdapter.notifyItemRangeInserted(aIndex, aDataList.size());
        return true;
    }

    public boolean addData(T aData) {
        return addData(mDataList.size(), aData);
    }

    public boolean addData(int aIndex, T aData) {
        if (aData == null) return false;
        if (aIndex > mDataList.size()) aIndex = mDataList.size();
        mDataList.add(aIndex, aData);
        if (mAdapter != null) mAdapter.notifyItemInserted(aIndex);
        if (mItemAdapter != null) mItemAdapter.notifyItemInserted(aIndex);
        return true;
    }

    public boolean notifyItemChanged(int aPosition) {
        if (aPosition < 0 || aPosition >= mDataList.size()) return false;
        if (mAdapter != null) mAdapter.notifyItemChanged(aPosition);
        if (mItemAdapter != null) mItemAdapter.notifyItemChanged(aPosition);
        return true;
    }

    public boolean notifyItemChanged(int aPosition, Object aPayload) {
        if (aPosition < 0 || aPosition >= mDataList.size()) return false;
        if (mAdapter != null) {
            if (aPayload != null) mAdapter.notifyItemChanged(aPosition, aPayload);
            else mAdapter.notifyItemChanged(aPosition);
        }
        if (mItemAdapter != null) {
            if (aPayload != null) mItemAdapter.notifyItemChanged(aPosition, aPayload);
            else mItemAdapter.notifyItemChanged(aPosition);
        }
        return true;
    }

    public boolean removeData(int aPosition) {
        T lItem = CollectionUtil.item(mDataList, aPosition);
        if (lItem == null) return false;
        mDataList.remove(aPosition);
        if (mAdapter != null) mAdapter.notifyItemRemoved(aPosition);
        if (mItemAdapter != null) mItemAdapter.notifyItemRemoved(aPosition);
        return true;
    }

    public boolean removeData(T aItem) {
        if (aItem == null) return false;
        int lIndex = mDataList.indexOf(aItem);
        return removeData(lIndex);
    }

    public int index(T aItem) {
        if (aItem == null) return -1;
        return mDataList.indexOf(aItem);
    }

    public T getDataItem(int aPosition) {
        return CollectionUtil.item(mDataList, aPosition);
    }

    public int getItemCount() {
        return CollectionUtil.size(mDataList);
    }

    public List<T> getDataList() {
        return mDataList;
    }

    public void clearData() {
        mDataList.clear();
        if (mAdapter != null) mAdapter.notifyDataSetChanged();
        if (mItemAdapter != null) mItemAdapter.notifyDataSetChanged();
    }

    public void removeData() {
        int lItemCount = getItemCount();
        if (lItemCount > 0) {
            mDataList.clear();
            if (mAdapter != null) mAdapter.notifyItemRangeRemoved(0, lItemCount);
            if (mItemAdapter != null) mItemAdapter.notifyItemRangeRemoved(0, lItemCount);
        }
    }


}
