package com.slava.model.imodel;

import java.util.List;

public interface IWeather {
    public String getName();
    public ICoordinates getCoordinates();
    public IParameters getParameters();
    public IWind getWind();

    public void setName(String name);
}
