package nl.topicus.all_rise.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.io.InterruptedIOException;

import nl.topicus.all_rise.R;
import nl.topicus.all_rise.activity.Authentication.InviteCodeActivity;
import nl.topicus.all_rise.activity.Workout.WorkoutActivity;
import nl.topicus.all_rise.data.DataProvider;
import nl.topicus.all_rise.data.FileReader;
import nl.topicus.all_rise.data.response.EmployeeResponse;
import nl.topicus.all_rise.model.Employee;
import nl.topicus.all_rise.utility.Data;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MainActivity extends AppCompatActivity {
    private final String LOCALSTORAGEFILENAME = "storage.json";

    private Context context;
    public JSONObject USERDATA;

    private SensorManager sensorManager;
    private double prevMagn = 0;
    boolean moving = false;
    TextView tvValMotion, tvWelcome;
    Button btnWorkout, btnRankings, btnStatistics, btnHistory, btnPreferences, btnZenmode;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        context = MainActivity.this;

        // AUTHENTICATION
        // instatiate filereader
        final FileReader fr = new FileReader();
        Data data = new Data(this);

        data.checkIfUserLoggedIn(fr);

        try {
            USERDATA = data.getUserDataFromLocalStorage(fr);
        } catch (InterruptedIOException e) {
            e.printStackTrace();
        }

        // User is not logged in.
        if (USERDATA == null || USERDATA.toString().equals("{}")) {
            Intent overviewIntent = new Intent(MainActivity.this, InviteCodeActivity.class);
            startActivity(overviewIntent);
            finish();
        } else {
            final Context ctx = this;
            final DataProvider dp = new DataProvider(getApplicationContext());

            dp.request(DataProvider.GET_EMPLOYEE_BY_CODE,
                    data.getUserData().getActivationCode(), null,
                    new EmployeeResponse() {

                        @Override
                        public void response(Employee employee) {
                            try {
                                if (employee != null) {
                                    JSONObject obj = new JSONObject();

                                    obj.put("id", employee.getId());
                                    obj.put("department_id", employee
                                            .getDepartmentId());
                                    obj.put("name", employee.getName());
                                    obj.put("surname", employee.getSurName());
                                    obj.put("activationCode", employee
                                            .getActivationCode());
                                    obj.put("verified", employee
                                            .isVerified());

                                    FileReader fr = new FileReader();
                                    if (employee.isVerified()) {
                                        System.out.println(employee);

                                        // Write user data to local file.
                                        fr.create(ctx, LOCALSTORAGEFILENAME, obj.toString());
                                    } else {
                                        System.out.println("SIGN OFF");
                                        fr.clearFile(ctx, LOCALSTORAGEFILENAME);

                                        Intent reloadView = new Intent(MainActivity.this, InviteCodeActivity.class);
                                        startActivity(reloadView);
                                        finish();
                                    }
                                }
                            } catch (Exception e) {
                                System.out.println("ERROR");
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void error(VolleyError error) {
                            System.out.println("VOLLEY ERROR: " + error.networkResponse.statusCode);
                            if (error.networkResponse.statusCode == 401) {
                                System.out.println("SIGN OFF");
                                fr.clearFile(ctx, LOCALSTORAGEFILENAME);

                                Intent reloadView = new Intent(MainActivity.this, InviteCodeActivity.class);
                                startActivity(reloadView);
                                finish();
                            }
                            error.printStackTrace();
                        }
                    });
        }

        if (data.getUserData() != null) {

            final Context ctx = this;
            View signOffButton = findViewById(R.id.sign_off);
            signOffButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("SIGN OFF");
                    fr.clearFile(ctx, LOCALSTORAGEFILENAME);

                    Intent reloadView = new Intent(
                            MainActivity.this,
                            MainActivity.class
                    );
                    startActivity(reloadView);
                    finish();


                }
            });

            // MAIN MENU
            tvWelcome = findViewById(R.id.tv_welcome);
            tvWelcome.setText("Welkom, " + data.getUserData().getName());

            btnWorkout = findViewById(R.id.btn_workout);
            btnWorkout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Intent naar WorkoutActivity
                    Intent view = new Intent(
                            MainActivity.this,
                            WorkoutActivity.class
                    );
                    startActivity(view);
                }
            });

            btnRankings = findViewById(R.id.btn_rankings);
            btnRankings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Intent naar RankingsActivity
                }
            });

            btnStatistics = findViewById(R.id.btn_statistics);
            btnStatistics.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Intent naar StatisticsActivity
                }
            });

            btnHistory = findViewById(R.id.btn_history);
            btnHistory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Intent naar HistoryActivity
                }
            });

            btnPreferences = findViewById(R.id.btn_preferences);
            btnPreferences.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Intent naar PreferencesActivity
                }
            });

            btnZenmode = findViewById(R.id.btn_zenmode);
            btnZenmode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Intent naar ZenmodeActivity
                }
            });

        Button buttonday = (Button) findViewById(R.id.button_daily);

        buttonday.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), RankingActivity.class);
                intent.putExtra("filter", 0);
                startActivity(intent);
            }
        });

        Button buttonweek = (Button) findViewById(R.id.button_weekly);

        buttonweek.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), RankingActivity.class);
                intent.putExtra("filter", 1);
                startActivity(intent);
            }
        });

        Button buttonmonth = (Button) findViewById(R.id.button_monthly);

        buttonmonth.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), RankingActivity.class);
                intent.putExtra("filter", 2);
                startActivity(intent);
            }
        });



            // SENSORS
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            tvValMotion = findViewById(R.id.tv_valMotion);

            sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
            Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

            SensorEventListener motionDetector = new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent event) {
                    if (event != null) {
                        float x_acceleration = event.values[0];
                        float y_acceleration = event.values[1];
                        float z_acceleration = event.values[2];

                        double magnitude = Math.sqrt(
                                x_acceleration * x_acceleration * y_acceleration * y_acceleration
                                        * z_acceleration * z_acceleration);
                        double deltaMagn = magnitude - prevMagn;
                        prevMagn = magnitude;

                        moving = deltaMagn > 6;
                        tvValMotion.setText("" + moving);
                    }
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) {

                }
            };

            sensorManager.registerListener(motionDetector, sensor,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
    }
}