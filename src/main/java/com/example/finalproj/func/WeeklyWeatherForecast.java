package com.example.finalproj.func;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class WeeklyWeatherForecast {

    private static final String API_KEY = "465bfd204e77f9a1e10a5cb712f1bdfd";
    private static final String API_URL = "https://api.openweathermap.org/data/2.5/forecast";

    public static String getWeeklyWeatherForecast(String cityName) {
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

        return formatForecastData(forecastData.toString());
    }

    private static String parseWeatherForecast(String json) {
        StringBuilder forecastData = new StringBuilder();
        JSONObject jsonObject = new JSONObject(json);
        JSONArray list = jsonObject.getJSONArray("list");

        for (int i = 0; i < list.length(); i += 8) {
            JSONObject item = list.getJSONObject(i);
            String timestamp = item.getString("dt_txt");
            JSONObject main = item.getJSONObject("main");
            double temperature = main.getDouble("temp");
            LocalDateTime dateTime = LocalDateTime.parse(timestamp, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            ZoneId zoneId = ZoneId.systemDefault();
            dateTime = dateTime.atZone(ZoneId.of("UTC")).withZoneSameInstant(zoneId).toLocalDateTime();

            LocalDate date = dateTime.toLocalDate();

            forecastData
                    .append(date)
                    .append("      ").append((int) (temperature - 273.15))
                    .append("°C\n");
        }

        return forecastData.toString();
    }

    private static String formatForecastData(String data) {
        return data;
    }
}
