package com.endlesscreator.tiviewlib.view.model.tidelegateadapter;

import android.support.annotation.NonNull;
import android.view.View;

public abstract class FullSpanDelegateViewHolderForChange<DI> extends FullSpanDelegateViewHolderDefault<DI> {

    public FullSpanDelegateViewHolderForChange(@NonNull View itemView) {
        super(itemView);
    }

    public void updateChange(DI aDataItem) {
    }

}
