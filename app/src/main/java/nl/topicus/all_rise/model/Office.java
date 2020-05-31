package nl.topicus.all_rise.model;

public class Office {
    private int id;
    private String city, zipcode, address;

    public Office(int id, String city, String zipcode, String address) {
        this.id = id;
        this.city = city;
        this.zipcode = zipcode;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
