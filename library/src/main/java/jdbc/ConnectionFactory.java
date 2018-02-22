package jdbc;

import dao.exceptions.ConnectionException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    public Connection getConnection(String host, String user, String password ) throws ConnectionException {
        try {
            return DriverManager.getConnection(host, user, password);
        } catch (SQLException e) {
            throw new ConnectionException("ERROR - On getting connection from database: host - " + host + "\n" + e);
        }
    }
}
