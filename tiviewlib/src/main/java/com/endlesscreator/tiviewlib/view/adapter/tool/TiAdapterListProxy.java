package com.endlesscreator.tiviewlib.view.adapter.tool;

import android.support.v7.widget.RecyclerView;

import com.endlesscreator.titoollib.utils.CollectionUtil;

import java.util.ArrayList;
import java.util.List;

public class TiAdapterListProxy<T> {

    private RecyclerView.Adapter mAdapter;
    private List<T> mDataList;

    public TiAdapterListProxy() {
        this(null, null);
    }

    public TiAdapterListProxy(RecyclerView.Adapter aAdapter) {
        this(aAdapter, null);
    }

    public TiAdapterListProxy(List<T> aDataList) {
        this(null, aDataList);
    }

    public TiAdapterListProxy(RecyclerView.Adapter aAdapter, List<T> aDataList) {
        setAdapter(aAdapter);
        mDataList = new ArrayList<>();
        if (!CollectionUtil.isEmpty(aDataList)) mDataList.addAll(aDataList);
    }

    public void setAdapter(RecyclerView.Adapter aAdapter) {
        mAdapter = aAdapter;
    }

    public boolean setData(T aData) {
        if (aData == null) return false;
        mDataList.clear();
        mDataList.add(aData);
        if (mAdapter != null) mAdapter.notifyDataSetChanged();
        return true;
    }

    public boolean setDataList(List<T> aDataList) {
        if (CollectionUtil.isEmpty(aDataList)) return false;
        mDataList.clear();
        mDataList.addAll(aDataList);
        if (mAdapter != null) mAdapter.notifyDataSetChanged();
        return true;
    }

    public boolean addDataList(List<T> aDataList) {
        return addDataList(mDataList.size(), aDataList);
    }

    public boolean addDataList(int aIndex, List<T> aDataList) {
        if (CollectionUtil.isEmpty(aDataList)) return false;
        if (aIndex > mDataList.size()) aIndex = mDataList.size();
        mDataList.addAll(aIndex, aDataList);
        if (mAdapter != null) mAdapter.notifyItemRangeInserted(aIndex, aDataList.size());
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
        return true;
    }

    public boolean notifyItemChanged(int aPosition) {
        if (mAdapter == null || aPosition < 0 || aPosition >= mDataList.size()) return false;
        mAdapter.notifyItemChanged(aPosition);
        return true;
    }

    public boolean removeData(int aPosition) {
        T lItem = CollectionUtil.getItem(mDataList, aPosition);
        if (lItem == null) return false;
        mDataList.remove(aPosition);
        if (mAdapter != null) mAdapter.notifyItemRemoved(aPosition);
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
        return CollectionUtil.getItem(mDataList, aPosition);
    }

    public int getItemCount() {
        return CollectionUtil.getSize(mDataList);
    }

    public List<T> getDataList() {
        return mDataList;
    }

    public void clearData() {
        mDataList.clear();
        if (mAdapter != null) mAdapter.notifyDataSetChanged();
    }

}
