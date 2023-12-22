package com.example.finalproj.func;

public class APIKeysManager {

    private static APIKeysManager instance;
    private String weatherAPIKey;

    private APIKeysManager() {
        this.weatherAPIKey = "465bfd204e77f9a1e10a5cb712f1bdfd";
    }

    public static synchronized APIKeysManager getInstance() {
        if (instance == null) {
            instance = new APIKeysManager();
        }
        return instance;
    }

    public String getWeatherAPIKey() {
        return weatherAPIKey;
    }

}
