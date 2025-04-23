package com.slava.model.imodel;

public interface ICoordinates {

    public String getName();
    public Double getLat();
    public Double getLon();

    public void setLat(Double lat);
    public void setLon(Double lon);
    public void setName(String name);
}
