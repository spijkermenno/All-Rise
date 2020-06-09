package nl.topicus.all_rise.activity.Workout;

import static java.nio.file.Paths.get;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.android.volley.VolleyError;

import java.util.ArrayList;

import nl.topicus.all_rise.Adapter.WorkoutListAdapter;
import nl.topicus.all_rise.R;
import nl.topicus.all_rise.data.DataProvider;
import nl.topicus.all_rise.data.response.ArrayListResponse;
import nl.topicus.all_rise.model.Workout;

public class WorkoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        final Activity context = this;

        final ListView listView = findViewById(R.id.ListView);

        DataProvider dp = new DataProvider(this);

        System.out.println("REQUESTING WORKOUTS");

        dp.request(DataProvider.GET_WORKOUTS, null, null, new ArrayListResponse() {
            @Override
            public void response(ArrayList<?> data) {
                ArrayList<Workout> workouts = (ArrayList<Workout>) data;

                for (Workout w: workouts) {
                    System.out.println(w.getExercise());
                };

                //WorkoutListAdapter whatever = new WorkoutListAdapter(context, workouts);
                //listView.setAdapter(whatever);
            }

            @Override
            public void error(VolleyError error) {

            }
        });
    }
}

// for (Object i: data) {
//                            System.out.println(i);
//                        }
//
//                        ArrayList<Workout> workouts = new ArrayList<>();
//
//                        //WorkoutListAdapter whatever = new WorkoutListAdapter(context, workouts);
//                        //listView.setAdapter(whatever);