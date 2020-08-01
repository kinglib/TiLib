package com.endlesscreator.tiviewlib.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * 支持重新setAdapter
 * 描述：解决重新设置 adapter 后，页面不刷新的问题
 * created by wty on 2020/8/1 11:33.
 */
public class TiFragmentPagerChangeAdapter extends TiFragmentPagerAdapter {

    private FragmentManager mFM;

    public TiFragmentPagerChangeAdapter(FragmentManager aFM, List<? extends Fragment> aDataList) {
        super(aFM, aDataList);
        mFM = aFM;
    }

    /**
     * 重写FragmentPagerAdapter里边的方法，如果fragment存在的话会调用缓存里边的fragment
     * 从而导致数据不更新问题
     */
    @NotNull
    public Object instantiateItem(@NotNull ViewGroup container, int position) {
        //拿到缓存的fragment，如果没有缓存的，就新建一个，新建发生在fragment的第一次初始化时
        Fragment lInstantiateFragment = (Fragment) super.instantiateItem(container, position);
        String lCacheFragmentTag = lInstantiateFragment.getTag();
        Fragment lRealFragment = getItem(position);
        if (lInstantiateFragment != lRealFragment) {
            //如果是新建的fragment，lInstantiateFragment 就和getItem(position)是同一个fragment，否则进入下面
            FragmentTransaction lFT = mFM.beginTransaction();
            //移除旧的fragment
            lFT.remove(lInstantiateFragment);
            //换成新的fragment
            lInstantiateFragment = lRealFragment;
            //添加新fragment时必须用前面获得的tag
            lFT.add(container.getId(), lInstantiateFragment, lCacheFragmentTag);
            lFT.attach(lInstantiateFragment);
            lFT.commitAllowingStateLoss();
        }
        return lInstantiateFragment;
    }
}
