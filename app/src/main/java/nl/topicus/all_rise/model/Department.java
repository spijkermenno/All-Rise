package nl.topicus.all_rise.model;

import java.time.LocalTime;
import java.util.ArrayList;

public class Department {

    private int id, officeId;
    private String name, description;

    private ArrayList<Employee> employees;

    public Department(int id, int officeId, String name, String description){
        this.id = id;
        this.officeId = officeId;
        this.name = name;
        this.description = description;

        this.employees = new ArrayList<>();
    }

    public Department(int id, int officeId, String name, String description, ArrayList<Employee> employees){
        this.id = id;
        this.officeId = officeId;
        this.name = name;
        this.description = description;

        this.employees = employees;
    }

    public void addEmployee(Employee employee){
        employees.add(employee);
    }

    public void removeEmployee(Employee employee){
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

    public ArrayList<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(ArrayList<Employee> employees) {
        this.employees = employees;
    }

    public int getOfficeId() {
        return officeId;
    }

    public void setOfficeId(int officeId) {
        this.officeId = officeId;
    }
}
