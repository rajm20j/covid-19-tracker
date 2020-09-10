package models;

import java.text.NumberFormat;

public class LocationStatus {
    private String state;
    private String country;
    private int latestTotal;
    private String latestTotalString;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getLatestTotal() {
        return latestTotal;
    }

    public void setLatestTotal(int latestTotal) {
        this.latestTotal = latestTotal;
    }

    public String getLatestTotalString() {
        return NumberFormat.getIntegerInstance().format(latestTotal);
    }
}
