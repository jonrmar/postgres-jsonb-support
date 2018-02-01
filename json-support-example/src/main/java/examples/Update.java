package examples;

import dao.EntityDAO;
import entity.EntityFilter;
import entity.EntityService;
import entity.Record;
import jdbc.ConnectionFactory;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

public class Update {
    public static void main(String[] args) {
        Map<String, String> favoriteFoods = new HashMap<>();
        favoriteFoods.put("desert", "banana");
        favoriteFoods.put("lunch", "fried chicken");
        favoriteFoods.put("snack", "ice cream");

        Record record = new Record("John Doe 3", "30", "movies", favoriteFoods);

        //Get Database Connection
        Connection connection = new ConnectionFactory()
                .getConnection(    "jdbc:postgresql://localhost:5433/docker", "docker", "docker");

        EntityDAO entityDAO = new EntityService(connection).getEntityDAO();
        entityDAO.update(record, EntityFilter.eq("age", "25"));
    }
}
