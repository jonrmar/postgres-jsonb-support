package crud;

import crud.dao.EntityDAO;
import crud.domain.Entity;
import crud.domain.Record;

public class Application {

    public static void main(String[] args) {
        Record record = new Record();
        record.setId(1L);
        EntityDAO<Record> recordEntityDAO = new EntityDAO<>();

        System.out.println("### Testing save operation ### ");
        boolean result = recordEntityDAO.save(record);
        System.out.println("Result from saving on db: "+result);

        System.out.println("### Testing read operation ###");
        Entity entity = recordEntityDAO.read(1L);
        System.out.println("Result from reading on db: "+ entity);

        System.out.println("### Testing delete operation ###");
        result = recordEntityDAO.delete(1L);
        entity = recordEntityDAO.read(1L);
        System.out.println("Result from deleting on db: "+ result + " and reading: "+ entity);

    }
}
