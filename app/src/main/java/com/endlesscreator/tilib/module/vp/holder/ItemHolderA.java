package com.endlesscreator.tilib.module.vp.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.endlesscreator.tilib.R;
import com.endlesscreator.tilib.module.vp.bean.ItemBean;
import com.endlesscreator.tiviewlib.view.model.tidelegateadapter.IFullSpanDelegateViewHolder;

public class ItemHolderA extends RecyclerView.ViewHolder implements IFullSpanDelegateViewHolder {

    private TextView tv;

    public ItemHolderA(View itemView) {
        super(itemView);
        tv = itemView.findViewById(R.id.item_recycler_view_tv);
        tv.setTextSize(18);
    }

    public void updateUI(ItemBean aItem) {
        tv.setText(aItem.toString());
    }

    @Override
    public boolean isFullSpan() {
        return true;
    }
}
