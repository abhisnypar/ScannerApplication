package pidugu.example.com.storagescanner.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pidugu.example.com.storagescanner.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ResultListFragment extends Fragment {
    private Set<Map.Entry<String, Long>> data;

    private ArrayList<String> keysList;
    private ArrayList<Integer> valuesList;

    private TextView averageValueTextView;

    private static final String KEY_LIST = "KeysList";
    private static final String VALUES_LIST = "ValuesList";

    public ResultListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_result_list, container, false);

        averageValueTextView = view.findViewById(R.id.average_text_view);
        initializeListViews(savedInstanceState);

        final RecyclerView recyclerView = view.findViewById(R.id.result_list_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        final ResultListAdapter resultListAdapter = new ResultListAdapter(keysList, valuesList);

        recyclerView.setAdapter(resultListAdapter);
        resultListAdapter.notifyDataSetChanged();
        return view;
    }

    public void setData(final Set<Map.Entry<String, Long>> data) {
        this.data = data;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putStringArrayList(KEY_LIST, keysList);
        outState.putIntegerArrayList(VALUES_LIST, valuesList);
        super.onSaveInstanceState(outState);
    }

    private void initializeListViews(Bundle savedInstanceState) {
        keysList = new ArrayList<>();
        valuesList = new ArrayList<>();

        if (data != null) {
            for (Map.Entry<String, Long> mapping : data) {
                keysList.add(mapping.getKey());
                Integer i = (int) (long) mapping.getValue();
                valuesList.add(i);
            }
        }

        if (savedInstanceState != null) {
            keysList = savedInstanceState.getStringArrayList(KEY_LIST);
            valuesList = savedInstanceState.getIntegerArrayList(VALUES_LIST);
        }

        Collections.reverse(keysList);
        Collections.reverse(valuesList);

        averageValueTextView.setText(String.valueOf(calculateAverage(valuesList)));

        keysList.subList(9, keysList.size() - 1).clear();
        valuesList.subList(9, valuesList.size() - 1).clear();
    }

    @Override
    public void onPause() {
        super.onPause();
        averageValueTextView.setText(" ");
    }

    private double calculateAverage(final List<Integer> marks) {
        Integer sum = 0;
        if (!marks.isEmpty()) {
            for (Integer mark : marks) {
                sum += mark;
            }
            return sum.doubleValue() / marks.size();
        }
        return sum;
    }
}
