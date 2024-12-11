package com.example.matrixplanner;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataConnection {
    private static final String URL = "jdbc:sqlite:D:\\Data\\test.db"; // Change this path

    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL);
            System.out.println("Connection to SQLite has been established.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return connection;
    }

    public void logUser(String userName, String timeOfLogin) {
        String query = "INSERT INTO User_logs (User_name, Time_of_login) VALUES (?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, timeOfLogin);
            preparedStatement.executeUpdate();

            System.out.println("User logged successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void archiveUser(String userName, String timeOfArchiving) {
        String query = "INSERT INTO User_archives (User_name, Time_of_archiving) VALUES (?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, timeOfArchiving);
            preparedStatement.executeUpdate();

            System.out.println("User archived successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Object[]> getTasksWithDetailsByUserId(int userId) {
        List<Object[]> tasks = new ArrayList<>();
        String query = "SELECT t.Task_id, t.Name, t.Description, t.Date_of_the_end, t.Time_of_ex, t.category " +
                "FROM Task t " +
                "JOIN Users_tasks ut ON t.Task_id = ut.Id_task " +
                "WHERE ut.Id_user = ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Object[] taskData = new Object[7];
                taskData[0] = userId; // Add userId as the first element
                taskData[1] = resultSet.getInt("Task_id");
                taskData[2] = resultSet.getString("Name");
                taskData[3] = resultSet.getString("Description");
                taskData[4] = resultSet.getString("Date_of_the_end");
                taskData[5] = resultSet.getString("Time_of_ex");
                taskData[6] = resultSet.getString("category");
                tasks.add(taskData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tasks;
    }
}
