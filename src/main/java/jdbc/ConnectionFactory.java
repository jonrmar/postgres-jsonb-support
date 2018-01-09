package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    public Connection getConnection(){
        try {
            return DriverManager.getConnection("jdbc:postgresql://<IP>/<DATABASE>", "<USERNAME>", "<PASSWORD>");
        } catch (SQLException e) {
            throw  new RuntimeException(e);
        }
    }
}
