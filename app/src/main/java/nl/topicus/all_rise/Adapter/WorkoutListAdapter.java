package nl.topicus.all_rise.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import nl.topicus.all_rise.R;

public class WorkoutListAdapter extends ArrayAdapter {
    private final Activity context;
    private final String[] workoutNames;

    public WorkoutListAdapter(Activity context, String[] workouts) {
        super(context, R.layout.activity_workout, workouts);

        this.context = context;
        this.workoutNames = workouts;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_row, null, true);

        //this code gets references to objects in the listview_row.xml file
        TextView workoutTextField = (TextView) rowView.findViewById(R.id.workoutName);

        //this code sets the values of the objects to values from the arrays
        workoutTextField.setText(workoutNames[position]);

        return rowView;

    }

    ;
}
