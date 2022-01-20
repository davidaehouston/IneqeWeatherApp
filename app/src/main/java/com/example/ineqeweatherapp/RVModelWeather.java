package com.example.ineqeweatherapp;

/**
 * Model class for the weather using OOP.
 */
public class RVModelWeather {

    //instance Vars.
    private String icon;
    private int temperature;
    private int maxTemp;
    private int minTemp;
    private String day;
    private int windSpeed;
    private String windDegree;

    //Default constructor.
    public RVModelWeather() {
    }

    //Constructor with Args.
    public RVModelWeather(String day, int maxTemp, int minTemp, int temperature, String icon, int windSpeed, String windDegree) {
        this.temperature = temperature;
        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
        this.day = day;
        this.icon = icon;
        this.windSpeed = windSpeed;
        this.windDegree = windDegree;
    }

    //Methods.
    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public int getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(int maxTemp) {
        this.maxTemp = maxTemp;
    }

    public int getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(int minTemp) {
        this.minTemp = minTemp;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(int windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getWindDegree() {
        return windDegree;
    }

    public void setWindDegree(String windDegree) {
        this.windDegree = windDegree;
    }
}

