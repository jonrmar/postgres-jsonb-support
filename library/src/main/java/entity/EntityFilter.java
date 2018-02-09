package entity;

public class EntityFilter {

    public static String eq(String key, String value) {
        return getNestedFields(key) + " = '" + value + "'";
    }

    public static String lt(String key, String value) {
        return getNestedFields(key) + " < '" + value + "'";
    }

    public static String gt(String key, String value) {
        return getNestedFields(key) + " > '" + value + "'";
    }

    public static String ge(String key, String value) {
        return getNestedFields(key) + " >= '" + value + "'";
    }

    public static String le(String key, String value) {
        return getNestedFields(key) + " <= '" + value + "'";
    }

    public static String and(String first, String second) {
        return first + " AND document " + second;
    }

    public static String or(String first, String second) {
        return first + " OR document " + second;
    }

    private static String getNestedFields(String key) {

        if (!key.contains(".")) return "->> '" + key + "'";

        String[] fields = key.split("\\.");
        String[] fieldsMarks = new String[fields.length];

        for (int i = 0; i < fields.length; i++) {
            fieldsMarks[i] = "'" + fields[i] + "'";
        }

        return " -> " + String.join(" ->> ", fieldsMarks);
    }
}
