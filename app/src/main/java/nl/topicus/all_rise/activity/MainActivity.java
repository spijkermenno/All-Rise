package nl.topicus.all_rise;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.InterruptedIOException;

import nl.topicus.all_rise.activity.Authentication.InviteCodeActivity;
import nl.topicus.all_rise.data.FileReader;

public class MainActivity extends AppCompatActivity {

    private Context context;
    private JSONObject USERDATA;

    private SensorManager sensorManager;
    private double prevMagn = 0;
    boolean moving = false;
    TextView tvValMotion, tvWelcome;
    Button btnRankings, btnStatistics, btnHistory, btnPreferences, btnZenmode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = MainActivity.this;

        // AUTHENTICATION
        // instatiate filereader
        FileReader fr = new FileReader();

        checkIfUserLoggedIn(fr);

        try {
            USERDATA = getUserDataFromLocalStorage(fr);
        } catch (InterruptedIOException e) {
            e.printStackTrace();
        }

        if (USERDATA == null || USERDATA.toString().equals("{}")) {
            System.out.println("====== SIGN IN ======");
            Intent overviewIntent = new Intent(MainActivity.this, InviteCodeActivity.class);
            startActivity(overviewIntent);
        }

        // MAIN MENU
        tvWelcome = findViewById(R.id.tv_welcome);
        //TODO Waar "Jan" staat moet naam van gebruiker komen.
        tvWelcome.setText("Welkom, " + "Jan");

        btnRankings = findViewById(R.id.btn_rankings);
        btnRankings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnStatistics = findViewById(R.id.btn_statistics);
        btnStatistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnHistory = findViewById(R.id.btn_history);
        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnPreferences = findViewById(R.id.btn_preferences);
        btnPreferences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnZenmode = findViewById(R.id.btn_zenmode);
        btnZenmode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

        sensorManager.registerListener(motionDetector, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected boolean checkIfUserLoggedIn(FileReader fr) {
        // check if user is logged in.
        fr.checkIfLocalStorageActivated(this, FileReader.LOCALSTORAGEFILENAME);
        String fileData = fr.read(this, FileReader.LOCALSTORAGEFILENAME);
        return !fileData.equals("{}");
    }

    protected JSONObject getUserDataFromLocalStorage(FileReader fr) throws InterruptedIOException {
        fr.checkIfLocalStorageActivated(this, FileReader.LOCALSTORAGEFILENAME);
        String fileData = fr.read(this, FileReader.LOCALSTORAGEFILENAME);

        JSONObject fileObject;
        try {
            return new JSONObject(fileData);

        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new java.io.InterruptedIOException("Data couldn't be casted to JSONObject.");
    }
}