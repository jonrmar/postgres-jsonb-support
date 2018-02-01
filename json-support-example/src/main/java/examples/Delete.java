package examples;

import dao.EntityDAO;
import entity.EntityService;
import entity.Record;
import jdbc.ConnectionFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static entity.EntityFilter.eq;

public class Delete {

    public static void main(String[] args) throws SQLException {
        Map<String, String> favoriteFoods = new HashMap<>();
        favoriteFoods.put("desert", "banana");
        favoriteFoods.put("lunch", "fried chicken");
        favoriteFoods.put("snack", "ice cream");

        //Can use any class to save into jsonb column
        Record record = new Record("John Doe 3", "25", "movies", favoriteFoods);

        //Get Database Connection
        Connection connection = new ConnectionFactory()
                .getConnection(    "jdbc:postgresql://localhost:5433/docker", "docker", "docker");

        EntityDAO entityDAO = new EntityService(connection).getEntityDAO();
        entityDAO.delete(eq("hobby", "movies"));

        connection.close();
    }
}
