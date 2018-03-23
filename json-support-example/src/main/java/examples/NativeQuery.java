package examples;

import dao.EntityDAO;
import dao.exceptions.ConnectionException;
import dao.exceptions.PSQLJsonBException;
import document.Record;
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
            List<Record> records = entityDAO.nativeQuery(query, Record.class);

            for (Record record : records)
                System.out.println(record);

        } catch (ConnectionException | PSQLJsonBException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
