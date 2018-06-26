package viewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import java.util.List;

import model.Contact;
import room.Database;

public class ContactsViewModel extends ViewModel {

    private LiveData<List<Contact>> contacts;

    public void setContacts(Context context, String search) {
        if (search.equals("")) {
            contacts = Database.getDatabase(context).getDao().getContacts();
        } else {
            contacts = Database.getDatabase(context).getDao().getContacts(search);
        }
    }

    public LiveData<List<Contact>> getContacts() {
        return contacts;
    }
}
