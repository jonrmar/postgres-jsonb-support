package jdbc;

import java.sql.DriverManager;
import java.sql.SQLException;

public class Connection {
    private java.sql.Connection connection;
    private boolean exportTable;
    private boolean exportSchema;

    public Connection(String host, String user, String password,
                      boolean exportTable, boolean exportSchema) throws SQLException {
        this.connection = DriverManager.getConnection(host, user, password);
        this.exportTable = exportTable;
        this.exportSchema = exportSchema;
    }

    public Connection(String host, String user, String password) throws SQLException {
        this.connection = DriverManager.getConnection(host, user, password);
        this.exportTable = false;
        this.exportSchema = false;
    }

    public java.sql.Connection getConnection() {
        return connection;
    }

    public boolean isExportTable() {
        return exportTable;
    }

    public boolean isExportSchema() {
        return exportSchema;
    }

    public void enableExportTable() {
        this.exportTable = true;
    }

    public void enableExportSchema() {
        this.exportSchema = true;
    }

    public void close() throws ConnectionException {
        try {
            this.connection.close();
        } catch (SQLException e) {
            throw new ConnectionException("ERROR - On closing connection from database: " + e);
        }
    }
}
