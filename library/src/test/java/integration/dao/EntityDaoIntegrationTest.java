package integration.dao;

import dao.EntityDAO;
import domain.Record;
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
        Record record = new Record();
        record.setId(2L);

        entityDAO.save(record);

        List entity = entityDAO.findAll(2L);

        Assert.assertEquals(1, entity.size());
    }

    @Test
    public void deleteOperationTest() {
        Record record = new Record();
        record.setId(2L);

        entityDAO.save(record);
        entityDAO.delete(2L);
        List entity = entityDAO.findAll(2L);

        Assert.assertEquals(0, entity.size());
    }

    @Test(expected = NullPointerException.class)
    public void insertNullTest(){
        entityDAO.save(null);
    }
}
