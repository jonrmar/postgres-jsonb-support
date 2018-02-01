package entity;

import com.google.gson.Gson;
import dao.EntityDAO;

import java.sql.Connection;

public class EntityService {

    private EntityDAO entityDAO;

    public EntityService(Connection connection) {
        Gson gson = new Gson();
        ObjectToEntity objectToEntity = new ObjectToEntity(gson);
        this.entityDAO = new EntityDAO(connection, gson, objectToEntity);
    }

    public EntityDAO getEntityDAO(){
        return entityDAO;
    }
}
