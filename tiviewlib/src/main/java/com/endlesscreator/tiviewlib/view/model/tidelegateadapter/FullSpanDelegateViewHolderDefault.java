package com.endlesscreator.tiviewlib.view.model.tidelegateadapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public abstract class FullSpanDelegateViewHolderDefault<DI> extends RecyclerView.ViewHolder implements IFullSpanDelegateViewHolder {

    public FullSpanDelegateViewHolderDefault(@NonNull View itemView) {
        super(itemView);
    }

    public abstract void updateUI(DI aDataItem);

    @Override
    public boolean isFullSpan() {
        return true;
    }
}
