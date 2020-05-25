package nl.topicus.all_rise.model;

import java.time.LocalTime;
import java.util.ArrayList;

public class Department {

    private int id;
    private String name, description;

    private LocalTime deadline;

    private ArrayList<Employee> employees;

    public Department(int id, String name, String description){
        this.id = id;
        this.name = name;
        this.description = description;

        this.deadline = LocalTime.NOON;
        this.employees = new ArrayList<Employee>();
    }

    public Department(int id, String name, String description, ArrayList<Employee> employees){
        this.id = id;
        this.name = name;
        this.description = description;

        this.deadline = LocalTime.NOON;

        this.employees = employees;
    }

    public Department(int id, String name, String description, LocalTime deadline){
        this.id = id;
        this.name = name;
        this.description = description;
        this.deadline = deadline;
        this.employees = new ArrayList<Employee>();
    }

    public void addUser(Employee employee){
        employees.add(employee);
    }

    public void removeUser(Employee employee){
        employees.remove(employee);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalTime deadline) {
        this.deadline = deadline;
    }

    public ArrayList<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(ArrayList<Employee> employees) {
        this.employees = employees;
    }

    public String toString(){
        return id + " " + name + " " + deadline;
    }
}
