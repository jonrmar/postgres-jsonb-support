package dao;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import entity.domain.Entity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static entity.ObjectToEntity.convert;

public class EntityDAO {

    private Connection connection;
    private Gson gson;

    public EntityDAO(Connection connection, Gson gson) {
        this.connection = connection;
        this.gson = gson;
    }

    public void save(Object object) throws PSQLJsonBException {
        String tableName = getTableName(object.getClass());
        String sql = String.format("insert into %s (document) values (?::JSONB)", tableName);

        Entity entity = convert(object);

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setObject(1, getJsonDocument(entity));

            stmt.execute();
            stmt.close();
        } catch (SQLException e) {
            throw new PSQLJsonBException("ERROR - Save operation:\n "+e);
        }
    }

    public <T> List<T> findAll(Class<T> clazz) throws PSQLJsonBException {
        String tableName = getTableName(clazz);
        String sql = String.format("select * from %s", tableName);

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            List<T> entities = getEntityFields(clazz, rs);

            rs.close();
            stmt.close();

            return entities;
        } catch (SQLException e) {
            throw new PSQLJsonBException("ERROR - FindAll operation:\n "+e);
        }
    }

    public <T> List<T> find(String filter, Class<T> clazz) throws PSQLJsonBException {
        String tableName = getTableName(clazz);
        String sql = String.format("select * from %s where document %s", tableName, filter);

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            List<T> entities = getEntityFields(clazz, rs);

            rs.close();
            stmt.close();

            return entities;
        } catch (SQLException e) {
            throw new PSQLJsonBException("ERROR - Find operation: \n" + e);
        }
    }

    public void update(Object object, String filter) throws PSQLJsonBException {
        String tableName = getTableName(object.getClass());
        String sql = String.format("update %s " +
                "set document =  (?::JSONB)"+
                "where document %s", tableName, filter);

        Entity entity = convert(object);

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setObject(1, getJsonDocument(entity));

            stmt.execute();
            stmt.close();
        } catch (SQLException e) {
            throw new PSQLJsonBException("ERROR - Update operation:\n "+e);
        }
    }

    public void delete(String filter, Class clazz) throws PSQLJsonBException {
        String tableName = getTableName(clazz);
        String sql = String.format("delete from %s where document %s", tableName, filter);

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.execute();
            stmt.close();
        } catch (SQLException e) {
            throw new PSQLJsonBException("ERROR - Delete operation:\n "+e);
        }
    }


    public <T> List<T> selectNativeQuery(String query, Class<T> clazz) throws PSQLJsonBException {
        query = query.toLowerCase();

        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            List<T> entities = getEntityFields(clazz, rs);

            stmt.close();

            return entities;
        } catch (SQLException e) {
            throw new PSQLJsonBException("ERROR - Select Native Query operation: " + query + " \n " + e);
        }
    }

    public void nativeQuery(String query) throws PSQLJsonBException {
        query = query.toLowerCase();

        try {
            PreparedStatement stmt = connection.prepareStatement(query);

            stmt.execute();
            stmt.close();

        } catch (SQLException e) {
            throw new PSQLJsonBException("ERROR - Native Query operation: " + query + " \n " + e);
        }
    }

    private <T> List<T> getEntityFields(Class<T> clazz, ResultSet rs) throws PSQLJsonBException {
        List<T> entities = new ArrayList<>();
        try {
            while (rs.next()) {
                Type map = new TypeToken<Map<String, Object>>() {
                }.getType();
                Map<String, Object> document = gson.fromJson(rs.getString("document"), map);

                T entity = clazz.getConstructor().newInstance();
                if(document != null) {
                    for (Field field : clazz.getDeclaredFields()) {
                        field.setAccessible(true);
                        switch (field.getName()) {
                            case "id":
                                field.set(entity, rs.getLong("id"));
                                break;
                            case "createdAt":
                                field.set(entity, rs.getTimestamp("created_at").toLocalDateTime());
                                break;
                            case "updatedAt":
                                field.set(entity, rs.getTimestamp("updated_at").toLocalDateTime());
                                break;
                            default:
                                field.set(entity, document.get(field.getName()));
                                break;
                        }
                    }
                    entities.add(entity);
                }
            }

            return entities;
        } catch (InstantiationException |
                IllegalAccessException |
                InvocationTargetException |
                NoSuchMethodException |
                SQLException e) {
            throw new PSQLJsonBException("ERROR - Mapping fields from db to Object " + clazz.getName() + ": \n" + e);
        }
    }

    public void exportTable(String tableName) throws PSQLJsonBException {
        String query = String.format("create table IF NOT EXISTS %s (" +
                " id serial NOT NULL PRIMARY KEY," +
                " document JSONB NOT NULL," +
                " created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()," +
                " updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()" +
                ");", tableName);

        try {
            PreparedStatement  stmt = connection.prepareStatement(query);

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new PSQLJsonBException("ERROR - Creating table: " + query + " \n " + e);
        }finally {
            createFunctionUpdateAt();
            dropTrigger(tableName);
            createTrigger(tableName);
        }

    }

    public void createSchema(String schema) throws PSQLJsonBException {
        String query = String.format("CREATE SCHEMA IF NOT EXISTS %s", schema);

        try {
            PreparedStatement stmt = connection.prepareStatement(query);

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new PSQLJsonBException("ERROR - Creating schema: " + schema + " \n " + e);
        }
    }

    private void createTrigger(String tableName) throws PSQLJsonBException {
        String query = String.format("CREATE TRIGGER set_timestamp" +
                " BEFORE UPDATE ON %s" +
                " FOR EACH ROW" +
                " EXECUTE PROCEDURE trigger_set_timestamp();", tableName);

        try {
            PreparedStatement  stmt = connection.prepareStatement(query);

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new PSQLJsonBException("ERROR - Creating trigger for timestamp: " + query + " \n " + e);
        }
    }

    private void dropTrigger(String tableName) throws PSQLJsonBException {
        String query = String.format("DROP TRIGGER IF EXISTS set_timestamp on %s;", tableName);

        try {
            PreparedStatement stmt = connection.prepareStatement(query);

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new PSQLJsonBException("ERROR - Droping trigger for timestamp: " + query + " \n " + e);
        }
    }

    private void createFunctionUpdateAt() throws PSQLJsonBException {
        String query = "CREATE OR REPLACE FUNCTION trigger_set_timestamp()" +
                " RETURNS TRIGGER AS $$" +
                " BEGIN" +
                "  NEW.updated_at = NOW();" +
                "  RETURN NEW;" +
                " END;" +
                " $$ LANGUAGE plpgsql;";
        try {
            PreparedStatement  stmt = connection.prepareStatement(query);

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new PSQLJsonBException("ERROR - Creating function for updating updated_at column: " + query + " \n " + e);
        }
    }


    private String getJsonDocument(Entity entity) {
        return gson.toJson(entity.getDocument());
    }

    private <T> String getTableName(Class<T> clazz) throws PSQLJsonBException {
        String clazzName = clazz.getName();

        if(!clazz.isAnnotationPresent(entity.annotations.Entity.class))
            throw new PSQLJsonBException(String.format("ERROR - Class %s not annotated with $s", clazzName, entity.annotations.Entity.class.getName()));

        String tableName = clazzName.substring(clazzName.lastIndexOf(".") + 1, clazzName.length());

        entity.annotations.Entity entity = clazz.getAnnotation(entity.annotations.Entity.class);
        String schema = entity.schema();

        return !schema.equals("") ? schema + "." + tableName : tableName;
    }
}
