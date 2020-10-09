package DesktopWeather;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

public class WeatherAPI {
    private static final String API_KEY = "46d3fd23fd73595139bcc963739e3c3b";
    private static final String API_KEY_PHRASE = "&appid=" + API_KEY;
    private static final String CALL_BY_ZIPCODE = "?zip=";
    private static final String DATA_FORMAT = "&mode=xml";
    private String urlCallAddress;
    private final UserHandler userHandler = new UserHandler();
    private final ForecastHandler forecastHandler = new ForecastHandler();
    private final String temperatureFormat = "&units=imperial";
    private String zipCode;
    private String countryCode;
    private Properties currentWeather = new Properties();
    private Properties forecastWeather = new Properties();
    private boolean canUpdate;
    private static LocalDateTime currentTimeDate;

    /**
     * Default configuration of weather API. Set zipcode and/or country code for different location. Call update weather
     * to return current weather information.
     */
    public WeatherAPI() {
        zipCode = "29644";
        countryCode = "us";
        canUpdate = true;
    }

    /**
     * Method organizes the weather call and timing.
     * @throws NetworkConnectionException & IOExceptions thrown by call to callWeather() so
     * that they can be handled in the GUI subsystem.
     */
    public void updateWeather() throws IOException {
        LocalDateTime localTime = LocalDateTime.now(ZoneOffset.UTC);
        
        if (localTime.isAfter(currentTimeDate.plusMinutes(10))){
            canUpdate = true;
        }

        if (canUpdate){
            canUpdate = false;
            getWeatherDataByZipCode(false);
            callWeather(userHandler);
            currentWeather = userHandler.readWeather();
            getWeatherDataByZipCode(true);
            callWeather(forecastHandler);
            forecastWeather = forecastHandler.readWeather();
        }else{
            System.out.println("Information is up to date.");
        }
    }

    /**
     * Method creates and runs the SAX parser to read in XML data from the Open Weather Map API url.
     * Handles exceptions that occur during the creation of the parser or its attempt to process 
     * incoming XML data.
     * @throws NetworkConnectionException thrown by call to checkNetworkConnection().
     * @throws IOException thrown by call to saxParser.parse(new URL(urlCallAddress).openStream(), userHandler)
     */
    private void callWeather(DefaultHandler userHandler) throws IOException{
        SAXParserFactory factory = SAXParserFactory.newInstance();
        
        try {
        SAXParser saxParser = factory.newSAXParser();
        saxParser.parse(new URL(urlCallAddress).openStream(), userHandler);
        } catch(SAXException e) {
        	System.out.println("Unable to parse XML data.");
        	e.printStackTrace();
        } catch(ParserConfigurationException e) {
        	System.out.println("Unable to configure SAX parser.");
        	e.printStackTrace();
        }
    }

    /**
     * Method executes the necessary instructions to check if the user's computer has an active internet
     * connection.
     * Handles exceptions that occur when the JVM attempts to create a thread to execute these
     * instructions or when that thread is interrupted before completing these instructions.
     * @throws NetworkConnectionException if an active internet connection is not detected.
     */
    void checkNetworkConnection() throws NetworkConnectionException {
    	try {
			Process netConnectionChecker = java.lang.Runtime.getRuntime().exec("ping www.google.com");
			int threadTermination = netConnectionChecker.waitFor();
			
			if(threadTermination == 0) {
				System.out.println("Network Connection Successful.");
			}
			else {
				throw new NetworkConnectionException();
			}
		} catch (IOException e) {
			System.out.println("Unable to create thread to check network connection.");
			e.printStackTrace();
		} catch (InterruptedException e) {
			System.out.println("Thread interrupted before network connection check could be completed.");
			e.printStackTrace();
		}
    }
    
    /**
     * Method builds the URL string using the zip code format.
     */
    private void getWeatherDataByZipCode(boolean isForecast) {
    	String serverName, forecastCount;
    	
    	if(isForecast) {
    		serverName = "http://api.openweathermap.org/data/2.5/forecast";
    		forecastCount = "&cnt=24";
    	}
    	else {
    		serverName = "http://api.openweathermap.org/data/2.5/weather";
    		forecastCount = "";
    	}
    	
		urlCallAddress = new StringBuilder()
                .append(serverName)
                .append(CALL_BY_ZIPCODE)
                .append(zipCode)
                .append(",")
                .append(countryCode)
                .append(DATA_FORMAT)
                .append(temperatureFormat)
                .append(forecastCount)
                .append(API_KEY_PHRASE)
                .toString();
    }

    /**
     * Method allows the zip code to be changed for the location to retrieve weather information for.
     * @param zipCode 5 digit standard zip code.
     */
    public void setZipCode(String zipCode) {
        if (!zipCode.equals(this.zipCode)){
            canUpdate = true;
        }
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
    
    /**
     * Method allows externally created objects of this class to retrieve the "currentTemperature" property
     * from the "weather" Properties object.
     */
    public String getTemperature() {
    	return currentWeather.getProperty("currentTemperature");
    }
    
    
    // temp (min/max) todayPlusOne
    public String getMinTempTodayPlusOne() {
    	return forecastWeather.getProperty("minTemperatureTodayPlusOne");
    }
    
    public String getMaxTempTodayPlusOne() {
    	return forecastWeather.getProperty("maxTemperatureTodayPlusOne");
    }
    
    
    // temp (min/max) todayPlusTwo
    public String getMinTempTodayPlusTwo() {
    	return forecastWeather.getProperty("minTemperatureTodayPlusTwo");
    }
    
    public String getMaxTempTodayPlusTwo() {
    	return forecastWeather.getProperty("maxTemperatureTodayPlusTwo");
    }
    
    // precip
    public String getPrecipChanceToday() {
    	return forecastWeather.getProperty("precipitationChanceToday");
    }
    
    public String getPrecipChanceTodayPlusOne() {
    	return forecastWeather.getProperty("precipitationChanceTodayPlusOne");
    }
    
    public String getPrecipChanceTodayPlusTwo() {
    	return forecastWeather.getProperty("precipitationChanceTodayPlusTwo");
    }
    
    /**
     * Method allows externally created objects of this class to retrieve the "hunidity" property
     * from the "weather" Properties object.
     */
    public String getHumidity() {
    	return currentWeather.getProperty("humidity");
    }
    
    public String getHumidityTodayPlusOne() {
    	return forecastWeather.getProperty("maxHumidityTodayPlusOne");
    }
    
    public String getHumidityTodayPlusTwo() {
    	return forecastWeather.getProperty("maxHumidityTodayPlusTwo");
    }
    
    // pressure
    public String getPressure() {
    	return currentWeather.getProperty("pressure");
    }
    
    public String getPressureTodayPlusOne() {
    	return forecastWeather.getProperty("maxPressureTodayPlusOne");
    }
    
    public String getPressureTodayPlusTwo() {
    	return forecastWeather.getProperty("maxPressureTodayPlusTwo");
    }
    
    // weather name
    public String getWeatherNameTodayPlusOne() {
    	return forecastWeather.getProperty("todayPlusOneWeatherName");
    }
    
    public String getWeatherNameTodayPlusTwo() {
    	return forecastWeather.getProperty("todayPlusTwoWeatherName");
    }
    
    // weather Number
    public String getWeatherNumTodayPlusOne() {
    	return forecastWeather.getProperty("todayPlusOneWeatherNum");
    }
    
    public String getWeatherNumTodayPlusTwo() {
    	return forecastWeather.getProperty("todayPlusTwoWeatherNum");
    }
    
    /**
     * Method allows externally created objects of this class to retrieve the "cityName" property
     * from the "weather" Properties object.
     */
    public String getCityName() {
    	return currentWeather.getProperty("cityName");
    }
    
    /**
     * Method allows externally created objects of this class to retrieve the "weatherValue" property
     * from the "weather" Properties object.
     */
    public String getWeatherType() {
    	return currentWeather.getProperty("weatherValue");
    }

    /**
     * Method allows externally created objects of this class to retrieve the "lastUpdate" property
     * from the "weather" Properties object.
     */
    public String getLastWeatherUpdate(){return currentWeather.getProperty("lastUpdate");}

    /**
     * Method allows externally created objects of this class to retrieve the "isEmpty" condition
     * from the "weather" Properties object.
     */
    public Boolean getWeatherIsEmpty(){return currentWeather.isEmpty();}

    /**
     * Method sets internal LocalDateTime "currentTimeDate" variable of this class to the current time
     * from the "WeatherTimeDate" Class.
     */
    public void setWeatherTimeDate(LocalDateTime input){this.currentTimeDate = input;}

}

/**
 * Class reads the XML data stream using SAX Parser.
 */
class UserHandler extends DefaultHandler {

    private String cityID, cityName, longitude, latitude, country, timezone, sunrise, sunset, currentTemperature,
            minimumTemperature, maximumTemperature, feelsLike, humidity, pressure, windSpeed, windName,
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

/**
 * This class defines a custom exception that is instantiated in the event that the 
 * application is unable to establish a connection with the internet.
 */
class NetworkConnectionException extends Exception {
	private static final long serialVersionUID = 1L;

	public NetworkConnectionException() {
		super("No active internet connection. "
				+ "Please establish a connection and try again.");
	}
}

class ForecastHandler extends DefaultHandler {
    private Properties weather = new Properties();
    private String[] temperature = new String[24];
    private String[] humidity = new String[24];
    private String[] pressure = new String[24];
    private String[] precipitation = new String[24];
    private String[] weatherNumber = new String[24];
    private String[] weatherName = new String[24];
    private String[] timeDate = new String[24];
    private int incremental = 0;

    /**
     * Method looks for specific string values in an XML document and then stores their associated values.
     * @param uri The Namespace URI.
     * @param localName The local name (without prefix).
     * @param qName The qualified name (with prefix).
     * @param attributes The attributes attached to the element.
     */
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        if (qName.equalsIgnoreCase("weatherData")){
            incremental = 0;
        } else if(qName.equalsIgnoreCase("time")){
            timeDate[incremental] = attributes.getValue("from");
        } else if(qName.equalsIgnoreCase("symbol")){
            weatherNumber[incremental] = attributes.getValue("number");
            weatherName[incremental] = attributes.getValue("name");
        } else if(qName.equalsIgnoreCase("precipitation")){
            precipitation[incremental] = attributes.getValue("probability");
        } else if (qName.equalsIgnoreCase("temperature")){
            temperature[incremental] = attributes.getValue("value");
        } else if (qName.equalsIgnoreCase("pressure")){
            pressure[incremental] = attributes.getValue("value");
        } else if (qName.equalsIgnoreCase("humidity")){
            humidity[incremental] = attributes.getValue("value");
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        if (qName.equalsIgnoreCase("time")) {
                incremental++;
        }
    }

    public Properties readWeather(){
        LocalDateTime localTime = LocalDateTime.now(ZoneOffset.UTC);
        LocalDate today = localTime.toLocalDate();
        LocalDate todayPlusOne = today.plusDays(1);
        LocalDate todayPlusTwo = today.plusDays(2);
        float maxTempToday = -1000.0f;
        float minTempToday = 1000.0f;
        float maxTempTodayPlusOne = -1000.0f;
        float minTempTodayPlusOne = 1000.0f;
        float maxTempTodayPlusTwo = -1000.0f;
        float minTempTodayPlusTwo = 1000.0f;

        //Min Max temperature per day
        for (int i = 0; i < temperature.length; i++) {
            String tempS = temperature[i];
            float tempF = Float.parseFloat(tempS);

            if (LocalDateTime.parse(timeDate[i],DateTimeFormatter.ISO_LOCAL_DATE_TIME).toLocalDate().equals(today)){
                if (tempF > maxTempToday){
                    maxTempToday = tempF;
                } else if (tempF < minTempToday){
                    minTempToday = tempF;
                }
            } else if (LocalDateTime.parse(timeDate[i],DateTimeFormatter.ISO_LOCAL_DATE_TIME).toLocalDate().equals(todayPlusOne)){
                if (tempF > maxTempTodayPlusOne){
                    maxTempTodayPlusOne = tempF;
                } else if (tempF < minTempTodayPlusOne){
                    minTempTodayPlusOne = tempF;
                }
            } else if (LocalDateTime.parse(timeDate[i],DateTimeFormatter.ISO_LOCAL_DATE_TIME).toLocalDate().equals(todayPlusTwo)){
                if (tempF > maxTempTodayPlusTwo){
                    maxTempTodayPlusTwo = tempF;
                } else if (tempF < minTempTodayPlusTwo){
                    minTempTodayPlusTwo = tempF;
                }
            }
        }
        weather.put("maxTemperatureTodayPlusOne", maxTempTodayPlusOne);
        weather.put("maxTemperatureTodayPlusTwo", maxTempTodayPlusTwo);
        weather.put("minTemperatureTodayPlusOne", minTempTodayPlusOne);
        weather.put("minTemperatureTodayPlusTwo", minTempTodayPlusTwo);

        //Precipitation chance per day
        float precipProbabilityToday = 0.0f;
        float precipProbabilityTodayPlusOne = 0.0f;
        float precipProbabilityTodayPlusTwo = 0.0f;

        for (int i = 0; i < precipitation.length; i++) {
            String precipS = precipitation[i];
            Float precipF = Float.parseFloat(precipS);
            System.out.println(precipF);

            if (LocalDateTime.parse(timeDate[i],DateTimeFormatter.ISO_LOCAL_DATE_TIME).toLocalDate().equals(today)){
                System.out.println("today");
                if(precipF > precipProbabilityToday){
                    precipProbabilityToday = precipF;
                }
            }else if (LocalDateTime.parse(timeDate[i],DateTimeFormatter.ISO_LOCAL_DATE_TIME).toLocalDate().equals(todayPlusOne)){
                System.out.println("Todayplusone");
                if(precipF > precipProbabilityTodayPlusOne){
                    precipProbabilityTodayPlusOne = precipF;
                }
            }else if (LocalDateTime.parse(timeDate[i],DateTimeFormatter.ISO_LOCAL_DATE_TIME).toLocalDate().equals(todayPlusTwo)){
                System.out.println("todayplustwo");
                if(precipF > precipProbabilityTodayPlusTwo){
                    precipProbabilityTodayPlusTwo= precipF;
                }
            }
        }

        weather.put("precipitationChanceToday", precipProbabilityToday);
        weather.put("precipitationChanceTodayPlusOne", precipProbabilityTodayPlusOne);
        weather.put("precipitationChanceTodayPlusTwo", precipProbabilityTodayPlusTwo);

        //humidity
        String humidityS;
        float humidityF, maxHumidityTodayPlusOne = 0, maxHumidityTodayPlusTwo = 0;
        
        for(int i = 0; i < humidity.length; i++) {
        	humidityS = humidity[i];
        	humidityF = Float.parseFloat(humidityS);
        	System.out.println("Humidity: " + humidityF);
        	
        	if (LocalDateTime.parse(timeDate[i],DateTimeFormatter.ISO_LOCAL_DATE_TIME).toLocalDate().equals(todayPlusOne)){
            	if(humidityF > maxHumidityTodayPlusOne) {
            		maxHumidityTodayPlusOne = humidityF;
            	}
            	System.out.println("maxHumidity todayPlusOne #" + "i: " +  maxHumidityTodayPlusOne);
            } else if (LocalDateTime.parse(timeDate[i],DateTimeFormatter.ISO_LOCAL_DATE_TIME).toLocalDate().equals(todayPlusTwo)){
            	if(humidityF > maxHumidityTodayPlusTwo) {
            		maxHumidityTodayPlusTwo = humidityF;
            	}
            	System.out.println("maxHumidity todayPlusTwo #" + "i: " +  maxHumidityTodayPlusTwo);
            }
        }
        
        weather.put("maxHumidityTodayPlusOne", maxHumidityTodayPlusOne);
        weather.put("maxHumidityTodayPlusTwo", maxHumidityTodayPlusTwo);
        
        //pressure
        String pressureS;
        float pressureF, maxPressureTodayPlusOne = 0, maxPressureTodayPlusTwo = 0;
        
        for(int i = 0; i < pressure.length; i++) {
        	pressureS = pressure[i];
        	pressureF = Float.parseFloat(pressureS);
        	System.out.println("Pressure: " + pressureF);
        	
	        if (LocalDateTime.parse(timeDate[i],DateTimeFormatter.ISO_LOCAL_DATE_TIME).toLocalDate().equals(todayPlusOne)){
	        	if(pressureF > maxPressureTodayPlusOne) {
	        		maxPressureTodayPlusOne = pressureF;
	        	}
	        } else if (LocalDateTime.parse(timeDate[i],DateTimeFormatter.ISO_LOCAL_DATE_TIME).toLocalDate().equals(todayPlusTwo)){
	        	if(pressureF > maxPressureTodayPlusOne) {
	        		maxPressureTodayPlusTwo = pressureF;
	        	}
	        }
        }
        
        weather.put("maxPressureTodayPlusOne", maxPressureTodayPlusOne);
        weather.put("maxPressureTodayPlusTwo", maxPressureTodayPlusTwo);
        
        //weather name & weather number
        String weatherNumS;
        int weatherNumI;
        int[] todayPlusOneCounter = {0, 0, 0, 0, 0, 0, 0}, 
        		todayPlusTwoCounter = {0, 0, 0, 0, 0, 0, 0};
        
        for(int i = 0; i < weatherNumber.length; i++) {
        	weatherNumS = weatherNumber[i];
        	weatherNumI = Integer.parseInt(weatherNumS);
        	
	        if (LocalDateTime.parse(timeDate[i],DateTimeFormatter.ISO_LOCAL_DATE_TIME).toLocalDate().equals(todayPlusOne)){
	        	if(weatherNumI >= 200 && weatherNumI <= 232) {
	    			todayPlusOneCounter[0]++;
	    		}
	    		else if(weatherNumI >= 300 && weatherNumI <= 321) {
	    			todayPlusOneCounter[1]++;
	    		}
	    		else if(weatherNumI >= 500 && weatherNumI <= 531) {
	    			todayPlusOneCounter[2]++;
	    		}
	    		else if(weatherNumI >= 600 && weatherNumI <= 622) {
	    			todayPlusOneCounter[3]++;
	    		}
	    		else if(weatherNumI >= 701 && weatherNumI <= 781) {
	    			todayPlusOneCounter[4]++;
	    		}
	    		else if(weatherNumI == 800) {
	    			todayPlusOneCounter[5]++;
	    		}
	    		else if(weatherNumI >= 801 && weatherNumI <= 804) {
	    			todayPlusOneCounter[6]++;
	    		}
	        } else if (LocalDateTime.parse(timeDate[i],DateTimeFormatter.ISO_LOCAL_DATE_TIME).toLocalDate().equals(todayPlusTwo)){
	        	if(weatherNumI >= 200 && weatherNumI <= 232) {
	    			todayPlusTwoCounter[0]++;
	    		}
	    		else if(weatherNumI >= 300 && weatherNumI <= 321) {
	    			todayPlusTwoCounter[1]++;
	    		}
	    		else if(weatherNumI >= 500 && weatherNumI <= 531) {
	    			todayPlusTwoCounter[2]++;
	    		}
	    		else if(weatherNumI >= 600 && weatherNumI <= 622) {
	    			todayPlusTwoCounter[3]++;
	    		}
	    		else if(weatherNumI >= 701 && weatherNumI <= 781) {
	    			todayPlusTwoCounter[4]++;
	    		}
	    		else if(weatherNumI == 800) {
	    			todayPlusTwoCounter[5]++;
	    		}
	    		else if(weatherNumI >= 801 && weatherNumI <= 804) {
	    			todayPlusTwoCounter[6]++;
	    		}
	        }
        }
        
        int todayPlusOneWeatherNum = 0, todayPlusTwoWeatherNum = 0, 
        		maxTodayPlusOne = 0, maxTodayPlusOneIndex = 0, 
        		maxTodayPlusTwo = 0, maxTodayPlusTwoIndex = 0;
        String todayPlusOneWeatherName = null, todayPlusTwoWeatherName = null;
        
      	for(int i = 0; i < 7; i++) {
      		if(todayPlusOneCounter[i] > maxTodayPlusOne) {
      			maxTodayPlusOne = todayPlusOneCounter[i];
      			maxTodayPlusOneIndex = i;
      		}
      		
      		if(todayPlusTwoCounter[i] > maxTodayPlusTwo) {
      			maxTodayPlusTwo = todayPlusTwoCounter[i];
      			maxTodayPlusTwoIndex = i;
      		}
      	}
      	
      	if(maxTodayPlusOneIndex == 0) {
      		todayPlusOneWeatherNum = 211;
      		todayPlusOneWeatherName = "thunderstorm";
      	}
      	else if(maxTodayPlusOneIndex == 1) {
      		todayPlusOneWeatherNum = 301;
      		todayPlusOneWeatherName = "drizzle";
      	}
      	else if(maxTodayPlusOneIndex == 2) {
      		todayPlusOneWeatherNum = 501;
      		todayPlusOneWeatherName = "Rain";
      	}
      	else if(maxTodayPlusOneIndex == 3) {
      		todayPlusOneWeatherNum = 601;
      		todayPlusOneWeatherName = "Snow";
      	}
      	else if(maxTodayPlusOneIndex == 4) {
      		todayPlusOneWeatherNum = 701;
      		todayPlusOneWeatherName = "Mist";
      	}
      	else if(maxTodayPlusOneIndex == 5) {
      		todayPlusOneWeatherNum = 800;
      		todayPlusOneWeatherName = "Clear Sky";
      	}
      	else if(maxTodayPlusOneIndex == 6) {
      		todayPlusOneWeatherNum = 801;
      		todayPlusOneWeatherName = "Cloudy";
      	}
      	
      	if(maxTodayPlusTwoIndex == 0) {
      		todayPlusTwoWeatherNum = 211;
      		todayPlusTwoWeatherName = "thunderstorm";
      	}
      	else if(maxTodayPlusTwoIndex == 1) {
      		todayPlusTwoWeatherNum = 301;
      		todayPlusTwoWeatherName = "drizzle";
      	}
      	else if(maxTodayPlusTwoIndex == 2) {
      		todayPlusTwoWeatherNum = 501;
      		todayPlusTwoWeatherName = "Rain";
      	}
      	else if(maxTodayPlusTwoIndex == 3) {
      		todayPlusTwoWeatherNum = 601;
      		todayPlusTwoWeatherName = "Snow";
      	}
      	else if(maxTodayPlusTwoIndex == 4) {
      		todayPlusTwoWeatherNum = 701;
      		todayPlusTwoWeatherName = "Mist";
      	}
      	else if(maxTodayPlusTwoIndex == 5) {
      		todayPlusTwoWeatherNum = 800;
      		todayPlusTwoWeatherName = "Clear Sky";
      	}
      	else if(maxTodayPlusTwoIndex == 6) {
      		todayPlusTwoWeatherNum = 801;
      		todayPlusTwoWeatherName = "Cloudy";
      	}
        
        weather.put("todayPlusOneWeatherName", todayPlusOneWeatherName);
        weather.put("todayPlusTwoWeatherName", todayPlusTwoWeatherName);
        weather.put("todayPlusOneWeatherNum", todayPlusOneWeatherNum);
        weather.put("todayPlusTwoWeatherNum", todayPlusTwoWeatherNum);
        
        return weather;
    }
}


