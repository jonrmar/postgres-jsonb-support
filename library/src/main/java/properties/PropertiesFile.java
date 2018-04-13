package properties;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesFile {

    public Properties loadPropertiesFile() throws ReadingFilePropertiesException {
        Properties prop = new Properties();
        String fileName = "application.properties";
        try {
            InputStream input = getClass().getClassLoader().getResourceAsStream(fileName);
            if (input != null) prop.load(input);

            return prop;
        } catch (IOException e) {
            throw new ReadingFilePropertiesException("Error trying to read property file:\n "+ e);
        }
    }
}
