package document;

import entity.annotations.Entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Entity(schema = "example")
public class Record {
    private Long id;
    private String name;
    private String age;
    private String hobby;
    private Map<String, String> favoriteFoods;
    private List<String> sports;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Record(){}

    public Record(String name, String age, String hobby, Map<String, String> favoriteFoods, List<String> sports) {
        this.name = name;
        this.age = age;
        this.hobby = hobby;
        this.favoriteFoods = favoriteFoods;
        this.sports = sports;
    }

    @Override
    public String toString() {
        return "Record{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age='" + age + '\'' +
                ", hobby='" + hobby + '\'' +
                ", favoriteFoods=" + favoriteFoods +
                ", sports=" + sports +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
