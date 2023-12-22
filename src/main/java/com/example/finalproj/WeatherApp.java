package com.example.finalproj;

import com.example.finalproj.func.APIKeysManager;
import com.example.finalproj.func.GeolocationService;
import com.example.finalproj.func.WeatherService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class WeatherApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(WeatherApp.class.getResource("MainMenu.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 640, 360);
            stage.setTitle("Weather App");

            GeolocationService geolocationService = GeolocationService.getInstance();
            APIKeysManager apiKeysManager = APIKeysManager.getInstance();
            WeatherService weatherService = new WeatherService(geolocationService, apiKeysManager);

            AppController mainMenuController = fxmlLoader.getController();
            mainMenuController.setWeatherService(weatherService);
            mainMenuController.initialize();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
