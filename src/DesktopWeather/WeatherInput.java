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

        /**
         * Call the GUI after the button is pressed
         * Get the input from the textfield and set as a string
         */
        String value = gui.getZipInput();
        //takes the user input value and passes it to WeatherAPI.java class
        timeDate.UpdateTimeDate();
        weatherAPI.setZipCode(value);
        try {
            weatherAPI.updateWeather();
        } catch (IOException e) {
            gui.dialogBox();
            e.printStackTrace();
        } catch (NullPointerException e){
            gui.dialogBox();
        }

        /**
         * Sends string value for current, day one,
         * and day two weather values to the GUI.
         * Calls weather name method and sends to weatherType
         * method to send over correct file type for icon
         */
        try {
            gui.setWeatherFirst( "Temperature: " + weatherAPI.getTemperature()+ "\u00B0" +
                    "\nPrecipitation: " + weatherAPI.getPrecipChanceToday()+ "\u0025" +
                    "\nHumidity: " + weatherAPI.getHumidity()+ "\u0025" +
                    "\nPressure: " + weatherAPI.getPressure()+ " mbar" +
                    "\nWeather Type: " + weatherAPI.getWeatherName());
            gui.setDayOneImage(weatherType(weatherAPI.getWeatherName()));

            gui.setWeatherSecond( "Temperature Max: " + weatherAPI.getMaxTempTodayPlusOne()+ "\u00B0" +
                    "\nTemperature Min: " + weatherAPI.getMinTempTodayPlusOne()+ "\u00B0" +
                    "\nPrecipitation: " + weatherAPI.getPrecipChanceTodayPlusOne()+ "\u0025" +
                    "\nHumidity: " + weatherAPI.getHumidityTodayPlusOne()+ "\u0025" +
                    "\nPressure: " + weatherAPI.getPressureTodayPlusOne()+ " mbar" +
                    "\nWeather Type: " + weatherAPI.getWeatherNameTodayPlusOne());
            gui.setDayTwoImage(weatherType(weatherAPI.getWeatherNameTodayPlusOne()));

            gui.setWeatherThird( "Temperature Max: " + weatherAPI.getMaxTempTodayPlusTwo()+ "\u00B0" +
                    "\nTemperature Min: " + weatherAPI.getMinTempTodayPlusTwo()+ "\u00B0" +
                    "\nPrecipitation: " + weatherAPI.getPrecipChanceTodayPlusTwo()+ "\u0025" +
                    "\nHumidity: " + weatherAPI.getHumidityTodayPlusTwo()+ "\u0025" +
                    "\nPressure: " + weatherAPI.getPressureTodayPlusTwo()+ " mbar" +
                    "\nWeather Type: " + weatherAPI.getWeatherNameTodayPlusTwo());
            gui.setDayThreeImage(weatherType(weatherAPI.getWeatherNameTodayPlusTwo()));

            gui.setLabelInput(weatherAPI.getCityName());
        }
        //invalid inputs throw null pointer exceptions, so when caught, the program displays an error box.
        catch (NullPointerException e){
            System.out.println("Invalid input or zipcode!\n");
            gui.dialogBox();
            gui.setZipInput();
        }
    }
    /**
     * Takes input string value of weather type
     * matches the weather type to icon list
     * returns value of correct icon file
     */
    private String weatherType (String value){
        String weatherType;
        switch (value) {
            case "Thunderstorm":
                weatherType = "file:weather_icons\\icon_lightning_cloudy.png";
                break;
            case "Drizzle":
                weatherType = "file:weather_icons\\icon_drip.png";
                break;
            case "Rain":
                weatherType = "file:weather_icons\\icon_raining.png";
                break;
            case "Snow":
                weatherType = "file:weather_icons\\icon_snowing.png";
                break;
            case "Mist":
                weatherType = "file:weather_icons\\icon_foggy_cloud.png";
                break;
            case "Clear Sky":
                weatherType = "file:weather_icons\\icon_sunny.png";
                break;
            case "Cloudy":
                weatherType = "file:weather_icons\\icon_cloudy.png";
                break;
            default:
                weatherType = "file:weather_icons\\icon_windy.png";
        }
        return weatherType;
    }
}
