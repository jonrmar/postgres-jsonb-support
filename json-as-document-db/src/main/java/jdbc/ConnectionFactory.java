package jdbc;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionFactory {

    private jdbc.Connection connection;
    private String host;
    private String user;
    private String password;

    public ConnectionFactory(String host, String user, String password) {
        this.host = host;
        this.user = user;
        this.password = password;
    }

    public jdbc.Connection createConnection(boolean exportTable, boolean exportSchema) throws ConnectionException {
        try {
            this.connection = new jdbc.Connection(host, user, password, exportTable, exportSchema);
            return connection;
        } catch (SQLException e) {
            throw new ConnectionException("ERROR - On getting connection from database: host - " + host + "\n" + e);
        }
    }

    public jdbc.Connection createConnection() throws ConnectionException {
        try {
            this.connection = new jdbc.Connection(host, user, password);
            return connection;
        } catch (SQLException e) {
            throw new ConnectionException("ERROR - On getting connection from database: host - " + host + "\n" + e);
        }
    }
}
