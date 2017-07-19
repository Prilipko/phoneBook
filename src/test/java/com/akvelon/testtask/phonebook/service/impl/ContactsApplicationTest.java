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
public class ContactsApplicationTest {
    private ContactsApplication application;

    @Before
    public void setUp() throws Exception {
        application = new ContactApplicationImpl(new HashMap<>());
    }

    @Test
    public void createContact__givenId() {
        Contact contact = application.createContact(Contact.Builder.aContact()
                .withFirstName("John")
                .withLastName("Doe")
                .withEmail("john.doe@gmail.com")
                .withPhone("122333")
                .build());

        assertNotNull(contact.getId());
    }

    @Test(expected = ContactApplicationException.class)
    public void createContact__addWithIdForbidden() {
        application.createContact(Contact.Builder.aContact()
                .withId(1L)
                .withFirstName("John")
                .withLastName("Doe")
                .withEmail("john.doe@gmail.com")
                .withPhone("122333")
                .build());
    }


    @Test
    public void getContact__any() throws Exception {
        application.createContact(Contact.Builder.aContact().withFirstName("John").withLastName("Doe")
                .withEmail("john.doe@gmail.com").withPhone("122333").build());
        application.createContact(Contact.Builder.aContact().withFirstName("Jane").withLastName("Doe")
                .withEmail("jane.doe@gmail.com").withPhone("455666").build());

        assertNotNull(application.getContact(1L));

    }

    @Test
    public void getContact__thatAbsent() throws Exception {
        application.createContact(Contact.Builder.aContact().withFirstName("John").withLastName("Doe")
                .withEmail("john.doe@gmail.com").withPhone("122333").build());
        application.createContact(Contact.Builder.aContact().withFirstName("Jane").withLastName("Doe")
                .withEmail("jane.doe@gmail.com").withPhone("455666").build());

        assertNull(application.getContact(10L));

    }

    @Test
    public void editContact__correctCase() throws Exception {
        application.createContact(Contact.Builder.aContact().withFirstName("John").withLastName("Doe")
                .withEmail("john.doe@gmail.com").withPhone("122333").build());
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
        Contact contact = Contact.Builder.aContact().withFirstName("John").withLastName("Doe")
                .withEmail("john.doe@gmail.com").withPhone("122333").build();
        application.editContact(contact);
    }

    @Test(expected = ContactApplicationException.class)
    public void editContact__AbsentOne() throws Exception {
        Contact contact = Contact.Builder.aContact().withId(10L).withFirstName("John").withLastName("Doe")
                .withEmail("john.doe@gmail.com").withPhone("122333").build();
        application.editContact(contact);
    }

    @Test
    public void deleteContact__correctCase() throws Exception {
        Contact contact = application.createContact(Contact.Builder.aContact().withFirstName("John").withLastName("Doe")
                .withEmail("john.doe@gmail.com").withPhone("122333").build());

        application.deleteContact(contact);

        assertNull(application.getContact(1L));
    }

    @Test(expected = ContactApplicationException.class)
    public void deleteContact__absent() throws Exception {
        Contact contact = Contact.Builder.aContact().withFirstName("John").withLastName("Doe")
                .withEmail("john.doe@gmail.com").withPhone("122333").build();

        application.deleteContact(contact);
    }

    @Test
    public void search_simpleCase() throws Exception {
        application.createContact(Contact.Builder.aContact().withFirstName("John").withLastName("Doe")
                .withEmail("john.doe@gmail.com").withPhone("122333").build());
        application.createContact(Contact.Builder.aContact().withFirstName("Jane").withLastName("Doe")
                .withEmail("jane.doe@gmail.com").withPhone("455666").build());
        application.createContact(Contact.Builder.aContact().withFirstName("Bob").withLastName("Sunset")
                .withEmail("bob.s@gmail.com").withPhone("123456").build());

        List<Contact> contacts = application.search("doe");

        assertEquals(2, contacts.size());
    }

    @Test
    public void search_noOne() throws Exception {
        application.createContact(Contact.Builder.aContact().withFirstName("John").withLastName("Doe")
                .withEmail("john.doe@gmail.com").withPhone("122333").build());
        application.createContact(Contact.Builder.aContact().withFirstName("Jane").withLastName("Doe")
                .withEmail("jane.doe@gmail.com").withPhone("455666").build());
        application.createContact(Contact.Builder.aContact().withFirstName("Bob").withLastName("Sunset")
                .withEmail("bob.s@gmail.com").withPhone("123456").build());

        List<Contact> contacts = application.search("AnotherName");

        assertEquals(0, contacts.size());
    }

    @Test
    public void search_withQualifier() throws Exception {
        application.createContact(Contact.Builder.aContact().withFirstName("John").withLastName("Doe")
                .withEmail("john.doe@gmail.com").withPhone("122333").build());
        application.createContact(Contact.Builder.aContact().withFirstName("Jane").withLastName("Doe")
                .withEmail("jane.doe@gmail.com").withPhone("455666").build());
        application.createContact(Contact.Builder.aContact().withFirstName("Bob").withLastName("Sunset")
                .withEmail("bob.s@gmail.com").withPhone("123456").build());

        List<Contact> contacts = application.search("doe 333");

        assertEquals(1, contacts.size());
    }

    @Test
    public void search_equal() throws Exception {
        application.createContact(Contact.Builder.aContact().withFirstName("John").withLastName("Doe")
                .withEmail("john.doe@gmail.com").withPhone("122333").build());
        application.createContact(Contact.Builder.aContact().withFirstName("Jane").withLastName("Doe")
                .withEmail("jane.doe@gmail.com").withPhone("455666").build());
        application.createContact(Contact.Builder.aContact().withFirstName("Bob").withLastName("Sunset")
                .withEmail("bob.s@gmail.com").withPhone("123456").build());

        List<Contact> contacts = application.search("John john.doe@gmail.com");

        assertEquals(1, contacts.size());
        assertEquals("John", contacts.get(0).getFirstName());
        assertEquals("john.doe@gmail.com", contacts.get(0).getEmail());
    }

}