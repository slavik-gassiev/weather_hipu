package com.slava.model.imodel;

public interface IWeatherDetails {
    public String getMain();
    public String getDescription();
    public String getIcon();

    public void setMain(String main);
    public void setDescription(String description);
    public void setIcon(String icon);
}
