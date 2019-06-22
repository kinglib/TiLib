package com.endlesscreator.tilib.module.vp.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.endlesscreator.tilib.R;
import com.endlesscreator.tilib.module.vp.bean.ItemBean;
import com.endlesscreator.tilib.module.vp.holder.ItemHolderA;
import com.endlesscreator.tilib.module.vp.holder.ItemHolderB;
import com.endlesscreator.tiviewlib.view.model.tidelegateadapter.ItemDelegateAdapterForChange;
import com.endlesscreator.tiviewlib.view.model.tidelegateadapter.ItemDelegateAdapterInfo;

public class ItemAdapterB extends ItemDelegateAdapterForChange<ItemHolderB, ItemBean> {

    @NonNull
    @Override
    public ItemHolderB onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View lView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_view, null);
        return new ItemHolderB(lView);
    }

    @Override
    public void onViewAttachedToWindow(ItemHolderB holder, ItemDelegateAdapterInfo aAdapterInfo) {
        System.out.println("------->>>> ItemHolderB in pos = " + aAdapterInfo.adapterInnerPosition
                + ", getText = " + holder.tv.getText());
    }

    @Override
    public void onViewDetachedFromWindow(ItemHolderB holder, ItemDelegateAdapterInfo aAdapterInfo) {
        System.out.println("------->>>> ItemHolderB out pos = " + aAdapterInfo.adapterInnerPosition
                + ", getText = " + holder.tv.getText());
    }

}
