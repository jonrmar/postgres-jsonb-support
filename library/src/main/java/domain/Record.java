package domain;

import java.util.Map;

public class Record implements Entity {
    private Long id;
    private Map<String, Object> document;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Map<String, Object> getDocument() {
        return document;
    }

    @Override
    public void setDocument(Map<String, Object> document) {
        this.document = document;
    }

    @Override
    public String toString() {
        return "Record{" +
                "id=" + id +
                ", document=" + document +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Record)) return false;

        Record record = (Record) o;

        return getId() != null ? getId().equals(record.getId()) : record.getId() == null;
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }
}
