package DesktopWeather;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.net.URL;
import java.util.Properties;

public class WeatherAPI {
    private static final String API_KEY = "46d3fd23fd73595139bcc963739e3c3b";
    private static final String API_KEY_PHRASE = "&appid=" + API_KEY;
    private static final String SERVER_NAME = "http://api.openweathermap.org/data/2.5/weather";
    private static final String CALL_BY_ZIPCODE = "?zip=";
    private static final String DATA_FORMAT = "&mode=xml";
    private String urlCallAddress;
    private UserHandler userHandler = new UserHandler();
    private String temperatureFormat = "&units=imperial";
    private String zipCode;
    private String countryCode;
    private Properties weather;

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
            weather = userHandler.readWeather();
        } catch (Exception e){
            //Some exception message
        }
    }

    /**
     * Method creates and runs the SAX parser to read in XML data from the Open Weather Map API url.
     * @throws Exception basic error or warning information from either the parser or the url connection.
     */
    private void callWeather() throws Exception {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();
        saxParser.parse(new URL(urlCallAddress).openStream(), userHandler);
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
    
    /*
     * Method allows externally created objects of this class to retrieve the "currentTemperature" property
     * from the "weather" Properties object.
     */
    public String getTemperature() {
    	return weather.getProperty("currentTemperature");
    }
    
    /*
     * Method allows externally created objects of this class to retrieve the "cityName" property
     * from the "weather" Properties object.
     */
    public String getCityName() {
    	return weather.getProperty("cityName");
    }
}

/**
 * Class reads the XML data stream using SAX Parser.
 */
class UserHandler extends DefaultHandler {

    private String cityID, cityName, longitude, latitude, country, timezone, sunrise, sunset, currentTemperature,
            minimumTemperature, maximumTemperature, feelsLike, humidity, pressure, windSpeed, windName, windGusts,
            windDirectionValue, windDirectionCode, windDirectionName, cloudyValue, cloudyName, visibility,
            precipitationMode, weatherNumber, weatherValue, weatherIcon, lastUpdate;
    private boolean hasCountry, hasTimezone = false;
    private Properties weather = new Properties();

    /**
     * Method looks for specific string values in an XML document and then stores their associated values.
     * @param uri The Namespace URI.
     * @param localName The local name (without prefix).
     * @param qName The qualified name (with prefix).
     * @param attributes The attributes attached to the element.
     */
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {

        if (qName.equalsIgnoreCase("city")) {
            cityID = attributes.getValue("id");
            cityName = attributes.getValue("name");
        } else if (qName.equalsIgnoreCase("coord")) {
            longitude = attributes.getValue("lon");
            latitude = attributes.getValue("lat");
        } else if (qName.equalsIgnoreCase("country")) {
            hasCountry = true;
        } else if (qName.equalsIgnoreCase("timezone")) {
            hasTimezone = true;
        } else if (qName.equalsIgnoreCase("sun")) {
            sunrise = attributes.getValue("rise");
            sunset = attributes.getValue("set");
        } else if (qName.equalsIgnoreCase("temperature")) {
            currentTemperature = attributes.getValue("value");
            minimumTemperature = attributes.getValue("min");
            maximumTemperature = attributes.getValue("max");
        } else if (qName.equalsIgnoreCase("feels_like")) {
            feelsLike = attributes.getValue("value");
        } else if (qName.equalsIgnoreCase("humidity")) {
            humidity = attributes.getValue("value");
        } else if (qName.equalsIgnoreCase("pressure")) {
            pressure = attributes.getValue("value");
        } else if (qName.equalsIgnoreCase("speed")) {
            windSpeed = attributes.getValue("value");
            windName = attributes.getValue("name");
        } else if (qName.equalsIgnoreCase("gusts")) {
            windGusts = attributes.getValue("value");
        } else if (qName.equalsIgnoreCase("direction")) {
            windDirectionValue = attributes.getValue("value");
            windDirectionCode = attributes.getValue("code");
            windDirectionName = attributes.getValue("name");
        } else if (qName.equalsIgnoreCase("clouds")) {
            cloudyValue = attributes.getValue("value");
            cloudyName = attributes.getValue("name");
        } else if (qName.equalsIgnoreCase("visibility")) {
            visibility = attributes.getValue("value");
        } else if (qName.equalsIgnoreCase("precipitation")) {
            precipitationMode = attributes.getValue("mode");
        } else if (qName.equalsIgnoreCase("weather")) {
            weatherNumber = attributes.getValue("number");
            weatherValue = attributes.getValue("value");
            weatherIcon = attributes.getValue("icon");
        } else if (qName.equalsIgnoreCase("lastupdate")) {
            lastUpdate = attributes.getValue("value");
        }
    }

    /**
     * Method reads in commented text in XML for country and timezone.
     * @param ch The characters.
     * @param start The start position in the character array.
     * @param length The number of characters to use from the character array.
     */
    @Override
    public void characters(char[] ch, int start, int length) {
        if (hasCountry){
            country = new String(ch, start, length);
            hasCountry = false;
        } else if (hasTimezone){
            timezone = new String(ch, start, length);
            hasTimezone = false;
        }
    }

    /**
     * Method looks for "current" to know when the end of the document has been reached.
     * @param uri The Namespace URI.
     * @param localName The local name (without prefix).
     * @param qName The qualified name (with prefix).
     */
    @Override
    public void endElement(String uri, String localName, String qName) {
        if (qName.equalsIgnoreCase("current")) {

        }
    }
    
    /**
     * Method assigns all of the stored variables into the Parameter weather.
     * @return Returns Properties of weather.
     */
    public Properties readWeather(){
        weather.put("cityID", cityID);
        weather.put("cityName", cityName);
        weather.put("longitude", longitude);
        weather.put("latitude", latitude);
        weather.put("country", country);
        weather.put("timezone", timezone);
        weather.put("sunrise", sunrise);
        weather.put("sunset", sunset);
        weather.put("currentTemperature", currentTemperature);
        weather.put("minimumTemperature", minimumTemperature);
        weather.put("maximumTemperature", maximumTemperature);
        weather.put("feelsLike", feelsLike);
        weather.put("humidity", humidity);
        weather.put("pressure", pressure);
        weather.put("windSpeed", windSpeed);
        weather.put("windName", windName);
        if (weather.get("windGusts") == null){
            weather.put("windGusts", "");
        }else {
            weather.put("windGusts", windGusts);
        }
        weather.put("windDirectionValue", windDirectionValue);
        weather.put("windDirectionCode", windDirectionCode);
        weather.put("windDirectionName", windDirectionName);
        weather.put("cloudyValue", cloudyValue);
        weather.put("cloudyName", cloudyName);
        weather.put("visibility", visibility);
        weather.put("precipitationMode", precipitationMode);
        weather.put("weatherNumber", weatherNumber);
        weather.put("weatherValue", weatherValue);
        weather.put("weatherIcon", weatherIcon);
        weather.put("lastUpdate", lastUpdate);

        return weather;
    }
}


