package org.tensorflow.lite.examples.detection;

public class Data {

    private String title;
    private String certification;
    private int resId;
    private double lat;
    private double lon;
    private String information;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCertification() {
        return certification;
    }

    public void setCertification(String certification) {
        this.certification = certification;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public void setLocation(double lat, double lon) {
        setLat(lat);
        setLon(lon);
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getInformation() { return information; }

    public void setInformation(String information) { this.information = information; }
}