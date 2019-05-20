package com.endlesscreator.tiviewlib.view.model.vlayout;

import com.alibaba.android.vlayout.LayoutManagerHelper;
import com.alibaba.android.vlayout.layout.StaggeredGridLayoutHelper;

/*
    解决VLayout瀑布流错位问题
    产生的原因是因为manager的onItemsChanged方法直接调用了helper中的clear，将原有排列顺序进行了清空，
    然后滑动时发现没有了记录所以重新计算位置，导致顺序错乱，
    临时修改方式是将清空方法暴露出来，然后将其在helper的onItemsChanged方法中删除，
    让用户自己决定是否清空（即上拉加载时不操作，重新请求数据时清空）

 */
public class TiStaggeredGridLayoutHelper extends StaggeredGridLayoutHelper {

    private boolean isReloadData = true;
    private boolean autoChangeReloadData = false;

    public TiStaggeredGridLayoutHelper() {
    }

    public TiStaggeredGridLayoutHelper(int lanes) {
        super(lanes);
    }

    public TiStaggeredGridLayoutHelper(int lanes, int gap) {
        super(lanes, gap);
    }

    @Override
    public void onItemsChanged(LayoutManagerHelper helper) {
        if (isReloadData) {
            super.onItemsChanged(helper);
            if (autoChangeReloadData) isReloadData = false;
        }
    }

    // 准备重新加载数据时使用
    // 注意，瀑布流尽量别用删除item，用的话可将recyclerview动画去掉（ mRecyclerView.setItemAnimator(null); ），但也会偶现顶部留白
    public void enableOnceReload() {
        autoChangeReloadData = true;
        isReloadData = true;
    }

    public void enableReload() {
        autoChangeReloadData = false;
        isReloadData = true;
    }

    // 准备重新加载数据时使用
    public void disableReload() {
        isReloadData = false;
    }

    public void setReloadData(boolean reloadData) {
        isReloadData = reloadData;
    }

    public void setAutoChangeReloadData(boolean autoChangeReloadData) {
        this.autoChangeReloadData = autoChangeReloadData;
    }


//    // 清空原有排序
//    public void clearPreviousShot() {
//        super.onItemsChanged(null);
//    }

}
