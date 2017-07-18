package com.akvelon.testtask.phonebook.domain;
import com.akvelon.testtask.phonebook.service.ContactsApplication;

/**
 * Simple definition of a Contact to store in the {@link ContactsApplication}
 */
public class Contact {

    private String firstName;
    private String lastName;
    private String email;
    private String phone;

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

}
