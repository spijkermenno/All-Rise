package nl.topicus.all_rise.model;

import java.io.Serializable;

/**
 * Class is used to test the employeeResponse.
 */

public class Employee implements Serializable {
    private int id, departmentId;
    private String name, surname, activationCode;
    private boolean verified;


    public Employee(int id, int departmentId, String name, String surname) {
        this.id = id;
        this.departmentId = departmentId;
        this.name = name;
        this.surname = surname;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurName() {
        return surname;
    }

    public void setSurName(String surname) {
        this.surname = surname;
    }

    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    /**
     * @return String
     */
    public String toString() {
        return this.id + " - " + this.departmentId + " " + this.name + " " + this.surname + " "
                + this.activationCode + " " + this.verified;
    }
}
