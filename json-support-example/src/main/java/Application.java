import dao.EntityDAO;
import domain.Entity;
import jdbc.ConnectionFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Application {

    public static void main(String[] args) throws SQLException {
        Entity entity = new Entity();
        entity.setId(1L);
        Map<String, Object> map = new HashMap<>();
        map.put("name", "John Doe");
        map.put("age", "30");
        entity.setDocument(map);

        //Creating DB connection
        Connection connection = new ConnectionFactory().getConnection();

        EntityDAO entityDAO = new EntityDAO(connection);

        System.out.println("### Testing save operation ### ");
        entityDAO.save(entity);

        System.out.println("### Testing findAll operation ###");
        entityDAO = new EntityDAO(connection);
        List<Entity> entities = entityDAO.findAll();

        System.out.println("Result from reading on db:");
        for (Entity entityRead: entities)
            System.out.println(entityRead);

        connection = new ConnectionFactory().getConnection();
        System.out.println("### Testing update operation ###");
        entityDAO = new EntityDAO(connection);
        entity.setId(1L);
        map.remove("name");
        map.put("name", "Name Test");
        entity.setDocument(map);
        entityDAO.update(entity);
        entities = entityDAO.findAll();
        System.out.println("Result from updating on db:");
        for (Entity entityRead: entities)
            System.out.println(entityRead);

        connection = new ConnectionFactory().getConnection();
        System.out.println("### Testing delete operation ###");
        entityDAO = new EntityDAO(connection);
        entityDAO.delete(1L);
        entities = entityDAO.findAll();
        System.out.println("Result from delete on db:");
        for (Entity entityRead: entities)
            System.out.println(entityRead);
    }
}
