package com.example.finalproj.func;

import javafx.application.Preloader;
import javafx.scene.control.Alert;

public class WeatherObserver {
    public void updateAlert(String weatherInfo) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Weather Update");
        alert.setHeaderText(null);
        alert.setContentText(weatherInfo);
        alert.showAndWait();

    }
}
