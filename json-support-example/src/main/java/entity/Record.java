package entity;

import java.util.Map;

public class Record {
    private String name;
    private String age;
    private String hobby;
    private Map<String, String> favoriteFoods;

    public Record(String name, String age, String hobby, Map<String, String> favoriteFoods) {
        this.name = name;
        this.age = age;
        this.hobby = hobby;
        this.favoriteFoods = favoriteFoods;
    }
}
