package nl.topicus.all_rise.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import nl.topicus.all_rise.R;
import nl.topicus.all_rise.activity.MainActivity;
import nl.topicus.all_rise.data.DataProvider;
import nl.topicus.all_rise.data.response.JsonObjectResponse;
import nl.topicus.all_rise.reciever.OnNotificationDismissBroadcastReceiver;
import nl.topicus.all_rise.utility.Data;

public class NotificationService extends Service {

    private double prevMagn = 0;
    private Timer timer;
    private DataProvider dataProvider;
    private int IdleTime = 0;
    private boolean moving = false;

    protected boolean timerStarted = false;
    private static boolean notificationsend = false;
    private static int count = 0;
    protected Context context;


    public static final String CHANNEL_ID = "MotionRecorder";

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createNotificationChannel();
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        final Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(getString(R.string.main_service_title))
                .setContentText(getString(R.string.main_service_content))
                .setSmallIcon(R.drawable.all_rise_x_small)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);


        this.context = this;
        this.dataProvider = new DataProvider(context);
        final Data util = new Data(context);
        dataProvider.request(DataProvider.GET_SETTINGS, null, null, new JsonObjectResponse() {

            @Override
            public void error(VolleyError error) {

            }

            @Override
            public void response(JSONObject data) throws JSONException {
                IdleTime = util.getIntSettingFromDb("idle_time", data);
            }
        });

        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
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

                    //If moving true set count to 0 because user is moving.
                    if (moving) {
                        count = 0;

                        //Only if the timer is started cancel the timer or an error will appear.
                        if (timerStarted) {
                            timer.cancel();
                            timerStarted = false;
                        }
                    } else {

                        //If the notification is not send.
                        if (!notificationsend) {

                            //If the timer isnt started and the idle time isnt at the prefferd value start the time and set started to true.
                            //TODO change to IdleTime so its readed from db.
                            if (!timerStarted && count != IdleTime) {
                                timer = new Timer();
                                timer.schedule(new Task(), 0, 1000);
                                timerStarted = true;
                            }

                            //If the notification is send stop the timer and set started to false.
                        } else {
                            timer.cancel();
                            timerStarted = false;
                        }
                    }
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };

        sensorManager.registerListener(motionDetector, sensor,
                SensorManager.SENSOR_DELAY_NORMAL);
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    class Task extends TimerTask {

        @Override
        public void run() {
            if (NotificationService.count != IdleTime) {
                NotificationService.count++;
            } else {
                if (notificationsend) {
                    NotificationService.count = 0;
                } else {

                    //Get channel.
                    createNotificationChannel();

                    //Create Notificaton with string resources.
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                            .setSmallIcon(R.drawable.all_rise_x_small)
                            .setContentTitle(context.getResources().getString(R.string.notification_title))
                            .setContentText(context.getResources().getString(R.string.notification_content))
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                    //Create Intent for on dismiss.
                    Intent onCancelIntent = new Intent(context, OnNotificationDismissBroadcastReceiver.class);
                    PendingIntent onDismissPendingIntent = PendingIntent.getBroadcast(context, 0, onCancelIntent, 0);
                    builder.setDeleteIntent(onDismissPendingIntent);

                    //Forward the notification to the manager and post it on the notification line.
                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                    notificationManager.notify(2, builder.build());

                    //Send is true so the system wont spam the line.
                    notificationsend = true;
                }
            }
        }

    }

    public static void reset() {
        count = 0;
        notificationsend = false;
    }

}

