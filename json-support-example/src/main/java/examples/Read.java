package examples;

import dao.EntityDAO;
import dao.exceptions.ConnectionException;
import dao.exceptions.PSQLJsonBException;
import entity.Entity;
import entity.EntityFilter;
import entity.EntityService;
import jdbc.ConnectionFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static entity.EntityFilter.asList;

public class Read {
    public static void main(String[] args) throws SQLException {
        //Get Database Connection
        try {
            Connection connection = new ConnectionFactory()
                    .getConnection("jdbc:postgresql://localhost:5433/docker", "docker", "docker");

            EntityDAO entityDAO = new EntityService(connection).getEntityDAO();

            List<Entity> entities = entityDAO.findAll(); //(EntityFilter.eq("age", asList("soccer")));

            for (Entity entity : entities)
                System.out.println(entity);

            connection.close();
        } catch (ConnectionException | PSQLJsonBException e) {
            e.printStackTrace();
        }
    }
}
