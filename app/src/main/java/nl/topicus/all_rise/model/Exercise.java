package nl.topicus.all_rise.model;

public class Exercise {

    private int id, exerciseTypeId;
    private String name;

    public Exercise(int id, int exerciseTypeId, String name) {
        this.id = id;
        this.exerciseTypeId = exerciseTypeId;
        this.name = name;
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
}
