package room;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import model.Contact;

@android.arch.persistence.room.Dao
public interface Dao {

    @Query("SELECT * FROM contacts")
    LiveData<List<Contact>> getContacts();

    @Query("SELECT * FROM contacts WHERE lastName LIKE :search")
    LiveData<List<Contact>> getContacts(String search);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addContact(Contact contact);

    @Delete
    void deleteContact(Contact contact);
}
