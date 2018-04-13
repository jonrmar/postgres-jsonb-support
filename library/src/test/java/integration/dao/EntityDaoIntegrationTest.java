package integration.dao;

import com.google.gson.Gson;
import dao.EntityDAO;
import jdbc.ConnectionException;
import dao.PSQLJsonBException;
import entity.domain.Entity;
import jdbc.ConnectionFactory;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

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
    public void insertClassWithOutAnnotationTest() throws PSQLJsonBException {
        Entity entity1 = new Entity();

        entityDAO.save(entity1);
    }

    @Test
    public void insertOnExportedSchemaAndTableTest() throws PSQLJsonBException {
        Record record = buildDefaultRecord();

        entityDAO.save(record);

        List<Record> records = entityDAO.findAll(Record.class);

        assertEquals(1, records.size());
        assertEquals(record, records.get(0));
    }

    @Test(expected = NullPointerException.class)
    public void insertNullTest() throws PSQLJsonBException {
        entityDAO.save(null);
    }

    private Record buildDefaultRecord() {
        Map<String, String> favoriteFoods = new HashMap<>();
        favoriteFoods.put("desert", "banana");
        favoriteFoods.put("lunch", "fried chicken");
        favoriteFoods.put("snack", "ice cream");

        List<String> sports = new ArrayList<>();
        sports.add("swim");
        sports.add("soccer");

        return new Record("John Doe 4", "30", "pc", favoriteFoods, sports);
    }
}
