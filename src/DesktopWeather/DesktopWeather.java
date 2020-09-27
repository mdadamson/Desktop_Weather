package DesktopWeather;

public class DesktopWeather extends WeatherGUI{
    public static void main(String[] args) {
        WeatherAPI weather = new WeatherAPI();
        weather.updateWeather();
        p("Current City Name: " + weather.getCityName());
        p("Current Temperature: " + weather.getTemperature());
        weather.setZipCode("10003");
        weather.updateWeather();
        p("Current City Name: " + weather.getCityName());
        p("Current Temperature: " + weather.getTemperature());

        launch(args);
    }
    
    /*
     * this method was added for testing purposes to make printing to the console
     * easier and faster. We can remove it later.
     */
    private static void p(Object s)
    {
    	System.out.println(s);
    }
}
