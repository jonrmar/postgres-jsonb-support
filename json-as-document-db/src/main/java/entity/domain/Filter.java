package entity.domain;

public class Filter<T> {
    Class<T> clazz;
    String type;
    Object value;

    public Filter(Class<T> clazz, String type, Object value) {
        this.clazz = clazz;
        this.type = type;
        this.value = value;
    }
}
