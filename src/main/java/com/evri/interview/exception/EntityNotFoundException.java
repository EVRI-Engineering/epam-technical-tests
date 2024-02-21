package com.evri.interview.exception;


/**
 * The following exception should be thrown if the entity not found.
 */
public class EntityNotFoundException extends RuntimeException {

    private static final String ERROR_MESSAGE = "%s with ID: %s not found";

    public EntityNotFoundException(String entityClassName, Long attributeValue) {
        super(String.format(ERROR_MESSAGE, entityClassName, attributeValue));
    }
}
