package unit.dao;

import com.google.gson.Gson;
import dao.EntityDAO;
import entity.Entity;
import entity.ObjectToEntity;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

public class EntityDAOTest {

    private EntityDAO entityDAO;
    @Mock private Connection connection;
    @Mock private PreparedStatement statement;
    @Mock private ResultSet resultSet;
    @Mock
    private ObjectToEntity objectToEntity;

    @Before
    public void setup() throws SQLException {
        MockitoAnnotations.initMocks(this);
        Gson gson = new Gson();
        this.entityDAO = new EntityDAO(connection, gson, objectToEntity);
        mocks();
    }

    @Test
    public void addAndReadOperationsTest(){
        entityDAO.save(mockEntity());

        List<Entity> entity = entityDAO.findAll();

        assertEquals(entity.size(), 1);
        assertEquals(entity, mockEntityList());
    }

    @Test(expected = NullPointerException.class)
    public void addNullObjectTest(){
        when(objectToEntity.convert(anyObject())).thenReturn(null);

        entityDAO.save(null);
    }

    @Test
    public void readEmptyDBTest() throws SQLException {
        when(resultSet.next()).thenReturn(false);
        List entity = entityDAO.findAll();

        assertEquals(entity.size(), 0);
    }

    @Test(expected = NullPointerException.class)
    public void updateOperationTest(){
        when(objectToEntity.convert(anyObject())).thenReturn(null);
        entityDAO.update(null, "");
    }

    private void mocks() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getLong(anyString())).thenReturn(2L);
        when(objectToEntity.convert(anyObject())).thenReturn(mockEntity());
    }

    private List<Entity> mockEntityList(){
        List<Entity> entities = new ArrayList<>();
        Entity entity = new Entity();

        entities.add(entity);

        return entities;
    }

    private Entity mockEntity() {
        Entity entity = new Entity();
        entity.setDocument(mockDocument());

        return entity;
    }

    private Map<String, Object> mockDocument() {
        Map<String, Object> document = new HashMap<>();
        document.put("desert", "banana");
        document.put("lunch", "fried chicken");
        document.put("snack", "ice cream");

        return document;
    }
}
