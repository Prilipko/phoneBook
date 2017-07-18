package com.akvelon.testtask.phonebook.service;

import com.akvelon.testtask.phonebook.domain.Contact;

import java.util.List;

/**
 * The Contacts Applications exposes Contact information for use by any application
 * outside of this application that has been granted permission by the user in the settings.
 */
public interface ContactsApplication {

    Contact createContact(final Contact contact);

    Contact getContact(String phone);

    Contact editContact(final Contact contact);

    void deleteContact(final Contact contact);

    List<Contact> search(String query);

}
