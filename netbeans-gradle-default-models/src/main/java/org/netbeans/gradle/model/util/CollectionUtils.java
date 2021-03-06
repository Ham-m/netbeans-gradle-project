package org.netbeans.gradle.model.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class CollectionUtils {
    public static void checkNoNullElements(Collection<?> collection, String name) {
        if (collection == null) throw new NullPointerException(name);

        int index = 0;
        for (Object element: collection) {
            if (element == null) throw new NullPointerException(name + "[" + index + "]");
            index++;
        }
    }

    public static <K, V> Map<K, V> copyNullSafeHashMap(Map<? extends K, ? extends V> map) {
        if (map == null) throw new NullPointerException("map");

        Map<K, V> result = Collections.unmodifiableMap(new HashMap<K, V>(map));
        for (Map.Entry<K, V> entry: result.entrySet()) {
            if (entry.getKey() == null) throw new NullPointerException("entry.getKey()");
            if (entry.getValue()== null) throw new NullPointerException("entry.getValue()");
        }
        return result;
    }

    public static <E> List<E> copyNullSafeList(Collection<? extends E> list) {
        if (list == null) throw new NullPointerException("list");

        List<E> result = Collections.unmodifiableList(new ArrayList<E>(list));
        for (E element: result) {
            if (element == null) throw new NullPointerException("element");
        }
        return result;
    }

    public static <E> ArrayList<E> copyNullSafeMutableList(Collection<? extends E> list) {
        if (list == null) throw new NullPointerException("list");

        ArrayList<E> result = new ArrayList<E>(list);
        for (E element: result) {
            if (element == null) throw new NullPointerException("element");
        }
        return result;
    }

    private CollectionUtils() {
        throw new AssertionError();
    }
}
