package nl.topicus.all_rise.data;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import nl.topicus.all_rise.data.response.ArrayListResponse;
import nl.topicus.all_rise.data.response.EmployeeResponse;
import nl.topicus.all_rise.data.response.JsonObjectResponse;
import nl.topicus.all_rise.data.response.ProviderResponse;
import nl.topicus.all_rise.model.Employee;
import nl.topicus.all_rise.utility.Data;

//TODO Fix Object or Array Nightmare
public class DataProvider {

    private Context ctx;

    //TODO: Add API url
    private final static String API = "http://api.tychoengberink.nl:3001/api";

    //Actions
    public static final String GET_VERIFIED = "GET_VERIFIED";
    public static final String GET_EMPLOYEE_BY_CODE = "GET_EMPLOYEE_BY_CODE";
    public static final String GET_EMPLOYEE = "GET_EMPLOYEE";
    public static final String GET_EMPLOYEES = "GET_EMPLOYEES";

    public static final String GET_POINTS_DAILY = "GET_POINTS_DAILY";
    public static final String GET_POINTS_WEEKLY = "GET_POINTS_WEEKLY";
    public static final String GET_POINTS_MONTHLY = "GET_POINTS_MONTHLY";
    public static final String GET_SETTINGS = "GET_SETTINGS";

    public static final String GET_WORKOUTS_DAILY = "GET_WORKOUTS_DAILY";
    public static final String GET_WORKOUTS_WEEKLY = "GET_WORKOUTS_WEEKLY";
    public static final String GET_WORKOUTS_MONTHLY = "GET_WORKOUTS_MONTHLY";

    public static final String GET_WORKOUT = "GET_WORKOUT";
    public static final String GET_WORKOUTS = "GET_WORKOUTS";

    public static final String GET_DEPARTMENT = "GET_DEPARTMENT";
    public static final String GET_DEPARTMENTS = "GET_DEPARTMENTS";

    public static final String GET_INVITECODE = "GET_INVITECODE";

    private String code = "";

    public DataProvider(Context ctx) {
        this.ctx = ctx;
        nl.topicus.all_rise.data.NukeSSLCerts.nuke();
    }

    /**
     * This method makes a request to the webserver.
     *
     * @param action           action to take.
     * @param id               (Optional) enter the id to find.
     * @param parameters       (Optional) Parameters to be send with the request.
     * @param providerResponse The response of the request, should be a subclass of the
     *                         ProviderResponse interface depending on the action.
     */
    public void request(final String action, final String id,
                        final HashMap<String, String> parameters, final ProviderResponse providerResponse) {
        String URL = "";
        this.code = id;

        boolean objectRequest = false;

        switch (action) {

            case GET_VERIFIED:
                URL = API + "/register/" + id;
                objectRequest = true;
                break;

            case GET_EMPLOYEE_BY_CODE:
                URL = API + "/employees/code/" + id;
                objectRequest = true;
                break;

            case GET_EMPLOYEE:
                URL = API + "/employees/" + id;
                objectRequest = true;
                break;

            case GET_EMPLOYEES:
                URL = API + "/employees/";
                break;

            case GET_POINTS_DAILY:
                URL = API + "/departments/" + id + "/points/day";
                objectRequest = true;
                break;

            case GET_POINTS_WEEKLY:
                URL = API + "/departments/" + id + "/points/week";
                objectRequest = true;
                break;

            case GET_POINTS_MONTHLY:
                URL = API + "/departments/" + id + "/points/month";
                objectRequest = true;
                break;

            case GET_WORKOUTS_DAILY:
                URL = API + "/employees/"+ id + "/workouts/day";
                objectRequest = true;
                break;

            case GET_WORKOUTS_WEEKLY:
                URL = API + "/employees/" + id + "/workouts/week";
                objectRequest = true;
                break;

            case GET_WORKOUTS_MONTHLY:
                URL = API + "/employees/" + id + "/workouts/month";
                objectRequest = true;
                break;

            case GET_SETTINGS:
                URL = API + "/settings";
                objectRequest = true;
                break;

        }
        JSONObject jsonObject = null;
        if (parameters != null) {
            jsonObject = new JSONObject(parameters);
        }

        if (objectRequest) {
            objectRequest(action, URL, jsonObject, providerResponse);
        } else {
            arrayRequest(action, URL, jsonObject, providerResponse);
        }
    }

    private void objectRequest(final String action, final String URL, final JSONObject parameters,
            final ProviderResponse providerResponse) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            switch (action) {
                                case GET_EMPLOYEE_BY_CODE:
                                    EmployeeResponse employeeResponse =
                                            (EmployeeResponse) providerResponse;

                                    JSONArray t = new JSONArray(response.get("data").toString());
                                    JSONObject j = t.getJSONObject(0);

                                    Employee employee = new Employee(
                                            j.getInt("ID"),
                                            j.getInt("Department_ID"),
                                            j.getString("Firstname"),
                                            j.getString("Lastname"),
                                            j.getString("Code"),
                                            j.getInt("Verfied") == 1
                                    );
                                    employeeResponse.response(employee);
                                    break;

                                case GET_POINTS_DAILY:
                                    JsonObjectResponse dailyResponse =
                                            (JsonObjectResponse) providerResponse;
                                    dailyResponse.response(response);
                                    break;

                                case GET_POINTS_WEEKLY:
                                    JsonObjectResponse weeklyResponse =
                                            (JsonObjectResponse) providerResponse;
                                    weeklyResponse.response(response);
                                    break;

                                case GET_POINTS_MONTHLY:
                                    JsonObjectResponse monthlyResponse =
                                            (JsonObjectResponse) providerResponse;
                                    monthlyResponse.response(response);
                                    break;

                                case GET_WORKOUTS_DAILY:
                                    JsonObjectResponse dailyWOResponse =
                                            (JsonObjectResponse) providerResponse;
                                    dailyWOResponse.response(response);

                                case GET_WORKOUTS_WEEKLY:
                                    JsonObjectResponse weeklyWOResponse =
                                            (JsonObjectResponse) providerResponse;
                                    weeklyWOResponse.response(response);

                                case GET_WORKOUTS_MONTHLY:
                                    JsonObjectResponse monthlyWOResponse =
                                            (JsonObjectResponse) providerResponse;
                                    monthlyWOResponse.response(response);

                                case GET_SETTINGS:
                                    JsonObjectResponse settingsResponse =
                                            (JsonObjectResponse) providerResponse;
                                    settingsResponse.response(response);
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse.statusCode == 400) {
                            System.out.println("BAD REQUEST 400");
                        }
                        providerResponse.error(error);
                        error.printStackTrace();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                Data data = new Data(ctx);

                if (!URL.contains("/register/") && data.getUserData() == null) {
                    params.put("verify", code);
                } else if (data.getUserData() != null) {
                    params.put("verify", data.getUserData().getActivationCode());
                }

                params.put("Content-Type", "application/json");
                return params;
            }

            @Override
            public byte[] getBody() {
                String requestBody = parameters.toString();
                return requestBody.getBytes(StandardCharsets.UTF_8);
            }
        };
        NetworkSingleton.getInstance(ctx).addToRequestQueue(
                jsonObjectRequest);
    }

    private void arrayRequest(final String action, final String URL, final JSONObject parameters,
            final ProviderResponse providerResponse) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, URL, null,
                        new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            final ArrayListResponse arrayListResponse =
                                    (ArrayListResponse) providerResponse;
                            switch (action) {
                                case GET_EMPLOYEES:
                                    ArrayList<Employee> employees = new ArrayList<>();
                                    for (int i = 0; i < response.length(); i++) {
                                        employees.add(
                                                new Employee(
                                                        response.getJSONObject(i).getInt("ID"),
                                                        response.getJSONObject(i).getInt(
                                                                "Department_ID"),
                                                        response.getJSONObject(i).getString("Firstname"),
                                                        response.getJSONObject(i).getString(
                                                                "Lastname"),
                                                        response.getJSONObject(i).getString(
                                                                "Code"),
                                                        response.getJSONObject(i).getBoolean(
                                                                "Verfied")
                                                )
                                        );
                                    }
                                    arrayListResponse.response(employees);
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        providerResponse.error(error);
                        error.printStackTrace();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                Data data = new Data(ctx);

                if (!URL.contains("/register/") && data.getUserData() == null) {
                    params.put("verify", code);
                } else if (data.getUserData() != null) {
                    params.put("verify", data.getUserData().getActivationCode());
                }

                params.put("Content-Type", "application/json");
                return params;
            }


            @Override
            public byte[] getBody() {
                String requestBody = parameters.toString();
                return requestBody.getBytes(StandardCharsets.UTF_8);
            }
        };
        NetworkSingleton.getInstance(ctx).addToRequestQueue(jsonArrayRequest);
    }


}