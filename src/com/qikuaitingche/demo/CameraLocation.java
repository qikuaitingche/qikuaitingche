package com.qikuaitingche.demo;

import java.io.Serializable;

public class CameraLocation implements Serializable {
    private static final long serialVersionUID = -7272905486020904055L;
    private double latitude;
    private double longtitude;
    private int imageId;
    private String name;
    private String distance;
    private String description;

    public CameraLocation(){}

    public CameraLocation(double latitude, double longtitude, int imageId,
            String name, String distance, String description) {
        super();
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.imageId = imageId;
        this.name = name;
        this.distance = distance;
        this.description = description;
    }



    /**
     * @return the latitude
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * @param latitude
     *            the latitude to set
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * @return the longtitude
     */
    public double getLongtitude() {
        return longtitude;
    }

    /**
     * @param longtitude
     *            the longtitude to set
     */
    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }

    /**
     * @return the imageId
     */
    public int getImageId() {
        return imageId;
    }

    /**
     * @param imageId
     *            the imageId to set
     */
    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the distance
     */
    public String getDistance() {
        return distance;
    }

    /**
     * @param distance
     *            the distance to set
     */
    public void setDistance(String distance) {
        this.distance = distance;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     *            the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }
}