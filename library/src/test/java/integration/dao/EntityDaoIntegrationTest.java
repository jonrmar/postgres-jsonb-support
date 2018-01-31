package integration.dao;

import dao.EntityDAO;
import domain.Entity;
import jdbc.ConnectionFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class EntityDaoIntegrationTest {

    private EntityDAO entityDAO;

    @Before
    public void setUp() throws SQLException {
        Connection connection = new ConnectionFactory().getConnection();
        connection.setAutoCommit(false);
        this.entityDAO = new EntityDAO(connection);
    }

    @Test
    public void insertAndReadTest() {
        Entity entity1 = new Entity();
        entity1.setId(2L);

        entityDAO.save(entity1);

        List entity = entityDAO.findAll();

        Assert.assertEquals(1, entity.size());
    }

    @Test
    public void deleteOperationTest() {
        Entity entity1 = new Entity();
        entity1.setId(2L);

        entityDAO.save(entity1);
        entityDAO.delete(2L);
        List entity = entityDAO.findAll();

        Assert.assertEquals(0, entity.size());
    }

    @Test(expected = NullPointerException.class)
    public void insertNullTest(){
        entityDAO.save(null);
    }
}
