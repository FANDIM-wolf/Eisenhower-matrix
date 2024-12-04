package com.example.matrixplanner;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
