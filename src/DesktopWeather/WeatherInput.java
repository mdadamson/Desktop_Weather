package DesktopWeather;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class WeatherInput implements EventHandler<ActionEvent> {

    @Override
    public void handle(ActionEvent event){

        WeatherGUI gui = new WeatherGUI();
        WeatherAPI weather = new WeatherAPI();

        String value = gui.getZipInput();
        weather.setZipCode(value);
        weather.updateWeather();

        try {
            gui.setValueInput("Current City Name: \n" + weather.getCityName() + "\nCurrent Temperature: \n" + weather.getTemperature());
            gui.setLabelInput(weather.getCityName() + ", CT");
            System.out.println("Current City Name: " + weather.getCityName());
            System.out.println("Current Temperature: " + weather.getTemperature() + "\n");
        }
        catch (NullPointerException e){
            System.out.println("Invalid input or zipcode!\n");
            gui.dialogBox();
            gui.setZipInput();
        }
    }
}
