package nl.topicus.all_rise.data;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import nl.topicus.all_rise.activity.MainActivity;
import nl.topicus.all_rise.data.response.ArrayListResponse;
import nl.topicus.all_rise.data.response.DepartmentResponse;
import nl.topicus.all_rise.data.response.ExerciseResponse;
import nl.topicus.all_rise.data.response.WorkoutResponse;
import nl.topicus.all_rise.data.response.JsonArrayResponse;
import nl.topicus.all_rise.data.response.JsonObjectResponse;
import nl.topicus.all_rise.data.response.ProviderResponse;
import nl.topicus.all_rise.data.response.EmployeeResponse;
import nl.topicus.all_rise.model.Department;
import nl.topicus.all_rise.model.Exercise;
import nl.topicus.all_rise.model.Workout;
import nl.topicus.all_rise.model.Employee;
import nl.topicus.all_rise.utility.Data;
import nl.topicus.all_rise.utility.Print;

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

    public static final String GET_EXERCISE = "GET_EXERCISE";
    public static final String GET_EXERCISES = "GET_EXERCISES";

    public static final String GET_POINTS_DAILY = "GET_POINTS_DAILY";
    public static final String GET_POINTS_WEEKLY = "GET_POINTS_WEEKLY";
    public static final String GET_POINTS_MONTHLY = "GET_POINTS_MONTHLY";
    public static final String GET_SETTINGS = "GET_SETTINGS";

    public static final String GET_WORKOUTS_DAILY = "GET_WORKOUTS_DAILY";
    public static final String GET_WORKOUTS_WEEKLY = "GET_WORKOUTS_WEEKLY";
    public static final String GET_WORKOUTS_MONTHLY = "GET_WORKOUTS_MONTHLY";
    public static final String GET_WORKOUTS_TOTAL = "GET_WORKOUTS_TOTAL";


    public static final String GET_WORKOUT = "GET_WORKOUT";
    public static final String GET_WORKOUTS = "GET_WORKOUTS";

    public static final String GET_DEPARTMENT = "GET_DEPARTMENT";
    public static final String GET_DEPARTMENTS = "GET_DEPARTMENTS";

    public static final String GET_INVITECODE = "GET_INVITECODE";

    public static final String POST_WORKOUT = "POST_WORKOUT";
    public static final String POST_HISTORY = "POST_HISTORY";

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
        boolean postReq = false;

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

            case GET_WORKOUT:
                URL = API + "/workouts/" + id;
                objectRequest = true;
                break;

            case GET_WORKOUTS:
                URL = API + "/workouts/exercises/";
                objectRequest = true;
                break;

            case GET_EXERCISE:
                URL = API + "/exercises/" + id;
                objectRequest = true;
                break;

            case GET_EXERCISES:
                URL = API + "/exercises/";
                objectRequest = true;
                break;

            case GET_WORKOUTS_DAILY:
                URL = API + "/employees/" + id + "/workouts/day";
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

            case GET_WORKOUTS_TOTAL:
                URL = API + "/employees/" + id + "/workouts";
                objectRequest = true;
                break;

            case GET_SETTINGS:
                URL = API + "/settings";
                objectRequest = true;
                break;

            case POST_WORKOUT:
                // duration
                // points
                // exercise_id

                URL = API + "/workouts";
                postReq = true;
                break;

            case POST_HISTORY:
                // workout_id
                // employee_id

                URL = API + "/histories";
                postReq = true;
                break;

        }
        JSONObject jsonObject = null;
        if (parameters != null) {
            jsonObject = new JSONObject(parameters);
        }

        if (objectRequest) {
            objectRequest(action, URL, jsonObject, providerResponse);
        } else if (postReq) {
            System.out.println(URL);
            postRequest(action, URL, jsonObject, providerResponse);
        } else {
            arrayRequest(action, URL, jsonObject, providerResponse);
        }
    }

    private void postRequest(final String action, final String URL, final JSONObject parameters,
            final ProviderResponse providerResponse) {
        System.out.println(parameters);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            switch (action) {
                                case POST_WORKOUT:
                                case POST_HISTORY:
                                    JsonObjectResponse jsonObjectResponse =
                                            (JsonObjectResponse) providerResponse;
                                    jsonObjectResponse.response(response);

                                    break;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
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

                params.put("verify", data.getUserData().getActivationCode());

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
        ;
    }

    private void objectRequest(final String action, final String URL, final JSONObject parameters,
            final ProviderResponse providerResponse) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            switch (action) {
                                case GET_EMPLOYEE:
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

                                case GET_WORKOUT:
                                    final WorkoutResponse workoutResponse =
                                            (WorkoutResponse) providerResponse;

                                    JSONObject workoutData = new JSONArray(
                                            response.get("data").toString()).getJSONObject(0);
                                    final Workout workout = new Workout(
                                            workoutData.getInt("ID"),
                                            workoutData.getInt("Exercise_ID"),
                                            workoutData.getInt("Duration"),
                                            workoutData.getInt("Points")
                                    );

                                    request(GET_EXERCISE, "" + workout.getExercise_id(), null,
                                            new ExerciseResponse() {
                                                @Override
                                                public void response(Exercise exercise) {
                                                    workout.setExercise(exercise);
                                                    workoutResponse.response(workout);
                                                }

                                                @Override
                                                public void error(VolleyError error) {
                                                    error.printStackTrace();
                                                }
                                            });

                                    break;

                                case GET_WORKOUTS:
                                    ArrayListResponse workoutsResponse =
                                            (ArrayListResponse) providerResponse;

                                    JSONArray s = new JSONArray(response.getString("data"));

                                    ArrayList<Workout> workouts = new ArrayList<>();

                                    System.out.println(s);

                                    for (int i = 0; i < s.length(); i++) {
                                        Workout w = new Workout(
                                                s.getJSONObject(i).getInt("ID"),
                                                s.getJSONObject(i).getInt("Exercise_ID"),
                                                s.getJSONObject(i).getInt("Duration"),
                                                s.getJSONObject(i).getInt("Points")
                                        );

                                        w.setExercise(
                                                new Exercise(
                                                        w.getExercise_id(),
                                                        s.getJSONObject(i).getInt(
                                                                "Exercise_Type_ID"),
                                                        s.getJSONObject(i).getString("Name"),
                                                        s.getJSONObject(i).getString("Description")
                                                ));

                                        workouts.add(w);
                                    }

                                    workoutsResponse.response(workouts);
                                    break;

                                case GET_EXERCISE:
                                    ExerciseResponse exerciseResponse =
                                            (ExerciseResponse) providerResponse;

                                    JSONObject data = new JSONArray(
                                            response.getString("data")).getJSONObject(0);

                                    Exercise w;


                                    try {
                                        System.out.println(data.get("Multiplier"));

                                        w = new Exercise(
                                                data.getInt("ID"),
                                                data.getInt("Exercise_Type_ID"),
                                                data.getString("Name"),
                                                data.getString("Description"),
                                                data.getInt("Multiplier")
                                        );
                                    } catch (Exception e){
                                        w = new Exercise(
                                                data.getInt("ID"),
                                                data.getInt("Exercise_Type_ID"),
                                                data.getString("Name"),
                                                data.getString("Description")
                                        );
                                    };

                                    exerciseResponse.response(w);
                                    break;

                                case GET_EXERCISES:
                                    ArrayListResponse exercisesResponse =
                                            (ArrayListResponse) providerResponse;

                                    JSONArray arrayData = new JSONArray(response.getString("data"));

                                    ArrayList<Exercise> exercises = new ArrayList<>();

                                    for (int i = 0; i < arrayData.length(); i++) {
                                        Exercise e = new Exercise(
                                                arrayData.getJSONObject(i).getInt(
                                                        "ID"),
                                                arrayData.getJSONObject(i).getInt(
                                                        "Exercise_Type_ID"),
                                                arrayData.getJSONObject(i).getString("Name"),
                                                arrayData.getJSONObject(i).getString("Description")
                                        );
                                        exercises.add(e);
                                    }

                                    exercisesResponse.response(exercises);
                                    break;

                                case GET_POINTS_DAILY:
                                case GET_POINTS_MONTHLY:
                                case GET_POINTS_WEEKLY:
                                    JsonObjectResponse dailyResponse =
                                            (JsonObjectResponse) providerResponse;
                                    dailyResponse.response(response);
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

                                case GET_WORKOUTS_TOTAL:
                                    JsonObjectResponse totalWOResponse =
                                            (JsonObjectResponse) providerResponse;
                                    totalWOResponse.response(response);

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
                        if (error.networkResponse != null) {
                            if (error.networkResponse.statusCode == 400) {
                                System.out.println("BAD REQUEST 400");
                            }
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

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URL, null,
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
                                                        response.getJSONObject(i).getString(
                                                                "Firstname"),
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