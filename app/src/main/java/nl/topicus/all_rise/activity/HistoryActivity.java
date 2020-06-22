package nl.topicus.all_rise.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import nl.topicus.all_rise.R;
import nl.topicus.all_rise.adapter.AdapterHelper;
import nl.topicus.all_rise.adapter.HistoryAdapter;
import nl.topicus.all_rise.adapter.RankListAdapter;
import nl.topicus.all_rise.data.DataProvider;
import nl.topicus.all_rise.data.response.JsonObjectResponse;
import nl.topicus.all_rise.model.HistoryEntry;
import nl.topicus.all_rise.model.RankEntry;

public class HistoryActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private final String[] spinnerOptions = {"Vandaag", "Deze week", "Deze maand", "Totaal"};

    private Spinner spinner;
    private DataProvider dataProvider;
    private ArrayAdapter<String> sortBySpinnerAdapter;
    private int userID;
    private ArrayList<HistoryEntry> historyItems;
    private HistoryAdapter adapter;
    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        userID = getIntent().getExtras().getInt("userID");
        dataProvider = new DataProvider(this);

        historyItems = new ArrayList<>();
        list = findViewById(R.id.history_list);

        adapter = new HistoryAdapter(this, R.layout.list_historyentry, historyItems);
        list.setAdapter(adapter);

        spinner = findViewById(R.id.spinnerhistory);
        sortBySpinnerAdapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, spinnerOptions);
        sortBySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(sortBySpinnerAdapter);
        spinner.setOnItemSelectedListener(this);

    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String action = null;
        switch (position){
            case 0:
                action = DataProvider.GET_WORKOUTS_DAILY;
                break;
            case 1:
                action = DataProvider.GET_WORKOUTS_WEEKLY;
                break;
            case 2:
                action = DataProvider.GET_WORKOUTS_MONTHLY;
                break;
            case 3:
                action = DataProvider.GET_WORKOUTS_TOTAL;
        }
        if (action != null) {
            dataProvider.request(action, String.valueOf(userID), null, new JsonObjectResponse() {
                @Override
                public void error(VolleyError error) {

                }

                @Override
                public void response(JSONObject data) {
                    try {
                        historyItems.clear();
                        JSONArray array = data.getJSONArray("data");
                        for (int i = 0; i < array.length(); i++) {
                            HistoryEntry historyEntry = new HistoryEntry(array.getJSONObject(i).getInt("Duration"), array.getJSONObject(i).getString("Name"));;
                            historyItems.add(historyEntry);
                        }
                        new AdapterHelper().update(adapter, new ArrayList<Object>(historyItems));
                        adapter.notifyDataSetChanged();
                        list.invalidateViews();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
