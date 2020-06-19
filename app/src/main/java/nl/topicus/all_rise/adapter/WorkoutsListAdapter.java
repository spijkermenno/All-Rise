package nl.topicus.all_rise.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import nl.topicus.all_rise.R;
import nl.topicus.all_rise.activity.Workout.WorkoutActivity;
import nl.topicus.all_rise.activity.Workout.WorkoutsActivity;
import nl.topicus.all_rise.model.Exercise;
import nl.topicus.all_rise.model.Workout;

public class WorkoutsListAdapter extends ArrayAdapter<Exercise> {
    WorkoutsActivity w;

    public WorkoutsListAdapter(Activity context, ArrayList<Exercise> exercises, WorkoutsActivity w) {
        super(context, 0, exercises);
        this.w = w;
    }

    public View getView(int position, View view, final ViewGroup parent) {
        final Exercise exercise = getItem(position);

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_row, parent, false);
        }

        //this code gets references to objects in the listview_row.xml file
        TextView workoutTextField = (TextView) view.findViewById(R.id.workoutName);

        //this code sets the values of the objects to values from the arrays

        workoutTextField.setText(exercise.getName());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                w.openWorkoutIntent(exercise);
            }
        });

        return view;
    }
}
