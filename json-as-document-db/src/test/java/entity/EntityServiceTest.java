import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.sql.Connection;
import java.util.List;
import java.io.File;

public class EntityServiceTest {
    private Connection connection;
    private EntityService entityService;

    @Before
    public void setUp() {
        connection = mock(Connection.class);
        entityService = new EntityService(connection);
    }

    @Test
    public void testListFiles() throws PSQLJsonBException {
        List<File> files = entityService.listFiles();
        assertNotNull(files);
    }
}