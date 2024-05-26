package com.jolivan.archivomotorclasicobackend.Collections.ColUtils;

import com.jolivan.archivomotorclasicobackend.Collections.Entities.CollectionNode;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class CollectionNodeUtils {
    private CollectionNodeUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static void removeRepeatedElements(List<CollectionNode> list) {
        Set<CollectionNode> set = new LinkedHashSet<>(list);
        list.clear();
        list.addAll(set);
    }
}
