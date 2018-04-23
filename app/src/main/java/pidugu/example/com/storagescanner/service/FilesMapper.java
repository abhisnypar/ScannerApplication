package pidugu.example.com.storagescanner.service;


import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class FilesMapper {

    public Set<Map.Entry<String, Long>> sortFiles(HashMap<String, Long> allFiles) {

        final TreeMap<String, Long> treeMap = new TreeMap<>(allFiles);
        final Set<Map.Entry<String, Long>> entries = treeMap.entrySet();

        final List<Map.Entry<String, Long>> list = new ArrayList<>(entries);

        Collections.sort(list, comparator());

        final LinkedHashMap<String, Long> sortedByValue = new LinkedHashMap<>(list.size());

        for (Map.Entry<String, Long> entry : list) {
            sortedByValue.put(entry.getKey(), entry.getValue());
        }

        final Set<Map.Entry<String, Long>> entrySetSortedByValue = sortedByValue.entrySet();

        for (Map.Entry<String, Long> mapping : entrySetSortedByValue) {
            Log.d("File Name : " + mapping.getKey(), "File Size: " + mapping.getValue());
        }

        return entrySetSortedByValue;
    }

    private Comparator<Map.Entry<String, Long>> comparator() {
        return new Comparator<Map.Entry<String, Long>>() {
            @Override
            public int compare(Map.Entry<String, Long> e1, Map.Entry<String, Long> e2) {
                Long v1 = e1.getValue();
                Long v2 = e2.getValue();
                return v1.compareTo(v2);
            }
        };
    }

}
