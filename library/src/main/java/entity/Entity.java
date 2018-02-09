package entity;

import java.time.LocalDateTime;
import java.util.Map;

public class Entity {

    private Long id;
    private Map<String, Object> document;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Map<String, Object> getDocument() {
        return document;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDocument(Map<String, Object> document) {
        this.document = document;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Entity)) return false;

        Entity entity = (Entity) o;

        return getDocument() != null ? getDocument().equals(entity.getDocument()) : entity.getDocument() == null;
    }

    @Override
    public int hashCode() {
        return getDocument() != null ? getDocument().hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Entity{" +
                "id=" + id +
                ", document=" + document +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
