package DesktopWeather;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

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
        weatherAPI.updateWeather();


        //calls the methods in WeatherAPI and then displays them on the GUI
        try {
            gui.setValueInput("Current City Name: \n" + weatherAPI.getCityName() + "\nCurrent Temperature: \n" + weatherAPI.getTemperature());
            gui.setLabelInput(weatherAPI.getCityName() + ", CT"); // can remove CT later - for testing purposes
            System.out.println("Current City Name: " + weatherAPI.getCityName()); // can delete these printouts later, used for testing purposes
            System.out.println("Current Temperature: " + weatherAPI.getTemperature() + "\n");
        }
        //invalid inputs throw null pointer exceptions, so when caught, the program displays an error box.
        catch (NullPointerException e){
            System.out.println("Invalid input or zipcode!\n");
            gui.dialogBox();
            gui.setZipInput();
        }
    }
}
