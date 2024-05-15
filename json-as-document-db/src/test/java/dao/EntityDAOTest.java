import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EntityDAOTest {
    private Connection connection;
    private EntityDAO entityDAO;

    @Before
    public void setUp() throws SQLException {
        connection = mock(Connection.class);
        entityDAO = new EntityDAO(connection);
    }

    @Test
    public void testSave() throws SQLException, PSQLJsonBException {
        String jsonDocument = "{\"name\":\"John Doe\"}";
        String sql = "insert into test_table (document) values (?::JSONB)";
        PreparedStatement stmt = mock(PreparedStatement.class);
        when(connection.prepareStatement(sql)).thenReturn(stmt);

        entityDAO.save(jsonDocument);

        verify(stmt, times(1)).setObject(1, jsonDocument);
        verify(stmt, times(1)).execute();
    }
}