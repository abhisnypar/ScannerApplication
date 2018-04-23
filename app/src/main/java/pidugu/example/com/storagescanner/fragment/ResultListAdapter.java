package pidugu.example.com.storagescanner.fragment;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import pidugu.example.com.storagescanner.R;

public class ResultListAdapter extends RecyclerView.Adapter<ResultListAdapter.ResultListViewHolder> {

    private ArrayList<String> keysList;
    private ArrayList<Integer> valuesList;

    public ResultListAdapter(ArrayList<String> keysList, ArrayList<Integer> valuesList) {
        this.keysList = keysList;
        this.valuesList = valuesList;
    }

    @Override
    public ResultListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.result_list_item_view, parent, false);
        return new ResultListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ResultListViewHolder holder, int position) {
            holder.fileNameTextView.setText(keysList.get(position));
            holder.fileLengthTextView.setText(String.valueOf(valuesList.get(position)));
    }

    @Override
    public int getItemCount() {
        return keysList.size();
    }

    public class ResultListViewHolder extends RecyclerView.ViewHolder {
        TextView fileNameTextView, fileLengthTextView;

        public ResultListViewHolder(View itemView) {
            super(itemView);
            fileNameTextView = itemView.findViewById(R.id.keyValue);
            fileLengthTextView = itemView.findViewById(R.id.sizeValue);
        }
    }
}
