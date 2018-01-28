package dao;

import domain.Entity;
import domain.Record;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EntityDAO<T extends Entity> {

    private Connection connection;

    public EntityDAO(Connection connection) {
        this.connection = connection;
    }

    public void save(Entity entity) {
        String sql = "insert into entity" +
                "(id) values (?)";

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setLong(1, entity.getId());

            stmt.execute();
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Entity> read(Long id) {
        String sql = "select * from entity";
        List<Entity> entities = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            //@TODO: Refactor: use generic
            while (rs.next()) {
                Record record = new Record();
                record.setId(rs.getLong("id"));

                entities.add(record);
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
        String sql = "update entity set id=? where id=?";

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setLong(1, entity.getId());
            stmt.setLong(2, entity.getId());

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
