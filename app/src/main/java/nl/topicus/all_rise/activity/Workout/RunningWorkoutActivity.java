package nl.topicus.all_rise.activity.Workout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.service.autofill.TextValueSanitizer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import nl.topicus.all_rise.R;
import nl.topicus.all_rise.data.DataProvider;
import nl.topicus.all_rise.data.response.JsonArrayResponse;
import nl.topicus.all_rise.data.response.JsonObjectResponse;
import nl.topicus.all_rise.utility.Data;

public class RunningWorkoutActivity extends AppCompatActivity {
    TextView secondsView;
    Button stopWorkout;
    long startTime, endTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running_workout);

        final String exercise_id = getIntent().getExtras().get("exercise_id").toString();


        secondsView = findViewById(R.id.secondsTimer);
        startTime = System.currentTimeMillis();
        final Context c = this;

        final Thread thread = new Thread() {

            @Override
            public void run() {
                try {
                    while (!this.isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                secondsView.setText("" + (System.currentTimeMillis() - startTime) / 1000);
                            }
                        });
                    }
                } catch (InterruptedException ignored) {
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