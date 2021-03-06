package examples;

import dao.EntityDAO;
import dao.PSQLJsonBException;
import document.Record;
import entity.EntityFilter;
import entity.EntityService;
import jdbc.Connection;
import jdbc.ConnectionException;
import jdbc.ConnectionFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Update {
    public static void main(String[] args) {
        Map<String, String> favoriteFoods = new HashMap<>();
        favoriteFoods.put("desert", "banana");
        favoriteFoods.put("lunch", "fried chicken");
        favoriteFoods.put("snack", "ice cream");

        List<String> sports = new ArrayList<>();
        sports.add("swim");
        sports.add("soccer");

        Record record = new Record("John Doe 3", "23", "movies", favoriteFoods, sports);

        //Get Database Connection
        try {
            ConnectionFactory factory = new ConnectionFactory("jdbc:postgresql://localhost:5433/docker", "docker", "docker");
            Connection connection = factory.createConnection();

            EntityDAO entityDAO = new EntityService(connection).getEntityDAO();
            entityDAO.update(record, EntityFilter.eq("age", "30"));

        } catch (ConnectionException | PSQLJsonBException e) {
            e.printStackTrace();
        }
    }
}
