package examples;

import dao.EntityDAO;
import entity.Entity;
import entity.EntityService;
import jdbc.ConnectionFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class Read {
    public static void main(String[] args) throws SQLException {
        //Get Database Connection
        Connection connection = new ConnectionFactory()
                .getConnection("jdbc:postgresql://localhost:5433/docker", "docker", "docker");

        EntityDAO entityDAO = new EntityService(connection).getEntityDAO();

        List<Entity> entities = entityDAO.findAll();

        for (Entity entity : entities)
            System.out.println(entity);

        connection.close();
    }
}
