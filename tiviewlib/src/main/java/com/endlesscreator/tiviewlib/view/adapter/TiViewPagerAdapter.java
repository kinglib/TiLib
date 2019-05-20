package com.endlesscreator.tiviewlib.view.adapter;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.endlesscreator.titoollib.utils.CollectionUtil;

import java.util.List;

public class TiViewPagerAdapter extends PagerAdapter {
    private List<View> mViewList;

    public TiViewPagerAdapter(List<View> aViewList) {
        this.mViewList = aViewList;
    }

    @Override
    public int getCount() {
        return CollectionUtil.isEmpty(mViewList) ? 0 : mViewList.size();
    }


    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View lItemView = mViewList.get(position);
        container.addView(lItemView);
        return lItemView;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }
}