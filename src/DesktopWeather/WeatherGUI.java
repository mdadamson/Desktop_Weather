package DesktopWeather;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class WeatherGUI extends Application {
    //Variables
    Button enterValue;
    static TextField zipValue;
    static TextArea weatherFirst;
    static TextArea weatherSecond;
    static TextArea weatherThird;
    static Label locationName;
    static Label timeDate;
    WeatherTimeDate timeDateInt = new WeatherTimeDate();
    FileInputStream firstImage;
    FileInputStream secondImage;
    FileInputStream thirdImage;
    static Image dayOneImage;
    static Image dayTwoImage;
    static Image dayThreeImage;
    static ImageView dayOne;
    static ImageView dayTwo;
    static ImageView dayThree;
    String initialDayOne = "file:weather_icons\\icon_sunny.png";
    String initialDayTwo = "file:weather_icons\\icon_sunny.png";
    String initialDayThree = "file:weather_icons\\icon_sunny.png";

    public void start (Stage weatherStage) throws Exception {

        //Placeholder for BorderPane
        Text rightText = new Text("");
        Text leftText = new Text("");

        //Variables - label for the zipcode, and the weather images
        Label zipLabel;

        //Image for weather for first of three day weather forecast
        //firstImage = new FileInputStream("weather_icons\\icon_sunny.png");
        dayOneImage = new Image(initialDayOne);
        dayOne = new ImageView();
        dayOne.setImage(dayOneImage);
        dayOne.setFitHeight(170);
        dayOne.setFitWidth(170);

        //Image for weather for second of three day weather forecast
        //secondImage = new FileInputStream("weather_icons\\icon_snowy.png");
        dayTwoImage = new Image(initialDayTwo);
        dayTwo = new ImageView();
        dayTwo.setImage(dayTwoImage);
        dayTwo.setFitHeight(170);
        dayTwo.setFitWidth(170);

        //Image for weather for third of three day weather forecast
        //thirdImage = new FileInputStream("weather_icons\\icon_lightning_cloudy.png");
        dayThreeImage = new Image(initialDayThree);
        dayThree = new ImageView();
        dayThree.setImage(dayThreeImage);
        dayThree.setFitHeight(170);
        dayThree.setFitWidth(170);

        //TextArea holding weather data output
        //Default location name
        locationName = new Label();
        timeDate = new Label(timeDateInt.getTimeDate());
        zipLabel = new Label("Enter Zip Code");

        zipValue = new TextField();
        zipValue.setPromptText("Zip Code");
        zipValue.setMaxWidth(130);

        enterValue = new Button("Enter");

        HBox inputField = new HBox(10, zipLabel, zipValue, enterValue);
        inputField.setAlignment(Pos.CENTER);

        //TextArea Properties
        weatherFirst = new TextArea();
        weatherFirst.setMaxHeight(140);
        weatherFirst.setMaxWidth(250);
        weatherFirst.setEditable(false);

        //TextArea Properties
        weatherSecond = new TextArea();
        weatherSecond.setMaxHeight(140);
        weatherSecond.setMaxWidth(250);
        weatherSecond.setEditable(false);

        //TextArea Properties
        weatherThird = new TextArea();
        weatherThird.setMaxHeight(140);
        weatherThird.setMaxWidth(250);
        weatherThird.setEditable(false);

        //Layout container for weather data
        HBox weatherDisplay = new HBox(40, weatherFirst, weatherSecond, weatherThird);
        weatherDisplay.setAlignment(Pos.CENTER);

        //Layout container for weather images
        HBox weatherImage = new HBox(120, dayOne, dayTwo, dayThree);
        weatherImage.setAlignment(Pos.CENTER);

        //Layout container for weather data and images
        VBox weatherView = new VBox(1, weatherImage, weatherDisplay);
        weatherDisplay.setAlignment(Pos.CENTER);

        VBox labelView = new VBox(1, locationName, timeDate);
        labelView.setAlignment(Pos.CENTER);

        // Set the alignment of the Top Text to Center
        BorderPane.setAlignment(labelView, Pos.TOP_CENTER);

        // Set the alignment of the Bottom Text to Center
        BorderPane.setAlignment(inputField, Pos.BOTTOM_CENTER);

        // Create a BorderPane with a Text node in each of the five regions
        BorderPane root = new BorderPane(weatherView, labelView, rightText, inputField, leftText);
        // Set the Size of the VBox
        root.setPrefSize(900, 450);
        // Set the Style-properties of the BorderPane
        root.setStyle("-fx-padding: 10;" + "-fx-border-style: solid inside;");

        // Create the Scene
        Scene scene = new Scene(root);
        // Add the scene to the Stage
        weatherStage.setScene(scene);
        // Set the title of the Stage
        weatherStage.setTitle("Weather Application");
        // Display the Stage
        weatherStage.show();

        WeatherInput handler = new WeatherInput();
        //zipValue.setOnAction(handler);
        enterValue.setOnAction(handler);

        System.out.println(zipValue.getText()); //can be deleted later, here for testing purposes
    }
    //retrieve the zip code input from the textfield
    public static String getZipInput() {
        String zipInput = zipValue.getText();
        return zipInput;
    }
    //set the zip code value to an empty string
    public static void setZipInput() {
        zipValue.setText("");
    }
    //set the text inside the first text area on the GUI
    public static void setWeatherFirst(String value) {
        weatherFirst.setText(value);
    }
    //set the text inside the second text area on the GUI
    public static void setWeatherSecond(String value) {
        weatherSecond.setText(value);
    }
    //set the text inside the third text area on the GUI
    public static void setWeatherThird(String value) {
        weatherThird.setText(value);
    }
    //sets the text in the label above the three images
    public static void setLabelInput(String value) {
        locationName.setText(value);
    }
    //setter for day one Image
    public void setDayOneImage(String value) {
        Platform.runLater(() -> {
                initialDayOne = value;
                dayOneImage = new Image(initialDayOne);
                dayOne.setImage(dayOneImage);
            }
        );
    }
    //setter for day two Image
    public void setDayTwoImage(String value) {
        Platform.runLater(() -> {
                initialDayTwo = value;
                dayTwoImage = new Image(initialDayTwo);
                dayTwo.setImage(dayTwoImage);
            }
        );
    }
    //setter for day three Image
    public void setDayThreeImage(String value) {
        Platform.runLater(() -> {
                initialDayThree = value;
                dayThreeImage = new Image(initialDayThree);
                dayThree.setImage(dayThreeImage);
            }
        );
    }
    //settings for the error message box if user enters invalid zip code input
    public static void dialogBox (){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.initStyle(StageStyle.UTILITY);
        alert.setTitle("Warning");
        alert.setHeaderText("Invalid Input");
        alert.setContentText("Invalid input or zipcode!");

        alert.showAndWait();
    }
    //settings for the error message box if the internet connection fails
    public static void intCheckBox () {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.initStyle(StageStyle.UTILITY);
        alert.setTitle("Desktop Weather Application");
        alert.setHeaderText("Not connected to internet!");
        alert.setContentText("Please check your internet connection!");
        alert.showAndWait();
    }
    //settings for the error message box if the internet connection fails
    public static void intAlertBox () throws InterruptedException {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        //alert.initStyle(StageStyle.UTILITY);
        alert.setTitle("Desktop Weather Application");
        alert.setHeaderText("Checking Internet Connection!");
        alert.setContentText("Please wait!");
        alert.show();
        Platform.runLater(() -> {
                try {
                    Thread.sleep(3000);
                    alert.close();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        );
    }

}