package DesktopWeather;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class WeatherTimeDate{
    private WeatherAPI weatherAPI = new WeatherAPI();
    private LocalDateTime timeDate;
    /**
     * Method retrieves the time and date of the last weather update and returns it as a LocalDateTime in
     * ISO_LOCAL_DATE_TIME format. If the last update date time is null the current date time is returned.
     */
    void UpdateTimeDate(){
        if (weatherAPI.getWeatherIsEmpty()){
            timeDate = LocalDateTime.now(ZoneOffset.UTC);
        }else{
            timeDate = LocalDateTime.parse(
                    weatherAPI.getLastWeatherUpdate(),
                    DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        }
        System.out.println(timeDate);
        weatherAPI.setWeatherTimeDate(timeDate);
    }
}
