package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    public Connection getConnection(String host, String user, String password ){
        try {
            return DriverManager.getConnection(host, user, password);
        } catch (SQLException e) {
            throw  new RuntimeException(e);
        }
    }
}
