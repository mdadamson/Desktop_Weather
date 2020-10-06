package DesktopWeather;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

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

    public String getTimeDate(){
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();

        // Date format to string
        DateTimeFormatter formatterD = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG);
        String dateNow = date.format(formatterD);

        // Time format to string
        DateTimeFormatter formatterT = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);
        String timeNow = time.format(formatterT);

        return "Time: " + timeNow + "  Date: " + dateNow;
    }
}
