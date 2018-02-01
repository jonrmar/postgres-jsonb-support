package entity;

import java.util.Map;

public class Entity {

    private Map<String, Object> document;

    public Map<String, Object> getDocument() {
        return document;
    }

    public void setDocument(Map<String, Object> document) {
        this.document = document;
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
                "document=" + document +
                '}';
    }
}
