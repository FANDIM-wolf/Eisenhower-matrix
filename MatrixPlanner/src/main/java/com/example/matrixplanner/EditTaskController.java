package com.example.matrixplanner;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class EditTaskController {

    @FXML
    private TextField taskIdField;
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
    private int taskId;

    public void setDatabaseConnection(DataConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    public void setData(int taskId, String taskName, String taskDescription, LocalDate dateOfEnd, String timeOfEx, String category) {
        this.taskId = taskId;
        taskIdField.setText(String.valueOf(taskId));
        taskNameField.setText(taskName);
        taskDescriptionField.setText(taskDescription);
        dateOfEndField.setValue(dateOfEnd);
        timeOfExField.setText(timeOfEx);
        categoryField.setText(category);
    }

    @FXML
    protected void handleSave(ActionEvent event) {
        String newTaskName = taskNameField.getText();
        String newTaskDescription = taskDescriptionField.getText();
        LocalDate newDateOfEnd = dateOfEndField.getValue();
        LocalTime newTimeOfEx = LocalTime.parse(timeOfExField.getText(), DateTimeFormatter.ofPattern("HH:mm"));
        String newCategory = categoryField.getText();

        updateTaskInDatabase(taskId, newTaskName, newTaskDescription, newDateOfEnd, newTimeOfEx, newCategory);
    }

    private void updateTaskInDatabase(int taskId, String taskName, String taskDescription, LocalDate dateOfEnd, LocalTime timeOfEx, String category) {
        String query = "UPDATE Task SET Name = ?, Description = ?, Date_of_the_end = ?, Time_of_ex = ?, category = ? WHERE Task_id = ?";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, taskName);
            preparedStatement.setString(2, taskDescription);
            preparedStatement.setDate(3, java.sql.Date.valueOf(dateOfEnd));
            preparedStatement.setTime(4, java.sql.Time.valueOf(timeOfEx));
            preparedStatement.setString(5, category);
            preparedStatement.setInt(6, taskId);
            preparedStatement.executeUpdate();

            System.out.println("Task updated in the database");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
