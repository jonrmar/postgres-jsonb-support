package integration.dao;

import com.google.gson.Gson;
import dao.EntityDAO;
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
    public void setUp() throws SQLException {
        Connection connection = new ConnectionFactory().getConnection("jdbc:postgresql://localhost:5433/docker", "docker", "docker");
        connection.setAutoCommit(false);
        Gson gson = new Gson();
        this.entityDAO = new EntityDAO(connection, gson, new ObjectToEntity(gson));
    }

    @Test
    public void insertAndReadTest() {
        Entity entity1 = new Entity();

        entityDAO.save(entity1);

        List entity = entityDAO.findAll();

        Assert.assertEquals(1, entity.size());
    }

    @Test(expected = NullPointerException.class)
    public void insertNullTest(){
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
