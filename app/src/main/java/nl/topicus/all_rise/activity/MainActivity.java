package nl.topicus.all_rise.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import org.json.JSONObject;

import java.io.InterruptedIOException;

import nl.topicus.all_rise.R;
import nl.topicus.all_rise.activity.Authentication.InviteCodeActivity;
import nl.topicus.all_rise.data.FileReader;

public class MainActivity extends AppCompatActivity {

    private Context context;
    private JSONObject USERDATA;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        context = MainActivity.this;

        // instatiate filereader
        FileReader fr = new FileReader();

        checkIfUserLoggedIn(fr);

        try {
            USERDATA = getUserDataFromLocalStorage(fr);
        } catch (InterruptedIOException e) {
            e.printStackTrace();
        }

        if (USERDATA == null || USERDATA.toString().equals("{}")) {
            System.out.println("====== SIGN IN ======");
            Intent overviewIntent = new Intent(MainActivity.this, InviteCodeActivity.class);
            startActivity(overviewIntent);
        }
    }

    protected boolean checkIfUserLoggedIn(FileReader fr) {
        // check if user is logged in.
        fr.checkIfLocalStorageActivated(this, FileReader.LOCALSTORAGEFILENAME);
        String fileData = fr.read(this, FileReader.LOCALSTORAGEFILENAME);
        return !fileData.equals("{}");
    }

    protected JSONObject getUserDataFromLocalStorage(FileReader fr) throws InterruptedIOException {
        fr.checkIfLocalStorageActivated(this, FileReader.LOCALSTORAGEFILENAME);
        String fileData = fr.read(this, FileReader.LOCALSTORAGEFILENAME);

        JSONObject fileObject;
        try {
            return new JSONObject(fileData);

        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new java.io.InterruptedIOException("Data couldn't be casted to JSONObject.");
    }
}
