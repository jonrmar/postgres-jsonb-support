package examples;

import dao.EntityDAO;
import dao.PSQLJsonBException;
import document.Record;
import entity.EntityFilter;
import entity.EntityService;
import jdbc.Connection;
import jdbc.ConnectionException;
import jdbc.ConnectionFactory;

import java.util.List;

public class Read {
    public static void main(String[] args) {
        //Get Database Connection
        try {
            ConnectionFactory factory = new ConnectionFactory("jdbc:postgresql://localhost:5433/docker", "docker", "docker");
            Connection connection = factory.createConnection();

            EntityDAO entityDAO = new EntityService(connection).getEntityDAO();

            List<Record> records = entityDAO.find(EntityFilter.gt("age", "28"), Record.class);

            for (Record record : records)
                System.out.println(record);

            connection.close();
        } catch (ConnectionException | PSQLJsonBException e) {
            e.printStackTrace();
        }
    }
}
