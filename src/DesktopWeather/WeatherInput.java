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
        
        
        
        /*
         * A note about handling the exceptions thrown by WeatherAPI.updateWeather():
         * 
         *		- There are 2 types of exceptions that should result in a message that
         *			is displayed to the user. 
         *				a.If an IOException is caught here, it means an internet connection
         *				  	was detected but the application couldn't connect to the API.
         *				b.If a NetworkConnectionException (the custom exception I wrote in 
         *					WeatherAPI.java) is caught here, it means an internet connection
         *					was not detected.
         *			
         *		- When I set this up, I was thinking the exceptions would be handled 
         *			something like this, but obviously make any changes necessary:
		
		String exceptionMessage;

		try{
			weatherAPI.updateWeather();
        } catch (IOException e){
            exceptionMessage = "Unable to connect to Open Weather API.";
            ...display exceptionMessage in GUI
        } catch (NetworkConnectionException e) {
        	exceptionMessage = e.getMessage();
        	...display exceptionMessage in GUI
        }
        
		*/
        


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
