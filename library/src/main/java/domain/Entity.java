package domain;

import java.util.Map;

public interface Entity {
    Long getId();
    void setId(Long Id);
    Map<String, Object> getDocument();
    void setDocument(Map<String, Object> document);
}
