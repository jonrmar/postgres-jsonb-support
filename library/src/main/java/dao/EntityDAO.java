package dao;

import com.google.gson.Gson;
import domain.Entity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityDAO {

    private Connection connection;

    public EntityDAO(Connection connection) {
        this.connection = connection;
    }

    public void save(Entity entity) {
        String sql = "insert into entity (id, document) values (?, ?::JSONB)";

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setLong(1, entity.getId());
            stmt.setObject(2, getJsonDocument(entity));

            stmt.execute();
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String getJsonDocument(Entity entity) {
        Gson gson = new Gson();
        return gson.toJson(entity.getDocument());
    }

    public List<Entity> findAll() {
        String sql = "select * from entity";
        List<Entity> entities = new ArrayList<>();
        Gson gson = new Gson();
        Map<String, Object> map = new HashMap<>();

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            //@TODO: Refactor: use generic
            while (rs.next()) {
                map = (Map<String, Object>) gson.fromJson(rs.getString("document"), map.getClass());
                Entity entity = new Entity();
                entity.setId(rs.getLong("id"));
                entity.setDocument(map);

                entities.add(entity);
            }

            rs.close();
            stmt.close();
            connection.close();

            return entities;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Entity entity) {
        String sql = "update entity set id=?, document=?::JSONB where id=?";

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setLong(1, entity.getId());
            stmt.setObject(2, getJsonDocument(entity));
            stmt.setLong(3, entity.getId());

            stmt.execute();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(Long id) {
        String sql = "delete from entity where id=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setLong(1, id);

            stmt.execute();
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
