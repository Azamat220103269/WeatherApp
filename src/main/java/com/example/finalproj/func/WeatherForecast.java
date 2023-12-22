package com.example.finalproj.func;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;

public class WeatherForecast {

    private static final String API_KEY = "465bfd204e77f9a1e10a5cb712f1bdfd";
    private static final String API_URL = "https://api.openweathermap.org/data/2.5/forecast";

    public static String getWeatherForecast(String cityName) {
        StringBuilder forecastData = new StringBuilder();

        try {
            URL url = new URL(API_URL + "?q=" + cityName + "&appid=" + API_KEY);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                in.close();

                forecastData.append(parseWeatherForecast(response.toString()));
            } else {
                forecastData.append("Error: ").append(responseCode);
            }

            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return forecastData.toString();
    }

    private static String parseWeatherForecast(String json) {
        StringBuilder forecastData = new StringBuilder();
        JSONObject jsonObject = new JSONObject(json);
        JSONArray list = jsonObject.getJSONArray("list");

        LocalDateTime currentDateTime = LocalDateTime.now();

        for (int i = 0; i < list.length(); i++) {
            JSONObject item = list.getJSONObject(i);
            String timestamp = item.getString("dt_txt");
            LocalDateTime dateTime = LocalDateTime.parse(timestamp, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            if (currentDateTime.until(dateTime, ChronoUnit.HOURS) <= 12) {
                String formattedDateTime = dateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
                forecastData.append(formattedDateTime).append(" ");
                JSONObject main = item.getJSONObject("main");
                double temperatureKelvin = main.getDouble("temp");
                int temperatureCelsius = (int) (temperatureKelvin - 273.15);

                forecastData.append("               "+temperatureCelsius).append("°C").append("\n");
            }
        }

        return forecastData.toString();
    }

}
