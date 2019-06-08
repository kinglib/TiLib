package com.endlesscreator.tiviewlib.view.model.tidelegateadapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class FullSpanDelegateViewHolderDefault extends RecyclerView.ViewHolder implements IFullSpanDelegateViewHolder {

    public FullSpanDelegateViewHolderDefault(@NonNull View itemView) {
        super(itemView);
    }

    @Override
    public boolean isFullSpan() {
        return false;
    }
}
