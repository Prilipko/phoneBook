package com.akvelon.testtask.phonebook.service.impl;

import com.akvelon.testtask.phonebook.domain.Contact;
import com.akvelon.testtask.phonebook.service.exception.ContactApplicationException;
import com.akvelon.testtask.phonebook.service.ContactsApplication;

import java.util.List;
import java.util.Map;
import java.util.Optional;


/**
 * @author oleksandr.prylypko
 */
public class ContactApplicationImpl implements ContactsApplication {

    private Map<String, Contact> storage;

    public Contact createContact(Contact contact) {
        return Optional.ofNullable(storage.get(contact.getPhone()))
                .map(contact1 -> storage.put(contact.getPhone(), contact))
                .orElseThrow(() -> new ContactApplicationException(
                        "You have tried to add contact with existed phone. Please edit it or add other one"));
    }

    public Contact getContact(String phone) {
        return storage.get(phone);
    }

    public Contact editContact(Contact contact) {
        return Optional.ofNullable(storage.replace(contact.getPhone(), contact))
                .orElseThrow(() -> new ContactApplicationException(
                        "Sorry you can not edit phone number. You can remove the old contact and add new one"));
    }

    public void deleteContact(Contact contact) {
        Optional.ofNullable(storage.remove(contact.getPhone()))
                .orElseThrow(() -> new ContactApplicationException("You have tried to remove absent contact"));
    }

    public List<Contact> search(String query) {
        return null;
    }

    public Map<String, Contact> getStorage() {
        return storage;
    }

    public void setStorage(Map<String, Contact> storage) {
        this.storage = storage;
    }
}
