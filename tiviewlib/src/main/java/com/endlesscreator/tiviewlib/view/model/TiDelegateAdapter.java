package com.endlesscreator.tiviewlib.view.model;

import android.util.Pair;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;

public class TiDelegateAdapter extends DelegateAdapter {

    public TiDelegateAdapter(VirtualLayoutManager layoutManager) {
        super(layoutManager);
    }

    public TiDelegateAdapter(VirtualLayoutManager layoutManager, boolean hasConsistItemType) {
        super(layoutManager, hasConsistItemType);
    }

    /**
     * @param aOutPosition 在整体RecyclerView中的真实位置
     */
    public AnalysisResult analysisPosition(int aOutPosition) {

        Pair<AdapterDataObserver, Adapter> adapterByPosition = findAdapterByPosition(aOutPosition);
        if (adapterByPosition != null) {
            AdapterDataObserver first = adapterByPosition.first;

            // 第几个Adapter
            int index = first.getIndex();

            // 当前adapter的头一个元素是全数据中的第几个位置
            int startPosition = first.getStartPosition();

            // 在内部是第几个位置
            int innerPosition = aOutPosition - startPosition;

            return new AnalysisResult(index, startPosition, innerPosition);
        }
        return new AnalysisResult(-1, -1, -1);
    }

    public class AnalysisResult {
        private int adapterIndex; // 第几个Adapter
        private int adapterFirstItemPosition; // 当前adapter的头一个元素是全数据中的第几个位置
        private int adapterInnerPosition; // 当前元素在Adapter内部是第几个位置

        public AnalysisResult(int adapterIndex, int adapterFirstItemPosition, int adapterInnerPosition) {
            this.adapterIndex = adapterIndex;
            this.adapterFirstItemPosition = adapterFirstItemPosition;
            this.adapterInnerPosition = adapterInnerPosition;
        }

        public int getAdapterIndex() {
            return adapterIndex;
        }

        public int getAdapterFirstItemPosition() {
            return adapterFirstItemPosition;
        }

        public int getAdapterInnerPosition() {
            return adapterInnerPosition;
        }

        @Override
        public String toString() {
            return "AnalysisResult{" +
                    "adapterIndex=" + adapterIndex +
                    ", adapterFirstItemPosition=" + adapterFirstItemPosition +
                    ", adapterInnerPosition=" + adapterInnerPosition +
                    '}';
        }
    }

//    public void f1(int position) {
//        Pair<AdapterDataObserver, Adapter> adapterByPosition = findAdapterByPosition(position);
//        System.out.println("------>>> adapterByPosition= " + adapterByPosition);
//        if (adapterByPosition != null) {
//
//            AdapterDataObserver first = adapterByPosition.first;
//
//            // 第几个Adapter
//            int index = first.getIndex();
//
//            // 当前adapter的头一个元素是全数据中的第几个位置
//            int startPosition = first.getStartPosition();
//
//            // 在内部是第几个位置
//            int innerPosition = position - startPosition;
//
//
//            InfoUtil.INSTANCE.show("position = " + position + ", adapter index = " + index + ", startPosition = " + startPosition + ", innerPosition = " + innerPosition);
//
//
////            System.out.println("------>>> adapterByPosition.first.getIndex()= " +  adapterByPosition.first.getIndex());
////            System.out.println("------>>> adapterByPosition.first.getStartPosition()= " +  adapterByPosition.first.getStartPosition());
////
////            System.out.println("------>>> adapterByPosition.second= " +  adapterByPosition.second);
////            System.out.println("------>>> adapterByPosition.second.getItemCount()= " +  adapterByPosition.second.getItemCount());
//
//
//        }
//
//
//    }
}
