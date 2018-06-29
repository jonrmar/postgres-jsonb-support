package examples;

import dao.EntityDAO;
import dao.PSQLJsonBException;
import document.Record;
import entity.EntityService;
import jdbc.Connection;
import jdbc.ConnectionException;
import jdbc.ConnectionFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Insert {
    public static void main(String[] args) {
        Map<String, String> favoriteFoods = new HashMap<>();
        favoriteFoods.put("desert", "banana");
        favoriteFoods.put("lunch", "fried chicken");
        favoriteFoods.put("snack", "ice cream");

        List<String> sports = new ArrayList<>();
        sports.add("swim");
        sports.add("soccer");

        //Can use any class to save into jsonb column
        Record record = new Record("John Doe 4", "30", "pc", favoriteFoods, sports);

        //Get Database Connection
        try {
            ConnectionFactory factory = new ConnectionFactory("jdbc:postgresql://localhost:5433/docker", "docker", "docker");
            Connection connection = factory.createConnection();
            connection.enableExportSchema();
            connection.enableExportTable();

            EntityDAO entityDAO = new EntityService(connection).getEntityDAO();
            entityDAO.save(record);

            connection.close();
        } catch (ConnectionException | PSQLJsonBException ex) {
            ex.printStackTrace();
        }
    }
}
