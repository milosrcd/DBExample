package com.example.kri.dbexample;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import adapter.ContactAdapter;
import model.Contact;
import room.Database;
import viewModel.ContactsViewModel;

public class MainActivity extends AppCompatActivity {

    private ContactAdapter adapter;
    private TextView noContacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertDialog();
            }
        });

        noContacts = findViewById(R.id.noContacts);
        EditText search = findViewById(R.id.search);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        adapter = new ContactAdapter(this);
        recyclerView.setAdapter(adapter);

        setUpViewModel("");

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    setUpViewModel("%" + charSequence.toString() + "%");
                } else {
                    setUpViewModel("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void setUpViewModel(String search) {
        ContactsViewModel contactsViewModel = ViewModelProviders.of(this).get(ContactsViewModel.class);
        contactsViewModel.setContacts(this, search);
        contactsViewModel.getContacts().observe(this, new Observer<List<Contact>>() {
            @Override
            public void onChanged(@Nullable List<Contact> contacts) {
                if (contacts != null) {
                    if (contacts.size() == 0) {
                        noContacts.setVisibility(View.VISIBLE);
                    } else {
                        noContacts.setVisibility(View.GONE);
                    }
                    adapter.addList(contacts);
                }
            }
        });
    }

    @SuppressLint("InflateParams")
    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.new_contact);
        builder.setMessage(R.string.new_contact_description);

        View root = getLayoutInflater().inflate(R.layout.alert_dialog_custom_layout, null);

        final EditText firstName = root.findViewById(R.id.etFirstName);
        final EditText lastName = root.findViewById(R.id.etLastName);
        final EditText phoneNumber = root.findViewById(R.id.etPhoneNumber);

        builder.setView(root);
        builder.setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                final String first = firstName.getText().toString();
                final String last = lastName.getText().toString();
                final String phone = phoneNumber.getText().toString();
                if (!TextUtils.isEmpty(first) && !TextUtils.isEmpty(last) && !TextUtils.isEmpty(phone)) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Database.getDatabase(MainActivity.this).getDao().addContact(new Contact(first, last, phone));
                        }
                    }).start();

                    Toast.makeText(MainActivity.this, "New contact successfully added.", Toast.LENGTH_SHORT).show();
                    setUpViewModel("");
                } else {
                    Toast.makeText(MainActivity.this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
                }
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
}
