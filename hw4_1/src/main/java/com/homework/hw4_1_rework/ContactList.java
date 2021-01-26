package com.homework.hw4_1_rework;

import java.util.ArrayList;

public class ContactList {
    private static ContactList objectContactList;
    private static ArrayList<Contact> contactList;

    private ContactList() {
    }

    public static ContactList getObjectContactList() {
        if (objectContactList == null) {
            objectContactList = new ContactList();
            contactList = new ArrayList<>();
        }
        return objectContactList;
    }

    public ArrayList<Contact> getContactList() {
        return contactList;
    }

    public void addContact(Contact contact) {
        contactList.add(contact);
    }

    public void setContact(int position, Contact contact) {
        contactList.set(position, contact);
    }

    public Contact getContact(int position) {
        return contactList.get(position);
    }
}
