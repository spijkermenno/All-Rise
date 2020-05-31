package nl.topicus.all_rise;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.WindowManager;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private SensorManager sensorManager;
    private double prevMagn = 0;
    private CountDownTimer cdt;
    private long timeLeft;
    private boolean startTimer;

    private static final long TIME_IN_MILLIS = 7200000;
    boolean moving = false;
    TextView tvValMotion, tvCtd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        tvValMotion = findViewById(R.id.tv_valMotion);
        tvCtd = findViewById(R.id.tv_inactiveCounter);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);


        /**
         * Checks if phone is moving.
         * Boolean moving returns true if moving, false if not moving.
         */
        SensorEventListener motionDetector = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                    float x_acceleration = event.values[0];
                    float y_acceleration = event.values[1];
                    float z_acceleration = event.values[2];

                    double magnitude = Math.sqrt(x_acceleration * x_acceleration * y_acceleration * y_acceleration * z_acceleration * z_acceleration);
                    double deltaMagn = magnitude - prevMagn;
                    prevMagn = magnitude;

                    moving = deltaMagn > 24;
                    tvValMotion.setText("" + moving);
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };

        //Counting inactivity time
        //TODO Keeps counting down, even after device has moved.
            if (!moving){
                timeLeft = TIME_IN_MILLIS;
                cdt = new CountDownTimer(timeLeft, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        timeLeft = millisUntilFinished;
                        tvCtd.setText((timeLeft/1000) / 60 + ":" + (timeLeft/1000) % 60);
                    }

                    @Override
                    public void onFinish() {
                        //Send notafication
                    }

                }.start();
            } else {
                timeLeft = TIME_IN_MILLIS;
                cdt.cancel();
            }



        sensorManager.registerListener(motionDetector, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }


}