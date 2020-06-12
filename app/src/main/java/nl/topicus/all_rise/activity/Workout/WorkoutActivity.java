package nl.topicus.all_rise.activity.Workout;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.android.volley.VolleyError;

import nl.topicus.all_rise.R;
import nl.topicus.all_rise.data.DataProvider;
import nl.topicus.all_rise.data.response.WorkoutResponse;
import nl.topicus.all_rise.model.Workout;

public class WorkoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        final String workoutId = getIntent().getExtras().get("workout_id").toString();

        DataProvider dp = new DataProvider(this);

        dp.request(DataProvider.GET_WORKOUT, workoutId, null, new WorkoutResponse() {
            @Override
            public void response(Workout workout) {
                System.out.println("WORKOUT VIEW");
                System.out.println(workout);
            }

            @Override
            public void error(VolleyError error) {
                error.printStackTrace();
            }
        });
    }
}
