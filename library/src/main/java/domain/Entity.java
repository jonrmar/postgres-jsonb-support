package domain;

import java.util.Map;

public class Entity {

    private Long id;
    private Map<String, Object> document;

    public Long getId() {
        return id;
    }

    public Map<String, Object> getDocument() {
        return document;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDocument(Map<String, Object> document) {
        this.document = document;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Entity)) return false;

        Entity entity = (Entity) o;

        if (getId() != null ? !getId().equals(entity.getId()) : entity.getId() != null) return false;
        return getDocument() != null ? getDocument().equals(entity.getDocument()) : entity.getDocument() == null;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getDocument() != null ? getDocument().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Entity{" +
                "id=" + id +
                ", document=" + document +
                '}';
    }
}
