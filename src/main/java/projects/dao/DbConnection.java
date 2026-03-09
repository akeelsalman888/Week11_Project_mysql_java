package projects.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import projects.exception.DbException;

public class DbConnection {

    private static final String HOST = "localhost";
    private static final String USER = "projects";
    private static final String PASSWORD = "projects";
    private static final int PORT = 3306;
    private static final String SCHEMA = "projects";

    // Returns a connection without printing anything automatically
    public static Connection getConnection() {
        String uri = String.format(
                "jdbc:mysql://%s:%d/%s?user=%s&password=%s",
                HOST, PORT, SCHEMA, USER, PASSWORD
        );

        try {
            return DriverManager.getConnection(uri);
        } catch (SQLException e) {
            throw new DbException("Unable to get connection at " + uri, e);
        }
    }

    // Optional helper to print connection success if needed
    public static void printConnectionSuccess() {
        System.out.println("Connection to schema '" + SCHEMA + "' is successful.");
    }
}