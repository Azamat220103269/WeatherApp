package com.example.finalproj.func;

public class WeatherData {
    private String city;
    private double temperature;
    private double humidity;
    private double feelsLike;
    private int visibility;
    private String description;


    public void updateData(String city, double temperature, double humidity, double feelsLike, int visibility, String description) {
        this.city=city;
        this.temperature = temperature;
        this.humidity = humidity;
        this.feelsLike = feelsLike;
        this.visibility = visibility;
        this.description = description;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getFeelsLike() {
        return feelsLike;
    }

    public void setFeelsLike(double feelsLike) {
        this.feelsLike = feelsLike;
    }

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public int getTemperatureInCelsius(){
        return (int) (getTemperature()-273);
    }
    public int getFeelsLikeInCelsius(){
        return (int) (getFeelsLike()-273);
    }
}
