package nl.topicus.all_rise.activity.Workout;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.android.volley.VolleyError;

import java.util.ArrayList;

import nl.topicus.all_rise.R;
import nl.topicus.all_rise.adapter.WorkoutsListAdapter;
import nl.topicus.all_rise.data.DataProvider;
import nl.topicus.all_rise.data.response.ArrayListResponse;
import nl.topicus.all_rise.model.Exercise;
import nl.topicus.all_rise.model.Workout;

public class WorkoutsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workouts);

        final Activity context = this;
        final WorkoutsActivity self = this;
        final ListView listView = findViewById(R.id.ListView);

        DataProvider dp = new DataProvider(this);

        dp.request(DataProvider.GET_EXERCISES, null, null, new ArrayListResponse() {
            @Override
            public void response(ArrayList<?> data) {
                ArrayList<Exercise> exercises = (ArrayList<Exercise>) data;

                WorkoutsListAdapter whatever = new WorkoutsListAdapter(context, exercises, self);
                listView.setAdapter(whatever);
            }

            @Override
            public void error(VolleyError error) {
                error.printStackTrace();
            }
        });
    }

    public void openWorkoutIntent(Exercise exercise) {
        Intent overviewIntent = new Intent(
                WorkoutsActivity.this,
                WorkoutActivity.class
        );
        overviewIntent.putExtra("exercise_id", exercise.getId());
        startActivity(overviewIntent);
    }
}