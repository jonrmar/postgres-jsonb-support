package examples;

import dao.EntityDAO;
import entity.EntityService;
import entity.Record;
import jdbc.ConnectionFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Insert {
    public static void main(String[] args) throws SQLException {
        Map<String, String> favoriteFoods = new HashMap<>();
        favoriteFoods.put("desert", "banana");
        favoriteFoods.put("lunch", "fried chicken");
        favoriteFoods.put("snack", "ice cream");

        //Can use any class to save into jsonb column
        Record record = new Record("John Doe 4", "30", "pc", favoriteFoods);

        //Get Database Connection
        Connection connection = new ConnectionFactory()
                .getConnection(    "jdbc:postgresql://localhost:5433/docker", "docker", "docker");

        EntityDAO entityDAO = new EntityService(connection).getEntityDAO();
        entityDAO.save(record);

        connection.close();
    }
}
