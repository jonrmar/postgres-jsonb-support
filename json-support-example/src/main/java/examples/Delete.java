package examples;

import dao.EntityDAO;
import dao.exceptions.ConnectionException;
import dao.exceptions.PSQLJsonBException;
import entity.EntityService;
import document.Record;
import jdbc.ConnectionFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static entity.EntityFilter.eq;

public class Delete {

    public static void main(String[] args) throws SQLException {
        //Get Database Connection
        try {
            Connection connection = new ConnectionFactory()
                    .getConnection("jdbc:postgresql://localhost:5433/docker", "docker", "docker");

            EntityDAO entityDAO = new EntityService(connection).getEntityDAO();
            entityDAO.delete(eq("hobby", "movies"), Record.class);

            connection.close();
        } catch (ConnectionException | PSQLJsonBException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
