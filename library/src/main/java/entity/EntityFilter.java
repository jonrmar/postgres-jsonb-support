package entity;

public class EntityFilter {

    public static String eq(String key, String value) {
        return "'" + key + "' = '" + value + "'";
    }

    public static String lt(String key, String value) {
        return "'" + key + "' < '" + value + "'";
    }

    public static String gt(String key, String value) {
        return "'" + key + "' > '" + value + "'";
    }

    public static String ge(String key, String value) {
        return "'" + key + "' >= '" + value + "'";
    }

    public static String le(String key, String value) {
        return "'" + key + "' <= '" + value + "'";
    }

    public static String and(String first, String second) {
        return "'" + first + "' AND '" + second + "'";
    }

    public static String or(String first, String second) {
        return "'" + first + "' OR '" + second + "'";
    }
}
