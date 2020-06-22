package nl.topicus.all_rise.model;

public class RankEntry {
    private String Firstname, Lastname;
    private int Rank, Total_Points;
    private boolean Currentuser;

    public RankEntry(int rank, String firstname, String lastname, int total_Points, boolean currentuser) {
        Rank = rank;
        Firstname = firstname;
        Lastname = lastname;
        Total_Points = total_Points;
        Currentuser = currentuser;
    }

    public int getRank() {
        return Rank;
    }

    public String getFirstname() {
        return Firstname;
    }

    public String getLastname() {
        return Lastname;
    }

    public int getTotal_Points() {
        return Total_Points;
    }

    public boolean isCurrentuser() {
        return Currentuser;
    }

    @Override
    public String toString() {
        return "RankEntry{" +
                "Firstname='" + Firstname + '\'' +
                ", Lastname='" + Lastname + '\'' +
                ", Total_Points=" + Total_Points +
                '}';
    }
}
