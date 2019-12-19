package engineer.babu.chatapplication.data.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import engineer.babu.chatapplication.data.model.Person;

@Dao
public interface PersonDao {

    @Query("SELECT * FROM PERSON ORDER BY ID")
    List<Person> loadAllPersons();

    @Insert
    void insertPerson(Person person);
    @Query("SELECT * FROM PERSON WHERE id = :id")
    Person loadPersonById(int id);
}
