package nl.topicus.all_rise.data;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import nl.topicus.all_rise.data.response.ArrayListResponse;
import nl.topicus.all_rise.data.response.DepartmentResponse;
import nl.topicus.all_rise.data.response.WorkoutResponse;
import nl.topicus.all_rise.data.response.JsonArrayResponse;
import nl.topicus.all_rise.data.response.JsonObjectResponse;
import nl.topicus.all_rise.data.response.ProviderResponse;
import nl.topicus.all_rise.data.response.EmployeeResponse;
import nl.topicus.all_rise.model.Department;
import nl.topicus.all_rise.model.Workout;
import nl.topicus.all_rise.model.Employee;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//TODO Fix Object or Array Nightmare
public class DataProvider {

    private Context ctx;

    //TODO: Add API url
    private final static String API = "API URL HERE";

    //Actions
    public static final String GET_EMPLOYEE = "GET_EMPLOYEE";
    public static final String GET_EMPLOYEES = "GET_EMPLOYEES";

    public static final String GET_WORKOUT = "GET_WORKOUT";
    public static final String GET_WORKOUTS = "GET_WORKOUTS";

    public static final String GET_DEPARTMENT = "GET_DEPARTMENT";
    public static final String GET_DEPARTMENTS = "GET_DEPARTMENTS";

    public static final String GET_INVITECODE = "GET_INVITECODE";

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
        JsonObjectRequest jsonObjectRequest = null;
        String URL = "";

        boolean objectRequest = false;

        switch (action) {

            case GET_EMPLOYEE:
                URL = API + "/users/" + id;
                objectRequest = true;
                break;

            case GET_EMPLOYEES:
                URL = API + "/users/";
                break;

            case GET_WORKOUT:
                URL = API + "/items/" + id;
                objectRequest = true;
                break;

            case GET_WORKOUTS:
                URL = API + "/items/";
                break;

            case GET_DEPARTMENT:
                URL = API + "/groups/" + id;
                objectRequest = true;
                break;

            case GET_DEPARTMENTS:
                URL = API + "/groups/";
                break;

            case GET_INVITECODE:
                URL = API + "/invitecodes/" + id;
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

    private void objectRequest(final String action, String URL, final JSONObject parameters,
            final ProviderResponse providerResponse) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            switch (action) {
                                case GET_EMPLOYEE:
                                    EmployeeResponse employeeResponse =
                                            (EmployeeResponse) providerResponse;
                                    Employee employee = new Employee(
                                            response.getInt("id"),
                                            response.getInt("department_id"),
                                            response.getString("name"),
                                            response.getString("surname"),
                                            response.getString("activation_code")
                                    );
                                    employeeResponse.response(employee);
                                    break;

                                case GET_WORKOUT:
                                    WorkoutResponse workoutResponse =
                                            (WorkoutResponse) providerResponse;
                                    Workout workout = new Workout(
                                            response.getInt("id"),
                                            response.getString("name"),
                                            response.getString("description")
                                    );
                                    workoutResponse.response(workout);
                                    break;

                                case GET_DEPARTMENT:
                                    DepartmentResponse departmentResponse =
                                            (DepartmentResponse) providerResponse;
                                    Department department = new Department(
                                            response.getInt("id"),
                                            response.getInt("officeId"),
                                            response.getString("name"),
                                            response.getString("description")
                                    );
                                    departmentResponse.response(department);
                                    break;

                                case GET_INVITECODE:
                                    JsonObjectResponse jsonObjectResponse =
                                            (JsonObjectResponse) providerResponse;
                                    jsonObjectResponse.response(response);
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
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("secretcode", "24091999");
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

    private void arrayRequest(final String action, String URL, final JSONObject parameters,
            final ProviderResponse providerResponse) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(final JSONArray response) {
                        try {
                            final ArrayListResponse arrayListResponse =
                                    (ArrayListResponse) providerResponse;
                            switch (action) {
                                case GET_EMPLOYEES:
                                    ArrayList<Employee> employees = new ArrayList<>();
                                    for (int i = 0; i < response.length(); i++) {
                                        employees.add(
                                                new Employee(
                                                        response.getJSONObject(i).getInt("id"),
                                                        response.getJSONObject(i).getInt("department_id"),
                                                        response.getJSONObject(i).getString("name"),
                                                        response.getJSONObject(i).getString("surname"),
                                                        response.getJSONObject(i).getString("activation_code")
                                                )
                                        );
                                    }
                                    arrayListResponse.response(employees);
                                    break;

                                case GET_WORKOUTS:
                                    ArrayList<Workout> workouts = new ArrayList<>();
                                    for (int i = 0; i < response.length(); i++) {
                                        JSONObject jsonObject = response.getJSONObject(i);
                                        workouts.add(
                                                new Workout(
                                                        jsonObject.getInt("id"),
                                                        jsonObject.getString("name"),
                                                        jsonObject.getString("description")
                                                )
                                        );
                                    }
                                    arrayListResponse.response(workouts);
                                    break;

                                case GET_DEPARTMENTS:
                                    ArrayList<Department> departments = new ArrayList<>();
                                    for (int i = 0; i < response.length(); i++) {
                                        JSONObject jsonObject = response.getJSONObject(i);
                                        departments.add(
                                                new Department(
                                                        jsonObject.getInt("id"),
                                                        jsonObject.getInt("department_id"),
                                                        jsonObject.getString("name"),
                                                        jsonObject.getString("description")
                                                )
                                        );
                                    }
                                    arrayListResponse.response(departments);
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
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("secretcode", "24091999");
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

    public void customObjectRequest(int method, String URL, final JSONObject parameters,
            final JsonObjectResponse jsonObjectResponse) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (method, API + URL, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            jsonObjectResponse.response(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        jsonObjectResponse.error(error);
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("secretcode", "24091999");
                return params;
            }

            @Override
            public byte[] getBody() {
                String requestBody = parameters.toString();
                try {
                    return requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    VolleyLog.wtf(
                            "Unsupported Encoding while trying to get the bytes of %s using %s",
                            requestBody, "utf-8");
                    return null;
                }
            }
        };
        NetworkSingleton.getInstance(ctx).addToRequestQueue(jsonObjectRequest);
    }

    public void customArrayRequest(int method, String URL, final JSONObject parameters,
            final JsonArrayResponse jsonArrayResponse) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (method, API + URL, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        jsonArrayResponse.response(response);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        jsonArrayResponse.error(error);
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("secretcode", "24091999");
                params.put("Content-Type", "application/json");
                return params;
            }

            @Override
            public byte[] getBody() {
                String requestBody = parameters.toString();
                try {
                    return requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    VolleyLog.wtf(
                            "Unsupported Encoding while trying to get the bytes of %s using %s",
                            requestBody, "utf-8");
                    return null;
                }
            }
        };
        NetworkSingleton.getInstance(ctx).addToRequestQueue(jsonArrayRequest);
    }
}