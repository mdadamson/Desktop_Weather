package DesktopWeather;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class WeatherInput implements EventHandler<ActionEvent> {
    private WeatherAPI weatherAPI = new WeatherAPI();
    private WeatherTimeDate timeDate = new WeatherTimeDate();
    private WeatherGUI gui = new WeatherGUI();

    @Override
    public void handle(ActionEvent event){

        //Call the GUI after the button is pressed
        //Get the input from the textfield and set as a string
        String value = gui.getZipInput();
        //takes the user input value and passes it to WeatherAPI.java class
        timeDate.UpdateTimeDate();
        weatherAPI.setZipCode(value);
        try {
            weatherAPI.updateWeather();
        } catch (IOException e) {
            gui.dialogBox();
            e.printStackTrace();
        } catch (NetworkConnectionException e) {
            System.out.println("No internet connection");
            e.printStackTrace();
        } catch (NullPointerException e){
            gui.dialogBox();
        }

        //calls the methods in WeatherAPI and then displays them on the GUI
        try {
            gui.setValueInput("Current City Name: " + weatherAPI.getCityName() +
                    "\nCurrent Temperature: " + weatherAPI.getTemperature()+ "\u00B0" +
                    "\nCurrent Humidity: " + weatherAPI.getHumidity()+"\u0025");
            gui.setLabelInput(weatherAPI.getCityName() + ", CT"); // can remove CT later - for testing purposes
            gui.setDayOneImage();
            System.out.println("Current City Name: " + weatherAPI.getCityName()); // can delete these printouts later, used for testing purposes
            System.out.println("Current Temperature: " + weatherAPI.getTemperature() + "\n");
        }
        //invalid inputs throw null pointer exceptions, so when caught, the program displays an error box.
        catch (NullPointerException | FileNotFoundException e){
            System.out.println("Invalid input or zipcode!\n");
            gui.dialogBox();
            gui.setZipInput();
        }
    }
}
