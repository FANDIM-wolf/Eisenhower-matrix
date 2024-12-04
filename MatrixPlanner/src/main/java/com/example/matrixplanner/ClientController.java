package com.example.matrixplanner;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClientController {

    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

    private DataConnection databaseConnection;

    public void setDatabaseConnection(DataConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    @FXML
    protected void handleSubmit(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        int userId = validateUser(username, password);
        if (userId != -1) {
            writeUserDataToJson(username, password, userId);
        } else {
            System.out.println("Invalid username or password");
        }
    }

    private int validateUser(String username, String password) {
        String query = "SELECT Id_user FROM User WHERE User_name = ? AND User_password = ?";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("Id_user");
            } else {
                return -1; // Invalid user
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1; // Invalid user
        }
    }

    private void writeUserDataToJson(String username, String password, int userId) {
        ObjectMapper objectMapper = new ObjectMapper();
        User user = new User();
        user.setId(userId);
        user.setName(username);
        user.setPassword(password);

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("scratch.json")) {
            if (inputStream == null) {
                throw new IOException("scratch.json not found");
            }

            // Read existing data from scratch.json
            User existingUser = objectMapper.readValue(inputStream, User.class);

            // Update the existing user data
            existingUser.setId(userId);
            existingUser.setName(username);
            existingUser.setPassword(password);

            // Write the updated data back to scratch.json
            File file = new File(getClass().getClassLoader().getResource("scratch.json").toURI());
            objectMapper.writeValue(file, existingUser);
            System.out.println("User data updated in scratch.json");
        } catch (IOException | java.net.URISyntaxException e) {
            e.printStackTrace();
        }
    }
}

