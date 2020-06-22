package com.endlesscreator.titoollib.utils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CollectionUtil {

    public static boolean empty(Collection aCollection) {
        return aCollection == null || aCollection.isEmpty();
    }

    public static boolean empty(Map aMap) {
        return aMap == null || aMap.isEmpty();
    }

    public static <C extends Collection> boolean has(C aCollection, int aIndex) {
        return aCollection != null && aIndex >= 0 && aIndex < aCollection.size();
    }

    public static <T, C extends Collection<T>> boolean hasI(C aCollection, T aItem) {
        return aCollection != null && aCollection.contains(aItem);
    }

    public static <T, L extends List<T>> T item(L aList, int aIndex) {
        return has(aList, aIndex) ? aList.get(aIndex) : null;
    }

    public static <T, L extends List<T>> T last(L aList) {
        return item(aList, size(aList) - 1);
    }

    public static int size(Collection aCollection) {
        return aCollection != null ? aCollection.size() : 0;
    }

    public static int size(Map aMap) {
        return aMap != null ? aMap.size() : 0;
    }

    public static <K, V extends Collection> int itemSize(Map<K, V> aMap) {
        int lSize = 0;
        if (!empty(aMap)) {
            Set<Map.Entry<K, V>> lEntries = aMap.entrySet();
            for (Map.Entry<K, V> lEntry : lEntries)
                lSize += size(lEntry.getValue());
        }
        return lSize;
    }


    public static <T> String join(CharSequence aDelimiter, List<T> aDataList) {
        StringBuilder lSB = new StringBuilder();
        if (!empty(aDataList)) {
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


    /**
     * {@link #empty(Collection)}
     */
    @Deprecated
    public static <T> boolean isEmpty(List<T> aList) {
        return empty(aList);
    }

    /**
     * {@link #item(List, int)}
     */
    @Deprecated
    public static <T> T getItem(List<T> aList, int aIndex) {
        return item(aList, aIndex);
    }

    /**
     * {@link #size(Collection)}
     */
    @Deprecated
    public static <T> int getSize(List<T> aList) {
        return size(aList);
    }

    /**
     * {@link #itemSize(Map)}
     */
    @Deprecated
    public static <K, T> int getItemsSize(Map<K, List<T>> aMap) {
        return itemSize(aMap);
    }


}
