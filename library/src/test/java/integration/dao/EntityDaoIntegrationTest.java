package integration.dao;

import com.google.gson.Gson;
import dao.EntityDAO;
import dao.PSQLJsonBException;
import entity.EntityFilter;
import entity.domain.Entity;
import jdbc.Connection;
import jdbc.ConnectionException;
import jdbc.ConnectionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class EntityDaoIntegrationTest {

    private EntityDAO entityDAO;
    private Connection connection;

    @Before
    public void setUp() throws ConnectionException, SQLException {
        ConnectionFactory factory = new ConnectionFactory("jdbc:postgresql://localhost:5433/docker", "docker", "docker");
        this.connection = factory.createConnection();
        this.connection.getConnection().setAutoCommit(false);
        Gson gson = new Gson();
        this.entityDAO = new EntityDAO(connection.getConnection(), gson);
    }

    @After
    public void after() throws ConnectionException {
        this.connection.close();
    }

    @Test(expected = PSQLJsonBException.class)
    public void insertClassWithOutAnnotationTest() throws PSQLJsonBException {
        Entity entity1 = new Entity();

        entityDAO.save(entity1);
    }

    @Test
    public void saveAndFindAllOnExportedSchemaAndTableTest() throws PSQLJsonBException {
        Record record = buildDefaultRecord();
        setupInicial(record);

        List<Record> records = entityDAO.findAll(Record.class);

        assertEquals(1, records.size());
        assertThat(record, new ReflectionEquals(records.get(0), excludedFields()));
    }

    @Test(expected = PSQLJsonBException.class)
    public void saveOnNotExportedTableTest() throws PSQLJsonBException {
        Record record = buildDefaultRecord();
        entityDAO.save(record);
    }

    @Test(expected = NullPointerException.class)
    public void insertNullTest() throws PSQLJsonBException {
        entityDAO.save(null);
    }

    @Test
    public void findOneTest() throws PSQLJsonBException {
        Record record = buildDefaultRecord();
        setupInicial(record);

        List<Record> records = entityDAO.find(EntityFilter.eq("age", "30"), Record.class);

        assertEquals(1, records.size());
        assertThat(record, new ReflectionEquals(records.get(0), excludedFields()));
    }

    @Test
    public void findInexistentRowTest() throws PSQLJsonBException {
        Record record = buildDefaultRecord();
        setupInicial(record);

        List<Record> records = entityDAO.find(EntityFilter.eq("age", "23"), Record.class);

        assertEquals(0, records.size());
    }

    @Test
    public void updateOneRowTest() throws PSQLJsonBException {
        Record record = buildDefaultRecord();
        setupInicial(record);
        Record expected = newRecord();

        entityDAO.update(expected, EntityFilter.eq("age", "30"));

        List<Record> records = entityDAO.findAll(Record.class);

        assertEquals(1, records.size());
        assertThat(expected, new ReflectionEquals(records.get(0), excludedFields()));
    }

    @Test
    public void updateManyRowsTest() throws PSQLJsonBException {
        List<Record> records = buildManyRecords();
        setupInicialManyRows(records);
        Record record = newRecord();

        entityDAO.update(record, EntityFilter.eq("age", "30"));

        List<Record> result = entityDAO.findAll(Record.class);

        assertEquals(3, result.size());
        assertThat(newRecords().get(0), new ReflectionEquals(result.get(0), excludedFields()));
        assertThat(newRecords().get(1), new ReflectionEquals(result.get(1), excludedFields()));
        assertThat(newRecords().get(2), new ReflectionEquals(result.get(2), excludedFields()));
    }

    @Test
    public void tryToUpdateInexistentRowTest() throws PSQLJsonBException {
        Record record = buildDefaultRecord();
        setupInicial(record);
        Record expected = newRecord();

        entityDAO.update(expected, EntityFilter.eq("age", "23"));

        List<Record> records = entityDAO.findAll(Record.class);

        assertEquals(1, records.size());
        assertThat(record, new ReflectionEquals(records.get(0), excludedFields()));
    }

    @Test
    public void deleteRowTest() throws PSQLJsonBException {
        Record record = buildDefaultRecord();
        setupInicial(record);

        entityDAO.delete(EntityFilter.eq("age", "30"), Record.class);

        List<Record> records = entityDAO.findAll(Record.class);

        assertEquals(0, records.size());
    }

    @Test
    public void deleteManyRowsTest() throws PSQLJsonBException {
        Record record = buildDefaultRecord();
        setupInicial(record);

        entityDAO.delete(EntityFilter.eq("age", "30"), Record.class);

        List<Record> records = entityDAO.findAll(Record.class);

        assertEquals(0, records.size());
    }


    @Test
    public void tryDeleteInexistentRowTest() throws PSQLJsonBException {
        Record record = buildDefaultRecord();
        setupInicial(record);

        entityDAO.delete(EntityFilter.eq("age", "23"), Record.class);

        List<Record> records = entityDAO.findAll(Record.class);

        assertEquals(1, records.size());
        assertThat(record, new ReflectionEquals(records.get(0), excludedFields()));
    }

    @Test
    public void selectNativeQueryTest() throws PSQLJsonBException {
        Record record = buildDefaultRecord();
        setupInicial(record);
        String query = "select * from record";

        List<Record> records = entityDAO.selectNativeQuery(query, Record.class);

        assertEquals(1, records.size());
        assertThat(record, new ReflectionEquals(records.get(0), excludedFields()));
    }

    @Test(expected = PSQLJsonBException.class)
    public void selectNativeQueryInvalidQueryTest() throws PSQLJsonBException {
        Record record = buildDefaultRecord();
        setupInicial(record);
        String query = "select from record";

        entityDAO.selectNativeQuery(query, Record.class);
    }

    @Test
    public void deleteNativeQueryTest() throws PSQLJsonBException {
        Record record = buildDefaultRecord();
        setupInicial(record);
        String query = "delete from record where document ->> 'age' = '30'";

        entityDAO.nativeQuery(query);

        List<Record> records = entityDAO.findAll(Record.class);

        assertEquals(0, records.size());
    }

    @Test(expected = PSQLJsonBException.class)
    public void deleteNativeQueryInvalidQueryTest() throws PSQLJsonBException {
        Record record = buildDefaultRecord();
        setupInicial(record);
        String query = "delete from bla";

        entityDAO.selectNativeQuery(query, Record.class);
    }

    private void setupInicial(Record record) throws PSQLJsonBException {
        entityDAO.exportTable("record");
        entityDAO.save(record);
    }

    private void setupInicialManyRows(List<Record> records) throws PSQLJsonBException {
        entityDAO.exportTable("record");
        for (Record record : records)
            entityDAO.save(record);
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

    private List<Record> buildManyRecords() {
        ArrayList<Record> records = new ArrayList<>();
        Map<String, String> favoriteFoods = new HashMap<>();
        favoriteFoods.put("desert", "banana");
        favoriteFoods.put("lunch", "fried chicken");
        favoriteFoods.put("snack", "ice cream");

        List<String> sports = new ArrayList<>();
        sports.add("swim");
        sports.add("soccer");

        Record record = new Record("John Doe 4", "30", "pc", favoriteFoods, sports);
        records.add(record);
        records.add(record);
        records.add(record);
        return records;
    }

    private Record newRecord() {
        Map<String, String> favoriteFoods = new HashMap<>();
        favoriteFoods.put("desert", "banana");
        favoriteFoods.put("lunch", "fried chicken");
        favoriteFoods.put("snack", "ice cream");

        List<String> sports = new ArrayList<>();
        sports.add("swim");
        sports.add("soccer");

        return new Record("John Doe 5", "25", "movies", favoriteFoods, sports);
    }

    private List<Record> newRecords() {
        ArrayList<Record> records = new ArrayList<>();
        Map<String, String> favoriteFoods = new HashMap<>();
        favoriteFoods.put("desert", "banana");
        favoriteFoods.put("lunch", "fried chicken");
        favoriteFoods.put("snack", "ice cream");

        List<String> sports = new ArrayList<>();
        sports.add("swim");
        sports.add("soccer");

        Record record = new Record("John Doe 5", "25", "movies", favoriteFoods, sports);
        records.add(record);
        records.add(record);
        records.add(record);
        return records;
    }

    private String[] excludedFields() {
        return new String[]{"id", "createdAt", "updatedAt"};
    }
}
