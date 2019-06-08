package com.endlesscreator.tilib.module.vp.holder;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.endlesscreator.tilib.R;
import com.endlesscreator.tilib.module.vp.bean.ItemBean;
import com.endlesscreator.tiviewlib.view.model.tidelegateadapter.FullSpanDelegateViewHolderDefault;
import com.endlesscreator.tiviewlib.view.model.tidelegateadapter.IFullSpanDelegateViewHolder;

public class ItemHolderB extends FullSpanDelegateViewHolderDefault {

    private TextView tv;

    public ItemHolderB(View itemView) {
        super(itemView);
        tv = itemView.findViewById(R.id.item_recycler_view_tv);
        tv.setTextSize(15);
    }

    public void updateUI(ItemBean aItem) {
        tv.setText(aItem.toString());
    }

}
