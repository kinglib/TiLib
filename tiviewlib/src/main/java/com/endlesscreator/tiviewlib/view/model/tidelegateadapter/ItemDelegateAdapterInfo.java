package com.endlesscreator.tiviewlib.view.model.tidelegateadapter;

// ItemAdapter的信息
public class ItemDelegateAdapterInfo {
    public int sourcePosition; // 大列表中的原始位置

    public IItemRecyclerViewDelegateAdapter adapter; // 此位置对应的 ItemAdapter
    public int adapterIndex; // 当前 ItemAdapter 在 ItemAdapter集合 中的位置
    public int adapterStartPosition; // 当前 ItemAdapter 第一个元素在大列表中开始的位置
    public int adapterItemCount; // 当前 ItemAdapter 数据长度
    public int adapterInnerPosition; // 该原始位置在 ItemAdapter 中的相对位置

    public ItemDelegateAdapterInfo(int sourcePosition, IItemRecyclerViewDelegateAdapter adapter, int adapterIndex, int adapterStartPosition, int adapterItemCount, int adapterInnerPosition) {
        this.sourcePosition = sourcePosition;
        this.adapter = adapter;
        this.adapterIndex = adapterIndex;
        this.adapterStartPosition = adapterStartPosition;
        this.adapterItemCount = adapterItemCount;
        this.adapterInnerPosition = adapterInnerPosition;
    }
}