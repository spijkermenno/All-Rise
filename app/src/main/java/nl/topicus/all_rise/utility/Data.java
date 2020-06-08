package nl.topicus.all_rise.utility;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.InterruptedIOException;

import nl.topicus.all_rise.data.FileReader;

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

    public JSONObject getUserData() {
        try {
            FileReader fr = new FileReader();
            if (checkIfUserLoggedIn(fr)) {
                return getUserDataFromLocalStorage(fr);
            } else {
                throw new InterruptedIOException("User not signed in.");
            }
        } catch (InterruptedIOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
