package entity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class ObjectToEntity {

    private Gson gson;

    public ObjectToEntity(Gson gson) {
        this.gson = gson;
    }

    public Entity convert(Object object){
        Type map = new TypeToken<Map<String, Object>>() {}.getType();

        String json = gson.toJson(object);
        Map<String, Object> document = gson.fromJson(json, map);

        Entity entity = new Entity();
        entity.setDocument(document);

        return !document.isEmpty()? entity: new Entity();
    }

}
