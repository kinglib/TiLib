package com.endlesscreator.tiviewlib.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.endlesscreator.titoollib.utils.CollectionUtil;

import java.util.List;

public class TiFragmentPagerAdapter extends FragmentPagerAdapter {

    private List<? extends Fragment> mList;

    public TiFragmentPagerAdapter(FragmentManager aFM, List<? extends Fragment> aDataList) {
        super(aFM);
        this.mList = aDataList;
    }

    @Override
    public int getCount() {
        return CollectionUtil.size(mList);
    }


    @Override
    public Fragment getItem(int position) {
        return mList.get(position);
    }

}