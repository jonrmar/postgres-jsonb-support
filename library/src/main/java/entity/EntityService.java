package entity;

import com.google.gson.Gson;
import dao.EntityDAO;

import java.io.File;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class EntityService {

    private EntityDAO entityDAO;

    public EntityService(Connection connection) {
        Gson gson = new Gson();
        ObjectToEntity objectToEntity = new ObjectToEntity(gson);
        this.entityDAO = new EntityDAO(connection, gson, objectToEntity);
    }

    public EntityDAO getEntityDAO() {
        return entityDAO;
    }

    private void listFiles() throws ClassNotFoundException, MalformedURLException {
        List<File> list = getFiles(System.getProperty("java.class.path"));
        for (File file : list) {
            System.out.println(file.getPath());
        }
    }
    private static List<File> getFiles(String paths) {
        List<File> filesList = new ArrayList<>();
        for (final String path : paths.split(File.pathSeparator)) {
            final File file = new File(path);
            if (file.isDirectory()) {
                recurse(filesList, file);
            } else {
                filesList.add(file);
            }
        }
        return filesList;
    }

    private static void recurse(List<File> filesList, File f) {
        File list[] = f.listFiles();

        for (File file : list) {
            if (file.isDirectory()) {
                recurse(filesList, file);
            } else {
                filesList.add(file);
            }
        }
    }

}
