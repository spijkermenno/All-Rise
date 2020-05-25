package nl.topicus.all_rise.activity.Authentication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.lunchmaster.lunchapp.R;
import com.lunchmaster.lunchapp.activity.MainActivity;
import com.lunchmaster.lunchapp.data.DataProvider;
import com.lunchmaster.lunchapp.data.FileReader;
import com.lunchmaster.lunchapp.data.response.JsonObjectResponse;

import org.json.JSONException;
import org.json.JSONObject;

public class SignInActivity extends AppCompatActivity {
    private final String LOCALSTORAGEFILENAME = "storage.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setting the contentView to the SignInActivity Class.
        setContentView(R.layout.activity_sign_in);

        // instatiating the intent.
        Intent intent = getIntent();


        // Hiding the menu bar at the top of the view.
        getSupportActionBar().hide();

        // get extra variables passed from previous activity.
        final JSONObject USERDATA = getExtra();

        // recieving the given data from the previous activity.
        String invitecode = null;
        try {
            invitecode = USERDATA.get("invitecode").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // connecting the user firstname input field to the userFirstName variable.
        // declaring it Final as it is not supposed to change.
        final EditText user_username = findViewById(R.id.user_username);

        // connecting the user lastname input field to the userLastName variable.
        // declaring it Final as it is not supposed to change.
        final EditText user_email = findViewById(R.id.user_email);

        // connecting the user credential submit button to the userCredentialsSubmitButton variable.
        // declaring it Final as it is not supposed to change.
        final Button userCredentialsSubmitButton = findViewById(R.id.user_credentials_submit);

        // connecting the credentials help textview to the credentialsHelp variable.
        // declaring it Final as it is not supposed to change.
        final TextView credentialsHelp = findViewById(R.id.credentials_help);
        credentialsHelp.setTextColor(Color.RED);

        userCredentialsSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                credentialsHelp.setText("");

                String username = user_username.getText().toString();
                String email = user_email.getText().toString();

                if (checkCredentials(username, email)) {

                    if (!isValidEmail(email)) {
                        credentialsHelp.setText(R.string.email_not_valid);
                    }
                    else {
                        try {
                            uploadCredentials(USERDATA.getString("user_id"), username, email, USERDATA.getString("invitecode"), USERDATA.getString("id"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    credentialsHelp.setText(R.string.filledCredentialsInvalid);
                }
            }
        });
    }

    public static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    /**
     * @param username
     * @param email
     * @return boolean
     */
    private boolean checkCredentials(String username, String email) {
        boolean proceed = true;
        if (username.isEmpty()) {
            proceed = false;
        }

        // email.
        if (email.isEmpty()) {
            // TODO: regex email.
            proceed = false;
        }

        return proceed;
    }


    /**
     * @param userid
     * @param username
     * @param email
     */
    private void uploadCredentials(final String userid, String username, String email, final String invitecode, final String invitecodeId) {
        final DataProvider dp = new DataProvider(getApplicationContext());

        final JSONObject userdata = new JSONObject();
        try {
            userdata.put("username", username);
            userdata.put("email", email);
            userdata.put("id", userid);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // RETURN NEW CREATED USER ID.
        dp.customObjectRequest(Request.Method.PUT, "/users/" + userid, userdata, new JsonObjectResponse() {
            @Override
            public void response(JSONObject data) {
                JSONObject invitecodeData = new JSONObject();
                try {
                    invitecodeData.put("id", invitecodeId);
                    invitecodeData.put("active", "1");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                dp.customObjectRequest(Request.Method.PUT, "/invitecodes/" + invitecode, invitecodeData, new JsonObjectResponse() {
                    @Override
                    public void response(JSONObject data) {
                        FileReader fr = new FileReader();
                        fr.create(SignInActivity.this, LOCALSTORAGEFILENAME, userdata.toString());

                        Intent overviewIntent = new Intent(SignInActivity.this, MainActivity.class);
                        overviewIntent.putExtra("userId", userid);
                        startActivity(overviewIntent);
                    }

                    @Override
                    public void error(VolleyError error) {
                        error.printStackTrace();
                    }
                });
            }

            @Override
            public void error(VolleyError error) {
                error.printStackTrace();
            }
        });
    }

    /**
     * @return JSONObject
     */
    private JSONObject getExtra() {
        Bundle extras = getIntent().getExtras();
        String newString = extras.getString("user_credentials");

        if (newString != null) {
            try {
                return new JSONObject(newString);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
