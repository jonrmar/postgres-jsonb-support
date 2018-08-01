package examples;

import dao.EntityDAO;
import dao.PSQLJsonBException;
import document.Record;
import entity.EntityService;
import jdbc.Connection;
import jdbc.ConnectionException;
import jdbc.ConnectionFactory;

import static entity.EntityFilter.eq;

public class Delete {

    public static void main(String[] args) {
        //Get Database Connection
        try {
            ConnectionFactory factory = new ConnectionFactory("jdbc:postgresql://localhost:5433/docker", "docker", "docker");
            Connection connection = factory.createConnection();

            EntityDAO entityDAO = new EntityService(connection).getEntityDAO();
            entityDAO.delete(eq("age", "30"), Record.class);

            connection.close();
        } catch (ConnectionException | PSQLJsonBException e) {
            e.printStackTrace();
        }
    }
}
