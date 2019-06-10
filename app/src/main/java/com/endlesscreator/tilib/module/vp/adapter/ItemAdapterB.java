package com.endlesscreator.tilib.module.vp.adapter;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.endlesscreator.tilib.R;
import com.endlesscreator.tilib.module.vp.bean.ItemBean;
import com.endlesscreator.tilib.module.vp.holder.ItemHolderB;
import com.endlesscreator.tiviewlib.view.model.tidelegateadapter.ItemDelegateAdapterForChange;

public class ItemAdapterB extends ItemDelegateAdapterForChange<ItemHolderB, ItemBean> {

    @NonNull
    @Override
    public ItemHolderB onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View lView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_view, null);
        return new ItemHolderB(lView);
    }

}
