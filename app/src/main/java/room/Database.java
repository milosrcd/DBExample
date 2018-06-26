package room;

import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import model.Contact;

@android.arch.persistence.room.Database(entities = {Contact.class}, version = 1)
public abstract class Database extends RoomDatabase {

    private static Database INSTANCE;

    public static synchronized Database getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), Database.class, "testDatabase").build();
        }
        return INSTANCE;
    }

    public abstract Dao getDao();
}
