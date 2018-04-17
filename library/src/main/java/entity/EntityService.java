package entity;

import com.google.gson.Gson;
import dao.EntityDAO;
import entity.annotations.Entity;
import dao.PSQLJsonBException;

import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class EntityService {

    private static final int CLASSES_SIZE = 8;
    private static final String EMPTY_SCHEMA = "";
    private EntityDAO entityDAO;


    public EntityService(Connection connection) throws PSQLJsonBException, ClassNotFoundException {
        Gson gson = new Gson();
        this.entityDAO = new EntityDAO(connection, gson);
        listFiles();
    }

    public EntityDAO getEntityDAO() {
        return entityDAO;
    }

    private void listFiles() throws PSQLJsonBException, ClassNotFoundException {
        List<File> list = getFiles(System.getProperty("java.class.path"));
        for (File file : list) {
            String fileName = getFileToLoad(file);

            ClassLoader classLoader = ClassLoader.getSystemClassLoader();
            Class clazz = classLoader.loadClass(fileName);

            if (clazz.isAnnotationPresent(Entity.class)) {
                String clazzName = clazz.getName();
                String tableName = clazzName.substring(clazzName.lastIndexOf(".")+1, clazzName.length());

                Entity annotation = (Entity) clazz.getAnnotation(Entity.class);

                if (!annotation.schema().equals(EMPTY_SCHEMA)) {
                    String schema = annotation.schema();
                    entityDAO.createSchema(schema);
                    entityDAO.exportTable(schema+"."+tableName);
                } else
                    entityDAO.exportTable(tableName);
            }
        }
    }

    private String getFileToLoad(File file) {
        String path = file.getAbsolutePath();
        String removeClass = path.substring(1, path.lastIndexOf("."));
        String normalizedPackage = removeClass.replaceAll("/", ".");
        Integer indexOfClasses = normalizedPackage.indexOf("classes");

        return normalizedPackage.substring(indexOfClasses+ CLASSES_SIZE, normalizedPackage.length());
    }

    private static List<File> getFiles(String paths) {
        List<File> filesList = new ArrayList<>();
        for (final String path : paths.split(File.pathSeparator)) {
            final File file = new File(path);
            if (file.isDirectory()) {
                recurse(filesList, file);
            } else {
                addFile(filesList, file);
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
                addFile(filesList, file);
            }
        }
    }

    private static void addFile(List<File> filesList, File file) {
        if (getFileExtension(file).equalsIgnoreCase("class"))
            filesList.add(file);
    }

    private static String getFileExtension(File file) {
        String fileName = file.getName();
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        else return "";
    }
}
