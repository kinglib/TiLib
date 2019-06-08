package com.endlesscreator.tilib.module.vp.holder;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.endlesscreator.tilib.R;
import com.endlesscreator.tilib.module.vp.bean.ItemBean;
import com.endlesscreator.tiviewlib.view.model.tidelegateadapter.FullSpanDelegateViewHolderAbs;

public class ItemHolderB extends FullSpanDelegateViewHolderAbs<ItemBean> {

    private TextView tv;

    public ItemHolderB(View itemView) {
        super(itemView);
        tv = itemView.findViewById(R.id.item_recycler_view_tv);
        tv.setTextSize(15);
    }

    @Override
    public void updateUI(ItemBean aDataItem) {
        tv.setText(aDataItem.toString());
        tv.setTextColor(Color.BLACK);
    }

    @Override
    public void updateChange(ItemBean aDataItem) {
        tv.setText(aDataItem.toString());
        tv.setTextColor(Color.BLUE);
    }

    @Override
    public boolean isFullSpan() {
        return false;
    }
}
