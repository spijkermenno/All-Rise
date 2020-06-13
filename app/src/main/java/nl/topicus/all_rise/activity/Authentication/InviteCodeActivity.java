package nl.topicus.all_rise.activity.Authentication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import nl.topicus.all_rise.activity.MainActivity;
import nl.topicus.all_rise.R;
import nl.topicus.all_rise.data.DataProvider;
import nl.topicus.all_rise.data.FileReader;
import nl.topicus.all_rise.data.response.EmployeeResponse;
import nl.topicus.all_rise.data.response.JsonObjectResponse;
import nl.topicus.all_rise.model.Employee;
import nl.topicus.all_rise.utility.Print;

public class InviteCodeActivity extends AppCompatActivity {
    // Filename local storage
    private final String LOCALSTORAGEFILENAME = "storage.json";
    EditText inviteCodeInputField;
    Button inviteCodeSubmitButton;
    TextView inviteCodeHelpText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // setting the contentView to the inviteCodeActivity Class.
        setContentView(R.layout.activity_invite_code);

        // Hiding the menu bar at the top of the view.
        getSupportActionBar().hide();

        // connecting the invite code input field to the inviteCodeInputField variable.
        // declaring it Final as it is not supposed to change.
        inviteCodeInputField = findViewById(R.id.invite_code_input);

        // connecting the invite code submit button to the inviteCodeSubmitButton variable.
        // declaring it Final as it is not supposed to change.
        inviteCodeSubmitButton = findViewById(R.id.invite_code_submit);

        // connecting the invite code submit button to the inviteCodeSubmitButton variable.
        // declaring it Final as it is not supposed to change.
        inviteCodeHelpText = findViewById(R.id.invite_code_help);

        // Connecting a clickListener to the invite code submit button.
        inviteCodeSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Making sure the inviteCodeHelpText is empty when user submits new data.
                inviteCodeHelpText.setText(null);

                final String submittedInviteCode = inviteCodeInputField.getText().toString();

                final DataProvider dp = new DataProvider(getApplicationContext());

                if (submittedInviteCode.length() == 8) {
                    // TODO: REMOVE BACKDOOR

                    if (submittedInviteCode.equals("all-rise")) {
                        Print.echo("BACKDOOR ACIVATED", Color.RED);

                        try {
                            JSONObject obj = new JSONObject();

                            obj.put("id", 0);
                            obj.put("department_id", 0);
                            obj.put("name", "Backdoor");
                            obj.put("surname", "Please fix");
                            obj.put("activationCode", "all-rise");

                            // Write user data to local file.
                            FileReader fr = new FileReader();
                            fr.create(
                                    InviteCodeActivity.this,
                                    LOCALSTORAGEFILENAME,
                                    obj.toString());

                            Intent overviewIntent = new Intent(InviteCodeActivity.this,
                                    MainActivity.class);
                            overviewIntent.putExtra("userId", 0);
                            startActivity(overviewIntent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return;
                    }

                    // API CALL
                    // Requesting user-data from the API
                    dp.request(DataProvider.GET_VERIFIED, submittedInviteCode, null,
                            new JsonObjectResponse() {

                                @Override
                                public void response(JSONObject data) {
                                    System.out.println(data);
                                    requestUserDataFormAPI(submittedInviteCode, dp);
                                }

                                @Override
                                public void error(VolleyError error) {
                                    try {
                                        String body = new String(error.networkResponse.data,
                                                StandardCharsets.UTF_8);
                                        JSONObject o = new JSONObject(body);

                                        System.out.println("USER IS ALLREADY VERIFIED");
                                        if (o.get("error").equals("Code already verified")) {
                                            System.out.println("REQUESTING URER DATA");
                                            requestUserDataFormAPI(submittedInviteCode, dp);
                                        }
                                    } catch (JSONException e) {
                                        System.out.println("Error in request...");
                                        System.out.println(e.getMessage());
                                    }
                                }
                            });

                } else if (submittedInviteCode.length() > 0 && submittedInviteCode.length() < 8) {
                    inviteCodeHelpText.setTextColor(Color.RED);
                    inviteCodeHelpText.setText(R.string.code_short);

                } else if (submittedInviteCode.length() > 8) {
                    inviteCodeHelpText.setTextColor(Color.RED);
                    inviteCodeHelpText.setText(R.string.code_long);

                } else {
                    inviteCodeHelpText.setTextColor(Color.RED);
                    inviteCodeHelpText.setText(R.string.code_empty);

                }
            }
        });
    }

    void requestUserDataFormAPI(String code, DataProvider dp) {
        dp.request(DataProvider.GET_EMPLOYEE_BY_CODE,
                code, null,
                new EmployeeResponse() {

                    @Override
                    public void response(Employee data) {
                        try {
                            if (data != null) {

                                inviteCodeHelpText.setText("");

                                JSONObject obj = new JSONObject();

                                obj.put("id", data.getId());
                                obj.put("department_id", data
                                        .getDepartmentId());
                                obj.put("name", data.getName());
                                obj.put("surname", data.getSurName());
                                obj.put("activationCode", data
                                        .getActivationCode());
                                obj.put("verified", data
                                        .isVerified());

                                System.out.println(obj);

                                // Write user data to local file.
                                FileReader fr = new FileReader();
                                if (fr.create(InviteCodeActivity.this, LOCALSTORAGEFILENAME, obj.toString())) {

                                    // Open new Intent with main screen.
                                    Intent overviewIntent = new Intent(
                                            InviteCodeActivity.this,
                                            MainActivity.class
                                    );
                                    overviewIntent.putExtra("userId", data.getId());
                                    startActivity(overviewIntent);
                                    finish();
                                } else {
                                    System.out.println("bla bla bla");
                                }
                            } else {
                                inviteCodeHelpText.setTextColor
                                        (Color.RED);
                                inviteCodeHelpText.setText(R.string
                                        .code_invalid);
                            }
                        } catch (
                                Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void error(VolleyError error) {
                        error.printStackTrace();
                    }
                });
    }

}
