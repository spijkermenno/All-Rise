package nl.topicus.all_rise.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import nl.topicus.all_rise.R;
import nl.topicus.all_rise.data.DataProvider;
import nl.topicus.all_rise.data.response.JsonObjectResponse;

public class StatisticsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private PieChart pieChart;
    private Spinner spinner;
    private TextView timeExercise;
    private DataProvider dataProvider;
    private ArrayAdapter<String> sortBySpinnerAdapter;
    private int userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        userID = getIntent().getExtras().getInt("userID");
        dataProvider = new DataProvider(this);

        spinner = findViewById(R.id.spinner);
        sortBySpinnerAdapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, new String[]{getResources().getString(R.string.rankingDay), getResources().getString(R.string.rankingWeek), getResources().getString(R.string.rankingMonth)});
        sortBySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(sortBySpinnerAdapter);
        spinner.setOnItemSelectedListener(this);
        pieChart = findViewById(R.id.piechart);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (position) {
            case 0:
                createPieChart(DataProvider.GET_WORKOUTS_DAILY);
                break;
            case 1:
                createPieChart(DataProvider.GET_WORKOUTS_WEEKLY);
                break;
            case 2:
                createPieChart(DataProvider.GET_WORKOUTS_MONTHLY);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void createPieChart(String api) {
        dataProvider.request(api, String.valueOf(userID), null, new JsonObjectResponse() {
            @Override
            public void error(VolleyError error) {

            }

            @Override
            public void response(JSONObject data) throws JSONException {
                pieChart = findViewById(R.id.piechart);
                pieChart.setUsePercentValues(false);
                pieChart.getDescription().setEnabled(false);
                pieChart.setExtraOffsets(10, 20, 10, 10);
                pieChart.setEntryLabelColor(Color.BLACK);

                JSONArray array = data.getJSONArray("data");
                ArrayList<PieEntry> chartValues = new ArrayList<>();
                for (int i = 0; i < array.length(); i++) {
                    chartValues.add(new PieEntry(array.getJSONObject(i).getInt("Duration"), array.getJSONObject(i).getString("Name")));
                }

                PieDataSet dataSet = new PieDataSet(chartValues, "");
                dataSet.setSliceSpace(3f);
                dataSet.setColors(ColorTemplate.JOYFUL_COLORS);

                PieData pieData = new PieData(dataSet);
                pieData.setValueTextSize(10f);
                pieData.setValueTextColor(Color.TRANSPARENT);

                pieChart.setData(pieData);
                pieChart.invalidate();

                //get total duration
                int duration = 0;
                for (int i = 0; i < array.length(); i++) {
                    duration = duration + array.getJSONObject(i).getInt("Duration");
                }
                timeExercise = findViewById(R.id.tv_statistics_amount);
                timeExercise.setText(String.valueOf(duration/1000));
            }
        });
    }
}
