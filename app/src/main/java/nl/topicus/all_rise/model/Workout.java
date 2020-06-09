package nl.topicus.all_rise.model;

import android.content.Context;

import com.android.volley.VolleyError;

import java.io.Serializable;

import nl.topicus.all_rise.data.DataProvider;
import nl.topicus.all_rise.data.response.ExerciseResponse;

public class Workout implements Serializable {

    private int id, exercise_id, timeInSeconds, points;

    private Exercise exercise = null;

    public Workout(int id, int exercise_id, int timeInSeconds, int points) {
        this.id = id;
        this.exercise_id = exercise_id;
        this.timeInSeconds = timeInSeconds;
        this.points = points;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTimeInSeconds() {
        return timeInSeconds;
    }

    public void setTimeInSeconds(int timeInSeconds) {
        this.timeInSeconds = timeInSeconds;
    }

    @Override
    public String toString() {
        return "ID: " + this.id + "\n" +
                "Points: " + this.points + "\n" +
                "Time: " + this.timeInSeconds;
    }

    public Exercise getExercise() {
        return exercise;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }

    public Boolean retrieveExercise(Context ctx) {
        DataProvider dp = new DataProvider(ctx);
        final Workout w = this;

        dp.request(DataProvider.GET_EXERCISE, ("" + this.exercise_id), null,
                new ExerciseResponse() {
                    @Override
                    public void response(Exercise exercise) {
                        setExercise(exercise);
                    }

                    @Override
                    public void error(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        while (this.exercise != null) {
            return true;
        }
        return false;
    }
}
