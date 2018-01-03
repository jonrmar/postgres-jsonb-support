package crud.dao;

import crud.domain.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EntityDAO<T extends Entity> {

    private List<Entity> entities;

    public EntityDAO() {
        this.entities = new ArrayList<>();
    }

    public boolean save(Entity entity){
        entities.add(entity);

        return entities.contains(entity);
    }

    public Entity read(Long id){
        return entities.stream()
                .filter(entity -> entity.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public boolean delete(Long id){
        entities.stream()
                .filter(entity -> entity.getId().equals(id))
                .findFirst()
                .ifPresent( entityFound -> {
                    entities.remove(entityFound);
                });

        return entities.stream()
                .noneMatch(entity -> entity.getId().equals(id));
    }
}
