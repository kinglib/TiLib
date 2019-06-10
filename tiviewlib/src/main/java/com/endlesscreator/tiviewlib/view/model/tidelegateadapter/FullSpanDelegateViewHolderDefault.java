package com.endlesscreator.tiviewlib.view.model.tidelegateadapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public abstract class FullSpanDelegateViewHolderDefault<T> extends RecyclerView.ViewHolder implements IFullSpanDelegateViewHolder {

    public FullSpanDelegateViewHolderDefault(@NonNull View itemView) {
        super(itemView);
    }

    public abstract void updateUI(T aDataItem);

    @Override
    public boolean isFullSpan() {
        return true;
    }
}
