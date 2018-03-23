package examples;

import dao.EntityDAO;
import dao.exceptions.ConnectionException;
import dao.exceptions.PSQLJsonBException;
import document.Record;
import entity.EntityFilter;
import entity.EntityService;
import jdbc.ConnectionFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static entity.EntityFilter.asList;

public class Read {
    public static void main(String[] args) throws ClassNotFoundException {
        //Get Database Connection
        try {
            Connection connection = new ConnectionFactory()
                    .getConnection("jdbc:postgresql://localhost:5433/docker", "docker", "docker");

            EntityDAO entityDAO = new EntityService(connection).getEntityDAO();

            List<Record> records = entityDAO.find(EntityFilter.gt("age", "28"), Record.class);

            for (Record record : records)
                System.out.println(record);

            connection.close();
        } catch (ConnectionException | PSQLJsonBException | SQLException e) {
            e.printStackTrace();
        }
    }
}
