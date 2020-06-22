package nl.topicus.all_rise.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.io.InterruptedIOException;

import nl.topicus.all_rise.R;
import nl.topicus.all_rise.activity.Authentication.InviteCodeActivity;
import nl.topicus.all_rise.activity.Workout.WorkoutsActivity;
import nl.topicus.all_rise.data.DataProvider;
import nl.topicus.all_rise.data.FileReader;
import nl.topicus.all_rise.data.response.EmployeeResponse;
import nl.topicus.all_rise.model.Employee;
import nl.topicus.all_rise.service.NotificationService;
import nl.topicus.all_rise.utility.Data;

public class MainActivity extends AppCompatActivity {
    private final String LOCALSTORAGEFILENAME = "storage.json";

    private Context context;
    public JSONObject USERDATA;
    TextView tvWelcome;
    Button btnWorkout, btnStatistics, btnHistory, btnZenmode;


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
        final Data data = new Data(this);

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

                                    System.out.println(obj);

                                    FileReader fr = new FileReader();

                                    if (employee.isVerified()) {
                                        System.out.println(employee);

                                        // Write user data to local file.
                                        fr.create(ctx, LOCALSTORAGEFILENAME, obj.toString());
                                    } else {
                                        System.out.println("SIGN OFF");
                                        fr.clearFile(ctx, LOCALSTORAGEFILENAME);

                                        Intent reloadView = new Intent(MainActivity.this,
                                                InviteCodeActivity.class);
                                        startActivity(reloadView);
                                        finish();
                                    }
                                } else {
                                    System.out.println("BLA BLA EMPLOYEE EMPTY");
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

                                Intent reloadView = new Intent(MainActivity.this,
                                        InviteCodeActivity.class);
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

            Intent serviceIntent = new Intent(context, NotificationService.class);
            ContextCompat.startForegroundService(context, serviceIntent);

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
                            WorkoutsActivity.class
                    );
                    startActivity(view);
                }
            });

            btnStatistics = findViewById(R.id.btn_statistics);
            btnStatistics.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), StatisticsActivity.class);
                    intent.putExtra("userID", data.getUserData().getId());
                    startActivity(intent);
                }
            });

            btnHistory = findViewById(R.id.btn_history);
            btnHistory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), HistoryActivity.class);
                    intent.putExtra("userID", data.getUserData().getId());
                    startActivity(intent);
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
                    intent.putExtra("userID", data.getUserData().getId());
                    startActivity(intent);
                }
            });

            Button buttonweek = (Button) findViewById(R.id.button_weekly);

            buttonweek.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), RankingActivity.class);
                    intent.putExtra("filter", 1);
                    intent.putExtra("userID", data.getUserData().getId());
                    startActivity(intent);
                }
            });

            Button buttonmonth = (Button) findViewById(R.id.button_monthly);

            buttonmonth.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), RankingActivity.class);
                    intent.putExtra("filter", 2);
                    intent.putExtra("userID", data.getUserData().getId());
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent serviceIntent = new Intent(this, NotificationService.class);
        stopService(serviceIntent);
    }
}