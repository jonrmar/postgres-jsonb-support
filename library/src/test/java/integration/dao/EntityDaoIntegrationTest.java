package integration.dao;

import com.google.gson.Gson;
import dao.EntityDAO;
import dao.exceptions.ConnectionException;
import dao.exceptions.PSQLJsonBException;
import entity.Entity;
import entity.EntityFilter;
import entity.ObjectToEntity;
import jdbc.ConnectionFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityDaoIntegrationTest {

    private EntityDAO entityDAO;

    @Before
    public void setUp() throws SQLException, ConnectionException {
        Connection connection = new ConnectionFactory().getConnection("jdbc:postgresql://localhost:5433/docker", "docker", "docker");
        connection.setAutoCommit(false);
        Gson gson = new Gson();
        this.entityDAO = new EntityDAO(connection, gson);
    }

    @Test(expected = PSQLJsonBException.class)
    public void insertTest() throws PSQLJsonBException {
        Entity entity1 = new Entity();

        entityDAO.save(entity1);
    }

    @Test(expected = NullPointerException.class)
    public void insertNullTest() throws PSQLJsonBException {
        entityDAO.save(null);
    }

    private Map<String, Object> mockDocument() {
        Map<String, Object> document = new HashMap<>();
        document.put("desert", "banana");
        document.put("lunch", "fried chicken");
        document.put("snack", "ice cream");

        return document;
    }
}
