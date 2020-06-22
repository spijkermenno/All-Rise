package nl.topicus.all_rise.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import nl.topicus.all_rise.R;
import nl.topicus.all_rise.model.HistoryEntry;

public class HistoryAdapter extends ArrayAdapter<HistoryEntry>{

    private final Context ctx;

    public HistoryAdapter(Context ctx, int layoutResourceId, List<HistoryEntry> arrayList) {
        super(ctx, layoutResourceId, arrayList);
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        HistoryEntry historyEntry = getItem(position);

        LayoutInflater layoutInflater = LayoutInflater.from(ctx);
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_historyentry, null);
        }

        TextView duration = convertView.findViewById(R.id.tv_workoutvalue);
        TextView exerciseType = convertView.findViewById(R.id.tv_exerciseType);

        duration.setText(String.valueOf(historyEntry.getDuration()/1000));
        exerciseType.setText(historyEntry.getExerciseType());

        return convertView;
    }
}
