package com.example.matrixplanner;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class HelloApplication extends Application {
    private DataConnection databaseConnection;

    @Override
    public void start(Stage stage) throws IOException {
        // Load the FXML file
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("layout.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 250);

        // Pass the main application instance to the controller
        HelloController controller = fxmlLoader.getController();
        controller.setMainApp(this);

        // Initialize your database connection
        databaseConnection = new DataConnection();

        // Set up the stage
        stage.setTitle("SQLite Connection Example");
        stage.setScene(scene);
        stage.getIcons().add(new Image(new File("D:\\Data\\icon.png").toURI().toURL().toString()));
        stage.show();
    }

    public DataConnection getDatabaseConnection() {
        return databaseConnection;
    }

    public static void main(String[] args) {
        launch();
    }
}