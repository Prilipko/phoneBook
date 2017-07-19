package com.akvelon.testtask.phonebook.service.impl;

import com.akvelon.testtask.phonebook.domain.Contact;
import com.akvelon.testtask.phonebook.service.exception.ContactApplicationException;
import com.akvelon.testtask.phonebook.service.ContactsApplication;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;


/**
 * @author oleksandr.prylypko
 */
public class ContactApplicationImpl implements ContactsApplication {

    private final Map<Long, Contact> storage = new HashMap<>();
    private final Supplier<Long> idGenerator = new Supplier<Long>() {
        private long counter = 1L;

        @Override
        public Long get() {
            Long threshold = -1L;
            while (storage.containsKey(++counter)) {
                if (threshold++ == Long.MAX_VALUE) {
                    throw new ContactApplicationException("Too many contacts on repository.");
                }
            }
            return counter;
        }
    };

    public Contact createContact(final Contact contact) {
        if (contact.getId() != null)
            throw new ContactApplicationException("You should add only new contact, without id");
        Contact newContact = Contact.Builder.aPrototypeContact(contact)
                .withId(idGenerator.get())
                .build();
        return Contact.Builder.aPrototypeContact(storage.put(newContact.getId(), contact)).build();
    }

    public Contact getContact(Long id) {
        return Contact.Builder.aPrototypeContact(storage.get(id)).build();
    }

    public Contact editContact(final Contact contact) {
        if (contact.getId() == null)
            throw new ContactApplicationException("Contact is absent in storage, add it or edit another one");
        return Optional.ofNullable(storage.replace(contact.getId(), Contact.Builder.aPrototypeContact(contact).build()))
                .orElseThrow(() -> new ContactApplicationException(
                        "You can not edit this contact. It is absent in storage"));
    }

    public void deleteContact(Contact contact) {
        Optional.ofNullable(storage.remove(contact.getId()))
                .orElseThrow(() -> new ContactApplicationException("You have tried to remove absent contact"));
    }

    public List<Contact> search(String query) {
        String[] qualifiers = query.split(" ");
        return storage.values().stream().filter(new Predicate<Contact>() {
            @Override
            public boolean test(Contact contact) {
                int cases = 0;
                for (String spec : qualifiers) {
                    if (StringUtils.containsIgnoreCase(contact.getFirstName(), spec)) {
                        cases++;
                        break;
                    }
                }
                return cases == qualifiers.length;
            }
        }).collect(Collectors.toList());
    }

}
