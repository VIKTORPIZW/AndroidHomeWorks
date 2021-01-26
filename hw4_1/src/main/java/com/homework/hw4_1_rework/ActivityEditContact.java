package com.homework.hw4_1_rework;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ActivityEditContact extends MainActivity {
    private TextView nameView;
    private TextView infoView;
    private Contact contact;
    private int contactNumber;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        nameView = findViewById(R.id.nameEditTextView);
        infoView = findViewById(R.id.editTextViewInfo);

        final Intent intent = getIntent();
        if (intent != null) {
            contactNumber = intent.getIntExtra(CONTACT_NUMBER, 0);
            contact = ContactList.getObjectContactList().getContact(contactNumber);
            nameView.setText(contact.getName());
            infoView.setText(contact.getInfo());
        }

        findViewById(R.id.toolbarEditSaveButton).setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            ContactList.getObjectContactList().setContact(contactNumber,
                    new Contact(nameView.getText().toString(),
                            infoView.getText().toString(),
                            contact.getInfoType()));
            resultIntent.putExtra(MODIFIED_CONTACT, "Contact was changed");
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
            nameView.setText("");
            infoView.setText("");
        });

        findViewById(R.id.toolbarEditBackButton).setOnClickListener(v -> {
            setResult(Activity.RESULT_CANCELED);
            finish();
        });

        findViewById(R.id.removeButton).setOnClickListener(v -> {
            nameView.setText("");
            infoView.setText("");
        });
    }

}

