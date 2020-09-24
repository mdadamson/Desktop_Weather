package DesktopWeather;

public class WeatherAPI {
    private static final String API_KEY = "46d3fd23fd73595139bcc963739e3c3b";
    private static final String API_KEY_PHRASE = "&appid=" + API_KEY;
    private static final String SERVER_NAME = "http://api.openweathermap.org/data/2.5/weather";
    private static final String CALL_BY_ZIPCODE = "?zip=";
    private static final String DATA_FORMAT = "&mode=xml";
    private String urlCallAddress;
    private String temperatureFormat = "&units=imperial";
    private String zipCode;
    private String countryCode;

    /**
     * Default configuration of weather API. Set zipcode and/or country code for different location. Call update weather
     * to return current weather information.
     */
    public WeatherAPI() {
        zipCode = "29644";
        countryCode = "us";
    }

    /**
     * Method organizes the weather call and test for internet connection.
     */
    public void updateWeather() {
        getWeatherDataByZipCode();
        try {
            callWeather();
        } catch (Exception e){
            //Some exception message
        }
    }

    /**
     *
     *
     */
    private void callWeather() {

    }

    /**
     * Method builds the URL string using the zip code format.
     */
    private void getWeatherDataByZipCode() {
        urlCallAddress = new StringBuilder()
                .append(SERVER_NAME)
                .append(CALL_BY_ZIPCODE)
                .append(zipCode)
                .append(",")
                .append(countryCode)
                .append(DATA_FORMAT)
                .append(temperatureFormat)
                .append(API_KEY_PHRASE)
                .toString();
    }

    /**
     * Method allows the zip code to be changed for the location to retrieve weather information for.
     * @param zipCode 5 digit standard zip code.
     */
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    /**
     * Method allows the country code to be changed for the location to retrieve weather information for.
     * Follow ISO 3166 format.
     * @param countryCode ISO 3166 designated code for Country.
     */
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
}


