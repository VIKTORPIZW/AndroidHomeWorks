package com.homework.hw4_1_rework;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

public class ActivityAddContact extends MainActivity {
    private RadioButton phoneButton;
    private RadioButton emailButton;
    private TextView nameView;
    private TextView infoView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        phoneButton = findViewById(R.id.PhoneButton);
        phoneButton.setOnClickListener(radioButtonClickListener);

        emailButton = findViewById(R.id.EmailButton);
        emailButton.setOnClickListener(radioButtonClickListener);

        nameView = findViewById(R.id.nameEditTextView);
        infoView = findViewById(R.id.editTextViewInfo);

        findViewById(R.id.toolbarBackButton).setOnClickListener(v -> {
            setResult(Activity.RESULT_CANCELED);
            finish();
        });

        findViewById(R.id.toolbarSaveButton).setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            if (phoneButton.isChecked()) {
                ContactList.getObjectContactList().addContact(
                        new Contact(nameView.getText().toString(),
                                infoView.getText().toString(),
                                InfoType.PHONE_NUMBER));
                resultIntent.putExtra(NEW_CONTACT, "Contact added");
            } else if (emailButton.isChecked()) {
                ContactList.getObjectContactList().addContact(
                        new Contact(nameView.getText().toString(),
                                infoView.getText().toString(),
                                InfoType.EMAIL));
                resultIntent.putExtra(NEW_CONTACT, "Contact added");
            }
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
            nameView.setText("");
            infoView.setText("");
        });
    }

    View.OnClickListener radioButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RadioButton rb = (RadioButton) v;
            switch (rb.getId()) {
                case R.id.PhoneButton:
                    infoView.setHint(R.string.Phone_number);
                    break;
                case R.id.EmailButton:
                    infoView.setHint(R.string.Email);
                    break;
                default:
                    break;
            }
        }
    };
}
