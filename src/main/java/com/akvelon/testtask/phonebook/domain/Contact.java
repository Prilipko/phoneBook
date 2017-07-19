package com.akvelon.testtask.phonebook.domain;

import com.akvelon.testtask.phonebook.service.ContactsApplication;

/**
 * Simple definition of a Contact to store in the {@link ContactsApplication}
 */
public class Contact {

    /*
    I am considering to add id field
    It is simplifying editing all fields and makes implementation better IMHO
    Also I have added builder if you not mind
    */
    private final Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    private Contact() {
        this(null);
    }

    private Contact(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public static final class Builder {
        private Long id;
        private String firstName;
        private String lastName;
        private String email;
        private String phone;

        private Builder(Long id) {
            this.id = id;
        }

        private Builder() {
            this(null);
        }

        public static Builder aContact() {
            return new Builder();
        }

        public static Builder from(Contact contact) {
            Builder builder = new Builder(contact.getId());
            builder.withFirstName(contact.getFirstName());
            builder.withLastName(contact.getLastName());
            builder.withEmail(contact.getEmail());
            builder.withPhone(contact.getPhone());
            return builder;
        }

        public Builder withId(Long id) {
            this.id = id;
            return this;
        }

        public Builder withFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder withLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder withEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder withPhone(String phone) {
            this.phone = phone;
            return this;
        }

        public Contact build() {
            Contact contact = new Contact(id);
            contact.setFirstName(firstName);
            contact.setLastName(lastName);
            contact.setEmail(email);
            contact.setPhone(phone);
            return contact;
        }
    }
}
