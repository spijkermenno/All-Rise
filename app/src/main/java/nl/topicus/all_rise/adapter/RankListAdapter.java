package nl.topicus.all_rise.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.List;

import nl.topicus.all_rise.R;
import nl.topicus.all_rise.model.RankEntry;

public class RankListAdapter extends ArrayAdapter<RankEntry> {

    private final Context ctx;

    public RankListAdapter(Context ctx, int layoutResourceId, List<RankEntry> arrayList) {
        super(ctx, layoutResourceId, arrayList);
        this.ctx = ctx;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RankEntry rankEntry = getItem(position);

        LayoutInflater layoutInflater = LayoutInflater.from(ctx);
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_rankentry, null);
        }

        convertView.setPadding(20, 20, 20, 20);

        ImageView crown = convertView.findViewById(R.id.crown);
        TextView rank = convertView.findViewById(R.id.rank);
        TextView firstname = convertView.findViewById(R.id.firstname);
        TextView lastname = convertView.findViewById(R.id.lastname);
        TextView total_points = convertView.findViewById(R.id.total_points);

        if (rankEntry.getRank() == 1) {
            crown.setVisibility(View.VISIBLE);
        } else {
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) rank.getLayoutParams();
            layoutParams.setMarginStart((int) (30 * ctx.getResources().getDisplayMetrics().density));
            rank.setLayoutParams(layoutParams);
        }
        rank.setText(String.valueOf(rankEntry.getRank()));
        firstname.setText(rankEntry.getFirstname());
        lastname.setText(rankEntry.getLastname());
        total_points.setText(String.valueOf(rankEntry.getTotal_Points()));

        if (rankEntry.isCurrentuser()) {
            rank.setTextColor(Color.BLACK);
            firstname.setTextColor(Color.BLACK);
            lastname.setTextColor(Color.BLACK);
            total_points.setTextColor(Color.BLACK);
            convertView.setBackgroundColor(Color.parseColor("#dddddd"));
        }

        return convertView;
    }

    public void refresh() {
        super.notifyDataSetChanged();
    }
}
