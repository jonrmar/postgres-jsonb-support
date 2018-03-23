package entity;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ObjectToEntity {

    public static Entity convert(Object object){
        Map<String, Object> document = new HashMap<>();

        for (Field field : object.getClass().getDeclaredFields()) {
            String fieldName = field.getName();
            if (!fieldName.equals("id") && !fieldName.equals("createdAt") && !fieldName.equals("updatedAt")) {
                try {
                    field.setAccessible(true);
                    document.put(fieldName, field.get(object));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        Entity entity = new Entity();
        entity.setDocument(document);

        return !document.isEmpty()? entity: new Entity();
    }

}
