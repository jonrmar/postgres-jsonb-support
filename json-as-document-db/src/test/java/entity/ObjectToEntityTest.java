package entity;

import entity.domain.Entity;
import integration.dao.Record;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class ObjectToEntityTest {

    @Test
    public void shouldBuildEntityTest(){
        Record record = newRecord();

        Entity entity = ObjectToEntity.convert(record);

        assertEqualsDocument(entity);
    }

    private void assertEqualsDocument(Entity entity) {
        assertEquals(entity.getDocument().get("name"), buildDocument().get("name"));
        assertEquals(entity.getDocument().get("age"), buildDocument().get("age"));
        assertEquals(entity.getDocument().get("hobby"), buildDocument().get("hobby"));
        assertEquals(entity.getDocument().get("sports").toString(), buildDocument().get("sports"));
        assertEquals(entity.getDocument().get("favoriteFoods").toString(), buildDocument().get("favoriteFoods"));
    }

    private Record newRecord() {
        Map<String, String> favoriteFoods = new HashMap<>();
        favoriteFoods.put("desert", "banana");
        favoriteFoods.put("lunch", "fried chicken");
        favoriteFoods.put("snack", "ice cream");

        List<String> sports = new ArrayList<>();
        sports.add("swim");
        sports.add("soccer");

        return new Record("John Doe 5", "25", "movies", favoriteFoods, sports);
    }

    private Map<String, Object> buildDocument(){
        Map<String, Object> document = new HashMap<>();
        document.put("sports", "[swim, soccer]");
        document.put("name", "John Doe 5");
        document.put("age", "25");
        document.put("hobby", "movies");
        document.put("favoriteFoods", "{lunch=fried chicken, snack=ice cream, desert=banana}");

        return document;
    }
}