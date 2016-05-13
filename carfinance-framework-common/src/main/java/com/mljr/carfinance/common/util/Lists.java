package com.mljr.carfinance.common.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class Lists {

    public static <E> ArrayList<E> newArrayList() {
        return new ArrayList<E>();
    }

    /**
     * 将map构造成ArrayList
     * @param map
     * @return
     */
    public static <E> ArrayList<E> newArrayList(Map<Long, E> map) {
        return (ArrayList<E>) new ArrayList<E>(map.values());
    }

    public static List<Map<String, Object>> newMapList() {
        return new ArrayList<Map<String, Object>>();
    }

    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    public static List<Object> arr2List(Object[] arr) {
        List<Object> list = new ArrayList<Object>();
        if (arr != null && arr.length > 0) {
            for (Object obj : arr) {
                list.add(obj);
            }
        }
        return list;
    }

    /**
     * 将item加入到list中
     * @param list
     * @param items
     */
    public static void addAll(Collection list, Collection items) {
        if (list != null && isNotEmpty(items)) {
            list.addAll(items);
        }
    }
}
