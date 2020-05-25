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
import com.lunchmaster.lunchapp.R;
import com.lunchmaster.lunchapp.activity.MainActivity;
import com.lunchmaster.lunchapp.data.DataProvider;
import com.lunchmaster.lunchapp.data.FileReader;
import com.lunchmaster.lunchapp.data.response.JsonObjectResponse;
import com.lunchmaster.lunchapp.data.response.UserResponse;
import com.lunchmaster.lunchapp.model.User;

import org.json.JSONException;
import org.json.JSONObject;

public class InviteCodeActivity extends AppCompatActivity {
    private final String LOCALSTORAGEFILENAME = "storage.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // setting the contentView to the inviteCodeActivity Class.
        setContentView(R.layout.activity_invite_code);


        // Hiding the menu bar at the top of the view.
        getSupportActionBar().hide();

        // connecting the invite code input field to the inviteCodeInputField variable.
        // declaring it Final as it is not supposed to change.
        final EditText inviteCodeInputField = findViewById(R.id.invite_code_input);

        // connecting the invite code submit button to the inviteCodeSubmitButton variable.
        // declaring it Final as it is not supposed to change.
        final Button inviteCodeSubmitButton = findViewById(R.id.invite_code_submit);

        // connecting the invite code submit button to the inviteCodeSubmitButton variable.
        // declaring it Final as it is not supposed to change.
        final TextView inviteCodeHelpText = findViewById(R.id.invite_code_help);

        // Connecting a clickListener to the invite code submit button.
        inviteCodeSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inviteCodeHelpText.setText(null);

                String submittedInviteCode = inviteCodeInputField.getText().toString();

                DataProvider dp = new DataProvider(getApplicationContext());

                if (submittedInviteCode.length() == 8) {
                    dp.request(DataProvider.GET_INVITECODE, submittedInviteCode, null, new JsonObjectResponse() {
                        @Override
                        public void response(JSONObject data) {

                            try {
                                if (data != null) {
                                    inviteCodeHelpText.setText("");
                                    if (!data.getString("active").equals("1")) {
                                        Intent menuIntent = new Intent(InviteCodeActivity.this, SignInActivity.class);
                                        menuIntent.putExtra("user_credentials", data.toString());
                                        startActivity(menuIntent);
                                    } else {
                                        try {
                                            DataProvider dp = new DataProvider(InviteCodeActivity.this);
                                            dp.request(DataProvider.GET_USER, data.getString("user_id"), null, new UserResponse() {
                                                @Override
                                                public void response(User data) {
                                                    try {
                                                        JSONObject obj = new JSONObject();

                                                        obj.put("id", data.getId());
                                                        obj.put("username", data.getUsername());
                                                        obj.put("email", data.getEmail());

                                                        FileReader fr = new FileReader();
                                                        fr.create(InviteCodeActivity.this, LOCALSTORAGEFILENAME, obj.toString());

                                                        Intent overviewIntent = new Intent(InviteCodeActivity.this, MainActivity.class);
                                                        overviewIntent.putExtra("userId", data.getId());
                                                        startActivity(overviewIntent);
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }

                                                @Override
                                                public void error(VolleyError error) {
                                                    error.printStackTrace();
                                                }
                                            });

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                } else {
                                    inviteCodeHelpText.setTextColor(Color.RED);
                                    inviteCodeHelpText.setText(R.string.code_invalid);
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
                } else if (submittedInviteCode.length() > 0 && submittedInviteCode.length() < 8) {
                    inviteCodeHelpText.setTextColor(Color.RED);
                    inviteCodeHelpText.setText(R.string.code_short);
                } else {
                    inviteCodeHelpText.setTextColor(Color.RED);
                    inviteCodeHelpText.setText(R.string.code_empty);
                }
            }
        });
    }
}
