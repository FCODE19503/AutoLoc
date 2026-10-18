package com.example.activite.service;

final class ServiceValidation {
    private ServiceValidation() { }
    static void requireText(String value, String field) {
        if (value == null || value.isBlank()) throw new IllegalArgumentException("Le champ " + field + " est obligatoire.");
    }
    static void requireNotNull(Object value, String field) {
        if (value == null) throw new IllegalArgumentException("Le champ " + field + " est obligatoire.");
    }
}
