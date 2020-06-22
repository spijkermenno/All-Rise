package nl.topicus.all_rise.model;

public class HistoryEntry {
    private int duration;
    private String exerciseType;

    public HistoryEntry(int duration, String exerciseType) {
        this.duration = duration;
        this.exerciseType = exerciseType;
    }

    public int getDuration() {
        return duration;
    }

    public String getExerciseType() {
        return exerciseType;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
