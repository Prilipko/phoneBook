package com.akvelon.testtask.phonebook.service.exception;

/**
 * @author oleksandr.prylypko
 */
public class ContactApplicationException extends RuntimeException {

    public ContactApplicationException() {
    }

    public ContactApplicationException(String message) {
        super(message);
    }
}
