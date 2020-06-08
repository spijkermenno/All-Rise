package nl.topicus.all_rise.utility;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

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
        System.out.println(fileData);
        return !fileData.equals("{}");
    }

    public Employee getUserData() {
        try {
            FileReader fr = new FileReader();
            if (checkIfUserLoggedIn(fr)) {
                JSONObject j = getUserDataFromLocalStorage(fr);

                return new Employee(j.getInt("id"), j.getInt("department_id"), j.getString("name"), j.getString("surname"), j.getString("activationCode"), j.getBoolean("verified"));
            } else {
                throw new InterruptedIOException("User not signed in.");
            }
        } catch (InterruptedIOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
