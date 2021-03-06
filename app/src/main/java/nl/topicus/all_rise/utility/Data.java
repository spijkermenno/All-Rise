package nl.topicus.all_rise.utility;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InterruptedIOException;

import nl.topicus.all_rise.data.FileReader;
import nl.topicus.all_rise.model.Employee;

public class Data {
    private Context ctx;

    public Data(Context ctx) {
        this.ctx = ctx;
    }

    public JSONObject getUserDataFromLocalStorage(FileReader fr) throws InterruptedIOException {
        fr.checkIfLocalStorageActivated(ctx, FileReader.LOCALSTORAGEFILENAME);
        String fileData = fr.read(ctx, FileReader.LOCALSTORAGEFILENAME);

        JSONObject fileObject;
        try {
            return new JSONObject(fileData);

        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new java.io.InterruptedIOException("Data couldn't be casted to JSONObject.");
    }

    public boolean checkIfUserLoggedIn(FileReader fr) {
        // check if user is logged in.
        fr.checkIfLocalStorageActivated(ctx, FileReader.LOCALSTORAGEFILENAME);
        String fileData = fr.read(ctx, FileReader.LOCALSTORAGEFILENAME);
        return !fileData.equals("{}");
    }

    public Employee getUserData() {
        try {
            FileReader fr = new FileReader();
            if (checkIfUserLoggedIn(fr)) {
                JSONObject j = getUserDataFromLocalStorage(fr);

                return new Employee(j.getInt("id"), j.getInt("department_id"), j.getString("name"), j.getString("surname"), j.getString("activationCode"), j.getBoolean("verified"));
            } else {
                JSONObject j = getUserDataFromLocalStorage(fr);
                throw new InterruptedIOException("User not signed in. " + j);

            }
        } catch (InterruptedIOException | JSONException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public void initializeWorkoutData() {

    }

    public String getStringSettingFromDb(String key, JSONObject data) throws JSONException {
        String value = null;
        for (int i = 0; i < data.getJSONArray("data").length(); i++) {
            JSONObject obj = data.getJSONArray("data").getJSONObject(i);
            if (obj.getString("Key").equals(key)) {
                value = obj.getString("Value");
            }
        }
        return value;
    }

    public int getIntSettingFromDb(String key, JSONObject data) throws JSONException {
        int value = -1;
        for (int i = 0; i < data.getJSONArray("data").length(); i++) {
            JSONObject obj = data.getJSONArray("data").getJSONObject(i);
            if (obj.getString("Key").equals(key)) {
                value = obj.getInt("Value");
            }
        }
        return value;
    }
}
