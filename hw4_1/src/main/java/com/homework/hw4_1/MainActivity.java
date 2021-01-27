package com.homework.hw4_1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.homework.hw4_1_rework.R;

public class MainActivity extends AppCompatActivity {
    public static final String NEW_CONTACT = "NewContact";
    public static final String CONTACT_NUMBER = "ContactNumber";
    public static final String MODIFIED_CONTACT = "ModifiedContact";
    private SearchView searchView;
    private ContactListAdapter adapter;
    private RecyclerView recyclerView;

    public interface ListContactActionListener {
        void onContactClicked(int number);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        adapter = new ContactListAdapter(
                new ListContactActionListener() {
                    @Override
                    public void onContactClicked(int number) {
                        Intent intent = new Intent(
                                MainActivity.this, ActivityEditContact.class);
                        intent.putExtra(CONTACT_NUMBER, number);
                        startActivityForResult(intent, 456);
                    }
                });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        findViewById(R.id.buttonAddContact).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(
                        new Intent(MainActivity.this, ActivityAddContact.class),
                        123);
            }
        });

        searchView = findViewById(R.id.searchContact);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 123 && resultCode == Activity.RESULT_OK && data != null) {
            adapter.addItems(ContactList.getObjectContactList().getContactList());
        } else if (requestCode == 456 && resultCode == Activity.RESULT_OK && data != null) {
            adapter.addItems(ContactList.getObjectContactList().getContactList());
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        adapter.addItems(ContactList.getObjectContactList().getContactList());
        super.onRestoreInstanceState(savedInstanceState);
    }

}
