package examples;

import dao.EntityDAO;
import jdbc.ConnectionException;
import dao.PSQLJsonBException;
import entity.EntityService;
import document.Record;
import jdbc.ConnectionFactory;

import java.sql.Connection;
import java.sql.SQLException;

import static entity.EntityFilter.eq;

public class Delete {

    public static void main(String[] args) throws SQLException {
        //Get Database Connection
        try {
            Connection connection = new ConnectionFactory()
                    .getConnection("jdbc:postgresql://localhost:5433/docker", "docker", "docker");

            EntityDAO entityDAO = new EntityService(connection).getEntityDAO();
            entityDAO.delete(eq("age", "30"), Record.class);

            connection.close();
        } catch (ConnectionException | PSQLJsonBException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
