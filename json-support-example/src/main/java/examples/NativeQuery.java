package examples;

import dao.EntityDAO;
import dao.PSQLJsonBException;
import document.Record;
import entity.EntityService;
import jdbc.Connection;
import jdbc.ConnectionException;
import jdbc.ConnectionFactory;

import java.util.List;

public class NativeQuery {
    public static void main(String[] args) {
        //Get Database Connection
        try {
            ConnectionFactory factory = new ConnectionFactory("jdbc:postgresql://localhost:5433/docker", "docker", "docker");
            Connection connection = factory.createConnection();

            // Native Query for Select
            String query = "select * from record";
            EntityDAO entityDAO = new EntityService(connection).getEntityDAO();
            List<Record> records = entityDAO.selectNativeQuery(query, Record.class);

            for (Record record : records)
                System.out.println(record);

            //Native query for others crud operations
            query = "delete from example.record where document ->> 'age' = '23'";
            entityDAO.nativeQuery(query);

        } catch (ConnectionException | PSQLJsonBException e) {
            e.printStackTrace();
        }
    }
}
