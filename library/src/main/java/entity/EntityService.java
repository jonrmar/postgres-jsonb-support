package entity;

import com.google.gson.Gson;
import dao.EntityDAO;
import dao.PSQLJsonBException;
import entity.annotations.Entity;
import jdbc.Connection;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class EntityService {

    private static final int CLASSES_SIZE = 8;
    private static final String EMPTY_SCHEMA = "";
    private final static Logger logger = Logger.getLogger(EntityService.class.getName());
    private EntityDAO entityDAO;

    public EntityService(jdbc.Connection connection) throws PSQLJsonBException {
        Gson gson = new Gson();
        this.entityDAO = new EntityDAO(connection.getConnection(), gson);
        listFiles(connection);
    }

    public EntityDAO getEntityDAO() {
        return entityDAO;
    }

    private void listFiles(Connection connection) throws PSQLJsonBException {
        try {
        List<File> list = getFiles(System.getProperty("java.class.path"));
        for (File file : list) {
            String fileName = getFileToLoad(file);
            ClassLoader classLoader = ClassLoader.getSystemClassLoader();
            Class clazz = classLoader.loadClass(fileName);

            if (clazz.isAnnotationPresent(Entity.class)) {
                String clazzName = clazz.getName();
                String tableName = clazzName.substring(clazzName.lastIndexOf(".")+1, clazzName.length());

                Entity annotation = (Entity) clazz.getAnnotation(Entity.class);

                exportFromProperties(tableName, annotation, connection);
            }
        }
        } catch (PSQLJsonBException | ClassNotFoundException ex) {
            String message = "Error catch on:" + ex;
            logger.severe(message);
            throw new PSQLJsonBException(message);
        }
    }

    private void exportFromProperties(String tableName, Entity annotation, Connection connection)
            throws PSQLJsonBException {
        if (!annotation.schema().equals(EMPTY_SCHEMA)) {
            String schema = annotation.schema();
            if (connection.isExportSchema())
                entityDAO.createSchema(schema);
            if (connection.isExportTable())
                entityDAO.exportTable(schema + "." + tableName);
        } else if (connection.isExportTable())
            entityDAO.exportTable(tableName);
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
