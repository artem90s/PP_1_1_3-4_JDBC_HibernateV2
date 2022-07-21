package jm.task.core.jdbc.util;

import com.mysql.jdbc.Driver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Util {
    private static final String userName = "root";
    private static final String password = "root";
    private static final String connectionUrl = "jdbc:mysql://localhost:3306/main";

    public Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(connectionUrl, userName, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }


}
