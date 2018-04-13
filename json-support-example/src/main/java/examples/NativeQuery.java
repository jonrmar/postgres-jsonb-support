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

            // Native Query for Select
            String query = "select * from record where document ->> 'age' = '23'";
            EntityDAO entityDAO = new EntityService(connection).getEntityDAO();
            List<Record> records = entityDAO.selectNativeQuery(query, Record.class);

            for (Record record : records)
                System.out.println(record);

            //Native query for others crud operations
            query = "delete from example.record where document ->> 'age' = '23'";
            entityDAO.nativeQuery(query);


        } catch (ConnectionException | PSQLJsonBException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
