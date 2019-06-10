package com.endlesscreator.tiviewlib.view.model.tidelegateadapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * 此class主要为瀑布流和横向填充视图组合展示设计，可将其设置为  {@link RecyclerView} 的Adapter作为代理
 * <p>
 * 其ViewHolder需要实现 {@link IFullSpanDelegateViewHolder} 或继承 {@link FullSpanDelegateViewHolderForChange}
 * <p>
 * 其子Adapter需要实现  {@link IItemRecyclerViewDelegateAdapter} 或继承 {@link ItemRecyclerViewDelegateAdapterDefault}
 */
public class TiRecyclerViewDelegateAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ITiRecyclerViewDelegateAdapter {

    private List<IItemRecyclerViewDelegateAdapter> mAdapters;

    public TiRecyclerViewDelegateAdapter() {
        mAdapters = new ArrayList<>();
    }

    public <IA extends IItemRecyclerViewDelegateAdapter> IA addAdapter(IA aItemAdapter) {
        mAdapters.add(aItemAdapter);
        aItemAdapter.setDelegateAdapter(this);
        return aItemAdapter;
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
        for (IItemRecyclerViewDelegateAdapter lAdapter : mAdapters) {
            lSize += lAdapter.getItemCount();
        }
        return lSize;
    }

    /**
     * 若使用多个ItemAdapter：
     * 可使用 {@link #notifyItemRangeRemoved(IItemRecyclerViewDelegateAdapter, int, int)}
     * 加上 {@link #notifyItemInserted(IItemRecyclerViewDelegateAdapter, int)}
     * 组合完成此效果
     */
    @Override
    public void notifyDataSetChanged(IItemRecyclerViewDelegateAdapter aAdapter) {
        // TODO 此方法不是进行区域刷新，待优化
        notifyDataSetChanged();
    }

    @Override
    public void notifyItemChanged(IItemRecyclerViewDelegateAdapter aAdapter, int aPosition) {
        notifyItemChanged(findAdapterFirstItemPosition(aAdapter) + aPosition);
    }

    @Override
    public void notifyItemChanged(IItemRecyclerViewDelegateAdapter aAdapter, int aPosition, Object aPayload) {
        notifyItemChanged(findAdapterFirstItemPosition(aAdapter) + aPosition, aPayload);
    }

    @Override
    public void notifyItemRangeChanged(IItemRecyclerViewDelegateAdapter aAdapter, int aPositionStart, int aItemCount) {
        notifyItemRangeChanged(findAdapterFirstItemPosition(aAdapter) + aPositionStart, aItemCount);
    }

    @Override
    public void notifyItemRangeChanged(IItemRecyclerViewDelegateAdapter aAdapter, int aPositionStart, int aItemCount, Object aPayload) {
        notifyItemRangeChanged(findAdapterFirstItemPosition(aAdapter) + aPositionStart, aItemCount, aPayload);
    }

    @Override
    public void notifyItemInserted(IItemRecyclerViewDelegateAdapter aAdapter, int aPosition) {
        notifyItemInserted(findAdapterFirstItemPosition(aAdapter) + aPosition);
    }

    @Override
    public void notifyItemRangeInserted(IItemRecyclerViewDelegateAdapter aAdapter, int aPreviousSize, int aSize) {
        notifyItemRangeInserted(findAdapterFirstItemPosition(aAdapter) + aPreviousSize, aSize);
    }

    @Override
    public void notifyItemRemoved(IItemRecyclerViewDelegateAdapter aAdapter, int aPosition) {
        notifyItemRemoved(findAdapterFirstItemPosition(aAdapter) + aPosition);
    }

    @Override
    public void notifyItemRangeRemoved(IItemRecyclerViewDelegateAdapter aAdapter, int aPosition, int aSize) {
        notifyItemRangeRemoved(findAdapterFirstItemPosition(aAdapter) + aPosition, aSize);
    }

    @Override
    public void notifyItemMoved(IItemRecyclerViewDelegateAdapter aAdapter, int aFromPosition, int aToPosition) {
        int lAdapterFirstItemPosition = findAdapterFirstItemPosition(aAdapter);
        notifyItemMoved(lAdapterFirstItemPosition + aFromPosition, lAdapterFirstItemPosition + aToPosition);
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

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull List<Object> payloads) {
        ItemDelegateAdapterInfo lAdapterInfo = findAdapterInfo(position);
        try { // 不进行泛型检测，因为ItemAdapter的holder可能都不同，所以不约束holder只能为RecyclerView.ViewHolder的某一种子类型
            lAdapterInfo.adapter.onBindViewHolder(holder, position - lAdapterInfo.adapterStartPosition, payloads);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 将大列表中的位置转换为Adapter内部的位置
    @Override
    public int getInnerPosition(IItemRecyclerViewDelegateAdapter aAdapter, int aRVPosition) {
        return aRVPosition - findAdapterFirstItemPosition(aAdapter);
    }

    // 获取Adapter在集合中的位置
    @Override
    public int findAdapterIndex(IItemRecyclerViewDelegateAdapter aAdapter) {
        return mAdapters.indexOf(aAdapter);
    }

    // 获取Adapter首个Item在大列表中的位置
    @Override
    public int findAdapterFirstItemPosition(IItemRecyclerViewDelegateAdapter aAdapter) {
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
    @Override
    public ItemDelegateAdapterInfo findAdapterInfo(int aRVPosition) {
        if (aRVPosition >= 0) {
            IItemRecyclerViewDelegateAdapter lAdapter;
            int lAdapterStartPos = 0;
            for (int i = 0, j = mAdapters.size(); i < j; i++) {
                lAdapter = mAdapters.get(i);
                int lItemCount = lAdapter.getItemCount();
                if (aRVPosition < lAdapterStartPos + lItemCount) {
                    return new ItemDelegateAdapterInfo(aRVPosition, lAdapter, i, lAdapterStartPos, lItemCount, aRVPosition - lAdapterStartPos);
                }
                lAdapterStartPos += lItemCount;
            }
        }
        // aRVPosition 不在数据区域范围内
        return new ItemDelegateAdapterInfo(aRVPosition, null, -1, -1, 0, -1);
    }

}