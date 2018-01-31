package unit.dao;

import dao.EntityDAO;
import domain.Entity;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

public class EntityDAOTest {

    private EntityDAO entityDAO;
    @Mock private Connection connection;
    @Mock private PreparedStatement statement;
    @Mock private ResultSet resultSet;

    @Before
    public void setup() throws SQLException {
        MockitoAnnotations.initMocks(this);
        this.entityDAO = new EntityDAO(connection);
        mocks();
    }

    @Test
    public void addAndReadOperationsTest(){
        Entity entity1 = new Entity();
        entity1.setId(2L);

        entityDAO.save(entity1);

        List<Entity> entity = entityDAO.findAll();

        assertEquals(entity.size(), 1);
        assertEquals(entity, mockEntityList());
    }

    @Test(expected = NullPointerException.class)
    public void addNullObjectTest(){
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
        entityDAO.update(null);
    }

    private void mocks() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getLong(anyString())).thenReturn(2L);
    }

    private List<Entity> mockEntityList(){
        List<Entity> entities = new ArrayList<>();
        Entity entity = new Entity();

        entity.setId(2L);
        entities.add(entity);

        return entities;
    }
}
