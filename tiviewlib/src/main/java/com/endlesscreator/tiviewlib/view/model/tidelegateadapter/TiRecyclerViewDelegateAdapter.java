package com.endlesscreator.tiviewlib.view.model.tidelegateadapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * 此class主要为瀑布流和横向填充视图组合展示设计，其ViewHolder需要实现 {@link IFullSpanDelegateViewHolder}
 */
public class TiRecyclerViewDelegateAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ITiRecyclerViewDelegateAdapter {

    private List<RecyclerView.Adapter> mAdapters;

    public TiRecyclerViewDelegateAdapter() {
        mAdapters = new ArrayList<>();
    }

    public void addAdapter(IItemRecyclerViewDelegateAdapter aDelegateAdapter) {
        mAdapters.add(aDelegateAdapter.getAdapter());
        aDelegateAdapter.setDelegateAdapter(this);
    }

    @Override
    public int getItemViewType(int position) {
        return findAdapterInfo(position).adapterIndex;
    }

    @Override
    public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        if (holder instanceof IFullSpanDelegateViewHolder) {
            ViewGroup.LayoutParams lItemParams = holder.itemView.getLayoutParams();
            if (lItemParams instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams lParams = (StaggeredGridLayoutManager.LayoutParams) lItemParams;
                lParams.setFullSpan(((IFullSpanDelegateViewHolder) holder).isFullSpan());
            }
        }
    }

    @Override
    public int getItemCount() {
        int lSize = 0;
        for (RecyclerView.Adapter lAdapter : mAdapters) {
            lSize += lAdapter.getItemCount();
        }
        return lSize;
    }

    // TODO 此方法不是进行区域刷新，多adapter组合时，建议手动remove自身数据再add新数据
    @Override
    public void notifyDataSetChanged(RecyclerView.Adapter aAdapter) {
        notifyDataSetChanged();
    }

    @Override
    public void notifyItemChanged(RecyclerView.Adapter aAdapter, int aPosition) {
        notifyItemChanged(findAdapterFirstItemPosition(aAdapter) + aPosition);
    }

    @Override
    public void notifyItemChanged(RecyclerView.Adapter aAdapter, int aPosition, Object aPayload) {
        notifyItemChanged(findAdapterFirstItemPosition(aAdapter) + aPosition, aPayload);
    }

    @Override
    public void notifyItemInserted(RecyclerView.Adapter aAdapter, int aPosition) {
        notifyItemInserted(findAdapterFirstItemPosition(aAdapter) + aPosition);
    }

    @Override
    public void notifyItemRangeInserted(RecyclerView.Adapter aAdapter, int aPreviousSize, int aSize) {
        int lPreviousSize = findAdapterFirstItemPosition(aAdapter) + aPreviousSize;
        notifyItemRangeInserted(lPreviousSize, aSize);
    }

    @Override
    public void notifyItemRemoved(RecyclerView.Adapter aAdapter, int aPosition) {
        notifyItemRemoved(findAdapterFirstItemPosition(aAdapter) + aPosition);
    }

    @Override
    public void notifyItemRangeRemoved(RecyclerView.Adapter aAdapter, int aPosition, int aSize) {
        notifyItemRangeRemoved(findAdapterFirstItemPosition(aAdapter) + aPosition, aSize);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return mAdapters.get(viewType).onCreateViewHolder(parent, viewType);
    }

    /**
     * 重写了 {@link #onBindViewHolder(RecyclerView.ViewHolder, int, List)} 后
     * 其不调用父类自行处理，此方法即可不必实现
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull List<Object> payloads) {
        AdapterInfo lAdapterInfo = findAdapterInfo(position);
        try { // 一般没问题，兼容极端情况
            lAdapterInfo.adapter.onBindViewHolder(holder, position - lAdapterInfo.startPosition, payloads);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 将大列表中的位置转换为Adapter内部的位置
    public int getInnerPosition(RecyclerView.Adapter aAdapter, int aRVPosition) {
        return aRVPosition - findAdapterFirstItemPosition(aAdapter);
    }

    // 获取Adapter在集合中的位置
    private int findAdapterIndex(RecyclerView.Adapter aAdapter) {
        return mAdapters.indexOf(aAdapter);
    }

    // 获取Adapter首个Item在大列表中的位置
    private int findAdapterFirstItemPosition(RecyclerView.Adapter aAdapter) {
        int lAdapterIndex = findAdapterIndex(aAdapter);
        if (lAdapterIndex < 0) return -1;
        int lPosition = 0;
        if (lAdapterIndex > 0) {
            for (int i = 0; i < lAdapterIndex; i++) {
                lPosition += mAdapters.get(i).getItemCount();
            }
        }
        return lPosition;
    }

    // 根据Item在大列表中的位置获取其Adapter的相关信息
    public AdapterInfo findAdapterInfo(int aRVPosition) {
        if (aRVPosition >= 0) {
            int aAdapterStartPos = 0;
            for (int i = 0; i < mAdapters.size(); i++) {
                RecyclerView.Adapter lAdapter = mAdapters.get(i);
                int lItemCount = lAdapter.getItemCount();
                if (aRVPosition < aAdapterStartPos + lItemCount) {
                    return new AdapterInfo(aAdapterStartPos, aRVPosition - aAdapterStartPos, i, lAdapter);
                }
                aAdapterStartPos += lItemCount;
            }
        }
        return null; // aRVPosition 不在区域内
    }

    public class AdapterInfo {
        public int startPosition;
        public int adapterInnerPosition;
        public int adapterIndex; // Adapter在集合中的位置
        public RecyclerView.Adapter adapter;

        public AdapterInfo(int startPosition, int adapterInnerPosition, int adapterIndex, RecyclerView.Adapter adapter) {
            this.startPosition = startPosition;
            this.adapterInnerPosition = adapterInnerPosition;
            this.adapterIndex = adapterIndex;
            this.adapter = adapter;
        }

        @NonNull
        @Override
        public String toString() {
            return "AdapterInfo{" +
                    "startPosition=" + startPosition +
                    ", adapterInnerPosition=" + adapterInnerPosition +
                    ", adapterIndex=" + adapterIndex +
                    ", adapter=" + adapter +
                    '}';
        }
    }
}