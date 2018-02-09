package dao;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import entity.Entity;
import entity.ObjectToEntity;

import java.lang.reflect.Type;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EntityDAO {

    private Connection connection;
    private Gson gson;
    private ObjectToEntity objectToEntity;

    public EntityDAO(Connection connection,  Gson gson, ObjectToEntity objectToEntity) {
        this.connection = connection;
        this.gson = gson;
        this.objectToEntity = objectToEntity;
    }

    public void save(Object object) {
        String sql = "insert into entity (document, created_at) values (?::JSONB, ?::DATE)";

        Entity entity = objectToEntity.convert(object);

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setObject(1, getJsonDocument(entity));
            stmt.setTimestamp(2, new Timestamp(new java.util.Date().getTime()));

            stmt.execute();
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Entity> findAll() {
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
            throw new RuntimeException(e);
        }
    }

    public List<Entity> find(String filter) {
        String sql = "select * from entity " +
                "where document ->> " + filter;

        List<Entity> entities = new ArrayList<>();

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Type map = new TypeToken<Map<String, Object>>(){}.getType();
                Map<String, Object> document = gson.fromJson(rs.getString("document"), map);

                Entity entity = new Entity();
                entity.setDocument(document);

                entities.add(entity);
            }

            rs.close();
            stmt.close();

            return entities;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Object object, String filter) {
        String sql = "update entity " +
                "set (document, updated_at) =  (?::JSONB,?)"+
                "where document ->> "+filter;

        Entity entity = objectToEntity.convert(object);

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setObject(1, getJsonDocument(entity));
            stmt.setTimestamp(2, new Timestamp(new java.util.Date().getTime()));

            stmt.execute();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(String filter) {
        String sql = "delete from entity where document ->> "+filter;
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.execute();
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String getJsonDocument(Entity entity) {
        return gson.toJson(entity.getDocument());
    }

}
