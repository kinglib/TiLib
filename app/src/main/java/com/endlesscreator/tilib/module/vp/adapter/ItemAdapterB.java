package com.endlesscreator.tilib.module.vp.adapter;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.endlesscreator.tilib.R;
import com.endlesscreator.tilib.module.vp.bean.ItemBean;
import com.endlesscreator.tilib.module.vp.holder.ItemHolderB;
import com.endlesscreator.titoollib.utils.CollectionUtil;
import com.endlesscreator.titoollib.utils.Util;
import com.endlesscreator.tiviewlib.view.model.tidelegateadapter.ItemRecyclerViewDelegateAdapterDefault;

import java.util.ArrayList;
import java.util.List;

public class ItemAdapterB extends ItemRecyclerViewDelegateAdapterDefault<ItemHolderB> {

    public static final String UPDATE_PAYLOAD = "update_payload";

    private List<ItemBean> mDataList;


    public ItemAdapterB() {
        mDataList = new ArrayList<>();
    }

    @Override
    public int getItemCount() {
        return CollectionUtil.getSize(mDataList);
    }

    public void setDataList(List<ItemBean> aDataList) {
        mDataList.clear();
        mDataList.addAll(aDataList);
        mDelegateAdapter.notifyDataSetChanged(this);
    }

    public void addDataList(List<ItemBean> aDataList) {
        int lPreviousSize = mDataList.size();
        mDataList.addAll(aDataList);
        mDelegateAdapter.notifyItemRangeInserted(this, lPreviousSize, aDataList.size());
    }

    @NonNull
    @Override
    public ItemHolderB onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View lView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_view, null);
        return new ItemHolderB(lView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolderB holder, int position) {
        holder.updateUI(CollectionUtil.getItem(mDataList, position));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolderB holder, int position, @NonNull List<Object> payloads) {
        if (!payloads.isEmpty()) {
            String lPayload = Util.toString(payloads.get(0));
            if (TextUtils.equals(UPDATE_PAYLOAD, lPayload)) {

                return;
            }
        }
        super.onBindViewHolder(holder, position, payloads);
    }
}
