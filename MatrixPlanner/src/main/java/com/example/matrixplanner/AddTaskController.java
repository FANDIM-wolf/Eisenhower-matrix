package com.example.matrixplanner;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class AddTaskController {
    @FXML
    private TextField taskNameField;
    @FXML
    private TextField taskDescriptionField;
    @FXML
    private DatePicker dateOfEndField;
    @FXML
    private TextField timeOfExField;
    @FXML
    private TextField categoryField;

    private DataConnection databaseConnection;

    public void setDatabaseConnection(DataConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    @FXML
    protected void handleSave(ActionEvent event) {
        String taskName = taskNameField.getText();
        String taskDescription = taskDescriptionField.getText();
        LocalDate dateOfEnd = dateOfEndField.getValue();
        LocalTime timeOfEx = null;
        String category = categoryField.getText();

        try {
            timeOfEx = LocalTime.parse(timeOfExField.getText(), DateTimeFormatter.ofPattern("HH:mm"));
        } catch (DateTimeParseException e) {
            System.out.println("Invalid time format. Please use HH:mm format.");
            return;
        }

        addTaskToDatabase(taskName, taskDescription, dateOfEnd, timeOfEx, category);
    }

    private void addTaskToDatabase(String taskName, String taskDescription, LocalDate dateOfEnd, LocalTime timeOfEx, String category) {
        int userId = getUserIdFromScratchJson();
        if (userId == -1) {
            System.out.println("User ID not found in scratch.json");
            return;
        }

        String taskQuery = "INSERT INTO Task (Name, Description, Date_of_the_end, Time_of_ex, category) VALUES (?, ?, ?, ?, ?)";
        String userTaskQuery = "INSERT INTO Users_tasks (Id_user, Id_task) VALUES (?, (SELECT Task_id FROM Task WHERE Name = ? AND Description = ? AND Date_of_the_end = ? AND Time_of_ex = ? AND category = ?))";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement taskStatement = connection.prepareStatement(taskQuery);
             PreparedStatement userTaskStatement = connection.prepareStatement(userTaskQuery)) {

            // Добавление задачи в таблицу Task
            taskStatement.setString(1, taskName);
            taskStatement.setString(2, taskDescription);
            taskStatement.setDate(3, java.sql.Date.valueOf(dateOfEnd));
            taskStatement.setTime(4, java.sql.Time.valueOf(timeOfEx));
            taskStatement.setString(5, category);
            taskStatement.executeUpdate();

            // Добавление записи в таблицу Users_tasks
            userTaskStatement.setInt(1, userId);
            userTaskStatement.setString(2, taskName);
            userTaskStatement.setString(3, taskDescription);
            userTaskStatement.setDate(4, java.sql.Date.valueOf(dateOfEnd));
            userTaskStatement.setTime(5, java.sql.Time.valueOf(timeOfEx));
            userTaskStatement.setString(6, category);
            userTaskStatement.executeUpdate();

            System.out.println("Task added to the database and Users_tasks table updated");
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
