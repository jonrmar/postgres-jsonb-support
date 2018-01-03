package crud;

import crud.dao.EntityDAO;
import crud.domain.Record;

public class Application {

    public static void main(String[] args) {
        Record record = new Record();
        record.setId(1L);

        EntityDAO<Record> recordEntityDAO = new EntityDAO<>();
        boolean result = recordEntityDAO.save(record);

        System.out.println("Result from db: "+result);
    }
}
