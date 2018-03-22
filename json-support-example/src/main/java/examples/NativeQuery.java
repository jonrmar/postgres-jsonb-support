package examples;

import dao.EntityDAO;
import dao.exceptions.ConnectionException;
import dao.exceptions.PSQLJsonBException;
import entity.Entity;
import entity.EntityService;
import jdbc.ConnectionFactory;

import java.sql.Connection;
import java.util.List;

public class NativeQuery {
    public static void main(String[] args) {
        //Get Database Connection
        try {
            Connection connection = new ConnectionFactory()
                    .getConnection("jdbc:postgresql://localhost:5433/docker", "docker", "docker");

            String query = "delete from entity where document ->> 'age' = '30'";
            EntityDAO entityDAO = new EntityService(connection).getEntityDAO();
            List<Entity> entities = entityDAO.nativeQuery(query);

            for (Entity entity : entities)
                System.out.println(entity);

        } catch (ConnectionException | PSQLJsonBException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
