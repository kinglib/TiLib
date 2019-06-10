package com.endlesscreator.tiviewlib.view.model.tidelegateadapter;

import android.support.annotation.NonNull;
import android.view.View;

public abstract class FullSpanDelegateViewHolderAbs<T> extends FullSpanDelegateViewHolderDefault<T> {

    public FullSpanDelegateViewHolderAbs(@NonNull View itemView) {
        super(itemView);
    }

    public void updateChange(T aDataItem) {
    }

}
