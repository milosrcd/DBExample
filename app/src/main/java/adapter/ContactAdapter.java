package adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kri.dbexample.R;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;

import model.Contact;
import room.Database;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {

    private Context context;
    private List<Contact> contacts;

    public ContactAdapter(Context context) {
        this.context = context;
        this.contacts = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Contact contact = contacts.get(position);
        holder.firstName.setText(contact.getFirstName());
        holder.lastName.setText(contact.getLastName());
        holder.phoneNumber.setText(contact.getPhoneNumber());

        holder.container.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showAlertDialog(context, contact);
                return true;
            }
        });
    }

    private void showAlertDialog(final Context context, final Contact contact) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.delete_contact);
        builder.setMessage(R.string.delete_contact_description);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Database.getDatabase(context).getDao().deleteContact(contact);
                    }
                }).start();

                Toast.makeText(context, "Contact successfully removed.", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.create().show();
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public void addList(List<Contact> contacts) {
        this.contacts = contacts;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout container;
        TextView firstName, lastName, phoneNumber;

        ViewHolder(View view) {
            super(view);
            container = view.findViewById(R.id.container);
            firstName = view.findViewById(R.id.tvFirstNameData);
            lastName = view.findViewById(R.id.tvLastNameData);
            phoneNumber = view.findViewById(R.id.tvPhoneNumberData);
        }
    }
}
