package com.example.finalproj;

import com.example.finalproj.func.*;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.Timer;
import java.util.TimerTask;

public class AppController {

    @FXML
    private Label LabelAdvice;

    @FXML
    private ImageView imageNot;

    @FXML
    private Label LabelWeek;

    @FXML
    private Label LabelTime1;

    @FXML
    private Button BtnAdvice;

    @FXML
    private Button BtnWeek;

    @FXML
    private Label LabelCity;

    @FXML
    private Label LabelTemperature;

    @FXML
    private Label Lfeelslike;

    @FXML
    private Label Lhumidity;

    @FXML
    private Label Lvisibily;

    @FXML
    private Label Forcast;

    @FXML
    private Button Notify;

    @FXML
    private TextField SearchCityText;

    private WeatherService weatherService;

    public void setWeatherService(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @FXML
    public void initialize() {
        updateWeatherData();
        addScaleTransition(LabelAdvice);
        addScaleTransition(LabelWeek);

        Text timeText = new Text();
        updateTimeText(timeText);
        Font arialFont = Font.font("Century Gothic", 20);
        timeText.setFill(Color.WHITE);

        timeText.setFont(arialFont);
        LabelTime1.setGraphic(timeText);

        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                javafx.application.Platform.runLater(() -> updateTimeText(timeText));
            }
        }, 0, 1000);
        SearchCityText.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                WeatherData setCity = new WeatherData();
                setCity.setCity(SearchCityText.getText().trim());
                getWeatherByCity();
            }
        });
    }

    private void updateTimeText(Text timeText) {
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss");
        String currentTime = now.format(formatter);
        timeText.setText(currentTime);
    }
    @FXML
    private void notifyButtonPressed() {
        WeatherObserver weatherObserver = new WeatherObserver();
        weatherService.addObserver(weatherObserver);
        weatherService.updateWeather();
    }
    private void addScaleTransition(Label label) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), label);

        label.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
            scaleTransition.setFromX(label.getScaleX());
            scaleTransition.setFromY(label.getScaleY());
            scaleTransition.setToX(1.2);
            scaleTransition.setToY(1.2);
            scaleTransition.play();
        });

        label.addEventHandler(MouseEvent.MOUSE_EXITED, event -> {
            scaleTransition.setFromX(label.getScaleX());
            scaleTransition.setFromY(label.getScaleY());
            scaleTransition.setToX(1.0);
            scaleTransition.setToY(1.0);
            scaleTransition.play();
        });
    }

    private void updateWeatherData() {
        if (weatherService != null) {
            WeatherData weatherData = weatherService.getWeatherByCurrentLocation();
            if (weatherData != null) {
                LabelCity.setText( weatherData.getCity());
                LabelTemperature.setText(weatherData.getTemperatureInCelsius()+"C");
                Lfeelslike.setText("Feels Like \n"+weatherData.getFeelsLikeInCelsius() + " C");
                Lhumidity.setText("Humidity \n"+weatherData.getHumidity() + "%");
                Lvisibily.setText("Visibility \n"+weatherData.getVisibility() + " m");
                updateWeatherForecast(weatherData.getCity());
            }
        }
    }
    @FXML
    private void handleGenerateAdvice() {
        WeatherData weatherData = weatherService.getWeatherByCurrentLocation();
        if (weatherService != null && weatherData != null) {
            try {
                String generatedAdvice = ChatGPT.chatGPT("What should I wear today? Temperature: "+weatherData.getTemperature()+"Feels like: "+weatherData.getFeelsLikeInCelsius()+"Humidity: "+weatherData.getHumidity()+"Visibility: "+weatherData.getVisibility()+"Answer in this pattern:\n" +
                        "You should dress like this:......\n" +
                        "Reason:........");
                showAlert("Сгенерированный совет", generatedAdvice, AlertType.INFORMATION);
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Ошибка", "Ошибка при получении совета.", AlertType.ERROR);
            }
        } else {
            showAlert("Ошибка", "Ошибка при получении данных о погоде.", AlertType.ERROR);
        }
    }

    private void showAlert(String title, String content, AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    @FXML
    private void getWeatherByCity() {
        String cityName = SearchCityText.getText().trim();
        if (weatherService != null && !cityName.isEmpty()) {
            WeatherData weatherData = weatherService.getWeatherByCity(cityName);
            if (weatherData != null) {
                LabelCity.setText(weatherData.getCity());
                LabelTemperature.setText(weatherData.getTemperatureInCelsius() + "C");
                Lfeelslike.setText("Feels Like \n" + weatherData.getFeelsLikeInCelsius() + " C");
                Lhumidity.setText("Humidity \n" + weatherData.getHumidity() + "%");
                Lvisibily.setText("Visibility \n" + weatherData.getVisibility() + " m");
                updateWeatherForecast(cityName);

            }
        }
    }
    private void updateWeatherForecast(String cityName) {
        String weatherForecast = WeatherForecast.getWeatherForecast(cityName);
        Forcast.setText(weatherForecast);
    }
    @FXML
    private void updateWeeklyForecast() {
        if (weatherService != null) {
            WeatherData weatherData = weatherService.getWeatherByCurrentLocation();
            if (weatherData != null) {
                String cityName = weatherData.getCity();
                System.out.println(cityName);
                String weeklyForecast = WeeklyWeatherForecast.getWeeklyWeatherForecast(cityName);
                Forcast.setText(weeklyForecast);
            }
        }
    }

}
