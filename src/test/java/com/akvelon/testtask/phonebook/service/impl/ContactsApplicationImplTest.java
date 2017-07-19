package com.akvelon.testtask.phonebook.service.impl;

import com.akvelon.testtask.phonebook.domain.Contact;
import com.akvelon.testtask.phonebook.service.ContactsApplication;
import com.akvelon.testtask.phonebook.service.exception.ContactApplicationException;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author oleksandr.prylypko
 */
public class ContactsApplicationImplTest {

    private final static String FIRST_NAME_1 = "John";
    private final static String LAST_NAME_1 = "Doe";
    private final static String EMAIL_1 = "john.doe@gmail.com";
    private final static String PHONE_1 = "122333";

    private final static String FIRST_NAME_2 = "Jane";
    private final static String LAST_NAME_2 = "Doe";
    private final static String EMAIL_2 = "jane.doe@gmail.com";
    private final static String PHONE_2 = "455666";

    private final static String FIRST_NAME_3 = "Bob";
    private final static String LAST_NAME_3 = "Sunset";
    private final static String EMAIL_3 = "bob.s@gmail.com";
    private final static String PHONE_3 = "123456";


    private ContactsApplication application;

    @Before
    public void setUp() throws Exception {
        application = new ContactApplicationImpl(new HashMap<>());
    }

    @Test
    public void createContact__givenId() {
        Contact contact = application.createContact(Contact.Builder.aContact()
                .withFirstName(FIRST_NAME_1)
                .withLastName(LAST_NAME_1)
                .withEmail(EMAIL_1)
                .withPhone(PHONE_1)
                .build());

        assertNotNull(contact.getId());
    }

    @Test(expected = ContactApplicationException.class)
    public void createContact__addWithIdForbidden() {
        application.createContact(Contact.Builder.aContact()
                .withId(1L)
                .withFirstName(FIRST_NAME_1)
                .withLastName(LAST_NAME_1)
                .withEmail(EMAIL_1)
                .withPhone(PHONE_1)
                .build());
    }


    @Test
    public void getContact__any() throws Exception {
        application.createContact(Contact.Builder.aContact()
                .withFirstName(FIRST_NAME_1)
                .withLastName(LAST_NAME_1)
                .withEmail(EMAIL_1)
                .withPhone(PHONE_1)
                .build());
        application.createContact(Contact.Builder.aContact().withFirstName(FIRST_NAME_2).withLastName(LAST_NAME_2)
                .withEmail(EMAIL_2).withPhone(PHONE_2).build());

        assertNotNull(application.getContact(1L));

    }

    @Test
    public void getContact__thatAbsent() throws Exception {
        application.createContact(Contact.Builder.aContact().withFirstName(FIRST_NAME_1).withLastName(LAST_NAME_1)
                .withEmail(EMAIL_1).withPhone(PHONE_1).build());
        application.createContact(Contact.Builder.aContact().withFirstName(FIRST_NAME_2).withLastName(LAST_NAME_2)
                .withEmail(EMAIL_2).withPhone(PHONE_2).build());

        assertNull(application.getContact(10L));

    }

    @Test
    public void editContact__correctCase() throws Exception {
        application.createContact(Contact.Builder.aContact().withFirstName(FIRST_NAME_1).withLastName(LAST_NAME_1)
                .withEmail(EMAIL_1).withPhone(PHONE_1).build());
        Contact changed = application.getContact(1L);
        changed.setEmail("new Mail");
        changed.setFirstName("new First name");
        changed.setLastName("new Last name");
        changed.setPhone("new phone");

        Contact edited = application.editContact(changed);
        assertEquals(changed.getId(), edited.getId());
        assertEquals(changed.getEmail(), edited.getEmail());
        assertEquals(changed.getFirstName(), edited.getFirstName());
        assertEquals(changed.getLastName(), edited.getLastName());
        assertEquals(changed.getPhone(), edited.getPhone());

    }

    @Test(expected = ContactApplicationException.class)
    public void editContact__newOne() throws Exception {
        Contact contact = Contact.Builder.aContact().withFirstName(FIRST_NAME_1).withLastName(LAST_NAME_1)
                .withEmail(EMAIL_1).withPhone(PHONE_1).build();
        application.editContact(contact);
    }

    @Test(expected = ContactApplicationException.class)
    public void editContact__AbsentOne() throws Exception {
        Contact contact = Contact.Builder.aContact().withId(10L).withFirstName(FIRST_NAME_1).withLastName(LAST_NAME_1)
                .withEmail(EMAIL_1).withPhone(PHONE_1).build();
        application.editContact(contact);
    }

    @Test
    public void deleteContact__correctCase() throws Exception {
        Contact contact = application.createContact(Contact.Builder.aContact().withFirstName(FIRST_NAME_1).withLastName(LAST_NAME_1)
                .withEmail(EMAIL_1).withPhone(PHONE_1).build());

        application.deleteContact(contact);

        assertNull(application.getContact(1L));
    }

    @Test(expected = ContactApplicationException.class)
    public void deleteContact__absent() throws Exception {
        Contact contact = Contact.Builder.aContact().withFirstName(FIRST_NAME_1).withLastName(LAST_NAME_1)
                .withEmail(EMAIL_1).withPhone(PHONE_1).build();

        application.deleteContact(contact);
    }

    @Test
    public void search_simpleCase() throws Exception {
        application.createContact(Contact.Builder.aContact().withFirstName(FIRST_NAME_1).withLastName(LAST_NAME_1)
                .withEmail(EMAIL_1).withPhone(PHONE_1).build());
        application.createContact(Contact.Builder.aContact().withFirstName(FIRST_NAME_2).withLastName(LAST_NAME_2)
                .withEmail(EMAIL_2).withPhone(PHONE_2).build());
        application.createContact(Contact.Builder.aContact().withFirstName(FIRST_NAME_3).withLastName(LAST_NAME_3)
                .withEmail(EMAIL_3).withPhone(PHONE_3).build());

        List<Contact> contacts = application.search("doe");

        assertEquals(2, contacts.size());
    }

    @Test
    public void search_noOne() throws Exception {
        application.createContact(Contact.Builder.aContact().withFirstName(FIRST_NAME_1).withLastName(LAST_NAME_1)
                .withEmail(EMAIL_1).withPhone(PHONE_1).build());
        application.createContact(Contact.Builder.aContact().withFirstName(FIRST_NAME_2).withLastName(LAST_NAME_2)
                .withEmail(EMAIL_2).withPhone(PHONE_2).build());
        application.createContact(Contact.Builder.aContact().withFirstName(FIRST_NAME_3).withLastName(LAST_NAME_3)
                .withEmail(EMAIL_3).withPhone(PHONE_3).build());

        List<Contact> contacts = application.search("AnotherName");

        assertEquals(0, contacts.size());
    }

    @Test
    public void search_withQualifier() throws Exception {
        application.createContact(Contact.Builder.aContact().withFirstName(FIRST_NAME_1).withLastName(LAST_NAME_1)
                .withEmail(EMAIL_1).withPhone(PHONE_1).build());
        application.createContact(Contact.Builder.aContact().withFirstName(FIRST_NAME_2).withLastName(LAST_NAME_2)
                .withEmail(EMAIL_2).withPhone(PHONE_2).build());
        application.createContact(Contact.Builder.aContact().withFirstName(FIRST_NAME_3).withLastName(LAST_NAME_3)
                .withEmail(EMAIL_3).withPhone(PHONE_3).build());

        List<Contact> contacts = application.search("doe 333");

        assertEquals(1, contacts.size());
    }

    @Test
    public void search_equal() throws Exception {
        application.createContact(Contact.Builder.aContact().withFirstName(FIRST_NAME_1).withLastName(LAST_NAME_1)
                .withEmail(EMAIL_1).withPhone(PHONE_1).build());
        application.createContact(Contact.Builder.aContact().withFirstName(FIRST_NAME_2).withLastName(LAST_NAME_2)
                .withEmail(EMAIL_2).withPhone(PHONE_2).build());
        application.createContact(Contact.Builder.aContact().withFirstName(FIRST_NAME_3).withLastName(LAST_NAME_3)
                .withEmail(EMAIL_3).withPhone(PHONE_3).build());

        List<Contact> contacts = application.search("John john.doe@gmail.com");

        assertEquals(1, contacts.size());
        assertEquals(FIRST_NAME_1, contacts.get(0).getFirstName());
        assertEquals(EMAIL_1, contacts.get(0).getEmail());
    }

}