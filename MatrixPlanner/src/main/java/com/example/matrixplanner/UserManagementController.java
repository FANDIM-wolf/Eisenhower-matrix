package com.example.matrixplanner;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UserManagementController {

    @FXML
    private TextField usernameField;

    private DataConnection databaseConnection;

    public void setDatabaseConnection(DataConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    @FXML
    protected void handleLogUser(ActionEvent event) {
        String userName = usernameField.getText();
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = now.format(formatter);
        databaseConnection.logUser(userName, formattedDateTime);
    }

    @FXML
    protected void handleArchiveUser(ActionEvent event) {
        String userName = usernameField.getText();
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = now.format(formatter);

        databaseConnection.archiveUser(userName, formattedDateTime);
        archiveUserTasks(userName);
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

    private void archiveUserTasks(String userName) {
        String query = "SELECT t.Task_id, t.Name, t.Description, t.Date_of_the_end, t.Time_of_ex, t.category " +
                "FROM Task t " +
                "JOIN Users_tasks ut ON t.Task_id = ut.Id_task " +
                "WHERE ut.Id_user = ?";
        // String query = "SELECT * FROM Task WHERE Id_user = (SELECT Id_user FROM User WHERE User_name = ?)";
        int userId = getUserIdFromScratchJson();
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            // Сохранение данных в текстовый файл
            String fileName = userName + "_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".txt";
            try (FileWriter fileWriter = new FileWriter(fileName)) {
                while (resultSet.next()) {
                    int taskId = resultSet.getInt("Task_id");
                    String taskName = resultSet.getString("Name");
                    String taskDescription = resultSet.getString("Description");
                    String dateOfEnd = resultSet.getString("Date_of_the_end");
                    String timeOfEx = resultSet.getString("Time_of_ex");
                    String category = resultSet.getString("category");

                    fileWriter.write("Task ID: " + taskId + "\n");
                    fileWriter.write("Task Name: " + taskName + "\n");
                    fileWriter.write("Task Description: " + taskDescription + "\n");
                    fileWriter.write("Date of End: " + dateOfEnd + "\n");
                    fileWriter.write("Time of Execution: " + timeOfEx + "\n");
                    fileWriter.write("Category: " + category + "\n");
                    fileWriter.write("\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }



            System.out.println("User tasks archived successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
