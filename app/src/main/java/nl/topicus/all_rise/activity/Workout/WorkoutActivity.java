package nl.topicus.all_rise.activity.Workout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import nl.topicus.all_rise.Adapter.WorkoutListAdapter;
import nl.topicus.all_rise.R;

public class WorkoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        ListView listView = findViewById(R.id.ListView);

        String[] data = {
                "test1",
                "test2",
                "test3",
        };

        WorkoutListAdapter whatever = new WorkoutListAdapter(this, data);
        listView = (ListView) findViewById(R.id.ListView);
        listView.setAdapter(whatever);
    }
}
