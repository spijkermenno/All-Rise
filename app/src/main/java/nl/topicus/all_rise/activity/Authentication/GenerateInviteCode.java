package nl.topicus.all_rise.activity.Authentication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import nl.topicus.all_rise.R;
import nl.topicus.all_rise.data.DataProvider;
import nl.topicus.all_rise.data.RandomStringGenerator;
import nl.topicus.all_rise.data.response.JsonObjectResponse;

public class GenerateInviteCode extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_generate_invitecode);

        Button generateNewInvitecode = findViewById(R.id.create_invitecode);

        // TODO: Fix groupID
        final String groupid = "1";

        generateNewInvitecode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateNewInvitecode(groupid);
            }
        });
    }

    private void generateNewInvitecode(final String groupid) {
        final String invitecode = RandomStringGenerator.getAlphaNumericString(8);

        final DataProvider provider = new DataProvider(this);
        provider.request(DataProvider.GET_INVITECODE, invitecode, null, new JsonObjectResponse() {
            @Override
            public void error(VolleyError error) {
                // if invitecode doesn't exist, create it.
                try {
                    JSONObject userObj = new JSONObject();
                    userObj.put("username", "null");
                    userObj.put("email", "null");
                    userObj.put("groupid", groupid);

                    provider.customObjectRequest(Request.Method.POST, "/users/", userObj, new JsonObjectResponse() {
                        @Override
                        public void response(JSONObject data) throws JSONException {
                            JSONObject invitecodeOjb = new JSONObject();
                            invitecodeOjb.put("invitecode", invitecode);
                            invitecodeOjb.put("userid", data.getString("id"));

                            provider.customObjectRequest(Request.Method.POST, "/invitecode/", invitecodeOjb, new JsonObjectResponse() {
                                @Override
                                public void error(VolleyError error) {
                                    error.printStackTrace();
                                }

                                @Override
                                public void response(JSONObject data) throws JSONException {
                                    System.out.println(data);
                                    Intent overviewIntent = new Intent(GenerateInviteCode.this, DisplayString.class);
                                    overviewIntent.putExtra("string", invitecode);
                                    startActivity(overviewIntent);
                                }
                            });
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

            @Override
            public void response(JSONObject data) throws JSONException {
                generateNewInvitecode(groupid);
            }
        });

    }
}
