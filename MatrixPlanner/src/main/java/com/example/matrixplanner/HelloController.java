package com.example.matrixplanner;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class HelloController {
    private HelloApplication mainApp;

    public void setMainApp(HelloApplication mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick(ActionEvent event) {
        welcomeText.setText("Hello Button Clicked!");
    }

    @FXML
    protected void handleOpenWindow1(ActionEvent event) {
        int userId = getUserIdFromScratchJson();
        if (userId != -1) {
            List<Object[]> tasks = mainApp.getDatabaseConnection().getTasksWithDetailsByUserId(userId);
            openNewWindow("Show Cases", "show_cases.fxml", tasks.toArray(new Object[0][]));
        } else {
            System.out.println("User ID not found in scratch.json");
        }
    }

    @FXML
    protected void handleOpenWindow2(ActionEvent event) {
        openNewWindow("Add Client", "client.fxml", null);
    }

    @FXML
    protected void handleOpenWindow3(ActionEvent event) {
        openNewWindow("Edit Client", "edit_client.fxml", null);
    }

    private void openNewWindow(String title, String fxmlFile, Object[] data) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));
            Scene scene = new Scene(fxmlLoader.load(), 300, 200);

            // Pass data to the controller if needed
            if (data != null && fxmlLoader.getController() instanceof ShowCasesController) {
                ShowCasesController controller = fxmlLoader.getController();
                controller.setData(data);
            } else if (fxmlLoader.getController() instanceof ClientController) {
                ClientController controller = fxmlLoader.getController();
                controller.setDatabaseConnection(mainApp.getDatabaseConnection());
            }

            Stage newStage = new Stage();
            newStage.setTitle(title);
            newStage.setScene(scene);
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleConnect(ActionEvent actionEvent) {
        // Test connection
        mainApp.getDatabaseConnection().getConnection();
    }

    private int getUserIdFromScratchJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("scratch.json")) {
            if (inputStream == null) {
                throw new IOException("scratch.json not found");
            }

            User user = objectMapper.readValue(inputStream, User.class);
            return user.getId();
        } catch (IOException e) {
            e.printStackTrace();
            return -1; // Invalid user ID
        }
    }
}
