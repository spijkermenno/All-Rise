package nl.topicus.all_rise.model;

import androidx.annotation.NonNull;

public class Exercise {

    private int id, exerciseTypeId, pointMultiplier;
    private String name, description;

    public Exercise(int id, int exerciseTypeId, String name, String description, int pointMultiplier) {
        this.id = id;
        this.exerciseTypeId = exerciseTypeId;
        this.name = name;
        this.description = description;
        this.pointMultiplier = pointMultiplier;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @NonNull
    @Override
    public String toString() {
        return "ID: " + this.id + " | Name: " + this.name + " | Description: " + this.description;
    }

    public int getMultiplier() {
        return pointMultiplier;
    }
}
