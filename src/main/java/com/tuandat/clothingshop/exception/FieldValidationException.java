package com.tuandat.clothingshop.exception;

import java.util.Map;

public class FieldValidationException extends RuntimeException {
    private final Map<String, String> fieldErrors;

    public FieldValidationException(Map<String, String> fieldErrors) {
        super("Validation error");
        this.fieldErrors = fieldErrors;
    }

    public Map<String, String> getFieldErrors() {
        return fieldErrors;
    }
}

