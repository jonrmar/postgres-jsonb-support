import dao.EntityDAO;
import domain.Entity;
import domain.Record;
import jdbc.ConnectionFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Application {

    public static void main(String[] args) throws SQLException {
        Record record = new Record();
        record.setId(1L);
        Map<String, Object> map = new HashMap<>();
        map.put("name", "John Doe");
        map.put("age", "30");
        record.setDocument(map);

        //Creating DB connection
        Connection connection = new ConnectionFactory().getConnection();

        EntityDAO<Record> recordEntityDAO = new EntityDAO<>(connection);

        System.out.println("### Testing save operation ### ");
        recordEntityDAO.save(record);

        System.out.println("### Testing findAll operation ###");
        recordEntityDAO = new EntityDAO<>(connection);
        List<Entity> entities = recordEntityDAO.findAll();

        System.out.println("Result from reading on db:");
        for (Entity entity: entities)
            System.out.println(entity);

        connection = new ConnectionFactory().getConnection();
        System.out.println("### Testing update operation ###");
        recordEntityDAO = new EntityDAO<>(connection);
        record.setId(1L);
        map.remove("name");
        map.put("name", "Name Test");
        record.setDocument(map);
        recordEntityDAO.update(record);
        entities = recordEntityDAO.findAll();
        System.out.println("Result from updating on db:");
        for (Entity entity: entities)
            System.out.println(entity);

        connection = new ConnectionFactory().getConnection();
        System.out.println("### Testing delete operation ###");
        recordEntityDAO = new EntityDAO<>(connection);
        recordEntityDAO.delete(1L);
        entities = recordEntityDAO.findAll();
        System.out.println("Result from delete on db:");
        for (Entity entity: entities)
            System.out.println(entity);
    }
}
