package nl.topicus.all_rise.activity.Workout;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.VolleyError;

import nl.topicus.all_rise.R;
import nl.topicus.all_rise.data.DataProvider;
import nl.topicus.all_rise.data.response.WorkoutResponse;
import nl.topicus.all_rise.model.Workout;

public class WorkoutActivity extends AppCompatActivity {
    TextView title, description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        final String workoutId = getIntent().getExtras().get("workout_id").toString();

        System.out.println(workoutId);

        DataProvider dp = new DataProvider(this);

        title = findViewById(R.id.workoutTitle);
        description = findViewById(R.id.workoutDescription);

        dp.request(DataProvider.GET_WORKOUT, workoutId, null, new WorkoutResponse() {
            @Override
            public void response(Workout workout) {
                System.out.print("WORKOUT VIEW: ");
                System.out.println(workout);

                title.setText(workout.getExercise().getName());
                description.setText(workout.getExercise().getDescription());
            }

            @Override
            public void error(VolleyError error) {
                error.printStackTrace();
            }
        });
    }
}
