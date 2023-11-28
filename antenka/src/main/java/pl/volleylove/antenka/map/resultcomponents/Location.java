package pl.volleylove.antenka.map.resultcomponents;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Location {

    @Column(name = "latitude")
    @JsonProperty("lat")
    private double latitude;
    @JsonProperty("lng")
    @Column(name = "longitude")
    private double longitude;

    public Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Location() {
    }

    public double getLatitude() {
        return latitude;
    }

    @JsonSetter
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }
    @JsonSetter
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "Location{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
