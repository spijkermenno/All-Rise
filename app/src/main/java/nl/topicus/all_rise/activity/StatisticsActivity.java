package nl.topicus.all_rise.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import nl.topicus.all_rise.R;

public class StatisticsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    PieChart pieChart;
    Spinner spinner;
    TextView timeExercise, timeSitting, timeWalking;
    private final String[] spinnerOptions = {"Vandaag", "Deze week", "Deze maand"};
    private ArrayAdapter<String> sortBySpinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        spinner = findViewById(R.id.spinner);
        sortBySpinnerAdapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, spinnerOptions);
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
                //TODO Get daily values
                createPieChart(2,2,1);
                break;
            case 1:
                //TODO get weekly values
                createPieChart(7,12,3);
                break;
            case 2:
                //TODO get monthly values
                createPieChart(29, 60, 17);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void createPieChart(int valExcersise, int valSitting, int valWalking){

        pieChart = findViewById(R.id.piechart);
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(10,20,10,10);
        pieChart.setEntryLabelColor(Color.TRANSPARENT);

        ArrayList<PieEntry> yvalues = new ArrayList<>();
        yvalues.add(new PieEntry(valExcersise, "Oefeningen"));
        yvalues.add(new PieEntry(valSitting, "Gezeten"));
        yvalues.add(new PieEntry(valWalking, "Gewandeld"));

        PieDataSet dataSet = new PieDataSet(yvalues,"");
        dataSet.setSliceSpace(3f);
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);

        PieData data = new PieData(dataSet);
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.TRANSPARENT);

        pieChart.setData(data);
        pieChart.invalidate();

        timeExercise = findViewById(R.id.timeExercise);
        timeExercise.setText("U heeft " + valExcersise + " uur besteed aan oefeningen");
        timeSitting = findViewById(R.id.timeSitting);
        timeSitting.setText("U heeft " + valSitting + " uur gezeten");
        timeWalking = findViewById(R.id.timeWalking);
        timeWalking.setText("U heeft " + valWalking + " uur gewandeld");
    }
}
