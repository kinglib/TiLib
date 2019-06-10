package com.endlesscreator.tiviewlib.view.model.tidelegateadapter;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.List;

public abstract class ItemDelegateAdapterAbsEx<VH extends FullSpanDelegateViewHolderAbs<T>, T> extends ItemDelegateAdapterAbs<VH, T> {

    public static final String CHANGE_PAYLOAD = "change_payload";

    public ItemDelegateAdapterAbsEx() {
        super();
    }

    public void changePayload(int aIndex) {
       change(aIndex, CHANGE_PAYLOAD);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        holder.updateUI(getData(position));
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position, @NonNull List<Object> payloads) {
        if (!payloads.isEmpty() && handlePayload(holder, position, payloads.get(0)))
            return;
        super.onBindViewHolder(holder, position, payloads);
    }

    protected boolean handlePayload(@NonNull VH holder, int position, Object payload) {
        return payload instanceof String && handlePayload(holder, position, (String) payload);
    }

    protected boolean handlePayload(@NonNull VH holder, int position, String payload) {
        return TextUtils.equals(CHANGE_PAYLOAD, payload) && handlePayloadForChange(holder, position);
    }

    protected boolean handlePayloadForChange(@NonNull VH holder, int position) {
        holder.updateChange(getData(position));
        return true;
    }

}
