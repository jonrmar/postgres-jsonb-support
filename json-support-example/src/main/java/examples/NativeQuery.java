package examples;

import dao.EntityDAO;
import dao.exceptions.ConnectionException;
import dao.exceptions.PSQLJsonBException;
import entity.Entity;
import entity.EntityFilter;
import entity.EntityService;
import entity.Record;
import jdbc.ConnectionFactory;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        } catch (ConnectionException | PSQLJsonBException e) {
            e.printStackTrace();
        }
    }
}
