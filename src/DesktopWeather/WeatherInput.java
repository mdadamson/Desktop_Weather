package DesktopWeather;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class WeatherInput implements EventHandler<ActionEvent> {

    @Override
    public void handle(ActionEvent event) {

        WeatherGUI gui = new WeatherGUI();
        WeatherAPI weather = new WeatherAPI();

        String value = gui.getValueInput();
        weather.setZipCode(value);
        weather.updateWeather();

        //Amanda - Working on this still...
        //gui.setValueInput("Current City Name: " + weather.getCityName());
        System.out.println("Current City Name: " + weather.getCityName());
        System.out.println("Current Temperature: " + weather.getTemperature());
    }
}
