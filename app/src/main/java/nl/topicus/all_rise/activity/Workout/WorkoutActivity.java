package nl.topicus.all_rise.activity.Workout;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.VolleyError;

import nl.topicus.all_rise.R;
import nl.topicus.all_rise.data.DataProvider;
import nl.topicus.all_rise.data.response.ExerciseResponse;
import nl.topicus.all_rise.data.response.WorkoutResponse;
import nl.topicus.all_rise.model.Exercise;
import nl.topicus.all_rise.model.Workout;

public class WorkoutActivity extends AppCompatActivity {
    TextView title, description;
    Button workoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        final String exercise_id = getIntent().getExtras().get("exercise_id").toString();
        final String points_multiplier = getIntent().getExtras().get("points_multiplier").toString();

        System.out.println(exercise_id);

        DataProvider dp = new DataProvider(this);

        title = findViewById(R.id.workoutTitle);
        description = findViewById(R.id.workoutDescription);
        workoutButton = findViewById(R.id.startWorkout);
        workoutButton.setEnabled(false);

        workoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent overviewIntent = new Intent(
                        WorkoutActivity.this,
                        RunningWorkoutActivity.class
                );
                overviewIntent.putExtra("exercise_id", exercise_id);
                overviewIntent.putExtra("points_multiplier", points_multiplier);
                startActivity(overviewIntent);
            }
        });

        dp.request(DataProvider.GET_EXERCISE, exercise_id, null, new ExerciseResponse() {
            @Override
            public void response(Exercise exercise) {
                title.setText(exercise.getName());
                description.setText(exercise.getDescription());
                workoutButton.setEnabled(true);
            }

            @Override
            public void error(VolleyError error) {
                error.printStackTrace();
            }
        });
    }
}
