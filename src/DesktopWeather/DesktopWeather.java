package DesktopWeather;

import javafx.application.Platform;
import javafx.scene.control.Alert;

public class DesktopWeather extends WeatherGUI {
    public static void main(String[] args) {
        // Variables
        boolean noConn = false;
        // Instantiates WeatherAPI
        WeatherAPI weatherAPI = new WeatherAPI();
        /* Try/Catch calls network check method
           if method creates no issues there would be
           no exception and JavaFX GUI will launch correctly
           If the method encounters a exception it will throw the
           exception and it will trigger a dialog box letting the user know.
         */
        try {
            weatherAPI.checkNetworkConnection();
            noConn = true;
        }
        catch (NetworkConnectionException e){
            Platform.runLater(() -> {
                    intCheckBox();
                }
            );
        }
        // Once internet connection is established the GUI launches
        if (noConn)
            launch(args);
    }
}
