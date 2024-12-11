package com.example.matrixplanner;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class AddTaskController {

    @FXML
    private TextField taskNameField;
    @FXML
    private TextField taskDescriptionField;
    @FXML
    private DatePicker dateOfEndField;
    @FXML
    private DatePicker timeOfExField;
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
        LocalDate timeOfEx = timeOfExField.getValue();
        String category = categoryField.getText();

        addTaskToDatabase(taskName, taskDescription, dateOfEnd, timeOfEx, category);
    }

    private void addTaskToDatabase(String taskName, String taskDescription, LocalDate dateOfEnd, LocalDate timeOfEx, String category) {
        String query = "INSERT INTO Task (Name, Description, Date_of_the_end, Time_of_ex, category) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, taskName);
            preparedStatement.setString(2, taskDescription);
            preparedStatement.setDate(3, java.sql.Date.valueOf(dateOfEnd));
            preparedStatement.setDate(4, java.sql.Date.valueOf(timeOfEx));
            preparedStatement.setString(5, category);
            preparedStatement.executeUpdate();

            System.out.println("Task added to the database");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
