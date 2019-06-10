package com.endlesscreator.tilib.module.vp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.endlesscreator.tilib.R;
import com.endlesscreator.tilib.base.BaseActivity;
import com.endlesscreator.tilib.module.vp.adapter.ItemAdapterA;
import com.endlesscreator.tilib.module.vp.adapter.ItemAdapterB;
import com.endlesscreator.tilib.module.vp.bean.ItemBean;
import com.endlesscreator.tiviewlib.view.TiRecyclerView;
import com.endlesscreator.tiviewlib.view.TiRefreshLayout;
import com.endlesscreator.tiviewlib.view.model.tidelegateadapter.FullSpanDelegateViewHolderForChange;
import com.endlesscreator.tiviewlib.view.model.tidelegateadapter.ItemDelegateAdapterForChange;
import com.endlesscreator.tiviewlib.view.model.tidelegateadapter.ItemDelegateAdapterInfo;
import com.endlesscreator.tiviewlib.view.model.tidelegateadapter.TiRecyclerViewDelegateAdapter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


public class TestRecyclerViewActivity extends BaseActivity implements OnRefreshLoadMoreListener, TiRecyclerView.OnItemClickListener, TiRecyclerView.OnItemChildViewClickListener {

    private TiRefreshLayout mRefreshLayout;
    private TiRecyclerView mRecyclerView;

    private TiRecyclerViewDelegateAdapter mDelegateAdapter;
    private ItemAdapterA mAdapterA;
    private ItemAdapterB mAdapterB;
    private StaggeredGridLayoutManager mLayoutManager;

    private List<Integer> mClickIdList = Arrays.asList(
            R.id.item_rv_delete,
            R.id.item_rv_add,
            R.id.item_rv_change,
            R.id.item_rv_clear
    );

    private Random mRandom;


    @Override
    protected int makeLayout() {
        return R.layout.activity_test_recycler_view;
    }

    @Override
    protected void initView() {
        mRefreshLayout = find(R.id.refresh_view);
        mRecyclerView = find(R.id.recycler_view);
    }

    @Override
    protected void initListener() {
        mRefreshLayout.setOnRefreshLoadMoreListener(this);

        //设置代理Adapter
        mDelegateAdapter = new TiRecyclerViewDelegateAdapter();

        // 第一种样式的Adapter，横向填充
        mAdapterA = mDelegateAdapter.addAdapter(new ItemAdapterA());

        // 第二种类型的Adapter，瀑布流
        mAdapterB = mDelegateAdapter.addAdapter(new ItemAdapterB());

        // 设置布局管理，两列的瀑布流
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mDelegateAdapter);

        // 设置点击事件
        mRecyclerView.setOnItemClickListener(this);
        mRecyclerView.setOnItemChildViewClickListener(this, mClickIdList);
    }

    @Override
    protected void initData() {
        mRandom = new Random();
        mAdapterA.setDataList(makeList(2, 0));
        mAdapterB.setDataList(makeList(10, 0));
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        show("onRefresh");

        mAdapterA.setDataList(makeList(2, 0), mRecyclerView);
        mAdapterB.setDataList(makeList(10, 0));

        mRefreshLayout.finishRefresh();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        show("onLoadMore");

        mAdapterB.addDataList(makeList(10, mAdapterB.getItemCount()));

        mRefreshLayout.finishLoadMore();
    }

    @Override
    public void onItemClick(RecyclerView aRecyclerView, RecyclerView.ViewHolder aViewHolder, int aPosition, float aRawX, float aRawY) {

        ItemDelegateAdapterInfo lAdapterInfo = mDelegateAdapter.findAdapterInfo(aPosition);

        show("item click, lAdapterInfo= " + lAdapterInfo);
    }


    @Override
    public void onItemChildViewClick(RecyclerView aRecyclerView, RecyclerView.ViewHolder aViewHolder, View aChildView, int aPosition, float aRawX, float aRawY) {

        ItemDelegateAdapterInfo lAdapterInfo = mDelegateAdapter.findAdapterInfo(aPosition);

        int lID = aChildView.getId();
        int index = mClickIdList.indexOf(lID);

        show("operate = " + index + ", lAdapterInfo = " + lAdapterInfo);

        ItemDelegateAdapterForChange<? extends FullSpanDelegateViewHolderForChange, ItemBean> lAdapter = lAdapterInfo.adapterIndex == 0 ? mAdapterA : mAdapterB;

        switch (index) {
            case 0: // 删除
                lAdapter.remove(lAdapterInfo.adapterInnerPosition);
                return;
            case 1: // 插入

                ItemBean data = lAdapter.getData(lAdapterInfo.adapterInnerPosition);
                lAdapter.add(lAdapterInfo.adapterInnerPosition, makeItem(data.getId() - 1));

                // 如果添加在最前面，则自动滚动到插入数据所在位置 (可选)
                int[] lFirstVisibleItemPositions = new int[mLayoutManager.getSpanCount()];
                mLayoutManager.findFirstVisibleItemPositions(lFirstVisibleItemPositions);
                int lFirstVisibleItemPosition = lFirstVisibleItemPositions[0];
                for (int i = 1, j = lFirstVisibleItemPositions.length; i < j; i++) {
                    int lItemPosition = lFirstVisibleItemPositions[i];
                    if (lItemPosition < lFirstVisibleItemPosition)
                        lFirstVisibleItemPosition = lItemPosition;
                }
                if (lAdapterInfo.sourcePosition <= lFirstVisibleItemPosition) {
                    mRecyclerView.scrollTo(lAdapterInfo.sourcePosition);
                }

                return;
            case 2: // 变化
                data = lAdapter.getData(lAdapterInfo.adapterInnerPosition);
                data.setText(makeText(data.getId()));
                lAdapter.changePayload(lAdapterInfo.adapterInnerPosition);
                return;
            case 3: // 清除列表
                lAdapter.proxy().removeData();
                return;
        }

    }


    private List<ItemBean> makeList(int size, int start) {
        List<ItemBean> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(makeItem(start + i));
        }
        return list;
    }


    private ItemBean makeItem(int id) {
        return new ItemBean(id, makeText(id));
    }


    private String makeText(int id) {
        StringBuilder lSB = new StringBuilder();
        for (int i = 0, j = id + mRandom.nextInt(30); i < id + j; i++) {
            lSB.append("测试 ");
        }
        return lSB.toString();
    }

}
