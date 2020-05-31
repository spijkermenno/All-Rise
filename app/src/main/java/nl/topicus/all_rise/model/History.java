package nl.topicus.all_rise.model;

import java.time.LocalDateTime;

public class History {
    private int id, employeeId, workoutId;
    private LocalDateTime momentOfExecution;

    public History(int id, int employeeId, int workoutId, LocalDateTime momentOfExecution) {
        this.id = id;
        this.employeeId = employeeId;
        this.workoutId = workoutId;

        this.momentOfExecution = momentOfExecution;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public int getWorkoutId() {
        return workoutId;
    }

    public void setWorkoutId(int workoutId) {
        this.workoutId = workoutId;
    }

    public LocalDateTime getMomentOfExecution() {
        return momentOfExecution;
    }

    public void setMomentOfExecution(LocalDateTime momentOfExecution) {
        this.momentOfExecution = momentOfExecution;
    }
}
