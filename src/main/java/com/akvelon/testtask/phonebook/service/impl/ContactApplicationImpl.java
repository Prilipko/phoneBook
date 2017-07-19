package com.akvelon.testtask.phonebook.service.impl;

import com.akvelon.testtask.phonebook.domain.Contact;
import com.akvelon.testtask.phonebook.service.exception.ContactApplicationException;
import com.akvelon.testtask.phonebook.service.ContactsApplication;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;


/**
 * @author oleksandr.prylypko
 */
public class ContactApplicationImpl implements ContactsApplication {

    private final Map<Long, Contact> storage;
    private final Supplier<Long> idGenerator = new Supplier<Long>() {
        private long counter = 0L;

        @Override
        public Long get() {
            Long threshold = -1L;
            while (storage.containsKey(++counter)) {
                if (threshold++ == Long.MAX_VALUE) {
                    //It might be never gonna happen, but I am noticing it
                    throw new ContactApplicationException("Too many contacts on repository.");
                }
            }
            return counter;
        }
    };

    public ContactApplicationImpl(Map<Long, Contact> storage) {
        this.storage = storage;
    }

    public Contact createContact(final Contact contact) {
        if (contact.getId() != null)
            throw new ContactApplicationException("You should add only new contact, without id");
        Contact newContact = Contact.Builder.from(contact)
                .withId(idGenerator.get())
                .build();
        storage.put(newContact.getId(), newContact);
        return Contact.Builder.from(newContact).build();
    }

    public Contact getContact(Long id) {
        return Optional.ofNullable(storage.get(id))
                .map(contact -> Contact.Builder.from(contact).build()).orElse(null);
    }

    public Contact editContact(final Contact contact) {
        if (contact.getId() == null)
            throw new ContactApplicationException("Contact is absent in storage, add it or edit another one");
        if (null == storage.replace(contact.getId(), Contact.Builder.from(contact).build())) {
            throw new ContactApplicationException(
                    "You can not edit this contact. It is absent in storage");
        }
        return Contact.Builder.from(contact).build();
    }

    public void deleteContact(Contact contact) {
        Optional.ofNullable(storage.remove(contact.getId()))
                .orElseThrow(() -> new ContactApplicationException("You have tried to remove absent contact"));
    }

    public List<Contact> search(String query) {
        String[] qualifiers = query.split(" ");
        return storage.values().stream().filter(contact -> {
            int cases = 0;
            for (String spec : qualifiers) {
                if (StringUtils.containsIgnoreCase(contact.getFirstName(), spec)) {
                    cases++;
                    continue;
                }
                if (StringUtils.containsIgnoreCase(contact.getLastName(), spec)) {
                    cases++;
                    continue;
                }
                if (StringUtils.containsIgnoreCase(contact.getPhone(), spec)) {
                    cases++;
                    continue;
                }
                if (StringUtils.containsIgnoreCase(contact.getEmail(), spec)) {
                    cases++;
                }
            }
            return cases == qualifiers.length;
        }).collect(Collectors.toList());
    }

}
