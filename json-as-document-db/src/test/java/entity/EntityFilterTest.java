package entity;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class EntityFilterTest {

    @Test
    public void eqTest() {
        String expected = "->> 'age' = '30'";
        String filter = EntityFilter.eq("age", "30");

        Assert.assertEquals(expected, filter);
    }
    @Test
    public void arrayEqTest() {
        String expected = "-> 'age' = '[\"30\",\"45\"]'";
        List<String> filters =  buildFilters();
        String filter = EntityFilter.eq("age", filters);

        Assert.assertEquals(expected, filter);
    }

    @Test
    public void ltTest() {
        String expected = "->> 'age' < '30'";
        String filter = EntityFilter.lt("age", "30");

        Assert.assertEquals(expected, filter);
    }

    @Test
    public void arrayLtTest() {
        String expected = "-> 'age' < '[\"30\",\"45\"]'";
        List<String> filters =  buildFilters();
        String filter = EntityFilter.lt("age", filters);

        Assert.assertEquals(expected, filter);
    }

    @Test
    public void gtTest() {
        String expected = "->> 'age' > '30'";
        String filter = EntityFilter.gt("age", "30");

        Assert.assertEquals(expected, filter);
    }

    @Test
    public void arrayGtTest() {
        String expected = "-> 'age' > '[\"30\",\"45\"]'";
        List<String> filters =  buildFilters();
        String filter = EntityFilter.gt("age", filters);

        Assert.assertEquals(expected, filter);
    }

    @Test
    public void geTest() {
        String expected = "->> 'age' >= '30'";
        String filter = EntityFilter.ge("age", "30");

        Assert.assertEquals(expected, filter);
    }

    @Test
    public void arrayGeTest() {
        String expected = "-> 'age' >= '[\"30\",\"45\"]'";
        List<String> filters =  buildFilters();
        String filter = EntityFilter.ge("age", filters);

        Assert.assertEquals(expected, filter);
    }

    @Test
    public void leTest() {
        String expected = "->> 'age' <= '30'";
        String filter = EntityFilter.le("age", "30");

        Assert.assertEquals(expected, filter);
    }

    @Test
    public void arrayLeTest() {
        String expected = "-> 'age' <= '[\"30\",\"45\"]'";
        List<String> filters =  buildFilters();
        String filter = EntityFilter.le("age", filters);

        Assert.assertEquals(expected, filter);
    }

    @Test
    public void andTest() {
        String expected = "age AND document ->> 30";
        String filter = EntityFilter.and("age", "->> 30");

        Assert.assertEquals(expected, filter);
    }

    @Test
    public void orTest() {
        String expected = "age OR document ->> 30";
        String filter = EntityFilter.or("age", "->> 30");

        Assert.assertEquals(expected, filter);
    }

    private ArrayList<String> buildFilters() {
        ArrayList<String> filters = new ArrayList<>();
        filters.add("30");
        filters.add("45");

        return filters;
    }
}