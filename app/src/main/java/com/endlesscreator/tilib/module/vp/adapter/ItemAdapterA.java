package com.endlesscreator.tilib.module.vp.adapter;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.endlesscreator.tilib.R;
import com.endlesscreator.tilib.module.vp.bean.ItemBean;
import com.endlesscreator.tilib.module.vp.holder.ItemHolderA;
import com.endlesscreator.tiviewlib.view.model.tidelegateadapter.ItemDelegateAdapterForChange;
import com.endlesscreator.tiviewlib.view.model.tidelegateadapter.ItemDelegateAdapterInfo;


public class ItemAdapterA extends ItemDelegateAdapterForChange<ItemHolderA, ItemBean> {

    @NonNull
    @Override
    public ItemHolderA onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View lView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_view, null);
        return new ItemHolderA(lView);
    }

    @Override
    public void onViewAttachedToWindow(ItemHolderA holder, ItemDelegateAdapterInfo aAdapterInfo) {
        System.out.println("------->>>> ItemHolderA in pos = " + aAdapterInfo.adapterInnerPosition
                + ", getText = " + holder.tv.getText());
    }

    @Override
    public void onViewDetachedFromWindow(ItemHolderA holder, ItemDelegateAdapterInfo aAdapterInfo) {
        System.out.println("------->>>> ItemHolderA out pos = " + aAdapterInfo.adapterInnerPosition
                + ", getText = " + holder.tv.getText());
    }

}
