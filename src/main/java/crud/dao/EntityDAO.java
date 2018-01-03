package crud.dao;

import crud.domain.Entity;

import java.util.ArrayList;
import java.util.List;

public class EntityDAO<T extends Entity> {

    private List<Entity> entities;

    public EntityDAO() {
        this.entities = new ArrayList<>();
    }

    public boolean save(Entity entity) {
        entities.add(entity);

        return entities.contains(entity);
    }

    public Entity read(Long id) {
        return entities.stream()
                .filter(entity -> entity.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public boolean update(Entity entity) {
        entities.stream()
                .filter(entityDB -> entityDB.getId().equals(entity.getId()))
                .findFirst()
                .ifPresent(entity1 -> entity1.setId(entity.getId()));

        return entities.stream()
                .anyMatch(entityFiltered -> entityFiltered.getId().equals(entity.getId()));
    }

    public boolean delete(Long id) {
        entities.stream()
                .filter(entity -> entity.getId().equals(id))
                .findFirst()
                .ifPresent(entityFound -> {
                    entities.remove(entityFound);
                });

        return entities.stream()
                .noneMatch(entity -> entity.getId().equals(id));
    }
}
