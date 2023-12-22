package com.example.finalproj.func;

import com.example.finalproj.func.APIKeysManager;
import com.example.finalproj.func.GeolocationService;
import com.example.finalproj.func.WeatherData;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class WeatherService{
    private GeolocationService geolocationService;
    private APIKeysManager apiKeysManager;
    private WeatherData weatherData;
    private List<WeatherObserver> observers = new ArrayList<>();

    public void addObserver(WeatherObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(WeatherObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers(String weatherInfo) {
        for (WeatherObserver observer : observers) {
            observer.updateAlert(weatherInfo);
        }
    }

    public WeatherService(GeolocationService geolocationService, APIKeysManager apiKeysManager) {
        this.geolocationService = geolocationService;
        this.apiKeysManager = apiKeysManager;
        this.weatherData = new WeatherData();
    }

    public WeatherData getWeatherByCurrentLocation() {
        String city = geolocationService.getCity();
        if (city != null) {
            try {
                String apiKey = apiKeysManager.getWeatherAPIKey();
                String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey;

                URL url = new URL(apiUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                int responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String inputLine;
                    StringBuilder response = new StringBuilder();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }

                    in.close();
                    JSONObject jsonResponse = new JSONObject(response.toString());
                    JSONObject main = jsonResponse.getJSONObject("main");
                    JSONArray weather = jsonResponse.getJSONArray("weather");
                    JSONObject weatherInfo = weather.getJSONObject(0);

                    double temperature = main.getDouble("temp");
                    double humidity = main.getDouble("humidity");
                    double feelsLike = main.getDouble("feels_like");
                    int visibility = jsonResponse.getInt("visibility");

                    String description = weatherInfo.getString("description");
                    weatherData.updateData(city,temperature, humidity, feelsLike, visibility, description);

                    return weatherData;
                } else {
                    System.out.println("Error: " + responseCode);
                }

                connection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Unable to determine city.");
        }
        return null;
    }
    public WeatherData getWeatherByCity(String cityName) {
        try {
            String apiKey = apiKeysManager.getWeatherAPIKey();
            String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=" + cityName + "&appid=" + apiKey;

            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                in.close();
                WeatherData weatherData = new WeatherData();
                parseJsonResponse(response.toString(), weatherData);
                return weatherData;
            } else {
                System.out.println("Error: " + responseCode);
            }

            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void parseJsonResponse(String jsonResponse, WeatherData weatherData) {
        try {
            JSONObject jsonResponseObj = new JSONObject(jsonResponse);
            JSONObject main = jsonResponseObj.getJSONObject("main");
            JSONArray weatherArray = jsonResponseObj.getJSONArray("weather");
            JSONObject weatherInfo = weatherArray.getJSONObject(0);

            double temperature = main.getDouble("temp");
            double humidity = main.getDouble("humidity");
            double feelsLike = main.getDouble("feels_like");
            int visibility = jsonResponseObj.getInt("visibility");
            String description = weatherInfo.getString("description");
            String city = jsonResponseObj.getString("name");

            weatherData.updateData(city, temperature, humidity, feelsLike, visibility, description);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void updateWeather() {
        WeatherData weatherData = getWeatherByCurrentLocation();
        if (weatherData != null) {
            String weatherInfo = "Weather in " + weatherData.getCity() + ": " +
                    weatherData.getTemperatureInCelsius() + "C, " +
                    "Feels like " + weatherData.getFeelsLikeInCelsius() + "C, " +
                    "Humidity " + weatherData.getHumidity() + "%, " +
                    "Visibility " + weatherData.getVisibility() + "m";

            notifyObservers(weatherInfo);
        }
    }
}
