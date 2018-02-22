package entity;

import java.util.Arrays;
import java.util.List;

public class EntityFilter {

    public static String eq(String key, String value) { return nestedFields(key) + " = '" + value + "'"; }

    public static String eq(String key, List<String> values) {
        StringBuilder builder = new StringBuilder();
        String jsonArray = String.join("\",\"", values);

        builder.append("-> '");
        builder.append(key);
        builder.append("' = '[\"");
        builder.append(jsonArray);
        builder.append("\"]'");

        return builder.toString();
    }

    public static String lt(String key, String value) {
        return nestedFields(key) + " < '" + value + "'";
    }

    public static String lt(String key, List<String> values) {
        StringBuilder builder = new StringBuilder();
        String jsonArray = String.join("\",\"", values);

        builder.append("-> '");
        builder.append(key);
        builder.append("' < '[\"");
        builder.append(jsonArray);
        builder.append("\"]'");

        return builder.toString();
    }

    public static String gt(String key, String value) {
        return nestedFields(key) + " > '" + value + "'";
    }

    public static String gt(String key, List<String> values) {
        StringBuilder builder = new StringBuilder();
        String jsonArray = String.join("\",\"", values);

        builder.append("-> '");
        builder.append(key);
        builder.append("' > '[\"");
        builder.append(jsonArray);
        builder.append("\"]'");

        return builder.toString();
    }

    public static String ge(String key, String value) {
        return nestedFields(key) + " >= '" + value + "'";
    }

    public static String ge(String key, List<String> values) {
        StringBuilder builder = new StringBuilder();
        String jsonArray = String.join("\",\"", values);

        builder.append("-> '");
        builder.append(key);
        builder.append("' >= '[\"");
        builder.append(jsonArray);
        builder.append("\"]'");

        return builder.toString();
    }

    public static String le(String key, String value) {
        return nestedFields(key) + " <= '" + value + "'";
    }

    public static String le(String key, List<String> values) {
        StringBuilder builder = new StringBuilder();
        String jsonArray = String.join("\",\"", values);

        builder.append("-> '");
        builder.append(key);
        builder.append("' <= '[\"");
        builder.append(jsonArray);
        builder.append("\"]'");

        return builder.toString();
    }

    public static String and(String first, String second) {
        return first + " AND document " + second;
    }

    public static String or(String first, String second) {
        return first + " OR document " + second;
    }

    public static List<String> asList(String... values) { return Arrays.asList(values); }

    private static String nestedFields(String key) {

        if (!key.contains(".")) return key + "'";

        String[] fields = key.split("\\.");
        String[] fieldsMarks = new String[fields.length];

        for (int i = 0; i < fields.length; i++) {
            fieldsMarks[i] = "'" + fields[i] + "'";
        }

        return String.join(" ->> ", fieldsMarks);
    }
}
