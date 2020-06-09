package nl.topicus.all_rise.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.all_rise.R;
import nl.topicus.all_rise.model.Workout;

public class WorkoutListAdapter extends ArrayAdapter<Workout> {

    public WorkoutListAdapter(Activity context, ArrayList<Workout> workouts) {
        super(context, 0, workouts);
    }

    public View getView(int position, View view, ViewGroup parent) {
        Workout workout = getItem(position);

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_row, parent, false);
        }

        System.out.println(workout.toString());

        //this code gets references to objects in the listview_row.xml file
        TextView workoutTextField = (TextView) view.findViewById(R.id.workoutName);

        //this code sets the values of the objects to values from the arrays
        workoutTextField.setText(workout.getExercise().getName());

        return view;
    }
}
