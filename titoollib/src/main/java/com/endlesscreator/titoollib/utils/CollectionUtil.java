package com.endlesscreator.titoollib.utils;

import java.util.List;
import java.util.Map;

public class CollectionUtil {

    public static <T> boolean isEmpty(List<T> aList) {
        return aList == null || aList.isEmpty();
    }

    public static <T> T getItem(List<T> aList, int aIndex) {
        if (aList != null && aIndex >= 0 && aIndex < aList.size()) {
            return aList.get(aIndex);
        }
        return null;
    }

    public static <T> int getSize(List<T> aList) {
        return aList != null ? aList.size() : 0;
    }

    public static <K, T> int getItemsSize(Map<K, List<T>> aMap) {
        int lSize = 0;
        if (aMap != null && aMap.size() > 0) {
            List<T> lValues;
            for (Map.Entry<K, List<T>> lItems : aMap.entrySet()) {
                lValues = lItems.getValue();
                if (lValues != null) lSize += lValues.size();
            }
        }
        return lSize;
    }

    public static <T> String join(CharSequence aDelimiter, List<T> aDataList) {
        StringBuilder lSB = new StringBuilder();
        if (aDataList != null) {
            int lSize = aDataList.size();
            for (int i = 0; i < lSize - 1; i++) {
                lSB.append(aDataList.get(i)).append(aDelimiter);
            }
            lSB.append(aDataList.get(lSize - 1));
        }
        return lSB.toString();
    }

    public static <T> String join(List<T> aDataList) {
        return join(",", aDataList);
    }

}
