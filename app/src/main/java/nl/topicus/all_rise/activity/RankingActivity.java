package nl.topicus.all_rise.activity;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import nl.topicus.all_rise.R;
import nl.topicus.all_rise.adapter.AdapterHelper;
import nl.topicus.all_rise.adapter.RankListAdapter;
import nl.topicus.all_rise.data.DataProvider;
import nl.topicus.all_rise.data.response.JsonObjectResponse;
import nl.topicus.all_rise.model.RankEntry;

public class RankingActivity extends AppCompatActivity {

    private ArrayList<RankEntry> rankEntries;
    private DataProvider dataProvider;
    private RankListAdapter adapter;
    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
        list = findViewById(R.id.list);

        rankEntries = new ArrayList<>();
        dataProvider = new DataProvider(this);
        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    getData();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                pullToRefresh.setRefreshing(false);
            }
        });
        adapter = new RankListAdapter(this, R.layout.list_rankentry, rankEntries);
        list.setAdapter(adapter);

        try {
            getData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getData() throws Exception {
        String action = null;
        switch (getIntent().getExtras().getInt("filter")) {
            case 0:
                action = DataProvider.GET_POINTS_DAILY;
                break;
            case 1:
                action = DataProvider.GET_POINTS_WEEKLY;
                break;
            case 2:
                action = DataProvider.GET_POINTS_MONTHLY;
                break;
        }
        if (action != null) {
            dataProvider.request(action, "1", null, new JsonObjectResponse() {
                @Override
                public void response(JSONObject data) {
                    try {
                        rankEntries.clear();
                        JSONArray array = data.getJSONArray("data");
                        for (int i = 0; i < array.length(); i++) {
                            RankEntry rankEntry = null;
                            System.out.println(array.getJSONObject(i));
                            if (array.getJSONObject(i).getInt("ID") == getIntent().getExtras().getInt("userID")) {
                                rankEntry = new RankEntry(i + 1, array.getJSONObject(i).getString("Firstname"), array.getJSONObject(i).getString("Lastname"), array.getJSONObject(i).getInt("total_points"), true);
                            } else {
                                rankEntry = new RankEntry(i + 1, array.getJSONObject(i).getString("Firstname"), array.getJSONObject(i).getString("Lastname"), array.getJSONObject(i).getInt("total_points"), false);
                            }
                           rankEntries.add(rankEntry);
                        }
                        new AdapterHelper().update(adapter, new ArrayList<Object>(rankEntries));
                        adapter.notifyDataSetChanged();
                        list.invalidateViews();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void error(VolleyError error) {


                }
            });
        } else {
            throw new Exception("Wrong intent code added");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}
