
package com.example.matrixplanner;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class ShowCasesController {

    @FXML
    private VBox casesContainer;


    public void setData(List<Object[]> cases) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (Object[] caseObj : cases) {
            HBox caseBox = new HBox(10);

            // Assuming caseObj is an array with userId, taskId, taskText, description, dateOfEnd, timeOfEx, category
            int userId = Integer.parseInt(caseObj[0].toString());
            int taskId = Integer.parseInt(caseObj[1].toString());
            String taskText = (String) caseObj[2];
            String description = (String) caseObj[3];
            LocalDate dateOfEnd = (LocalDate) caseObj[4];
            String timeOfEx = (String) caseObj[5];
            String category = (String) caseObj[6];

            // Combine all data into one string with spaces
            String combinedText = userId + " " + taskId + " " + taskText + " " + description + " " + dateOfEnd + " " + timeOfEx + " " + category;

            Label caseLabel = new Label(combinedText);
            Button editButton = new Button("Edit");
            Button deleteButton = new Button("Delete");

            // Apply styles from CSS file
            editButton.getStyleClass().add("edit-button");
            deleteButton.getStyleClass().add("delete-button");

            // Add action handlers for buttons if needed
            final int finalUserId = userId;
            final int finalTaskId = taskId;
            final String finalTaskText = taskText;
            final String finalDescription = description;
            final LocalDate finalDateOfEnd = dateOfEnd;
            final String finalTimeOfEx = timeOfEx;
            final String finalCategory = category;

            editButton.setOnAction(event -> handleEdit(finalUserId, finalTaskId, finalTaskText, finalDescription, finalDateOfEnd, finalTimeOfEx, finalCategory));
            deleteButton.setOnAction(event -> handleDelete(finalUserId, finalTaskId, finalTaskText, finalDescription, finalDateOfEnd, finalTimeOfEx, finalCategory));

            caseBox.getChildren().addAll(caseLabel, editButton, deleteButton);
            casesContainer.getChildren().add(caseBox);
        }
    }

    private void handleEdit(int userId, int taskId, String taskText, String description, LocalDate dateOfEnd, String timeOfEx, String category) {
        openEditWindow(userId, taskId, taskText, description, dateOfEnd, timeOfEx, category);
    }

    private void handleDelete(int userId, int taskId, String taskText, String description, LocalDate dateOfEnd, String timeOfEx, String category) {
        // Implement delete functionality
        System.out.println("Delete: User ID = " + userId + ", Task ID = " + taskId + ", Task Text = " + taskText + ", Description = " + description + ", Date of End = " + dateOfEnd + ", Time of Ex = " + timeOfEx + ", Category = " + category);
    }

    private void openEditWindow(int userId, int taskId, String taskText, String description, LocalDate dateOfEnd, String timeOfEx, String category) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("edit_task.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 400, 300);

            // Pass data to the controller
            EditTaskController controller = fxmlLoader.getController();
            controller.setDatabaseConnection(new DataConnection());
            controller.setData(taskId, taskText, description, dateOfEnd, timeOfEx, category);

            Stage newStage = new Stage();
            newStage.setTitle("Edit Task");
            newStage.setScene(scene);
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
