package nl.topicus.all_rise.activity.Workout;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import nl.topicus.all_rise.R;
import nl.topicus.all_rise.data.DataProvider;
import nl.topicus.all_rise.data.response.JsonObjectResponse;
import nl.topicus.all_rise.utility.Data;

public class RunningWorkoutActivity extends AppCompatActivity {
    TextView secondsView, pointsView;
    Button stopWorkout;
    long startTime, endTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running_workout);

        final String exercise_id = getIntent().getExtras().get("exercise_id").toString();


        secondsView = findViewById(R.id.secondsTimer);
        pointsView = findViewById(R.id.pointsTimer);
        startTime = System.currentTimeMillis();


        final Context c = this;

        final Thread thread = new Thread() {
            @Override
            public void run() {
                while (!this.isInterrupted()) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            long seconds = System.currentTimeMillis() - startTime;

                            int secs = (int) (seconds / 1000);
                            int minutes = secs / 60;
                            int milisecs = (int) (seconds % 1000);
                            secs = secs % 60;

//


                            // Set the text view text.
                            secondsView.setText("" + minutes + ":"
                                    + String.format("%02d", secs) + ":"
                                    + String.format("%03d", milisecs));
                             pointsView.setText("Punten: " + (System.currentTimeMillis() - startTime) / 1000);
                        }
                    });
                }
            }
        };

        thread.start();

        stopWorkout = findViewById(R.id.stopWorkout);
        stopWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endTime = System.currentTimeMillis();

                System.out.println(endTime - startTime);

                long duration = endTime - startTime;
                int points = (int) Math.ceil(duration / 1000);

                final DataProvider dp = new DataProvider(c);
                final HashMap<String, String> h = new HashMap<String, String>();
                h.put("duration", "" + duration);
                h.put("points", "" + points);
                h.put("exercise_id", "" + exercise_id);

                dp.request(DataProvider.POST_WORKOUT, null, h, new JsonObjectResponse() {
                    @Override
                    public void response(JSONObject data) throws JSONException {
                        if (data.getInt("id") >= 0) {

                            Data user = new Data(c);
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("employee_id", "" + user.getUserData().getId());
                            map.put("workout_id", "" + data.getInt("id"));

                            dp.request(DataProvider.POST_HISTORY, null, map,
                                    new JsonObjectResponse() {
                                        @Override
                                        public void response(JSONObject data) throws JSONException {
                                            System.out.println(data);
                                        }

                                        @Override
                                        public void error(VolleyError error) {
                                            error.printStackTrace();
                                        }
                                    });
                        }
                    }

                    @Override
                    public void error(VolleyError error) {
                        error.printStackTrace();
                    }
                });

                finish();
            }
        });
    }
}
