package dao;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dao.exceptions.PSQLJsonBException;
import entity.Entity;
import entity.ObjectToEntity;

import java.lang.reflect.Type;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EntityDAO {

    private static final String SELECT = "select";
    private Connection connection;
    private Gson gson;
    private ObjectToEntity objectToEntity;

    public EntityDAO(Connection connection,  Gson gson, ObjectToEntity objectToEntity) {
        this.connection = connection;
        this.gson = gson;
        this.objectToEntity = objectToEntity;
    }

    public void save(Object object) throws PSQLJsonBException {
        String sql = "insert into entity (document) values (?::JSONB)";

        Entity entity = objectToEntity.convert(object);

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setObject(1, getJsonDocument(entity));

            stmt.execute();
            stmt.close();
        } catch (SQLException e) {
            throw new PSQLJsonBException("ERROR - Save operation:\n "+e);
        }
    }

    public List<Entity> findAll() throws PSQLJsonBException {
        String sql = "select * from entity";
        List<Entity> entities = new ArrayList<>();

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Type map = new TypeToken<Map<String, Object>>() {}.getType();
                Map<String, Object> document = gson.fromJson(rs.getString("document"), map);

                Entity entity = new Entity();
                entity.setDocument(document);
                entity.setId(rs.getLong("id"));
                entity.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                Timestamp timestamp = rs.getTimestamp("updated_at");
                if(timestamp != null) entity.setUpdatedAt(timestamp.toLocalDateTime());

                entities.add(entity);
            }

            rs.close();
            stmt.close();

            return entities;
        } catch (SQLException e) {
            throw new PSQLJsonBException("ERROR - FindAll operation:\n "+e);
        }
    }

    public List<Entity> find(String filter) throws PSQLJsonBException {
        String sql = "select * from entity " +
                "where document " + filter;

        List<Entity> entities = new ArrayList<>();

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Type map = new TypeToken<Map<String, Object>>(){}.getType();
                Map<String, Object> document = gson.fromJson(rs.getString("document"), map);

                Entity entity = new Entity();
                entity.setDocument(document);
                entity.setId(rs.getLong("id"));
                entity.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                Timestamp timestamp = rs.getTimestamp("updated_at");
                if(timestamp != null) entity.setUpdatedAt(timestamp.toLocalDateTime());

                entities.add(entity);
            }

            rs.close();
            stmt.close();

            return entities;
        } catch (SQLException e) {
            throw new PSQLJsonBException("ERROR - Find operation: \n"+e);
        }
    }

    public void update(Object object, String filter) throws PSQLJsonBException {
        String sql = "update entity " +
                "set document =  (?::JSONB)"+
                "where document "+filter;

        Entity entity = objectToEntity.convert(object);

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setObject(1, getJsonDocument(entity));

            stmt.execute();
            stmt.close();
        } catch (SQLException e) {
            throw new PSQLJsonBException("ERROR - Update operation:\n "+e);
        }
    }

    public void delete(String filter) throws PSQLJsonBException {
        String sql = "delete from entity where document "+filter;
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.execute();
            stmt.close();
        } catch (SQLException e) {
            throw new PSQLJsonBException("ERROR - Delete operation:\n "+e);
        }
    }

    public List<Entity> nativeQuery(String query) throws PSQLJsonBException {
        List<Entity> entities = new ArrayList<>();

        query = query.toLowerCase();
        try {
            PreparedStatement stmt = connection.prepareStatement(query);

            if (query.startsWith(SELECT)) {
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    Type map = new TypeToken<Map<String, Object>>() {
                    }.getType();
                    Map<String, Object> document = gson.fromJson(rs.getString("document"), map);

                    Entity entity = new Entity();
                    entity.setDocument(document);
                    entity.setId(rs.getLong("id"));
                    entity.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    Timestamp timestamp = rs.getTimestamp("updated_at");
                    if (timestamp != null) entity.setUpdatedAt(timestamp.toLocalDateTime());

                    entities.add(entity);
                }
            }else
                stmt.execute();

            stmt.close();

            return entities;
        } catch (SQLException e) {
            throw new PSQLJsonBException("ERROR - Native Query operation: " + query + " \n " + e);
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
            createTrigger(tableName);
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
}
