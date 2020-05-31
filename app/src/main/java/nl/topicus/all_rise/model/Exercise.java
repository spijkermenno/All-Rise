package nl.topicus.all_rise.model;

public class Exercise {

    private int id, exerciseTypeId;
    private String name, description;

    public Exercise(int id, int exerciseTypeId, String name, String description) {
        this.id = id;
        this.exerciseTypeId = exerciseTypeId;
        this.name = name;
        this.description = description;
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

    public int getExerciseTypeId() {
        return exerciseTypeId;
    }

    public void setExerciseTypeId(int exerciseTypeId) {
        this.exerciseTypeId = exerciseTypeId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}