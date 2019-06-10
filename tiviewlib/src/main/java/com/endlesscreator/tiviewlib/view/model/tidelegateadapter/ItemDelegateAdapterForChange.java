package com.endlesscreator.tiviewlib.view.model.tidelegateadapter;

public abstract class ItemDelegateAdapterForChange<VH extends FullSpanDelegateViewHolderForChange<DI>, DI> extends ItemDelegateAdapterAbs<VH, DI> {

    /**
     * 对应函数 {@link #handlePayloadForChange(FullSpanDelegateViewHolderForChange, int)}
     */
    public static final String CHANGE_PAYLOAD = "handlePayloadForChange";

    public ItemDelegateAdapterForChange() {
        super();
    }

    public void changePayload(int aIndex) {
        change(aIndex, CHANGE_PAYLOAD);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        holder.updateUI(getData(position));
    }

    public boolean handlePayloadForChange(VH holder, int position) {
        holder.updateChange(getData(position));
        return true;
    }

}
