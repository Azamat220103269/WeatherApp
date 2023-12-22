package com.example.finalproj.func;


import org.json.JSONObject;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class GeolocationService {
    private static GeolocationService instance;

    private GeolocationService() {
    }

    public static GeolocationService getInstance() {
        if (instance == null) {
            instance = new GeolocationService();
        }
        return instance;
    }

    public String getCity() {
        try {
            URL url = new URL("https://ipinfo.io/json");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                Scanner scanner = new Scanner(connection.getInputStream());
                StringBuilder response = new StringBuilder();
                while (scanner.hasNextLine()) {
                    response.append(scanner.nextLine());
                }
                scanner.close();
                String city = parseCityFromResponse(response.toString());
                return city;
            } else {
                System.out.println("Error: " + responseCode);
            }
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    private String parseCityFromResponse(String jsonResponse) {
        JSONObject jsonObject = new JSONObject(jsonResponse);
        return jsonObject.getString("city");
    }
}
