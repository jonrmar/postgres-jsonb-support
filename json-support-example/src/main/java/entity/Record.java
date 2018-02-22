package entity;

import java.util.List;
import java.util.Map;

public class Record {
    private String name;
    private String age;
    private String hobby;
    private Map<String, String> favoriteFoods;
    private List<String> sports;

    public Record(String name, String age, String hobby, Map<String, String> favoriteFoods, List<String> sports) {
        this.name = name;
        this.age = age;
        this.hobby = hobby;
        this.favoriteFoods = favoriteFoods;
        this.sports = sports;
    }
}
